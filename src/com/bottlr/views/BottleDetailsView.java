package com.bottlr.views;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
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

import com.bottlr.R;
import com.bottlr.dataacess.BottleDetails;
import com.bottlr.utils.TAGS;
import com.bottlr.utils.Utils;

public class BottleDetailsView extends Activity implements OnGestureListener {

	private static final int SWIPE_MIN_DISTANCE = 6;// 120;
	private static final int SWIPE_MAX_OFF_PATH = 125;// 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 100;// 200;
	private static final String TAG = "BottleDetailsView";
	public static LinearLayout SELECTED_LAYOUT = null;
	private GestureDetector gestureScanner;
	private WebView webView;

	static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(
			ViewGroup.LayoutParams.MATCH_PARENT,
			ViewGroup.LayoutParams.MATCH_PARENT);

	public static BottleDetails CURRENT_OPEN_BOTTLE;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bottledetils_layout);

		if (SELECTED_LAYOUT == null) {
			Log.e(TAG, "Selected layout is null");
			Toast.makeText(getApplicationContext(), "Selected layout empty.",
					Toast.LENGTH_SHORT).show();
			return;
		}

		initGUI(savedInstanceState);

		gestureScanner = new GestureDetector(this);

	}

	private void initGUI(Bundle savedInstanceState) {

		
		//startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://player.vimeo.com/video/27244727")));
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

//		webView.setWebViewClient(new WebViewClient() {
//			@Override
//			public boolean shouldOverrideUrlLoading(WebView view, String url) {
//				if (url.contains("vimeo.com")) {
//
//					webView.loadUrl(url
//							+ "?player_id=player&title=0&byline=0&portrait=0&autoplay=1&api=1");
//					// System.out.println("override url is:::"+url);
//				}
//				return false;
//			}
//
//		});
		
		
		webView.loadData("<iframe src=\"http://player.vimeo.com/video/25349114\"></iframe>", "text/html", "utf-8");
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
		String video_audio_id = "MTTyLUIchNo";

		String iFrameData = Utils.generateIFrameTag(video_audio_id,
				TAGS.BOTTLE_YOUTUBE_TYPE);
		Log.e(TAG, "iFrame data: " + iFrameData);

		//webView.loadData(iFrameData, "text/html", "utf-8");
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

	private void navigateToHomeScreen() {
		Intent intent = new Intent(BottleDetailsView.this, HomeScreenView.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);

	}
}
