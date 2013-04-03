package com.bottlr.views;

import com.bottlr.R;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PressBottleCapView extends Activity {

	private ImageView bottle_button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pressbottlecap_layout);
		final TextView open_text = (TextView) findViewById(R.id.pressbottle_opentext);
		bottle_button = (ImageView) findViewById(R.id.presscap_bottlebutton);

		bottle_button.setOnTouchListener(new OnTouchListener() {
			 @Override
	            public boolean onTouch(View arg0, MotionEvent arg1) {
	                switch (arg1.getAction()) {
	                case MotionEvent.ACTION_DOWN: {
	                	bottle_button.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bottlecap_click));
	                	open_text.setTextColor(0xFF000000); //black
	                	openBottle();
	                	break;
	                }
	                case MotionEvent.ACTION_UP:{
	                	bottle_button.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bottlecap_unclick));
	                	open_text.setTextColor(0xFFFFFFFF); //white
	                	break;
	                }
	                }
	                return true;
	            }
		}
			
);

	}

	protected void openBottle() {
		Toast.makeText(getApplicationContext(), "Openning bottle..",
				Toast.LENGTH_LONG).show();
		//show login screen
		Intent intent = new Intent(PressBottleCapView.this,
				HomeScreenView.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);

	}

}
