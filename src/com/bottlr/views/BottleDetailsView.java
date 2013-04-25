package com.bottlr.views;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.text.InputFilter.LengthFilter;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bottlr.R;
import com.bottlr.dataacess.BottleDetails;
import com.bottlr.helpers.DownloadImageTask;
import com.bottlr.helpers.WebServiceRequesterHelper;
import com.bottlr.imgloader.ImageLoader;
import com.bottlr.utils.TAGS;
import com.bottlr.utils.UIUtils;
import com.bottlr.utils.URLs;
import com.bottlr.utils.Utils;

public class BottleDetailsView extends Activity implements OnGestureListener {

	static private final Logger logger = LoggerFactory
			.getLogger(BottleDetailsView.class);

	private static final int SWIPE_MIN_DISTANCE = 6;// 120;
	private static final int SWIPE_MAX_OFF_PATH = 125;// 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 100;// 200;
	private static final String TAG = "BottleDetailsView";
	public static LinearLayout SELECTED_LAYOUT = null;
	private GestureDetector gestureScanner;

	private String webViewIFrameData = "";
	private boolean isWebShow = false;
	private boolean isHeadderShow = false;
	private boolean isVideoShow = false;

	private TextView messagesView;
	private WebView webView;
	private ImageView openedBottleImage;
	private ImageView headderImage;
	private ImageLoader imageLoader;

	private VideoView mVideoView;

	private ImageButton mPlay;

	private ImageButton mPause;

	private ImageButton mReset;

	private ImageButton mStop;

	private String viddyvideolink;

	protected String current;

	private LinearLayout video_include_layout;

	private ProgressDialog progressDialog;

	private ImageButton mVideoView_Play;

	private TextView medianame_textview;

	static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(
			ViewGroup.LayoutParams.MATCH_PARENT,
			ViewGroup.LayoutParams.MATCH_PARENT);

	public static BottleDetails CURRENT_OPEN_BOTTLE;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		setContentView(R.layout.bottledetils_layout);

		imageLoader = new ImageLoader(this);
		long initGUI_start = System.currentTimeMillis();
		initGUI(savedInstanceState);
		long initGUI_end = System.currentTimeMillis();
		Log.e(TAG,
				"Init Gui Time: "
						+ Utils.milliToSeconds((initGUI_end - initGUI_start)));
		logger.info("Init Gui Time: "
				+ Utils.milliToSeconds((initGUI_end - initGUI_start)));
		// configureGUI(savedInstanceState);

		gestureScanner = new GestureDetector(this);
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void onStart() {

		super.onStart();
		long initConf_start = System.currentTimeMillis();
		configureGUI();
		long initConf_end = System.currentTimeMillis();
		Log.e(TAG,
				"Init Gui Time: "
						+ Utils.milliToSeconds((initConf_end - initConf_start)));
		logger.info("Init Gui Time: "
				+ Utils.milliToSeconds((initConf_end - initConf_start)));
	}

	private void initGUI(Bundle savedInstanceState) {
		openedBottleImage = (ImageView) findViewById(R.id.bottledetails_open_botl_img);
		String localPath = Utils.openBottleLocalPath(
				CURRENT_OPEN_BOTTLE.getBotlImageUrl(), TAGS.BOTTLE_OPEN_TYPE);
		Log.v(TAG, "Bottle local path: " + localPath);

		openedBottleImage.setImageDrawable(Utils.loadImgFromAssets(this,
				localPath));

		webView = (WebView) findViewById(R.id.bottledetails_webiew);
		headderImage = (ImageView) findViewById(R.id.bottledetails_bottleHImage);
		messagesView = (TextView) findViewById(R.id.bottledetails_messages_filed);

		// load videoview layout
		// video_include_layout = (LinearLayout)
		// findViewById(R.id.bottledetails_videoview_layout);
		video_include_layout = (LinearLayout) findViewById(R.id.bottle_details_videoview_layout);
		mVideoView = (VideoView) findViewById(R.id.bottle_detail_surface_view);

		// mVideoViewThumb = (ImageView)
		// findViewById(R.id.bottle_detailoutside_imageview);
		mVideoView_Play = (ImageButton) findViewById(R.id.bottle_detail_videoview_playbutton);
		mVideoView_Play.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				playVideo();
			}
		});

		medianame_textview = (TextView) findViewById(R.id.bottle_detail_media_name);

		// mPath.setText("http://logisticinfotech.com/extra/Veer.mp4");
		// mPath.setText("http://www.youtube.com/watch?v=rkOySwlEtVk&amp;feature=youtube_gdata_player");
		// mPath.setText("http://socialcam.edgesuite.net/videos/2013-4-9/XA4ybJ8S.mp4");
		// mPath.setText("http://cdn.viddy.com/media/video/964c032b-28ce-4d97-aece-950d33b20a32-high.mp4?t=635017344652830000");
		viddyvideolink = "http://cdn.viddy.com/media/video/964c032b-28ce-4d97-aece-950d33b20a32-high.mp4?t=635017344652830000";
		mPlay = (ImageButton) findViewById(R.id.bottle_detail_play);
		mPause = (ImageButton) findViewById(R.id.bottle_detail_pause);
		mReset = (ImageButton) findViewById(R.id.bottle_detail_reset);
		mStop = (ImageButton) findViewById(R.id.bottle_detail_stop);
		mPlay.setVisibility(View.GONE);
		mPause.setVisibility(View.GONE);
		mReset.setVisibility(View.GONE);
		mStop.setVisibility(View.GONE);

		mPlay.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				playVideo();
			}
		});
		mPause.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				if (mVideoView != null) {
					mVideoView.pause();
				}
			}
		});
		mReset.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				if (mVideoView != null) {
					mVideoView.seekTo(0);
				}
			}
		});
		mStop.setOnClickListener(new OnClickListener() {

			public void onClick(View view) {
				if (mVideoView != null) {
					current = null;
					mVideoView.stopPlayback();
				}
			}
		});

		// runOnUiThread(new Runnable() {
		// public void run() {
		// playVideo();
		//
		// }
		//
		// });

		video_include_layout.setVisibility(View.GONE);

	}

	@TargetApi(11)
	private void configureGUI() {

		Log.v(TAG, "------Bottle details-----------");
		String botlType = CURRENT_OPEN_BOTTLE.getBotlType();
		Log.v(TAG, "Bottle id: " + CURRENT_OPEN_BOTTLE.getBottle_id());
		Log.v(TAG, "Bottle type: " + botlType);
		Log.v(TAG,
				"Headder image url: "
						+ CURRENT_OPEN_BOTTLE.getFull_top_image_url());
		Log.v(TAG, "Headder image url: " + CURRENT_OPEN_BOTTLE.getVideoType());
		logger.debug("Bottle id: " + CURRENT_OPEN_BOTTLE.getBottle_id());
		logger.debug("Bottle type: " + botlType);

		if (botlType.equalsIgnoreCase("image")
				|| botlType.equalsIgnoreCase("imageurl")
				|| botlType.equalsIgnoreCase("AudioUrl")
				|| botlType.equalsIgnoreCase("audio")) {
			// read json key value audiofrom
			String audio_id = CURRENT_OPEN_BOTTLE.getAudio_url();
			String audio_from = CURRENT_OPEN_BOTTLE.getAudio_from();// "Soundcloud";

			Log.v(TAG, "Audio id: " + audio_id + " Audio from: " + audio_from);

			if (!CURRENT_OPEN_BOTTLE.getFull_top_image_url().equalsIgnoreCase(
					"")) {
				isHeadderShow = true;
			}
			if (audio_from.equalsIgnoreCase("Soundcloud")
					&& !audio_id.equalsIgnoreCase("")) {
				Log.v(TAG, "Create sound cloud iFrame data. ");
				isWebShow = true;
				isVideoShow = false;
				webViewIFrameData = Utils.generateIFrameTag(audio_id,
						audio_from);

			} else {
				if (!audio_id.equalsIgnoreCase("")) {
					Log.v(TAG, "Create mp3 link of server");
					isWebShow = false;
					isVideoShow = true;
					// mVideoViewThumb.setVisibility(View.VISIBLE);
					// webViewIFrameData = getMp3FullURL("36");
					// String audiourl = WebServiceRequesterHelper.getInstance(
					// getApplicationContext()).getMP3AudioAPI(audio_id);
					AsyncDownloader asyncDownloader = new AsyncDownloader(
							audio_id, "audio");
					String audiourl = null;
					try {
						audiourl = asyncDownloader.execute().get();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					// make videoview smaller

					LinearLayout.LayoutParams videoviewlp = new LinearLayout.LayoutParams(
							mVideoView.getWidth(), 50);
					// mVideoView.setLayoutParams(videoviewlp);
					medianame_textview.setText(TAGS.CURRENT_MP3_FileName);
					Log.v(TAG, "Audio url: " + audiourl);
					webViewIFrameData = audiourl;
				} else {
					Log.v(TAG, "Show image only.");
					isWebShow = false;
					isVideoShow = false;
					webViewIFrameData = null;
				}
			}
		}

		else if (botlType.equalsIgnoreCase("video")) {
			String video_id = CURRENT_OPEN_BOTTLE.getVidUrl();
			String video_from = CURRENT_OPEN_BOTTLE.getVidfrom();
			if (!video_from.equalsIgnoreCase("viddy")) {

				Log.v(TAG, "Video id: " + video_id + " Video from: "
						+ video_from);
				webViewIFrameData = Utils.generateIFrameTag(video_id,
						video_from);
				isWebShow = true;
			} else {
				isWebShow = false;
				isVideoShow = true;
				LinearLayout.LayoutParams videoviewlp = new LinearLayout.LayoutParams(
						mVideoView.getWidth(), 160);

				medianame_textview.setVisibility(View.GONE);
				// mVideoViewThumb.setVisibility(View.GONE);
				// mVideoView.setLayoutParams(videoviewlp);
				webViewIFrameData = Utils.generateIFrameTag(video_id,
						video_from);
			}

		} else if (botlType != null && botlType.equalsIgnoreCase("widget")) {
			isWebShow = false;
			isVideoShow = false;
			isHeadderShow = true;
		}

//		Toast.makeText(this,
//				"isWebShow: " + isWebShow + " isHeadderShow:" + isHeadderShow,
//				Toast.LENGTH_LONG).show();
		Log.v(TAG, "isWebShow: " + isWebShow + " isHeadderShow:"
				+ isHeadderShow);
		logger.debug("isWebShow: " + isWebShow + " isHeadderShow:"
				+ isHeadderShow);
		Log.v(TAG, "iFrame Data: " + webViewIFrameData);

		if (isHeadderShow) {
//			Toast.makeText(this, "showing headder image..", Toast.LENGTH_SHORT)
//					.show();
			Log.v(TAG,
					"Headder image url: "
							+ CURRENT_OPEN_BOTTLE.getFull_top_image_url());
			headderImage.setVisibility(View.VISIBLE);
			// new DownloadImageTask(this, headderImage, null,
			// CURRENT_OPEN_BOTTLE.getFull_top_image_url()).execute();
			imageLoader.DisplayImage(
					CURRENT_OPEN_BOTTLE.getFull_top_image_url(), headderImage);
		} else {
			headderImage.setVisibility(View.GONE);
		}

		// configure webview based on flags
		if (isWebShow && webViewIFrameData != null) {
//			Toast.makeText(this, "showing web bottle for.." + botlType,
//					Toast.LENGTH_SHORT).show();
			if (botlType.equalsIgnoreCase(""))
				headderImage.setVisibility(View.GONE);
			configureWebView();
		} else {
			webView.setVisibility(View.GONE);

		}

		// configure videoview
		if (isVideoShow && webViewIFrameData != null) {
			// show videoview
//			Toast.makeText(this,
//					"showing video view. Data: " + webViewIFrameData,
//					Toast.LENGTH_SHORT).show();
			video_include_layout.setVisibility(View.VISIBLE);
			// playVideo();
			new Timer().schedule(new TimerTask() {

				@Override
				public void run() {
					runOnUiThread(new Runnable() {
						public void run() {
							// playVideo();

						}

					});
				}
			}, 1000);

		}

		configureMessageView();
		// myLayout.addView(messagesView, ViewGroup.LayoutParams.WRAP_CONTENT,
		// ViewGroup.LayoutParams.WRAP_CONTENT);
//		Toast.makeText(this, "Bottle details opened", Toast.LENGTH_SHORT)
//				.show();
		Log.v(TAG, "-------------------------------");
	}

	private void configureMessageView() {
		// show messages of the bottle.
		String messageHeadder = "<h1> Message:</h1> \n";
		String bottle_message = CURRENT_OPEN_BOTTLE.getMessage();
		String mesaageData = "<h1> No Messages.</h1>";
		if (!bottle_message.equalsIgnoreCase("")) {
			mesaageData = messageHeadder + bottle_message;
		}

		// String mesaageData = messageHeadder +
		// CURRENT_OPEN_BOTTLE.getMessage();

		messagesView.setMovementMethod(new ScrollingMovementMethod());
		messagesView.setText(Html.fromHtml(mesaageData));

	}

	private void configureWebView() {
		// check for flash player
		boolean isFlashPlayer = Utils.isFlashAvailable(this);
		if (isFlashPlayer) {
			Log.e(TAG, "Falsh Player Available. ");
			logger.debug("Flash player available");
			// configure webview

			webView.getSettings().setJavaScriptEnabled(true);
			webView.getSettings().setAppCacheEnabled(true);
			webView.getSettings().setDomStorageEnabled(true);
			webView.getSettings().setLoadWithOverviewMode(true);
			webView.getSettings().setUseWideViewPort(true);
			webView.getSettings().setBuiltInZoomControls(false);
			webView.getSettings().setLoadsImagesAutomatically(true);
			webView.getSettings()
					.setJavaScriptCanOpenWindowsAutomatically(true);
			webView.getSettings().setAllowFileAccess(true);
			webView.setInitialScale(60);
			webView.setBackgroundColor(Color.WHITE);
			getWindow().addFlags(128);
			webView.getSettings()
					.setUserAgentString(
							"Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2 (.NET CLR 3.5.30729)");
			webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

			webView.setWebChromeClient(new WebChromeClient() {

				@Override
				public void onShowCustomView(View view,
						CustomViewCallback callback) {
					// TODO Auto-generated method stub
					super.onShowCustomView(view, callback);
					if (view instanceof FrameLayout) {
						FrameLayout frame = (FrameLayout) view;
						if (frame.getFocusedChild() instanceof VideoView) {
							VideoView video = (VideoView) frame
									.getFocusedChild();
							frame.removeView(video);
							video.start();

						}
					}

				}

				@Override
				public boolean onJsAlert(WebView view, String url,
						String message, final android.webkit.JsResult result) {
					Log.d(TAG, message);
					new AlertDialog.Builder(view.getContext())
							.setMessage(message).setCancelable(true).show();
					result.confirm();
					return true;
				}
			});
			// how to enable the webview plugin changed in API 8
			if (Build.VERSION.SDK_INT < 8) {
				webView.getSettings().setPluginsEnabled(true);
			} else {
				webView.getSettings().setPluginState(PluginState.ON);
			}

			// webViewIFrameData =
			// "<iframe allowfullscreen=\"allowfullscreen\" frameborder=\"0\" height=\"391px\" marginheight=\"0\" marginwidth=\"0\" scrolling=\"no\" src=\"http://socialcam.com/videos/HSXAcuWQ/embed?utm_campaign=external&amp;utm_source=api\" width=\"520px\"></iframe>";
			// webViewIFrameData =
			// "<iframe allowfullscreen=\"allowfullscreen\" frameborder=\"0\" height=\"391px\" marginheight=\"0\" marginwidth=\"0\" scrolling=\"no\" src=\"http://cdn.viddy.com/media/video/964c032b-28ce-4d97-aece-950d33b20a32-high.mp4?t=635017344652830000\" width=\"520px\"></iframe>";
			// webViewIFrameData =
			// "<iframe allowfullscreen=\"allowfullscreen\" frameborder=\"0\" height=\"391px\" marginheight=\"0\" marginwidth=\"0\" scrolling=\"no\" src=\"http://cdn.viddy.com/media/video/964c032b-28ce-4d97-aece-950d33b20a32-high.mp4\" width=\"520px\"></iframe>";
			// webViewIFrameData =
			// "<iframe width=\"640\" height=\"640\" src=\"http://www.viddy.com/embed/video/25517315-b52c-45b3-a62f-99b766abfaf8\" frameborder=\"0\" allowfullscreen></iframe>";
			Log.v(TAG, "Web view iframe data: " + webViewIFrameData);
			logger.debug("WebView Data: " + webViewIFrameData);
			// webView.loadData(webViewIFrameData, "text/html", "utf-8");
			// webView.loadUrl("https://w.soundcloud.com/player/?url=https://soundcloud.com/cnn/newsday042313");
			// webView.loadUrl("file:///android_asset/code.html");
			// webView.loadUrl("https://soundcloud.com/cnn/newsday042313");
			// webView.loadUrl(webViewIFrameData);
			//webView.loadUrl("file://"+webViewIFrameData);
			if (webViewIFrameData.contains("sdcard")) {
				Log.v(TAG, "Html path: "+webViewIFrameData);
				webView.loadUrl("file://"+webViewIFrameData);
			} else {
				webView.loadData(webViewIFrameData, "text/html", "utf-8");
			}

		} else {
			UIUtils.OkDialog(this, "Flash player is not installed. ");
			logger.debug("Flash player not installed.");
		}

	}

	@Override
	protected void onDestroy() {
		gestureScanner = null;
		super.onDestroy();
	}

	@Override
	public boolean onTouchEvent(MotionEvent me) {
		return gestureScanner.onTouchEvent(me);
	}

	// @Override
	public boolean onDown(MotionEvent e) {
		// viewA.setText("-" + "DOWN" + "-");
		return true;
	}

	// @Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		try {
			if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
				return false;
			// right to left swipe
			if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				Toast.makeText(getApplicationContext(), "Left Swipe",
						Toast.LENGTH_SHORT).show();

			} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				Toast.makeText(getApplicationContext(), "Right Swipe",
						Toast.LENGTH_SHORT).show();
				navigateToHomeScreen();

			} else if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				Toast.makeText(getApplicationContext(), "Swipe up",
						Toast.LENGTH_SHORT).show();

			} else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				Toast.makeText(getApplicationContext(), "Swipe down",
						Toast.LENGTH_SHORT).show();

			}
		} catch (Exception e) {
			// nothing
		}

		return true;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		Toast mToast = Toast.makeText(getApplicationContext(), "Long Press",
				Toast.LENGTH_SHORT);
		mToast.show();
	}

	// @Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// viewA.setText("-" + "SCROLL" + "-");
		return true;
	}

	// @Override
	public void onShowPress(MotionEvent e) {
		// viewA.setText("-" + "SHOW PRESS" + "-");
	} // @Override

	public boolean onSingleTapUp(MotionEvent e) {
		// Toast mToast = Toast.makeText(getApplicationContext(), "Single Tap",
		// Toast.LENGTH_SHORT).show();

		return true;
	}

	@Override
	protected void onPause() {
		Log.e(TAG, "onpause");
		//
		super.onPause();

		if (webView != null) {
//			webView.clearCache(true);
//			webView.freeMemory();
			deleteDatabase("webview.db");
			deleteDatabase("webviewCache.db");
			webView.destroy();
			webView = null;
		}
	}

	@Override
	protected void onStop() {
		Log.e(TAG, "onstop");
		super.onStop();
	}

	private void navigateToHomeScreen() {
		Intent intent = new Intent(BottleDetailsView.this, HomeScreenView.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		this.finish();
	}

	private void playVideo() {
		try {
			// final String path = viddyvideolink;
			final String videoPath = webViewIFrameData;

			progressDialog = ProgressDialog.show(this, "",
					"Buffering media...", true);
			progressDialog.setCancelable(true);

			getWindow().setFormat(PixelFormat.TRANSLUCENT);
			MediaController mediaController = new MediaController(this);

			mediaController.setAnchorView(mVideoView);

			Uri video = Uri.parse(videoPath);
			mVideoView.setMediaController(mediaController);
			mVideoView.setVideoURI(video);
			mVideoView.requestFocus();
			mVideoView.setOnPreparedListener(new OnPreparedListener() {

				public void onPrepared(MediaPlayer mp) {
					progressDialog.dismiss();
					mVideoView.start();
				}
			});

			//
			// System.out.println("path --> " + path);
			// Log.v(TAG, "path: " + path);
			// if (path == null || path.length() == 0) {
			// Toast.makeText(this, "File URL/path is empty",
			// Toast.LENGTH_LONG).show();
			//
			// } else {
			// // If the path has not changed, just start the media player
			// if (path.equals(current) && mVideoView != null) {
			// mVideoView.start();
			// mVideoView.requestFocus();
			// return;
			// }
			// current = path;
			// System.out.println("Current path --> " + path);
			// mVideoView.setVideoPath(getDataSource(path));
			// mVideoView.start();
			// mVideoView.requestFocus();
			//
			// System.out.println("end try in play");

		} catch (Exception e) {
			Log.e(TAG, "error: " + e.getMessage(), e);
			if (mVideoView != null) {
				mVideoView.stopPlayback();
			}
		}
	}

	private String getDataSource(String path) throws IOException {
		if (!URLUtil.isNetworkUrl(path)) {
			return path;
		} else {
			URL url = new URL(path);
			URLConnection cn = url.openConnection();
			cn.connect();
			InputStream stream = cn.getInputStream();
			if (stream == null)
				throw new RuntimeException("stream is null");
			File temp = File.createTempFile("mediaplayertmp", "mp4");
			System.out.println("hi");
			temp.deleteOnExit();
			String tempPath = temp.getAbsolutePath();

			FileOutputStream out = new FileOutputStream(temp);
			byte buf[] = new byte[128];
			do {
				try {
					int numread = stream.read(buf);
					if (numread <= 0)
						break;
					out.write(buf, 0, numread);
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(),
							"connection timeout.", Toast.LENGTH_SHORT).show();
					logger.error("Connection time out for viddy video...");
					e.printStackTrace();
				}
			} while (true);
			try {
				stream.close();
			} catch (IOException ex) {
				Log.e(TAG, "error: " + ex.getMessage(), ex);
			}
			return tempPath;
		}
	}

	private String getMp3FullURL(String aid) {
		return URLs.BOTTLE_DIRECT_AUDIO_BASE_URL + "JingleBells.mp3";
	}

	private class AsyncDownloader extends AsyncTask<Void, Void, String> {

		private String process_name;
		private String video_audio_id;

		public AsyncDownloader(String video_audio_id, String process_name) {
			this.video_audio_id = video_audio_id;
			this.process_name = process_name;
		}

		@Override
		protected String doInBackground(Void... params) {
			String result = "";
			if (process_name.equalsIgnoreCase("audio")) {
				result = WebServiceRequesterHelper.getInstance(
						getApplicationContext()).getMP3AudioAPI(video_audio_id);
			} else if (process_name.equalsIgnoreCase("video")) {
				result = WebServiceRequesterHelper.getInstance(
						getApplicationContext()).getMP3AudioAPI(video_audio_id);
			}
			return result;
		}

	}
}
