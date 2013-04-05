package com.bottlr.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class Utils {

	private static final String TAG = "Utils";

	public static long secsToMilliSeconds(long seconds) {
		return (seconds * 1000);
	}

	public static long minitToMilliSeconds(long minits) {
		return (minits * 60 * 1000);
	}

	public static long minitToSeconds(long minits) {
		return (minits * 1000);
	}

	public static long milliToMinits(long millisces) {
		return (millisces / 60000);
	}

	public static long milliToSeconds(long millsecs) {
		return (millsecs / 1000);
	}

	public static Bitmap loadBitmap(String url) throws Exception {
		Bitmap bitmap = null;
		InputStream in = null;
		BufferedOutputStream out = null;

		try {
			int IO_BUFFER_SIZE = 500;
			in = new BufferedInputStream(new URL(url).openStream(),
					IO_BUFFER_SIZE);

			final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
			out = new BufferedOutputStream(dataStream, IO_BUFFER_SIZE);
			// copy(in, out);
			out.flush();

			final byte[] data = dataStream.toByteArray();
			BitmapFactory.Options options = new BitmapFactory.Options();
			// options.inSampleSize = 1;

			bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,
					options);
		} catch (IOException e) {
			Log.e(TAG, "Could not load Bitmap from: " + url);
		} finally {
			in.close();
			out.close();
		}

		return bitmap;
	}

	/* Generate the top large image from image id */
	public static String generateLargeTopImage(String imageName) {

		return URLs.HEADImageBaseURL + imageName;

	}

	public static String generateFullVideoUrl(String videoid, String video_type) {
		String url = "";
		if (video_type.equalsIgnoreCase("youtube") || video_type == "youtube") {
			url = URLs.YOUTUBE_BASEURL + videoid;
			// Log.d(TAG, "Youtube video url: " + url);
		} else if (video_type.equalsIgnoreCase("vimeo")
				|| video_type == "vimeo") {

		} else if (video_type.equalsIgnoreCase("socialcam")
				|| video_type == "socialcam") {

		} else if (video_type.equalsIgnoreCase("soundcloud")
				|| video_type == "soundcloud") {

		}

		return url;
	}

	public static String generateVideoThumbImgUrl(String videoid,
			String video_type) {
		String url = "";
		if (video_type.equalsIgnoreCase("youtube") || video_type == "youtube") {
			url = URLs.YOUTUBE_THUMB_URLBASE + videoid
					+ URLs.YOUTUBE_THUMB_URLEND;
			// Log.d(TAG, "Youtube video thumb url: " + url);
		} else if (video_type.equalsIgnoreCase("vimeo")
				|| video_type == "vimeo") {

		} else if (video_type.equalsIgnoreCase("socialcam")
				|| video_type == "socialcam") {

		} else if (video_type.equalsIgnoreCase("soundcloud")
				|| video_type == "soundcloud") {

		}

		return url;
	}

	public static String generateFullAudioUrl(String audio_url) {

		return audio_url;
	}

	public static Drawable loadImgFromAssets(Context context, String imgpath) {
		// load image
		try {
			// get input stream
			InputStream ims = context.getAssets().open(imgpath);
			// load image as Drawable
			Drawable d = Drawable.createFromStream(ims, null);

			return d;
			// set image to ImageView
			// mImage.setImageDrawable(d);
		} catch (IOException ex) {
			return null;
		}
	}

	public static String openBottleLocalPath(String bottleImgUrl, String type) {

		String path = "";
		if (type.equalsIgnoreCase(TAGS.BOTTLE_LARGE_TYPE))
			path = TAGS.BOTTLE_FOLDER + TAGS.PATH_SEPERATER + bottleImgUrl
					+ TAGS.BOTTLE_LARGE_EXTENSION + TAGS.BOTTLE_TYPE_EXTENSION;
		else if (type.equalsIgnoreCase(TAGS.BOTTLE_SMALL_TYPE))
			path = TAGS.BOTTLE_FOLDER + TAGS.PATH_SEPERATER + bottleImgUrl
					+ TAGS.BOTTLE_SMALL_EXTENSION + TAGS.BOTTLE_TYPE_EXTENSION;

		else if (type.equalsIgnoreCase(TAGS.BOTTLE_OPEN_TYPE))
			path = TAGS.BOTTLE_FOLDER + TAGS.PATH_SEPERATER + bottleImgUrl
					+ TAGS.BOTTLE_OPEN_EXTENSION + TAGS.BOTTLE_TYPE_EXTENSION;

		return path;
	}

}
