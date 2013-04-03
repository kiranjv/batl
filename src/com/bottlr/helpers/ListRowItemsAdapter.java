package com.bottlr.helpers;

import java.util.ArrayList;

import com.bottlr.R;
import com.bottlr.dataacess.BottleDetails;



import android.annotation.TargetApi;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ListRowItemsAdapter extends BaseAdapter {

	private LayoutInflater l_Inflater;
	private Context context;
	private static ArrayList<BottleDetails> itemDetailsrrayList;

	public ListRowItemsAdapter(Context context, ArrayList<BottleDetails> results) {
		this.context = context;
		itemDetailsrrayList = results;
		l_Inflater = LayoutInflater.from(this.context);
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
		View row = l_Inflater.inflate(R.layout.listrow_layout, null);
		//LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.listrow_firstitem);
		
//		ImageView mainImage = (ImageView) layout.findViewById(R.id.sing_botl_mainTopImage);
//		mainImage.setOnClickListener(new OnClickListener() {
//			
//			@TargetApi(11)
//			@Override
//			public void onClick(View v) {
//				Toast.makeText(context, "main image clicked.", Toast.LENGTH_SHORT).show();
//			}
//	});
////		
////		
//		TextView textview = (TextView) layout.findViewById(R.id.singlebotl_likepanel_likelayout_likes_number);
//		textview.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				TextView tv = (TextView) v;
//				tv.setText("1234");
//				Toast.makeText(context, "Likes: "+tv.getText(), Toast.LENGTH_SHORT).show();
//				
//			}
//		});
		
		return row;
	}

	
	
}
