package com.bottlr.helpers;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bottlr.R;
import com.bottlr.dataacess.BottleDetails;
import com.bottlr.utils.TAGS;
import com.bottlr.utils.Utils;
import com.bottlr.views.HomeScreenView;

public class ListRowItemsAdapter extends BaseAdapter {

	private static final String TAG = "ListRowItemsAdapter";
	private LayoutInflater l_Inflater;
	private Context context;
	private static ArrayList<BottleDetails> itemDetailsrrayList;
	private int ALLOWED_ROWS = 0;
	private static ArrayList<BottleDetails> firstBottleArrayList = new ArrayList<BottleDetails>();
	private static ArrayList<BottleDetails> secondBottleArrayList = new ArrayList<BottleDetails>();

	public ListRowItemsAdapter(Context context, ArrayList<BottleDetails> results) {
		this.context = context;
		itemDetailsrrayList = results;
		l_Inflater = LayoutInflater.from(this.context);
		splitBottlesList();
		ALLOWED_ROWS = firstBottleArrayList.size();
	}

	private void splitBottlesList() {
		int total_bottles = itemDetailsrrayList.size();
		for (int i = 0; i < total_bottles; i++) {
			firstBottleArrayList.add(itemDetailsrrayList.get(i));
			i++;
			if (i < total_bottles)
				secondBottleArrayList.add(itemDetailsrrayList.get(i));
		}

		Log.e(TAG, "First array bottles:" + firstBottleArrayList);
		Log.e(TAG, "Second array bottles:" + secondBottleArrayList);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return firstBottleArrayList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return itemDetailsrrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.v(TAG, "-----------------------------------");
		Log.v(TAG, "Row position: " + position);
		Log.v(TAG, "View: " + convertView);
		Log.v(TAG,
				"firstItemBottle List Size: " + firstBottleArrayList.size()
						+ " secondBottleArrayList size: "
						+ secondBottleArrayList.size());

		View row = l_Inflater.inflate(R.layout.listrow_layout, null);
		try {

			if (position >= ALLOWED_ROWS)
				return row;

			/* first layout items update. */

			BottleDetails firstItemBottle = firstBottleArrayList.get(position);
			updateFirstLayout(row, firstItemBottle);

			/* second layout items update. */
			if (position < secondBottleArrayList.size()) {
				BottleDetails secondItemBottle = secondBottleArrayList
						.get(position);
				updateSecondLayout(row, secondItemBottle);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return row;
	}

	private void updateFirstLayout(View row, BottleDetails bottle) {
		/* update top image. */
		final ImageView headderImage = (ImageView) row
				.findViewById(R.id.sing_botl_mainTopImage);
		ProgressBar progress = (ProgressBar) row
				.findViewById(R.id.single_botl_Img_progressBar1);
		new DownloadImageTask(context, headderImage, progress,
				bottle.getFull_top_image_url()).execute();

		// fetch a remote resource in raw bitmap
		// AQuery aq = new AQuery(HomeScreenView.context);
		// // String url =
		// // "http://www.vikispot.com/z/images/vikispot/android-w.png";
		// String url = bottle.getFull_top_image_url();
		// aq.ajax(url, Bitmap.class, new AjaxCallback<Bitmap>() {
		//
		// @Override
		// public void callback(String url, Bitmap bitmap, AjaxStatus status) {
		// headderImage.setImageBitmap(result);
		// }
		// });

		/* update bottle title (id - sing_botl_title) */
		TextView titleView = (TextView) row.findViewById(R.id.sing_botl_title);
		titleView.setText(bottle.getTitle());

		/* update profile img id - singlebotl_profile_img */
		ImageView profileImg = (ImageView) row
				.findViewById(R.id.singlebotl_profile_img);
		new DownloadImageTask(context, profileImg, progress,
				bottle.getAvatar_img()).execute();

		/* update profile name id = singlebotl_profile_firstname */
		TextView profileNameView = (TextView) row
				.findViewById(R.id.singlebotl_profile_firstname);
		profileNameView.setText(bottle.getReal_name());

		/* update username id - singlebotl_profile_username */
		TextView userNameView = (TextView) row
				.findViewById(R.id.singlebotl_profile_username);
		String username = "@" + bottle.getUsername();
		userNameView.setText(username);

		/* update bottle date id - singlebotl_bottle_date */
		TextView dateView = (TextView) row
				.findViewById(R.id.singlebotl_bottle_date);
		String date_msg = bottle.getBottled_date_msg();
		Log.v(TAG, "Data msg: " + date_msg);
		dateView.setText(date_msg);

		/* update bottle img id - singlebotl_bottleimage */
		ImageView bottleImg = (ImageView) row
				.findViewById(R.id.singlebotl_bottleimage);
		String localPath = Utils.openBottleLocalPath(bottle.getBotlImageUrl(),
				TAGS.BOTTLE_LARGE_TYPE);
		Log.v(TAG, "Bottle local path: " + localPath);
		bottleImg.setImageDrawable(Utils.loadImgFromAssets(context, localPath));

		/* update likes id - singlebotl_likepanel_likelayout_likes_number */
		TextView likesView = (TextView) row
				.findViewById(R.id.singlebotl_likepanel_likelayout_likes_number);
		likesView.setText(bottle.getLikeCount());

		/* update view id - singlebotl_likepanel_likelayout_likes_number */
		TextView viewsView = (TextView) row
				.findViewById(R.id.singlebotl_viewspanel_viewlayout_views_number);
		viewsView.setText(bottle.getLocationsCount());

		/* update miles id - singlebotl_likepanel_likelayout_likes_number */
		TextView milesView = (TextView) row
				.findViewById(R.id.singlebotl_milespanel_milelayout_miles_number);
		milesView.setText(bottle.getDistance());

		/* update pattern image */
		ImageView pattern_img = (ImageView) row
				.findViewById(R.id.singlebotl_pattren_image);
		String patternLocalPath = Utils.createBottlePatternPath(bottle
				.getPattern_url());
		patternLocalPath = TAGS.BOTTLE_FOLDER + "/" + "pattern-020.gif";
		Log.v(TAG, "pattren local path: " + patternLocalPath);
		// Utils.loadImageFromAssets(context, pattern_img, patternLocalPath);
		// pattern_img.setImageDrawable(Utils.loadImgFromAssets(context,
		// patternLocalPath));

		/* setting bottle id for invisible textview */
		TextView bottleView = (TextView) row
				.findViewById(R.id.singlebottle_clicked_bottleid);
		bottleView.setText(bottle.getBottle_id());

	}

	private void updateSecondLayout(View row, BottleDetails bottle) {
		// Log.v(TAG, "secondItemBottle: " + secondItemBottle);
		ImageView mainImage2 = (ImageView) row
				.findViewById(R.id.second_sing_botl_mainTopImage);
		ProgressBar second_progress = (ProgressBar) row
				.findViewById(R.id.second_single_botl_Img_progressBar1);
		new DownloadImageTask(context, mainImage2, second_progress,
				bottle.getFull_top_image_url()).execute();

		/* update bottle title (id - sing_botl_profiledesc) */
		TextView titleView = (TextView) row
				.findViewById(R.id.second_sing_botl_title);
		titleView.setText(bottle.getTitle());

		/* update profile img id - second_singlebotl_profile_img */
		ImageView profileImg = (ImageView) row
				.findViewById(R.id.second_singlebotl_profileimg);
		new DownloadImageTask(context, profileImg, second_progress,
				bottle.getAvatar_img()).execute();

		/* update profile name id = singlebotl_profile_firstname */
		TextView profileNameView = (TextView) row
				.findViewById(R.id.second_singlebotl_profile_firstname);
		profileNameView.setText(bottle.getReal_name());

		/* update username id - singlebotl_profile_username */
		TextView userNameView = (TextView) row
				.findViewById(R.id.second_singlebotl_profile_lastname);
		userNameView.setText("@" + bottle.getUsername());

		/* update bottle date id - singlebotl_bottle_date */
		TextView dateView = (TextView) row
				.findViewById(R.id.second_singlebotl_bottle_date);
		String date_msg = bottle.getBottled_date_msg();
		Log.v(TAG, "Data msg: " + date_msg);
		dateView.setText(date_msg);

		/* update bottle img id - singlebotl_bottleimage */
		ImageView bottleImg = (ImageView) row
				.findViewById(R.id.second_singlebotl_bottleimage);
		String localPath = Utils.openBottleLocalPath(bottle.getBotlImageUrl(),
				TAGS.BOTTLE_LARGE_TYPE);
		bottleImg.setImageDrawable(Utils.loadImgFromAssets(context, localPath));

		/* update likes id - singlebotl_likepanel_likelayout_likes_number */
		TextView likesView = (TextView) row
				.findViewById(R.id.second_singlebotl_likepanel_likelayout_likes_number);
		likesView.setText(bottle.getLikeCount());

		/* update view id - singlebotl_likepanel_likelayout_likes_number */
		TextView viewsView = (TextView) row
				.findViewById(R.id.second_singlebotl_viewspanel_viewlayout_views_number);
		viewsView.setText(bottle.getLocationsCount());

		/* update miles id - singlebotl_likepanel_likelayout_likes_number */
		TextView milesView = (TextView) row
				.findViewById(R.id.second_singlebotl_milespanel_milelayout_miles_number);
		milesView.setText(bottle.getDistance());

		/* update pattern image */
		ImageView pattern_img = (ImageView) row
				.findViewById(R.id.second_botl_pattren_image);
		String patternLocalPath = Utils.createBottlePatternPath(bottle
				.getPattern_url());
		patternLocalPath = TAGS.BOTTLE_FOLDER + "/" + "pattern-020.gif";
		Log.v(TAG, "pattren local path: " + patternLocalPath);
		// Utils.loadImageFromAssets(context, pattern_img, patternLocalPath);
		// pattern_img.setImageDrawable(Utils.loadImgFromAssets(context,
		// patternLocalPath));

		/* setting bottle id for invisible textview */
		TextView bottleView = (TextView) row
				.findViewById(R.id.second_bottle_clicked_bottleid);
		bottleView.setText(bottle.getBottle_id());

	}

}
