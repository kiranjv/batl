package com.bottlr.views;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.bottlr.R;
import com.bottlr.dataacess.BottleDetails;
import com.bottlr.dataacess.BottlesStoreManager;
import com.bottlr.helpers.BottleParseHelper;
import com.bottlr.helpers.GridListRowItemsAdapter;
import com.bottlr.helpers.GridListRowItemsAdapterLeft;
import com.bottlr.helpers.GridListRowItemsAdapterRight;
import com.bottlr.helpers.ListRowItemsAdapter;
import com.bottlr.helpers.ListRowItemsShuffleAdapter;
import com.bottlr.network.BottlesDownloadModel;
import com.bottlr.utils.TAGS;
import com.bottlr.utils.URLs;
import com.bottlr.utils.Utils;
import com.bottlr.views.HomeScreenView.AsyncOldBottleDownload;

public class HomeScreenListViewShuffleActivity extends Activity {

	static private final Logger logger = LoggerFactory
			.getLogger(HomeScreenListViewShuffleActivity.class);

	private static final String TAG = "HomeScreenListViewShuffleActivity";

	private ListView listViewLeft;
	private ListView listViewRight;
	private GridListRowItemsAdapter leftAdapter;
	private GridListRowItemsAdapter rightAdapter;
	private ArrayList<BottleDetails> leftItems = new ArrayList<BottleDetails>();
	private ArrayList<BottleDetails> rightItems = new ArrayList<BottleDetails>();

	private static Timer UpdateBottlesTimer = null;
	private TimerTask UpdateTimerTask;

	Vector<Integer> leftViewsHeights = new Vector<Integer>();
	Vector<Integer> rightViewsHeights = new Vector<Integer>();

	public boolean loadingMore;
	private LinearLayout loading_layout;
	private Context context;

	private ArrayList<BottleDetails> bottle_details_list;
	private ArrayList<BottleDetails> firstBottleArrayList;

	private ArrayList<BottleDetails> secondBottleArrayList;

	public static Typeface tf;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homescreen_shufflegrid_layout);
		context = this;
		tf = Typeface.createFromAsset(getAssets(),
				"fonts/AlikeAngular-Regular.ttf");
		listViewLeft = (ListView) findViewById(R.id.homescreen_list_view_left);
		listViewRight = (ListView) findViewById(R.id.homescreen_list_view_right);

		loadItems();

		listViewLeft.setOnItemClickListener(itemClickListener);
		listViewRight.setOnItemClickListener(itemClickListener);
		listViewLeft.setOnTouchListener(touchListener);
		listViewRight.setOnTouchListener(touchListener);
		listViewLeft.setOnScrollListener(scrollListener);
		listViewRight.setOnScrollListener(scrollListener);
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
							 new AsyncLatestBottleDownload(
							 new Random().nextInt(20)).execute();
//							new AsyncOldBottleDownload(
//									Utils.getDownloadOldBottlesCount())
//									.execute();
							listViewLeft.refreshDrawableState();
							listViewRight.refreshDrawableState();
						}
					});

				}
			};

			UpdateBottlesTimer = new Timer("Update Bottle");
			UpdateBottlesTimer.schedule(UpdateTimerTask, Utils
					.secsToMilliSeconds(TAGS.SYNC_LATEST_BOTLE_TIMER_DELAY),
					Utils.secsToMilliSeconds(30));

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

	// Passing the touch event to the opposite list
	OnTouchListener touchListener = new OnTouchListener() {
		boolean dispatched = false;

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (v.equals(listViewLeft) && !dispatched) {
				dispatched = true;
				listViewRight.dispatchTouchEvent(event);
			} else if (v.equals(listViewRight) && !dispatched) {
				dispatched = true;
				listViewLeft.dispatchTouchEvent(event);
			}

			dispatched = false;

			return false;
		}
	};

	
	OnItemClickListener leftItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> a, View v, int position, long id) {
			BottleDetails obj_BottleDetails = (BottleDetails) listViewLeft
					.getItemAtPosition(position);
			doBottleClickAction(obj_BottleDetails);
			
		}
	};
	
	
	OnItemClickListener rightItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> a, View v, int position, long id) {
			BottleDetails obj_BottleDetails = (BottleDetails) listViewRight
					.getItemAtPosition(position);
			doBottleClickAction(obj_BottleDetails);
			
		}
	};
	
	

	// Passing the click event to the opposite list

	OnItemClickListener itemClickListener = new OnItemClickListener() {
		boolean dispatched = false;
		int count = 0;

		@Override
		public void onItemClick(AdapterView<?> a, View v, int position, long id) {

			/*
			 * Log.e(TAG, "a: "+a.toString()); Log.e(TAG,
			 * "listViewLeft: "+listViewLeft.toString()); Log.e(TAG,
			 * "listViewRight: "+listViewRight.toString()); Log.e(TAG,
			 * "leftAdapter: "+leftAdapter.toString()); Log.e(TAG,
			 * "rightAdapter: "+rightAdapter.toString());
			 */

			Log.e("MD", "id: "+id);
			count++;
			if(count == 1) {
				dispatched = false;
				return;
			}
			if (a.equals(listViewLeft) && !dispatched) {
				Log.v("MD", "Left adapter item seleted.");
				dispatched = true;
				BottleDetails obj_BottleDetails = (BottleDetails) listViewLeft
						.getItemAtPosition(position);
				doBottleClickAction(obj_BottleDetails);

			} else if (a.equals(listViewRight) && !dispatched) {
				Log.v("MD", "Right adapter item seleted.");
				dispatched = true;
				BottleDetails obj_BottleDetails = (BottleDetails) listViewRight
						.getItemAtPosition(position);
				doBottleClickAction(obj_BottleDetails);
			}
			
			dispatched = false;
			count = 0;

		}

		

	};

	/**
	 * Synchronizing scrolling Distance from the top of the first visible
	 * element opposite list: sum_heights(opposite invisible screens) -
	 * sum_heights(invisible screens) + distance from top of the first visible
	 * child
	 */
	OnScrollListener scrollListener = new OnScrollListener() {

		@Override
		public void onScrollStateChanged(AbsListView v, int scrollState) {
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {


			try {

			if (view.getChildAt(0) != null) {
				if (view.equals(listViewLeft)) {
					leftViewsHeights.add(view.getFirstVisiblePosition(), view
							.getChildAt(0).getHeight());

					int h = 0;
					for (int i = 0; i < listViewRight.getFirstVisiblePosition(); i++) {
						h += (int) rightViewsHeights.get(i);
					}

					int hi = 0;
					for (int i = 0; i < listViewLeft.getFirstVisiblePosition(); i++) {
						hi += (int) leftViewsHeights.get(i);
					}

					int top = h - hi + view.getChildAt(0).getTop();
					listViewRight.setSelectionFromTop(
							listViewRight.getFirstVisiblePosition(), top);
					
					/* checking scrolled to end. comparing visbleItemCount and listheight size. */
//					Log.d("MD", "visibleItemCount: "+visibleItemCount);
//					Log.d("MD", "listViewLeft items count: "+listViewLeft.getCount());
//					if(visibleItemCount == listViewLeft.getCount()) {
//						Log.e("MD", "List reached end. Need to download.");
//						
//					}
					
					
					// what is the bottom iten that is visible
					int lastInScreen = firstVisibleItem + visibleItemCount;
					Log.d("MD", "lastInScreen: "+lastInScreen);
					Log.d("MD", "listViewLeft items count: "+listViewLeft.getCount());
					if ((lastInScreen == listViewLeft.getCount()) && !(loadingMore)) {
						Log.e("MD", "List reached end. Need to download.");
						// Run background thread
						 Toast.makeText(context, "Update old bottles activated. ",
						 Toast.LENGTH_SHORT).show();
						 new AsyncOldBottleDownload(Utils.getDownloadOldBottlesCount())
						 .execute();

						// Thread tt = new loadBackgroung(lastInScreen,
						// listview_adapter);
						// tt.start();
					}
					
				} else if (view.equals(listViewRight)) {
					rightViewsHeights.add(view.getFirstVisiblePosition(), view
							.getChildAt(0).getHeight());

					int h = 0;
					for (int i = 0; i < listViewLeft.getFirstVisiblePosition(); i++) {
						h += leftViewsHeights.get(i);
					}

					int hi = 0;
					for (int i = 0; i < listViewRight.getFirstVisiblePosition(); i++) {
						hi += rightViewsHeights.get(i);
					}

					int top = h - hi + view.getChildAt(0).getTop();
					listViewLeft.setSelectionFromTop(
							listViewLeft.getFirstVisiblePosition(), top);
				}

			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		}
	};

	private void loadItems() {

		loading_layout = (LinearLayout) findViewById(R.id.home_loading_layout);
		loading_layout.setVisibility(View.GONE);

		bottle_details_list = BottlesStoreManager.getStoreInstance(context)
				.getBottles_list();

		// split bottles
		splitBottlesList(bottle_details_list);

		leftAdapter = new GridListRowItemsAdapter(this, leftItems);
		rightAdapter = new GridListRowItemsAdapter(this, rightItems);
		listViewLeft.setAdapter(leftAdapter);
		listViewRight.setAdapter(rightAdapter);

		listViewLeft.setDivider(null);
		listViewRight.setDivider(null);

	}

	private void splitBottlesList(List<BottleDetails> bottle_details_list2) {
		int total_bottles = bottle_details_list2.size();
		for (int i = 0; i < total_bottles; i++) {
			leftItems.add(bottle_details_list2.get(i));
			i++;
			if (i < total_bottles)
				rightItems.add(bottle_details_list2.get(i));
		}

		Log.e(TAG, "First array bottles:" + leftItems);
		Log.e(TAG, "Second array bottles:" + rightItems);

	}

	class AsyncOldBottleDownload extends AsyncTask<Void, Void, Boolean> {

		private static final String TAG = "AsyncHomeBottleDownload";

		private int bottle_count;

		List<BottleDetails> parsedBottles;
		private String failureMSG;
		private int counter = 1;

		public AsyncOldBottleDownload(int bottle_count) {

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
			// String url = URLs.USER_OLD_BOTTLE_URL + bottle_count;
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
			Log.v("AsyncOldBottleDownload", "Counter: " + counter);
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
				Toast.makeText(context, "No bottles.", Toast.LENGTH_SHORT)
						.show();
			} else {
				try {
					splitBottlesList(parsedBottles);

					leftAdapter.notifyDataSetChanged();
					rightAdapter.notifyDataSetChanged();
				} catch (Exception e) {
					Log.e(TAG,
							"Error notify dataset change.. " + e.getMessage());
					logger.error("Error notify dataset change.. "
							+ e.getMessage());
					e.printStackTrace();
				}
				Toast.makeText(context,
						parsedBottles.size() + " bottles downloaded.",
						Toast.LENGTH_SHORT).show();
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

		List<BottleDetails> parsedBottles;
		private String failureMSG;

		public AsyncLatestBottleDownload(int bottle_count) {

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
			String url = URLs.BOTTLE_KING_NEW_BOTTLE_URL + top_bottle_time
					+ TAGS.PATH_SEPERATER + 0;
			// String url = URLs.USER_NEW_BOTTLE_URL + top_bottle_time
			// + TAGS.PATH_SEPERATER + 0;

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
				Toast.makeText(context, "No bottles.", Toast.LENGTH_SHORT)
						.show();
			} else {
				try {

					splitBottlesList(parsedBottles);

					leftAdapter.notifyDataSetChanged();
					rightAdapter.notifyDataSetChanged();
				} catch (Exception e) {
					Log.e(TAG,
							"Error notify dataset change.. " + e.getMessage());
					logger.error("Error notify dataset change.. "
							+ e.getMessage());
					e.printStackTrace();
				}
				Toast.makeText(context, parsedBottles.size() + " bottles.",
						Toast.LENGTH_SHORT).show();
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
	
	private void doBottleClickAction(BottleDetails obj_BottleDetails) {

		Log.e("MD",
				"Openning bottle id: " + obj_BottleDetails.getBottle_id());

		BottleDetailsView.CURRENT_OPEN_BOTTLE = obj_BottleDetails;
		
		// Toast.makeText(
		// HomeScreenView.this,
		// "You have chosen bottle id : " + " "
		// + obj_BottleDetails.getBottle_id()
		// + " Position: " + position,
		// Toast.LENGTH_SHORT).show();
		
		
		Intent bottleopen_intent = new Intent(
					HomeScreenListViewShuffleActivity.this,
					BottleDetailsView.class);
			bottleopen_intent
					.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
			bottleopen_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			startActivity(bottleopen_intent);
			

			// HomeScreenListViewShuffleActivity.this.finish();
		

	}

}