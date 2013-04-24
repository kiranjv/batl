package com.bottlr.views;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bottlr.R;
import com.bottlr.dataacess.BottleDetails;
import com.bottlr.dataacess.BottlesStoreManager;
import com.bottlr.dataacess.DBAdapter;

import com.bottlr.helpers.BottleParseHelper;
import com.bottlr.network.BottlesDownloadModel;
import com.bottlr.utils.URLs;

public class SplashScreenView extends Activity {

	static private final Logger logger = LoggerFactory
			.getLogger(SplashScreenView.class);
	protected static final String TAG = "SplashScreenView";

	Handler handler;
	Runnable runnable;
	private Context context;
	private ProgressThread mThread;
	private ProgressBar progressDialog;
	private String failureMSG;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashscreen_layout);

		context = SplashScreenView.this;

		progressDialog = (ProgressBar) findViewById(R.id.splashscreen_ProgressBar01);

		DBAdapter dbAdapter = new DBAdapter(context);
		try {

			dbAdapter.closeDatabase();
			dbAdapter.open();
		} catch (Exception e) {
			e.printStackTrace();
		}

		mThread = new ProgressThread();
		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				try {
					progressDialog.clearAnimation();
					startOpenBottleView();
				} catch (Exception e) {
					Log.e(TAG, "Exception while dismissing progress dialog");
					e.printStackTrace();
				}
				if (mThread.isAlive()) {
					mThread = new ProgressThread();
				}
			}
		};

		mThread.start();

	}

	protected void startOpenBottleView() {
		Log.d(TAG, "Starting press bottle cap view");
		Intent intent = new Intent(SplashScreenView.this,
				PressBottleCapView.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);

	}

	private class ProgressThread extends Thread {

		private List<BottleDetails> parsedBottles;

		ProgressThread() {
		}

		public void run() {
			try {
				Looper.prepare();
				Log.v(TAG, "Download bottles");
				BottlesDownloadModel download = new BottlesDownloadModel(
						context);
				// download inital botls with 0
				// String url = URLs.BOTTLE_KING_OLD_BOTTLE_URL + 0;
				String url = URLs.USER_OLD_BOTTLE_URL + 0;
				logger.debug("Initial url: " + url);
				String bottles = download.downloadBottlesJson(url);
				logger.debug("Initial url response: " + bottles);
				Log.v(TAG, "URL: " + url);

				if (bottles == null) {
					failureMSG = download.getFailureMessage();
					Toast.makeText(context,
							"No bottles downloaded. " + failureMSG,
							Toast.LENGTH_LONG).show();

				} else {
					BottleParseHelper bottlesParser = new BottleParseHelper(
							context);
					parsedBottles = bottlesParser.parseBottles(bottles);
					Log.e(TAG,
							"Downloaded bottles size: " + parsedBottles.size());
					Log.e(TAG, "Downloaded bottles json: " + bottles);
					Toast.makeText(context,
							"Downloaded bottles: " + parsedBottles.size(),
							Toast.LENGTH_LONG).show();
					// bottlesParser.storeBottleLocal(parsedBottles);
					BottlesStoreManager.getStoreInstance(context)
							.storeBottlesLast(parsedBottles);
				}

			} catch (Exception e) {
				Log.e(TAG, "Exception while getting profile information");
				e.printStackTrace();
			}

			handler.sendEmptyMessage(0);
			Looper.loop();
		}
	}

}
