package com.bottlr.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.bottlr.R;

public class LoginView extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);
		initGUI();
		
	}

	private void initGUI() {
		Button login_button = (Button) findViewById(R.id.LoginRetriveProfileButton);
		login_button.setOnClickListener(new LoginclickListener());
		
	}
	
	
	private class LoginclickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			
			Intent intent = new Intent(LoginView.this,
					HomeScreenView.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			
		}
		
	}
	
}
