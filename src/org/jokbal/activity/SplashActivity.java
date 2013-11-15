package org.jokbal.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import org.jokbal.R;


public class SplashActivity extends Activity
{

	protected void onCreate(Bundle savedInstanceState)
    {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

		Handler handler = new Handler();

		handler.postDelayed(new Runnable() {
			public void run() {
				Intent intent = new Intent(SplashActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		}, 1000);
	}
}
