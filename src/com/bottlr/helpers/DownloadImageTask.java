package com.bottlr.helpers;

import java.io.InputStream;

import com.bottlr.R;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class DownloadImageTask extends AsyncTask<Void, Void, Bitmap> {
	private static final String TAG = "DownloadImageTask";
	ImageView bmImage;
	private String urldisplay;
	Bitmap mIcon11 = null;
	private ProgressBar progress;
	private String failure_message = "";
	private Context context;

	public DownloadImageTask(Context context, ImageView bmImage, ProgressBar progress,String urldisplay) {
		this.context = context;
		this.bmImage = bmImage;
		this.urldisplay = urldisplay;
		this.progress = progress;
	}



	protected Bitmap doInBackground(Void... params) {

		try {
			BitmapFactory.Options bmOptions = new BitmapFactory.Options();
			bmOptions.inSampleSize = 2; // 1 = 100% if you write 4 means 1/4 = 25% 
			InputStream in = new java.net.URL(urldisplay).openStream();
			mIcon11 = BitmapFactory.decodeStream(in);
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
		return mIcon11;
	}

	protected void onPostExecute(Bitmap result) {
		
		bmImage.setVisibility(View.VISIBLE);
		if(result != null)
		bmImage.setImageBitmap(result); 
		else {
			bmImage.setImageResource(R.drawable.nopreview);
			if(!failure_message.equalsIgnoreCase("")) {
				//Toast.makeText(context, failure_message, Toast.LENGTH_LONG).show();
			} 
		}
		
	}

	/**
	 * @return the mIcon11
	 */
	public Bitmap getmIcon11() {
		return mIcon11;
	}

	/**
	 * @param mIcon11
	 *            the mIcon11 to set
	 */
	public void setmIcon11(Bitmap mIcon11) {
		this.mIcon11 = mIcon11;
	}

}