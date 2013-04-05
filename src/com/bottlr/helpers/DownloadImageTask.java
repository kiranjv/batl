package com.bottlr.helpers;

import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class DownloadImageTask extends AsyncTask<Void, Void, Bitmap> {
	ImageView bmImage;
	private String urldisplay;
	Bitmap mIcon11 = null;
	private ProgressBar progress;

	public DownloadImageTask(ImageView bmImage, ProgressBar progress,String urldisplay) {
		this.bmImage = bmImage;
		this.urldisplay = urldisplay;
		this.progress = progress;
	}



	protected Bitmap doInBackground(Void... params) {

		try {
			InputStream in = new java.net.URL(urldisplay).openStream();
			mIcon11 = BitmapFactory.decodeStream(in);
		} catch (Exception e) {
			Log.e("Error", e.getMessage());
			mIcon11 = null;
			e.printStackTrace();
		}
		return mIcon11;
	}

	protected void onPostExecute(Bitmap result) {
		progress.setVisibility(View.GONE);
		bmImage.setVisibility(View.VISIBLE);
		if(result != null)
		bmImage.setImageBitmap(result);
		
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