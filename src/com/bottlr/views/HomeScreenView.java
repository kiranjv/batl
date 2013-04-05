package com.bottlr.views;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.bottlr.helpers.AsyncBottleDownload;
import com.bottlr.helpers.ItemDetails;
import com.bottlr.helpers.ListRowItemsAdapter;

public class HomeScreenView extends Activity {

	private static final String TAG = "HomeScreenView";
	public ListView list_bottles;
	public Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.context = this;
		setContentView(R.layout.homescreen_layout);
		// show The Image
		// new DownloadImageTask((ImageView) findViewById(R.id.sampleimage))
		// .execute("http://java.sogeti.nl/JavaBlog/wp-content/uploads/2009/04/android_icon_256.png");

		initGUI();

	}

	private void initGUI() {

		Button login_button = (Button) findViewById(R.id.homescreen_loginbutton);
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
		TextView nobottle_view = (TextView) findViewById(R.id.homeview_nobottle_text);
		ArrayList<BottleDetails> bottle_details = new BottlesRepository(context)
				.retriveBottles();
		if(bottle_details.size() > 1 ) {
		// Log.e(TAG, "Bottles from bottle repo: "+bottle_details);
		list_bottles = (ListView) findViewById(R.id.homescreen_listview);
		// list_bottles.setAdapter(new ItemListBaseAdapter(this,
		// bottle_details));
		list_bottles.setAdapter(new ListRowItemsAdapter(this, bottle_details));
		list_bottles.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {
				Object o = list_bottles.getItemAtPosition(position);
				BottleDetails obj_BottleDetails = (BottleDetails) o;
				BottleDetailsView.CURRENT_OPEN_BOTTLE = obj_BottleDetails;
				Toast.makeText(
						HomeScreenView.this,
						"You have chosen bottle id : " + " "
								+ obj_BottleDetails.getBottle_id()
								+ " Position: " + position, Toast.LENGTH_SHORT)
						.show();

				Intent intent = new Intent(HomeScreenView.this,
						BottleDetailsView.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				startActivity(intent);
			}
		});
		}
		else {
			nobottle_view.setVisibility(View.VISIBLE);
		}
		super.onStart();

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	public void onSingleBottleClick(View v) {
		LinearLayout layout = (LinearLayout) v;
		BottleDetailsView.SELECTED_LAYOUT = layout;
		TextView bottleView = (TextView) layout
				.findViewById(R.id.singlebottle_clicked_bottleid);

		Toast.makeText(this,
				"Selected first bottle clicked id:" + bottleView.getText(),
				Toast.LENGTH_SHORT).show();

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

		Toast.makeText(this,
				"Selected second bottle clicked id:" + bottleView.getText(),
				Toast.LENGTH_SHORT).show();
	}

	public void onSingleBottleLikeClick(View v) {

		LinearLayout likelayout = (LinearLayout) v;
		TextView likeText = (TextView) v
				.findViewById(R.id.singlebotl_likepanel_likelayout_likes_number);
		Toast.makeText(this,
				"Bottle like clicked. Likes: " + likeText.getText(),
				Toast.LENGTH_SHORT).show();

	}

}
