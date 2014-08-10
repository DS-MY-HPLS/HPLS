package com.hp.hpls3;

import com.hp.hpls3.androidFeatures.AndroidFeatures;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class WelcomeScreen extends Activity {

	AndroidFeatures adFt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome_screen);
		adFt = new AndroidFeatures(getApplicationContext());
//		setContentView();
	}
	
	public void onClick (View v)
	{
		switch (v.getId())
		{
			case R.id.btnVisualSearch:
				adFt.showToastMethod("VisualSearch");
				break;
			case R.id.btnResultPage:
				adFt.showToastMethod("ResultPage");
				break;
			default:
				break;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
