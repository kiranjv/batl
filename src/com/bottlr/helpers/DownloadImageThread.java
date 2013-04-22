package com.bottlr.helpers;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class DownloadImageThread implements Runnable {

	private static final String TAG = null;
	private Context context;
	private ImageView bmImage;
	private String urldisplay;
	private String failure_message;

	public DownloadImageThread(Context context, ImageView bmImage,String urldisplay) {
		this.context = context;
		this.bmImage = bmImage;
		this.urldisplay = urldisplay;
	}
	
	@Override
	public void run() {
		Bitmap mIcon11;
		try {
			InputStream in = new java.net.URL(urldisplay).openStream();
			mIcon11 = BitmapFactory.decodeStream(in);
			bmImage.setImageBitmap(mIcon11); 
		} 
		catch (OutOfMemoryError e) {
			Log.e(TAG, "Out memory exception raised");
			failure_message = "Device is out of memory";
			mIcon11 = null;
			e.printStackTrace();
		}
		catch (Exception e) {
			Log.e("Error", e.getMessage());
			mIcon11 = null;
			e.printStackTrace();
		}
		
	}

}
