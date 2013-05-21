package com.bottlr.helpers;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bottlr.R;
import com.bottlr.dataacess.BottleDetails;
import com.bottlr.imgloader.ImageLoader;
import com.bottlr.utils.TAGS;
import com.bottlr.utils.Utils;
import com.bottlr.views.HomeScreenGridView;
import com.bottlr.views.HomeScreenView;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


public class ListRowItemsShuffleAdapter extends ArrayAdapter{

	static private final Logger logger = LoggerFactory
			.getLogger(ListRowItemsShuffleAdapter.class);
    private static final String TAG = "ListRowItemsShuffleAdapter";
	Context context; 
    LayoutInflater inflater;
    int layoutResourceId;
    float imageWidth;
	private ArrayList<BottleDetails> itemDetailsrrayList;
	private ImageLoader imageLoader;
    
    public ListRowItemsShuffleAdapter(Context context, int layoutResourceId, ArrayList<BottleDetails> items) {
       super(context, layoutResourceId);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.itemDetailsrrayList = items;
        float width = ((Activity)context).getWindowManager().getDefaultDisplay().getWidth();
        float margin = (int)convertDpToPixel(10f, (Activity)context);
        // two images, three margins of 10dips
		imageWidth = ((width - (3 * margin)) / 2);
    }

	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FrameLayout row = (FrameLayout) convertView;
        ItemHolder holder;
        
		if (row == null) {
			holder = new ItemHolder();
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = (FrameLayout) inflater.inflate(layoutResourceId, parent, false);
            try {

    			/* first layout items update. */

    			BottleDetails bottle = itemDetailsrrayList.get(position);
    			updateRowItemLayout(row, bottle);

    		} catch (Exception e) {
    			e.printStackTrace();
    		}
			holder.row = row;
		} else {
			holder = (ItemHolder) row.getTag();
		}
		
		row.setTag(holder);
		//setImageBitmap(item, holder.itemImage);
        return row;
    }

    public static class ItemHolder
    {
    	FrameLayout row;
    }
	
    // resize the image proportionately so it fits the entire space
	private void setImageBitmap(Integer item, ImageView imageView){
		Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), item);
		float i = ((float) imageWidth) / ((float) bitmap.getWidth());
		float imageHeight = i * (bitmap.getHeight());
		FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) imageView.getLayoutParams();
		params.height = (int) imageHeight;
		params.width = (int) imageWidth;
		imageView.setLayoutParams(params);
		imageView.setImageResource(item);
	}
	
	public static float convertDpToPixel(float dp, Context context){
	    Resources resources = context.getResources();
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	    float px = dp * (metrics.densityDpi/160f);
	    return px;
	}

	private void updateRowItemLayout(View row, BottleDetails bottle) {
		/* update top image. */
		final ImageView headderImage = (ImageView) row
				.findViewById(R.id.new_single_headder_imageView1);
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
				.findViewById(R.id.new_botl_title_textView1);
		titleView.setTypeface(HomeScreenView.tf);
		titleView.setText(bottle.getTitle());

		/* update profile img id - singlebotl_profile_img */
		ImageView profileImg = (ImageView) row
				.findViewById(R.id.new_bottle_profileig_imageView2);
		// new DownloadImageTask(context, profileImg, null,
		// bottle.getAvatar_img()).execute();
		imageLoader.DisplayImage(bottle.getAvatar_img(), profileImg);

		/* update profile name id = singlebotl_profile_firstname */
		TextView profileNameView = (TextView) row
				.findViewById(R.id.new_botl_realname_new_botl_username_new_botl_title_textView1);
		profileNameView.setTypeface(HomeScreenView.tf);
		profileNameView.setText(bottle.getReal_name());

		/* update username id - singlebotl_profile_username */
		TextView userNameView = (TextView) row
				.findViewById(R.id.new_botl_username_new_botl_title_textView1);
		userNameView.setTypeface(HomeScreenView.tf);
		String username = "@" + bottle.getUsername();
		userNameView.setText(username);

		/* update bottle date id - singlebotl_bottle_date */
		TextView dateView = (TextView) row
				.findViewById(R.id.new_botl_datemsg_textView2);
		
		dateView.setTypeface(HomeScreenView.tf);
		String date_msg = bottle.getBottled_date_msg();
		Log.v(TAG, "Data msg: " + date_msg);
		logger.debug("Data msg: " + date_msg);
		dateView.setText(date_msg);

		/* update bottle img id - singlebotl_bottleimage */
		ImageView bottleImg = (ImageView) row
				.findViewById(R.id.new_botl_botlimg_imageView10);
		String localPath = Utils.openBottleLocalPath(bottle.getBotlImageUrl(),
				TAGS.BOTTLE_LARGE_TYPE);
		Log.v(TAG, "Bottle local path: " + localPath);
		bottleImg.setImageDrawable(Utils.loadImgFromAssets(context, localPath));

		/* update likes id - singlebotl_likepanel_likelayout_likes_number */
		TextView likesView = (TextView) row
				.findViewById(R.id.new_botl_likestext_textView4);
		likesView.setText(bottle.getLikeCount());

		/* update view id - singlebotl_likepanel_likelayout_likes_number */
		TextView viewsView = (TextView) row
				.findViewById(R.id.new_botl_viewstext_textView6);
		viewsView.setText(bottle.getLocationsCount());

		/* update miles id - singlebotl_likepanel_likelayout_likes_number */
		TextView milesView = (TextView) row
				.findViewById(R.id.new_botl_milestext_textView7);
		milesView.setText(bottle.getDistance());

		/* update pattern image */
		ImageView pattren_layout_img = (ImageView) row
				.findViewById(R.id.new_botl_pattrenimg_imageView9);

		String patternLocalPath = Utils.createBottlePatternPath(bottle
				.getPattern_url());
		patternLocalPath = TAGS.PATTERNS_FOLDER + TAGS.PATH_SEPERATER
				+ bottle.getPattern_url();
		Log.v(TAG, "pattren local path: " + patternLocalPath);
		Drawable drwable = Utils.loadImgFromAssets(context, patternLocalPath);
		//pattren_layout_img.setBackgroundDrawable(drwable);
		pattren_layout_img.setImageDrawable(drwable);
		// Utils.loadImageFromAssets(context, pattern_img, patternLocalPath);
		// pattren_layout.setBackgroundDrawable(Utils.loadImgFromAssets(context,
		// patternLocalPath));

		// pattern_img.setImageDrawable(Utils.loadImgFromAssets(context,
		// patternLocalPath));

		/* setting bottle in TAGS to acess from open bottle */
		// TAGS.SELECTED_BOTTLE = bottle;

	}
	
}