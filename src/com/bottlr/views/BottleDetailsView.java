package com.bottlr.views;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.InputFilter.LengthFilter;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bottlr.R;
import com.bottlr.dataacess.BottleDetails;
import com.bottlr.helpers.DownloadImageTask;
import com.bottlr.imgloader.ImageLoader;
import com.bottlr.utils.TAGS;
import com.bottlr.utils.UIUtils;
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

	private TextView messagesView;
	private WebView webView;
	private ImageView openedBottleImage;
	private ImageView headderImage;
	private ImageLoader imageLoader;

	static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(
			ViewGroup.LayoutParams.MATCH_PARENT,
			ViewGroup.LayoutParams.MATCH_PARENT);

	public static BottleDetails CURRENT_OPEN_BOTTLE;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bottledetils_layout);

		imageLoader = new ImageLoader(this);
		initGUI(savedInstanceState);
		configureGUI(savedInstanceState);

		gestureScanner = new GestureDetector(this);

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
	}

	private void configureGUI(Bundle savedInstanceState) {

		Log.v(TAG, "------Bottle details-----------");
		String botlType = CURRENT_OPEN_BOTTLE.getBotlType();
		Log.v(TAG, "Bottle id: " + CURRENT_OPEN_BOTTLE.getBottle_id());
		Log.v(TAG, "Bottle type: " + botlType);
		logger.debug("Bottle id: " + CURRENT_OPEN_BOTTLE.getBottle_id());
		logger.debug("Bottle type: " + botlType);
		if (botlType.equalsIgnoreCase("image")
				|| botlType.equalsIgnoreCase("AudioUrl")) {
			// read json key value audiofrom
			String audio_id = CURRENT_OPEN_BOTTLE.getAudio_url();
			String audio_from = "Soundcloud";

			Log.v(TAG, "Audio id: " + audio_id + " Audio from: " + audio_from);

			if (!CURRENT_OPEN_BOTTLE.getFull_top_image_url().equalsIgnoreCase(
					"")) {
				isHeadderShow = true;
			}
			if (audio_from.equalsIgnoreCase("Soundcloud")
					&& !audio_id.equalsIgnoreCase("")) {
				Log.v(TAG, "Create sound cloud iFrame data. ");
				isWebShow = true;
				webViewIFrameData = Utils.generateIFrameTag(audio_id,
						audio_from);

			} else {
				if (!audio_id.equalsIgnoreCase("")) {
					Log.v(TAG, "Create mp3 link of server");
					isWebShow = false;
					webViewIFrameData = null;
				} else {
					Log.v(TAG, "Show image only.");
					isWebShow = false;
					webViewIFrameData = null;
				}
			}
		}

		else if (botlType.equalsIgnoreCase("video")) {
			String video_id = CURRENT_OPEN_BOTTLE.getVidUrl();
			String video_from = CURRENT_OPEN_BOTTLE.getVidfrom();
			Log.v(TAG, "Video id: " + video_id + " Video from: " + video_from);
			webViewIFrameData = Utils.generateIFrameTag(video_id, video_from);
			isWebShow = true;

		}

		Toast.makeText(this,
				"isWebShow: " + isWebShow + " isHeadderShow:" + isHeadderShow,
				Toast.LENGTH_LONG).show();
		Log.v(TAG, "isWebShow: " + isWebShow + " isHeadderShow:"
				+ isHeadderShow);
		logger.debug("isWebShow: " + isWebShow + " isHeadderShow:"
				+ isHeadderShow);
		Log.v(TAG, "iFrame Data: " + webViewIFrameData);

		if (isHeadderShow) {
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
			if (botlType.equalsIgnoreCase(""))
				headderImage.setVisibility(View.GONE);
			configureWebView();
		} else {
			webView.setVisibility(View.GONE);
		}

		configureMessageView();
		// myLayout.addView(messagesView, ViewGroup.LayoutParams.WRAP_CONTENT,
		// ViewGroup.LayoutParams.WRAP_CONTENT);
		Toast.makeText(this, "Bottle details opened", Toast.LENGTH_SHORT)
				.show();
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
			webView.setBackgroundColor(Color.BLACK);
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

			

			webViewIFrameData = "<iframe allowfullscreen=\"allowfullscreen\" frameborder=\"0\" height=\"391px\" marginheight=\"0\" marginwidth=\"0\" scrolling=\"no\" src=\"http://socialcam.com/videos/HSXAcuWQ/embed?utm_campaign=external&amp;utm_source=api\" width=\"520px\"></iframe>";
			//webViewIFrameData = "<iframe allowfullscreen=\"allowfullscreen\" frameborder=\"0\" height=\"391px\" marginheight=\"0\" marginwidth=\"0\" scrolling=\"no\" src=\"http://cdn.viddy.com/media/video/964c032b-28ce-4d97-aece-950d33b20a32-high.mp4?t=635017344652830000\" width=\"520px\"></iframe>";
			
			Log.v(TAG, "Web view iframe data: " + webViewIFrameData);
			logger.debug("WebView Data: " + webViewIFrameData);
			webView.loadData(webViewIFrameData, "text/html", "utf-8");
			// webView.loadUrl("http://www.viddy.com/embed/video/0b2b103a-0c40-48a4-877a-64645ef5a0ae");
		} else {
			UIUtils.OkDialog(this, "Flash player is not installed. ");
			logger.debug("Flash player not installed.");
		}

	}

	private void initGUI_old(Bundle savedInstanceState) {

		// startActivity(new Intent(Intent.ACTION_VIEW,
		// Uri.parse("http://player.vimeo.com/video/27244727")));
		// LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
		// ViewGroup.LayoutParams.MATCH_PARENT,
		// ViewGroup.LayoutParams.MATCH_PARENT);
		// LinearLayout myLayout = new LinearLayout(this);
		// myLayout.setLayoutParams(params);

		// HTML5WebView mWebView = new HTML5WebView(this);
		// myLayout.addView(mWebView.getLayout(), 250, 150);

		// HTML5WebView mWebView = (HTML5WebView)
		// findViewById(R.id.html5WebView);
		// if (savedInstanceState != null) {
		// mWebView.restoreState(savedInstanceState);
		// } else {
		// mWebView.loadUrl("http://player.vimeo.com/video/27244727");
		// }

		// LinearLayout layout = SELECTED_LAYOUT;
		// ImageView openbottle = (ImageView) findViewById(R.id.bottleImage);
		// Drawable drawble = Utils.loadImgFromAssets(this,
		// "bottles/botl-001-open.png");
		// openbottle.setImageDrawable(drawble);

		// Resources r = getResources();
		// Drawable[] layers = new Drawable[2];
		// layers[0] = r.getDrawable(R.drawable.bottle_img);
		// layers[1] = r.getDrawable(R.drawable.eye);
		// LayerDrawable layerDrawble = new LayerDrawable(layers);
		// openbottle.setImageDrawable(layerDrawble);

		webView = (WebView) findViewById(R.id.bottledetails_webiew);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setAppCacheEnabled(true);
		webView.getSettings().setDomStorageEnabled(true);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setBuiltInZoomControls(true);

		webView.setWebChromeClient(new WebChromeClient() {
		});
		// how to enable the webview plugin changed in API 8
		if (Build.VERSION.SDK_INT < 8) {
			webView.getSettings().setPluginsEnabled(true);
		} else {
			webView.getSettings().setPluginState(PluginState.ON);
		}

		// webView.setWebViewClient(new WebViewClient() {
		// @Override
		// public boolean shouldOverrideUrlLoading(WebView view, String url) {
		// if (url.contains("vimeo.com")) {
		//
		// webView.loadUrl(url
		// + "?player_id=player&title=0&byline=0&portrait=0&autoplay=1&api=1");
		// // System.out.println("override url is:::"+url);
		// }
		// return false;
		// }
		//
		// });

		// webView.loadData("<iframe src=\"http://player.vimeo.com/video/25349114\"></iframe>",
		// "text/html", "utf-8");
		// http://www.youtube.com/watch?v=MTTyLUIchNo&wide=1
		// <iframe width="100%" height="166" scrolling="no" frameborder="no"
		// src="https://w.soundcloud.com/player/?url=http%3A%2F%2Fapi.soundcloud.com%2Ftracks%2F78558263"></iframe>
		// <iframe src=\"http://www.google.com\"></iframe>"
		// webView.loadData("<iframe src=\"http://www.google.com\"></iframe>",
		// "text/html","utf-8");
		// String webData =
		// "<iframe src=\"http://player.vimeo.com/video/25349114\"></iframe>";
		// <iframe
		// src=\"https://w.soundcloud.com/player/?url=http%3A%2F%2Fapi.soundcloud.com%2Ftracks%2F78558263\"></iframe>
		// String iFrameData =
		// "<center><iframe src=\"http://www.youtube.com/watch?v=MTTyLUIchNo&wide=1\"></iframe></center>";
		// "<center><iframe src=\"http://www.youtube.com/embed/ChHijYrsdsY?feature=player_detailpage\" frameborder=\"0\" allowfullscreen></iframe></center>";

		Log.e(TAG, "isFalsh Player Available: " + Utils.isFlashAvailable(this));
		String video_audio_id = "25349114";

		String iFrameData = Utils.generateIFrameTag(video_audio_id,
				TAGS.BOTTLE_VIMEO_TYPE);
		Log.e(TAG, "iFrame data: " + iFrameData);

		webView.loadData(iFrameData, "text/html", "utf-8");
		// webView.loadUrl("http://player.vimeo.com/video/24577973?player_id=player&autoplay=1&title=0&byline=0&portrait=0&api=1&maxheight=480&maxwidth=800");

		// HTML5WebView mWebView = (HTML5WebView)
		// findViewById(R.id.html5WebView);

		String messageHeadder = "<h1> Message:</h1> \n";
		String mesaageData = messageHeadder
				+ "<p>\n\t<span style=\"font-size:11px;\"><span style=\"font-family: lucida sans unicode,lucida grande,sans-serif;\">While he attempts to put pressure on Congress to pass federal measures that would increase gun control, President Obama is visiting Colorado today to draw attention to its recently passed gun control laws.</span></span></p>\n<p>\n\t<span style=\"font-size:11px;\"><span style=\"font-family: lucida sans unicode,lucida grande,sans-serif;\">Despite the state&#39;s tradition of hunting and historically highly valued gun ownership rights, Colorado expanded its restrictions on magazines and expanded background checks with a bill passed two weeks ago. Obama will visit community leaders and law enforcement officers not far from Aurora, where James Holmes killed 12 people in a movie theater last summer (prosecutors announced this week that they would pursue the death penalty for Holmes).</span></span></p>\n<p>\n\t<span style=\"font-size:11px;\"><span style=\"font-family: lucida sans unicode,lucida grande,sans-serif;\">The President is advocating for Congress to at least vote on a ban of assault weapons, limits on large-capacity ammunition magazines, and universal background checks on gun buyers. The background check requirement is the most important component of the law to gun control advocates, and while the issue divides congress, 90 percent of Americans polled in public surveys support expanded background checks.</span></span></p>\n<p>\n\t<span style=\"font-size:11px;\"><span style=\"font-family: lucida sans unicode,lucida grande,sans-serif;\">Obama has made a series of high-profile appearances over the past few weeks to advocate for gun control. As he called for legislation last week, he stood with 21 mothers who have lost their children to gun violence, saying: &quot;I haven&#39;t forgotten those kids.&quot;</span></span></p>\n<p>\n\t<br />\n\t<span style=\"font-size:11px;\"><span style=\"font-family: lucida sans unicode,lucida grande,sans-serif;\">- Maggie Lange<br />\n\tABC</span></span></p>\n<div class=\"post-body\">\n\t<h1>\n\t\t<span style=\"font-size:8px;\"><a class=\"plus-icon modfont\">Maggie Lange</a></span></h1>\n</div>\n<p>\n\t&nbsp;</p>\n";
		// String mesaageData = messageHeadder +
		// CURRENT_OPEN_BOTTLE.getMessage();
		TextView messagesView = (TextView) findViewById(R.id.bottledetails_messages_filed);
		messagesView.setMovementMethod(new ScrollingMovementMethod());
		messagesView.setText(Html.fromHtml(mesaageData));
		// myLayout.addView(messagesView, ViewGroup.LayoutParams.WRAP_CONTENT,
		// ViewGroup.LayoutParams.WRAP_CONTENT);
		Toast.makeText(this, "Bottle details opened", Toast.LENGTH_SHORT)
				.show();
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
		webView.destroy();

		super.onPause();
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
}
