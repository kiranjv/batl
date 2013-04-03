package com.bottlr.views;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bottlr.R;
import com.bottlr.utils.Utils;

public class BottleDetailsView extends Activity implements OnGestureListener {

	private static final int SWIPE_MIN_DISTANCE = 6;// 120;
	private static final int SWIPE_MAX_OFF_PATH = 125;// 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 100;// 200;
	private static final String TAG = "BottleDetailsView";
	public static LinearLayout SELECTED_LAYOUT = null;
	private GestureDetector gestureScanner;

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

		initGUI();

		gestureScanner = new GestureDetector(this);

	}

	private void initGUI() {
		LinearLayout layout = SELECTED_LAYOUT;
		ImageView openbottle = (ImageView) findViewById(R.id.bottleImage);
//		Drawable drawble = Utils.loadImgFromAssets(this, "bottles/botl-001-open.png");
//		openbottle.setImageDrawable(drawble);
		
		Resources r = getResources();
		Drawable[] layers = new Drawable[2];
		layers[0] = r.getDrawable(R.drawable.bottle_img);
		layers[1] = r.getDrawable(R.drawable.eye);
		LayerDrawable layerDrawble = new  LayerDrawable(layers);
		openbottle.setImageDrawable(layerDrawble);
		
		
		WebView webView = (WebView) findViewById(R.id.bottledetails_webiew);
		WebSettings webViewSettings = webView.getSettings();
		webViewSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		webViewSettings.setJavaScriptEnabled(true);
		
		webViewSettings.setBuiltInZoomControls(true);
		
		// how to enable the webview plugin changed in API 8
		if (Build.VERSION.SDK_INT < 8) {
			webViewSettings.setPluginsEnabled(true);
		} else {
			webViewSettings.setPluginState(PluginState.ON);
		}
		
		//<iframe width="100%" height="166" scrolling="no" frameborder="no" src="https://w.soundcloud.com/player/?url=http%3A%2F%2Fapi.soundcloud.com%2Ftracks%2F78558263"></iframe>
		//<iframe src=\"http://www.google.com\"></iframe>"
		//webView.loadData("<iframe src=\"http://www.google.com\"></iframe>", "text/html","utf-8");
		//String webData = "<iframe src=\"http://player.vimeo.com/video/25349114\"></iframe>";
		String webData = "<iframe src=\"https://w.soundcloud.com/player/?url=http%3A%2F%2Fapi.soundcloud.com%2Ftracks%2F78558263\"></iframe>";
		webView.loadData(webData, "text/html",
                "utf-8");
		
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
