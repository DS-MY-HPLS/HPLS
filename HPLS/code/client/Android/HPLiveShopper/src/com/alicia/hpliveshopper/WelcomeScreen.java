package com.alicia.hpliveshopper;


import com.alicia.defineData.DefineData;
import com.hp.androidFeature.AndroidFeatures;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class WelcomeScreen extends Activity {

	AndroidFeatures adFt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_screen);
		adFt = new AndroidFeatures(getApplicationContext());
		new Handler().postDelayed(new Runnable() {
       	 
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
 
            @Override
            public void run() {
                Intent i = new Intent(WelcomeScreen.this, MainActivity.class);
                startActivity(i);
 
                // close this activity
                finish();
            }
        }, 3000);
//		setContentView();
	}
	
	public void onClick (View v)
	{
		switch (v.getId())
		{
			case R.id.btnVisualSearch:
				adFt.showToastMethod("VisualSearch");
				Log.d("onClick", "visualSearch");
				Intent vsIntent = new Intent(WelcomeScreen.this, MainActivity.class);
				startActivity(vsIntent);
				break;
			case R.id.btnResultPage:
				adFt.showToastMethod("ResultPage");
				Log.d("onClick", "ResultPage");
				
				String JsonValue = "{" +
									"\"ar_type\": \"prod\"," +
									"\"code\": \"F9J18UA\"," +
									"\"title\": \"HP Pavilion 11 x360 Convertible Laptop\"," +
									"\"price\": \"USD$399.00\"," +
									"\"description\": \"Easily convert from laptop to stand to tablet mode with this amazingly value-packed convertible PC, featuring a 360-degree hinge. With optimized touchscreen performance and BeatsAudio™, all your productivity and entertainment needs are at your fingertips.\"," +
									"\"url_images\": \"http://product-images.www8-hp.com/digmedialib/prodimg/lowres/c03584783.png\"," +
									"\"url_datasheet\": \"http://www8.hp.com/h20195/v2/GetPDF.aspx/c04172423.pdf\"," +
									"\"url_video\": \"\"," +
									"\"url_buynow\": \"http://www8.hp.com/us/en/store-finder/find.do?bs=SR2&type=reseller&sku=F9J18UA\"," +
									"\"url_reseller\": \"https://goo.gl/maps/CeVPj\"" +
								  "}";
				Log.d("JSON", JsonValue);
				
				DefineData dD = new DefineData(JsonValue);
				Intent rsIntent = new Intent(WelcomeScreen.this, ResultActivity.class);
				rsIntent.putExtra("toNextScreenDd", (Parcelable)dD);
				startActivity(rsIntent);
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
