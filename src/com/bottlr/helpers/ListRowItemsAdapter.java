package com.bottlr.helpers;

import java.util.ArrayList;

import com.bottlr.R;
import com.bottlr.dataacess.BottleDetails;
import com.bottlr.utils.TAGS;
import com.bottlr.utils.URLs;
import com.bottlr.utils.Utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

		if (position >= ALLOWED_ROWS)
			return row;

		/* first layout items update. */

		BottleDetails firstItemBottle = firstBottleArrayList.get(position);
		updateFirstLayout(row, firstItemBottle);

		/* second layout items update. */
		if(position < secondBottleArrayList.size()) {
		BottleDetails secondItemBottle = secondBottleArrayList.get(position);
		updateSecondLayout(row, secondItemBottle);
		}

		return row;
	}

	private void updateFirstLayout(View row, BottleDetails bottle) {
		/* update top image. */
		ImageView headderImage = (ImageView) row
				.findViewById(R.id.sing_botl_mainTopImage);
		ProgressBar progress = (ProgressBar) row
				.findViewById(R.id.single_botl_Img_progressBar1);
		new DownloadImageTask(headderImage, progress,
				bottle.getFull_top_image_url())
				.execute();

		/* update bottle title (id - sing_botl_profiledesc) */
		TextView titleView = (TextView) row
				.findViewById(R.id.sing_botl_profiledesc);
		titleView.setText(bottle.getTitle());

		/* update profile img id - singlebotl_profile_img */
		ImageView profileImg = (ImageView) row
				.findViewById(R.id.singlebotl_profile_img);

		/* update profile name id = singlebotl_profile_firstname */
		TextView profileNameView = (TextView) row
				.findViewById(R.id.singlebotl_profile_firstname);
		profileNameView.setText("bottle king");

		/* update username id - singlebotl_profile_username */
		TextView userNameView = (TextView) row
				.findViewById(R.id.singlebotl_profile_username);
		userNameView.setText("bottle king");

		/* update bottle date id - singlebotl_bottle_date */
		TextView dateView = (TextView) row
				.findViewById(R.id.singlebotl_bottle_date);
		userNameView.setText(bottle.getDateCreated());

		/* update bottle img id - singlebotl_bottleimage */
		ImageView bottleImg = (ImageView) row
				.findViewById(R.id.singlebotl_bottleimage);
		String localPath = Utils.openBottleLocalPath(bottle.getBotlImageUrl(),
				TAGS.BOTTLE_LARGE_TYPE);
		bottleImg.setImageDrawable(Utils.loadImgFromAssets(context, localPath));

		/* update likes id - singlebotl_likepanel_likelayout_likes_number*/
		TextView likesView = (TextView) row
				.findViewById(R.id.singlebotl_likepanel_likelayout_likes_number);
		likesView.setText(bottle.getLikeCount());
		
		/* update view id - singlebotl_likepanel_likelayout_likes_number*/
		TextView viewsView = (TextView) row
				.findViewById(R.id.singlebotl_viewspanel_viewlayout_views_number);
		viewsView.setText(bottle.getLocationsCount());
		
		/* update miles id - singlebotl_likepanel_likelayout_likes_number*/
		TextView milesView = (TextView) row
				.findViewById(R.id.singlebotl_milespanel_milelayout_miles_number);
		milesView.setText(bottle.getDistance());
		
		
		/*setting bottle id for invisible textview*/
		TextView bottleView = (TextView) row
				.findViewById(R.id.singlebottle_clicked_bottleid);
		bottleView.setText(bottle.getBottle_id());

	}

	private void updateSecondLayout(View row, BottleDetails secondItemBottle) {
		// Log.v(TAG, "secondItemBottle: " + secondItemBottle);
		ImageView mainImage2 = (ImageView) row
				.findViewById(R.id.second_sing_botl_mainTopImage);
		ProgressBar second_progress = (ProgressBar) row
				.findViewById(R.id.second_single_botl_Img_progressBar1);
		new DownloadImageTask(mainImage2, second_progress,
				"http://c801459.r59.cf2.rackcdn.com/940d5158_large.gif")
				.execute();

		TextView likesView2 = (TextView) row
				.findViewById(R.id.second_singlebotl_likepanel_likelayout_likes_number);
		likesView2.setText(secondItemBottle.getLikeCount());

		TextView secondbottleidView = (TextView) row
				.findViewById(R.id.second_bottle_clicked_bottleid);
		secondbottleidView.setText(secondItemBottle.getBottle_id());

	}

}
