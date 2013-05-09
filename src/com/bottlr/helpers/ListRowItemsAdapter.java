package com.bottlr.helpers;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AbsoluteLayout;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bottlr.R;
import com.bottlr.dataacess.BottleDetails;
import com.bottlr.imgloader.ImageLoader;
import com.bottlr.utils.TAGS;
import com.bottlr.utils.Utils;
import com.bottlr.views.HomeScreenView;

public class ListRowItemsAdapter extends BaseAdapter {

	private static final String TAG = "ListRowItemsAdapter";
	private LayoutInflater l_Inflater;
	private Context context;
	private static ArrayList<BottleDetails> itemDetailsrrayList;
	private int ALLOWED_ROWS = 0;
	private ImageLoader imageLoader;
	static private final Logger logger = LoggerFactory
			.getLogger(ListRowItemsAdapter.class);

	public ListRowItemsAdapter(Context context, ArrayList<BottleDetails> results) {
		this.context = context;
		itemDetailsrrayList = results;
		l_Inflater = LayoutInflater.from(this.context);
		imageLoader = new ImageLoader(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return itemDetailsrrayList.size();
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
		Log.v(TAG,
				"itemDetailsrrayList List Size: " + itemDetailsrrayList.size());

		View row = l_Inflater.inflate(R.layout.listrow_layout, null);
		
		try {

			/* first layout items update. */

			BottleDetails bottle = itemDetailsrrayList.get(position);
			updateRowItemLayout(row, bottle);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return row;
	}

	private void updateRowItemLayout(View row, BottleDetails bottle) {
		/* update top image. */
		final ImageView headderImage = (ImageView) row
				.findViewById(R.id.final_bottle_top_imageView1);
		Log.v(TAG,
				"Bottle type: " + bottle.getBotlType() + " Title:"
						+ bottle.getTitle());
		Log.e(TAG,
				"Full top image URL: " + bottle.getFull_top_image_url()
						+ " bottle type: " + bottle.getBotlType() + " Title: "
						+ bottle.getTitle());
		logger.debug("Bottle type: " + bottle.getBotlType() + " Title:"
				+ bottle.getTitle());
		logger.debug("Headder image url: " + bottle.getFull_top_image_url());
		if (imageLoader == null) {
			imageLoader = new ImageLoader(context);
		}
		imageLoader.DisplayImage(bottle.getFull_top_image_url(), headderImage);

		// new DownloadImageTask(context, headderImage, null,
		// bottle.getFull_top_image_url()).execute();
		// DownloadThreadPollHelper.getExcecutor().execute(new
		// DownloadImageThread(context, headderImage,
		// bottle.getFull_top_image_url()));

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
		
		//Typeface tf = Typeface.createFromAsset(context.getAssets(),"fonts/Lucida Grande.ttf");
		
		TextView titleView = (TextView) row
				.findViewById(R.id.final_bottle_title_textView1);
		titleView.setTypeface(HomeScreenView.tf);
		titleView.setText(bottle.getTitle());

		/* update profile img id - singlebotl_profile_img */
		ImageView profileImg = (ImageView) row
				.findViewById(R.id.final_bottle_avatar_imageView1);
		// new DownloadImageTask(context, profileImg, null,
		// bottle.getAvatar_img()).execute();
		imageLoader.DisplayImage(bottle.getAvatar_img(), profileImg);

		/* update profile name id = singlebotl_profile_firstname */
		TextView profileNameView = (TextView) row
				.findViewById(R.id.final_bottle_realname_textView2);
		profileNameView.setTypeface(HomeScreenView.tf);
		profileNameView.setText(bottle.getReal_name());

		/* update username id - singlebotl_profile_username */
		TextView userNameView = (TextView) row
				.findViewById(R.id.final_bottle_username_textView3);
		userNameView.setTypeface(HomeScreenView.tf);
		String username = "@" + bottle.getUsername();
		userNameView.setText(username);

		/* update bottle date id - singlebotl_bottle_date */
		TextView dateView = (TextView) row
				.findViewById(R.id.final_bottle_datemsg_textView4);
		
		dateView.setTypeface(HomeScreenView.tf);
		String date_msg = bottle.getBottled_date_msg();
		Log.v(TAG, "Data msg: " + date_msg);
		logger.debug("Data msg: " + date_msg);
		dateView.setText(date_msg);

		/* update bottle img id - singlebotl_bottleimage */
		ImageView bottleImg = (ImageView) row
				.findViewById(R.id.final_bottle_bottle_imageView1);
		String localPath = Utils.openBottleLocalPath(bottle.getBotlImageUrl(),
				TAGS.BOTTLE_LARGE_TYPE);
		Log.v(TAG, "Bottle local path: " + localPath);
		bottleImg.setImageDrawable(Utils.loadImgFromAssets(context, localPath));

		/* update likes id - singlebotl_likepanel_likelayout_likes_number */
		TextView likesView = (TextView) row
				.findViewById(R.id.final_bottle_likes_value_textView1);
		likesView.setText(bottle.getLikeCount());

		/* update view id - singlebotl_likepanel_likelayout_likes_number */
		TextView viewsView = (TextView) row
				.findViewById(R.id.final_bottle_views_value_textView3);
		viewsView.setText(bottle.getLocationsCount());

		/* update miles id - singlebotl_likepanel_likelayout_likes_number */
		TextView milesView = (TextView) row
				.findViewById(R.id.final_bottle_miles_value_textView5);
		milesView.setText(bottle.getDistance());

		/* update pattern image */
		AbsoluteLayout pattren_layout = (AbsoluteLayout) row
				.findViewById(R.id.final_bottle_pattern_img_abosolutelayout);

		String patternLocalPath = Utils.createBottlePatternPath(bottle
				.getPattern_url());
		patternLocalPath = TAGS.PATTERNS_FOLDER + TAGS.PATH_SEPERATER
				+ bottle.getPattern_url();
		Log.v(TAG, "pattren local path: " + patternLocalPath);
		Drawable drwable = Utils.loadImgFromAssets(context, patternLocalPath);
		pattren_layout.setBackgroundDrawable(drwable);
		// Utils.loadImageFromAssets(context, pattern_img, patternLocalPath);
		// pattren_layout.setBackgroundDrawable(Utils.loadImgFromAssets(context,
		// patternLocalPath));

		// pattern_img.setImageDrawable(Utils.loadImgFromAssets(context,
		// patternLocalPath));

		/* setting bottle in TAGS to acess from open bottle */
		// TAGS.SELECTED_BOTTLE = bottle;

	}

}
