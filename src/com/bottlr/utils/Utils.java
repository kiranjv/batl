package com.bottlr.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.bottlr.dataacess.BottleDetails;
import com.bottlr.dataacess.BottlesRepository;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
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

	public static String generateIFrameTag(String video_audio_id, String type) {

		// id_vimeo = "17336587";
		// socail cam ="3Eha19fC";
		String iFrame = null;

		if (type.equalsIgnoreCase(TAGS.BOTTLE_YOUTUBE_TYPE)) {
			iFrame = "<center><iframe src=\"http://www.youtube.com/embed/"
					+ video_audio_id
					+ "?feature=player_detailpage\" frameborder=\"0\" allowfullscreen></iframe></center>";

		} else if (type.equalsIgnoreCase(TAGS.BOTTLE_VAMIEO_TYPE)) {

			// iFrame = "<center><iframe src=\"http://player.vimeo.com/video/"
			// + video_audio_id +
			// "\" frameborder=\"0\" webkitAllowFullScreen mozallowfullscreen allowFullScreen></iframe></center>";

			// iFrame =
			// "<iframe src=\"http://player.vimeo.com/video/17336587\" frameborder=\"0\" webkitAllowFullScreen mozallowfullscreen allowFullScreen></iframe>";

			iFrame = "<center><iframe src=\"http://player.vimeo.com/video/"
					+ video_audio_id
					+ "?player_id=player&title=0&byline=0&portrait=0&autoplay=1&api=1\" ></iframe>";

		}

		else if (type.equalsIgnoreCase(TAGS.BOTTLE_SOCIALCAM_TYPE)) {
			// iFrame = "<center><iframe src=\"https://socialcam.com/v/"
			// + video_audio_id + "?autostart=true\" </iframe></center>";

			iFrame = "<center><iframe src=\"https://socialcam.com/v/"
					+ video_audio_id + "?autostart=true\" </iframe></center>";

		} else if (type.equalsIgnoreCase(TAGS.BOTTLE_SOUNDCLOUD_TYPE)) {
			iFrame = "<center><iframe src=\"https://w.soundcloud.com/player/?url=http%3A%2F%2Fapi.soundcloud.com%2Ftracks%"
					+ video_audio_id + "\"></iframe></center>";

		}

		return iFrame;

	}

	public static boolean isFlashAvailable(Context context) {
		String mVersion;
		try {
			mVersion = context.getPackageManager().getPackageInfo(
					"com.adobe.flashplayer", 0).versionName;
			Log.d("Flash", "Installed: " + mVersion);
			return true;
		} catch (NameNotFoundException e) {
			Log.d("Flash", "Not installed");
			return false;
		}
	}
	
	public static BottleDetails getBottleDetails(Context context, String bottle_id) {
		BottlesRepository bottle_repo = new BottlesRepository(context);
		return bottle_repo.searchBottle(bottle_id);
	}
}
