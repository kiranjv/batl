package com.bottlr.views;

import java.util.concurrent.ExecutionException;

import com.bottlr.R;
import com.bottlr.R.layout;
import com.bottlr.R.menu;
import com.bottlr.dataacess.DBAdapter;
import com.bottlr.helpers.AsyncBottleDownload;
import com.bottlr.utils.Utils;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;

public class SplashScreenView extends Activity {

	protected static final String TAG = "SplashScreenView";
	private static long WAITING_TIME = 5; // sec's
	Handler handler;
	Runnable runnable;
	private Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashscreen_layout);

		context = SplashScreenView.this;

		DBAdapter dbAdapter = new DBAdapter(context);
		try {

			dbAdapter.closeDatabase();
			dbAdapter.open();
		} catch (Exception e) {
			e.printStackTrace();
		}

		downloadBottles(30);

		
		

		try {
			Thread.sleep(Utils.secsToMilliSeconds(5));
			loadPressBottleView();

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void downloadBottles(int bottle_count) {
		AsyncBottleDownload downloadTask = new AsyncBottleDownload(context,
				bottle_count);
		downloadTask.execute();

	}

	private void loadPressBottleView() {
		runnable = new Runnable() {

			@Override
			public void run() {
				startOpenBottleView();

			}
		};
		if (handler != null) {
			handler.removeCallbacks(runnable);
			handler = null;
		}
		handler = new Handler();

		handler.postDelayed(runnable, Utils.secsToMilliSeconds(WAITING_TIME));

	}

	protected void startOpenBottleView() {
		Log.d(TAG, "Starting press bottle cap view");
		Intent intent = new Intent(SplashScreenView.this,
				PressBottleCapView.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);

	}

}
