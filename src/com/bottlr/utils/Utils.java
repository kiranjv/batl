package com.bottlr.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bottlr.dataacess.BottleDetails;
import com.bottlr.dataacess.BottlesRepository;
import com.bottlr.helpers.WebServiceRequesterHelper;

public class Utils {

	static private final Logger logger = LoggerFactory.getLogger(Utils.class);

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

	public static String generateVideoThumbImgUrl(Context context,
			String videoid, String video_type, JSONObject json_bottle) {
		String url = "";
		if (video_type.equalsIgnoreCase("youtube") || video_type == "youtube") {
			url = URLs.YOUTUBE_THUMB_URLBASE + videoid
					+ URLs.YOUTUBE_THUMB_URLEND;
			 Log.d(TAG, "Youtube video thumb url: " + url);
		} else if (video_type.equalsIgnoreCase("vimeo")
				|| video_type == "vimeo") {
			url = getJsonValue(json_bottle, "vimeoimg");

		} else if (video_type.equalsIgnoreCase("socialcam")
				|| video_type == "socialcam") {
			// url = getJsonValue(json_bottle, "vimeoimg");
			url = WebServiceRequesterHelper.getInstance(context)
					.getSocailCamThumbImageAPI(videoid);
		}

		else if (video_type.equalsIgnoreCase("viddy") || video_type == "viddy") {
			url = "http://cdn-edc-ns.viddy.com/images/video/" + videoid
					+ ".jpg";

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
			ex.printStackTrace();
			return null;
		}
	}

	public static void loadImageFromAssets(Context context,
			ImageView image_view, String img_path) {
		InputStream bitmap_ip = null;
		try {
			bitmap_ip = context.getAssets().open(img_path);
			Bitmap bitmap = BitmapFactory.decodeStream(bitmap_ip);
			image_view.setImageBitmap(bitmap);
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (bitmap_ip != null) {
				try {
					bitmap_ip.close();
				} catch (IOException e) {
					e.printStackTrace();
					e.printStackTrace();
				}
			}
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

	public static String createBottlePatternPath(String patternImgUrl) {

		String path = TAGS.PATTERNS_FOLDER + TAGS.PATH_SEPERATER
				+ patternImgUrl;

		return path;
	}

	public static String generateIFrameTag(String video_audio_id, String type) {

		// id_vimeo = "17336587";
		// socail cam ="3Eha19fC";
		String iFrame = null;

		if (type.equalsIgnoreCase(TAGS.BOTTLE_YOUTUBE_TYPE)) {
			iFrame = "<center><iframe width=\"1050\" height=\"950\" src=\"http://www.youtube.com/embed/"
					+ video_audio_id
					+ "?feature=player_detailpage\" frameborder=\"0\" allowfullscreen></iframe></center>";

		} else if (type.equalsIgnoreCase(TAGS.BOTTLE_VIMEO_TYPE)) {

			// iFrame = "<center><iframe src=\"http://player.vimeo.com/video/"
			// + video_audio_id +
			// "\" frameborder=\"0\" webkitAllowFullScreen mozallowfullscreen allowFullScreen></iframe></center>";

			// iFrame =
			// "<iframe src=\"http://player.vimeo.com/video/17336587\" frameborder=\"0\" webkitAllowFullScreen mozallowfullscreen allowFullScreen></iframe>";

			// iFrame = "<center><iframe src=\"http://player.vimeo.com/video/"
			// + video_audio_id
			// +
			// "?player_id=player&title=0&byline=0&portrait=0&autoplay=1&api=1\" ></iframe>";

			// working one
			iFrame = "<center><iframe frameborder=\"0\" width=\"1050\" height=\"950\" src=\"http://player.vimeo.com/video/"
					+ video_audio_id + "\"></iframe></center>";

		}

		else if (type.equalsIgnoreCase(TAGS.BOTTLE_SOCIALCAM_TYPE)) {
			// iFrame = "<center><iframe src=\"https://socialcam.com/v/"
			// + video_audio_id + "?autostart=true\" </iframe></center>";

			// iFrame = "<center><iframe src=\"http://socialcam.com/v/"
			// + video_audio_id + "?autostart=true\" </iframe></center>";

			// iFrame =
			// "<iframe width=\"450\" height=\"350\" src=\"http://socialcam.com/videos/3Eha19fC/embed?utm_campaign=external&utm_source=api\" scrolling=\"no\"></iframe>";

			// video_audio_id = "HSXAcuWQ";
			// working url
			iFrame = "<center><iframe allowfullscreen=\"allowfullscreen\" frameborder=\"0\" height=\"1050\" marginheight=\"0\" marginwidth=\"0\" scrolling=\"no\" src=\"http://socialcam.com/videos/"
					+ video_audio_id
					+ "/embed?utm_campaign=external&amp;utm_source=api\" width=\"950\"></iframe></center>";

		}

		else if (type.equalsIgnoreCase(TAGS.BOTTLE_VIDDY_TYPE)) {
			// iFrame =
			// "<iframe width=\"850\" height=\"650\" src=\"http://www.viddy.com/embed/video/0b2b103a-0c40-48a4-877a-64645ef5a0ae\"></iframe>";
			// iFrame =
			// "<iframe width=\"850\" height=\"650\" src=\"http://cdn.viddy.com/media/video/964c032b-28ce-4d97-aece-950d33b20a32-high.mp4?t=635017344652830000\"></iframe>";
			iFrame = "http://cdn.viddy.com/media/video/" + video_audio_id
					+ "-high.mp4?t=635017344652830000";
		}

		else if (type.equalsIgnoreCase(TAGS.BOTTLE_SOUNDCLOUD_TYPE)) {
			String url = "";
			if (video_audio_id.contains("http://soundcloud.com/")) {
				url = "http://testbotl.com:8080/soundcloudapi?soundcloudurl="
						+ video_audio_id;
				Log.v(TAG, "URL: " + url);

			} else {
				url = "http://testbotl.com:8080/soundcloudapi?trackid="
						+ video_audio_id;
				Log.v(TAG, "URL: " + url);
			}

			try {
				iFrame = new soundapi().execute(url).get();
				Log.v(TAG, "Response: " + iFrame);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		// else if (type.equalsIgnoreCase(TAGS.BOTTLE_SOUNDCLOUD_TYPE)) {
		// if (video_audio_id.contains("http://soundcloud.com/")) {
		// // String soundcloud_url =
		// // "http://soundcloud.com/moontide/dirty-water-standells-cover";
		// iFrame =
		// "<iframe id=\"iframesoundcloud\" width=\"1200\" height=\"250\" scrolling=\"no\" frameborder=\"no\" src=\"http://w.soundcloud.com/player/?url="
		// + video_audio_id
		// +
		// "&amp;auto_play=true&amp;show_artwork=false&amp;color=ff7700\"></iframe>";
		// } else {
		// iFrame =
		// "<center><iframe width=\"1200\" height=\"250\" src=\"https://w.soundcloud.com/player/?url=http%3A%2F%2Fapi.soundcloud.com%2Ftracks%2F"
		// + video_audio_id + "\"></iframe></center>";
		// // iFrame =
		// //
		// "<center><iframe width=\"100\" height=\"166\" scrolling=\"no\" frameborder=\"no\" src=\"https://api.soundcloud.com/tracks/3100297/stream?client_id=0995afa4963d1c7a0b9041b8efa08fc6\"></iframe></center>";
		// // iFrame =
		// //
		// "<html><body><object height=\"81\" width=\"100\" id=\"yourPlayerId\" classid=\"clsid:D27CDB6E-AE6D-11cf-96B8-444553540000\"><param name=\"movie\" value=\"http://player.soundcloud.com/player.swf?url=http%3A%2F%2Fsoundcloud.com%2Fmatas%2Fhobnotropic&amp;enable_api=true&amp;object_id=yourPlayerId\"></param><param name=\"allowscriptaccess\" value=\"always\"></param><embed allowscriptaccess=\"always\" height=\"81\" src=\"http://player.soundcloud.com/player.swf?url=http%3A%2F%2Fsoundcloud.com%2Fmatas%2Fhobnotropic&amp;enable_api=true&amp;object_id=yourPlayerId\" type=\"application/x-shockwave-flash\" width=\"100\" name=\"yourPlayerId\"></embed></object></body></html>";
		// }
		//
		// }

		return iFrame;

	}

	private static class soundapi extends AsyncTask<String, String, String> {

		private String failureMessage;

		@Override
		protected String doInBackground(String... params) {
			String url = params[0];
			String data = getData(url);
			String file_name = Environment.getExternalStorageDirectory()+"/code.html";
			String filename = writeToFile(file_name, data);

			return filename;
		}

		/**
		 * Prints some data to a file using a BufferedWriter
		 * 
		 * @return
		 */
		public String writeToFile(String filename, String data) {
			File myFile = new File(filename);
			try {
				myFile.createNewFile();
				FileOutputStream fOut = new FileOutputStream(myFile);
				OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
				myOutWriter.write(data);
				myOutWriter.close();
				fOut.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return filename;
		}

		private String getData(String url) {
			DefaultHttpClient client = new DefaultHttpClient();
			HttpConnectionParams
					.setConnectionTimeout(client.getParams(), 10000); // Timeout

			QueryString queryString = new QueryString();
			// queryString.add("lat", "" + latitude);
			// queryString.add("lng", "" + longitude);
			// queryString.add("distance", "" + radius);

			Log.v(TAG, "URL: " + url);
			HttpGet getRequest = new HttpGet(url);
			// getRequest.setHeader("Content-Type", "application/json");

			String result = null;

			try {
				HttpResponse response = client.execute(getRequest);
				Log.v(TAG, "Server response: " + response);
				if (response == null) {

					result = null;
					failureMessage = "Server sent null response.";

				} else if (response.getStatusLine().getStatusCode() != 200) {
					response = null;
					result = null;
					failureMessage = "Bottle download fail. Response code: "
							+ response.getStatusLine().getStatusCode();
				} else {
					result = EntityUtils.toString(response.getEntity());
				}

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return result;
		}

	}

	private static String soundCloudScrip() {
		String data = "<html><body><script src=\"http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js\"></script> <script src=\"http://connect.soundcloud.com/sdk.js\"></script> <script>SC.initialize({client_id: \"0995afa4963d1c7a0b9041b8efa08fc6\"});SC.get(\"/tracks/293\", {limit: 1}, function(tracks){SC.oEmbed(tracks.uri, document.getElementById(\"track\"));});</script> <div id=\"track\"></div></body></html>";

		return data;

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

	public static BottleDetails getBottleDetails(Context context,
			String bottle_id) {
		BottlesRepository bottle_repo = new BottlesRepository(context);
		return bottle_repo.searchBottle(bottle_id);
	}

	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}

	public static String getJsonValue(JSONObject json_object, String key_value) {
		String value = "";
		try {
			if (!json_object.isNull(key_value)
					&& !json_object.getString(key_value).equalsIgnoreCase(""))
				value = json_object.getString(key_value);
			return value;
		} catch (JSONException e) {

			e.printStackTrace();

		}
		return value;
	}

	public static int getDownloadOldBottlesCount() {
		TAGS.CURRENT_SYNC_OLD_BOTTLE_COUNT += TAGS.SYNC_BOTTLE_OFFSET;
		return TAGS.CURRENT_SYNC_OLD_BOTTLE_COUNT;
	}
	
	public static void setFontDroidSans(TextView textView) {
	    Typeface tf = Typeface.createFromAsset(textView.getContext()
	            .getAssets(), "fonts/DroidSansMono.ttf");

	    textView.setTypeface(tf);

	}
}
