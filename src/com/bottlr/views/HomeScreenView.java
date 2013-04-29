package com.bottlr.views;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bottlr.R;
import com.bottlr.dataacess.BottleDetails;
import com.bottlr.dataacess.BottlesRepository;
import com.bottlr.dataacess.BottlesStoreManager;

import com.bottlr.helpers.BottleParseHelper;
import com.bottlr.helpers.ItemDetails;
import com.bottlr.helpers.ListRowItemsAdapter;
import com.bottlr.network.BottlesDownloadModel;
import com.bottlr.utils.TAGS;
import com.bottlr.utils.URLs;
import com.bottlr.utils.Utils;

public class HomeScreenView extends Activity {

	static private final Logger logger = LoggerFactory
			.getLogger(HomeScreenView.class);

	private int prev_selected_position = -1;
	private static final String TAG = "HomeScreenView";
	public ListView listview_bottles;
	private ListRowItemsAdapter listview_adapter;
	public static Context context;

	private boolean loadingMore = false;
	private Handler handler = new Handler();
	private ArrayList<BottleDetails> bottle_details_list;
	private LinearLayout loading_layout;

	private TimerTask UpdateTimerTask;

	private static Timer UpdateBottlesTimer = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e("ME", "onCreate");
		this.context = this;
		setContentView(R.layout.homescreen_layout);
		// show The Image
		// new DownloadImageTask((ImageView) findViewById(R.id.sampleimage))
		// .execute("http://java.sogeti.nl/JavaBlog/wp-content/uploads/2009/04/android_icon_256.png");

		initGUI();

	}

	private void initGUI() {

		Button login_button = (Button) findViewById(R.id.homescreen_loginbutton);
		loading_layout = (LinearLayout) findViewById(R.id.home_loading_layout);
		loading_layout.setVisibility(View.GONE);

		login_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HomeScreenView.this, LoginView.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);

			}
		});

		login_button.setEnabled(false);
	}

	@Override
	protected void onStart() {
		Log.e("ME", "onStart");
		if (UpdateBottlesTimer == null && UpdateTimerTask == null) {
			UpdateTimerTask = new TimerTask() {

				@Override
				public void run() {
					Log.v(TAG, "Lastest bottle update timer activated.");
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							new AsyncLatestBottleDownload(listview_adapter,
									new Random().nextInt(20)).execute();
							try {
							if(listview_bottles != null) {
							listview_bottles.refreshDrawableState();
							}
							}
							catch (Exception e) {
								e.printStackTrace();
							}
						}
					});

				}
			};

			UpdateBottlesTimer = new Timer("Update Bottle");
			UpdateBottlesTimer.schedule(UpdateTimerTask, Utils
					.secsToMilliSeconds(TAGS.SYNC_LATEST_BOTLE_TIMER_DELAY),
					Utils.secsToMilliSeconds(30));

		}

		TextView nobottle_view = (TextView) findViewById(R.id.homeview_nobottle_text);

		// bottle_details_list = new
		// BottlesRepository(context).retriveBottles();
		bottle_details_list = BottlesStoreManager.getStoreInstance(context)
				.getBottles_list();
		if (bottle_details_list.size() > 1) {
			// Log.e(TAG, "Bottles from bottle repo: "+bottle_details);
			listview_bottles = (ListView) findViewById(R.id.homescreen_listview);
			listview_bottles.setDivider(null);
			
			// list_bottles.setAdapter(new ItemListBaseAdapter(this,
			// bottle_details));
			listview_adapter = new ListRowItemsAdapter(this,
					bottle_details_list);
			listview_bottles.setAdapter(listview_adapter);
			listview_bottles.setOnItemClickListener(new OnItemClickListener() {
				

				@Override
				public void onItemClick(AdapterView<?> a, View v, int position,
						long id) {
					 prev_selected_position = position;
					
					Object o = listview_bottles.getItemAtPosition(position);
					BottleDetails obj_BottleDetails = (BottleDetails) o;
					BottleDetailsView.CURRENT_OPEN_BOTTLE = obj_BottleDetails;
//					Toast.makeText(
//							HomeScreenView.this,
//							"You have chosen bottle id : " + " "
//									+ obj_BottleDetails.getBottle_id()
//									+ " Position: " + position,
//							Toast.LENGTH_SHORT).show();

					Intent intent = new Intent(HomeScreenView.this,
							BottleDetailsView.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

					startActivity(intent);
				}
			});

			// adding scroll listener
			listview_bottles.setOnScrollListener(new OnScrollListener() {

				@Override
				public void onScrollStateChanged(AbsListView view,
						int scrollState) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					// what is the bottom iten that is visible
					int lastInScreen = firstVisibleItem + visibleItemCount;
					if ((lastInScreen == totalItemCount) && !(loadingMore)) {
						// Run background thread

						new AsyncOldBottleDownload(lastInScreen,
								listview_adapter, Utils
										.getDownloadOldBottlesCount())
								.execute();

						// Thread tt = new loadBackgroung(lastInScreen,
						// listview_adapter);
						// tt.start();
					}
				}
			});

			if(prev_selected_position != -1)
			listview_bottles.smoothScrollToPosition(prev_selected_position);
			
		} else {
			nobottle_view.setVisibility(View.VISIBLE);
		}

		super.onStart();

	}

	@Override
	protected void onPause() {
		Log.e("ME", "onPause");

		if (UpdateBottlesTimer != null && UpdateTimerTask != null) {
			UpdateTimerTask.cancel();
			UpdateTimerTask = null;
			UpdateBottlesTimer = null;

		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		Log.e("ME", "onResume");
		super.onResume();
	}

	@Override
	protected void onRestart() {
		Log.e("ME", "onRestart");
		super.onRestart();
	}

	@Override
	protected void onStop() {
		Log.e("ME", "onStop");
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		Log.e("ME", "onDestroy");
		super.onDestroy();
	}

	public void onSingleBottleClick(View v) {
		LinearLayout layout = (LinearLayout) v;
		BottleDetailsView.SELECTED_LAYOUT = layout;
		TextView bottleView = (TextView) layout
				.findViewById(R.id.singlebottle_clicked_bottleid);
		String bottle_id = (String) bottleView.getText();
//		Toast.makeText(this, "Selected first bottle clicked id:" + bottle_id,
//				Toast.LENGTH_SHORT).show();

		// BottleDetails bottle = Utils.getBottleDetails(this, bottle_id);
		// BottleDetailsView.CURRENT_OPEN_BOTTLE = bottle;
		Intent intent = new Intent(HomeScreenView.this, BottleDetailsView.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		startActivity(intent);
	}

	public void onSecondSingleBottleClick(View v) {
		LinearLayout layout = (LinearLayout) v;
		BottleDetailsView.SELECTED_LAYOUT = layout;
		TextView bottleView = (TextView) layout
				.findViewById(R.id.second_bottle_clicked_bottleid);

		String bottle_id = (String) bottleView.getText();
//		Toast.makeText(this, "Selected second bottle clicked id:" + bottle_id,
//				Toast.LENGTH_SHORT).show();

		// BottleDetails bottle = Utils.getBottleDetails(this, bottle_id);
		// BottleDetailsView.CURRENT_OPEN_BOTTLE = bottle;
		Intent intent = new Intent(HomeScreenView.this, BottleDetailsView.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		startActivity(intent);
	}

	public void onSingleBottleLikeClick(View v) {

		LinearLayout likelayout = (LinearLayout) v;
		TextView likeText = (TextView) v
				.findViewById(R.id.singlebotl_likepanel_likelayout_likes_number);
//		Toast.makeText(this,
//				"Bottle like clicked. Likes: " + likeText.getText(),
//				Toast.LENGTH_SHORT).show();

	}

	class AsyncOldBottleDownload extends AsyncTask<Void, Void, Boolean> {

		private static final String TAG = "AsyncHomeBottleDownload";

		public int lastIndex;
		private int bottle_count;
		private ListRowItemsAdapter adaptor;
		List<BottleDetails> parsedBottles;
		private String failureMSG;
        private int counter = 1;
		public AsyncOldBottleDownload(int lastIndex,
				ListRowItemsAdapter adaptor, int bottle_count) {
			this.lastIndex = lastIndex;
			this.adaptor = adaptor;
			this.bottle_count = bottle_count;
		}

		@Override
		protected void onPreExecute() {
			loadingMore = true;
			loading_layout.setVisibility(View.VISIBLE);
			super.onPreExecute();
		}

		/**
		 * asyncronus background service to download the bottles from bottle
		 * server.
		 */
		@Override
		protected Boolean doInBackground(Void... params) {
			BottlesDownloadModel download = new BottlesDownloadModel(context);

			// prepare url for old bottle download with count.
			
			String url = URLs.BOTTLE_KING_OLD_BOTTLE_URL + bottle_count;
//			String url = URLs.USER_OLD_BOTTLE_URL + bottle_count;
			Log.v(TAG, "Old bottle URL: " + url);
			logger.debug("old bottles url: " + url);
			String bottles = download.downloadBottlesJson(url);
			logger.debug("old bottles response : " + bottles);
			if (bottles == null) {
				failureMSG = download.getFailureMessage();
				return false;
			}

			BottleParseHelper bottlesParser = new BottleParseHelper(context);
			parsedBottles = bottlesParser.parseBottles(bottles);
			Log.e(TAG, "Downloaded bottles size: " + parsedBottles.size());
			Log.e(TAG, "Downloaded bottles json: " + bottles);
			// bottlesParser.storeBottleLocal(parsedBottles);
			Log.v("AsyncOldBottleDownload", "Counter: "+counter);
			counter++;
			// BottlesStoreManager.getStoreInstance(context).storeBottlesLast(
			// parsedBottles);

			
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			counter = 1;
			loading_layout.setVisibility(View.GONE);

			if (!result) {
				// UIUtils.OkDialog(context,
				// "No bottles from server. Message is "+failureMSG);
				Toast.makeText(context,
						"No bottles.",
						Toast.LENGTH_SHORT).show();
			} else {
				try {
					// TODO Auto-generated method stub
					for (int i = 0; i < parsedBottles.size(); i++) {
						bottle_details_list.add(parsedBottles.get(i));
					}
					adaptor.notifyDataSetChanged();
				} catch (Exception e) {
					Log.e(TAG,
							"Error notify dataset change.. " + e.getMessage());
					logger.error("Error notify dataset change.. "
							+ e.getMessage());
					e.printStackTrace();
				}
				Toast.makeText(
						context,
						parsedBottles.size()+" bottles downloaded.", Toast.LENGTH_SHORT)
						.show();
			}
			
			BottlesStoreManager.getStoreInstance(context).printBottlesListIds();
			
			loadingMore = false;

			super.onPostExecute(result);
		}

		/**
		 * @return the parsedBottles
		 */
		public List<BottleDetails> getParsedBottles() {
			return parsedBottles;
		}

		/**
		 * @param parsedBottles
		 *            the parsedBottles to set
		 */
		public void setParsedBottles(List<BottleDetails> parsedBottles) {
			this.parsedBottles = parsedBottles;
		}

	}

	class AsyncLatestBottleDownload extends AsyncTask<Void, Void, Boolean> {

		private static final String TAG = "AsyncHomeBottleDownload";

		private int bottle_count;
		private ListRowItemsAdapter adaptor;
		List<BottleDetails> parsedBottles;
		private String failureMSG;

		public AsyncLatestBottleDownload(ListRowItemsAdapter adaptor,
				int bottle_count) {

			this.adaptor = adaptor;
			this.bottle_count = bottle_count;
		}

		@Override
		protected void onPreExecute() {
			loadingMore = true;
			loading_layout.setVisibility(View.VISIBLE);
			super.onPreExecute();
		}

		/**
		 * asyncronus background service to download the bottles from bottle
		 * server.
		 */
		@Override
		protected Boolean doInBackground(Void... params) {
			String top_bottle_time = BottlesStoreManager.getStoreInstance(
					getApplicationContext()).getTopBottleCreatedAtTime();
			
			if(top_bottle_time == null) 
				return false;
			
			String url = URLs.BOTTLE_KING_NEW_BOTTLE_URL + top_bottle_time
					+ TAGS.PATH_SEPERATER + 0;
			 //String url = URLs.USER_NEW_BOTTLE_URL + top_bottle_time
			 //+ TAGS.PATH_SEPERATER + 0;

			Log.v(TAG, "New bottles url: " + url);
			logger.debug("New bottles url: " + url);
			BottlesDownloadModel download = new BottlesDownloadModel(context);
			String bottles = download.downloadBottlesJson(url);
			logger.debug("new bottles response : " + bottles);
			if (bottles == null) {
				failureMSG = download.getFailureMessage();
				return false;
			}

			BottleParseHelper bottlesParser = new BottleParseHelper(context);
			parsedBottles = bottlesParser.parseBottles(bottles);
			Log.e(TAG, "Downloaded bottles size: " + parsedBottles.size());
			Log.e(TAG, "Downloaded bottles json: " + bottles);
			// bottlesParser.storeBottleLocal(parsedBottles);
			BottlesStoreManager.getStoreInstance(context).storeBottlesFront(
					parsedBottles);

			loadingMore = false;
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			loading_layout.setVisibility(View.GONE);

			if (!result) {
				// UIUtils.OkDialog(context,
				// "No bottles from server. Message is "+failureMSG);
				Toast.makeText(context,
						"No bottles.",
						Toast.LENGTH_SHORT).show();
			} else {
				try {
					// TODO Auto-generated method stub
					for (int i = 0; i < parsedBottles.size(); i++) {
						bottle_details_list.add(parsedBottles.get(i));
					}
					adaptor.notifyDataSetChanged();
				} catch (Exception e) {
					Log.e(TAG,
							"Error notify dataset change.. " + e.getMessage());
					logger.error("Error notify dataset change.. "
							+ e.getMessage());
					e.printStackTrace();
				}
				Toast.makeText(
						context,
						parsedBottles.size()
								+ " bottles.", Toast.LENGTH_SHORT)
						.show();
			}

			super.onPostExecute(result);
		}

		/**
		 * @return the parsedBottles
		 */
		public List<BottleDetails> getParsedBottles() {
			return parsedBottles;
		}

		/**
		 * @param parsedBottles
		 *            the parsedBottles to set
		 */
		public void setParsedBottles(List<BottleDetails> parsedBottles) {
			this.parsedBottles = parsedBottles;
		}

	}

}
