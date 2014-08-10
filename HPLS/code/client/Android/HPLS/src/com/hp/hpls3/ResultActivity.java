package com.hp.hpls3;

import com.hp.hpls3.defineData.DefineData;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.os.Build;

public class ResultActivity extends Activity {
	
	DefineData resultDd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activiry_result);
//		Intent rsIntent = getIntent();
//		resultDd = (DefineData)rsIntent.getParcelableExtra("resultDd");
		Log.d("resultPage", "oncreate");
		
		try{
			Intent rsIntent = this.getIntent();
			resultDd = (DefineData)rsIntent.getParcelableExtra("toResult");
	//		resultDd = (DefineData)rsIntent.getParcelableExtra("resultDd");
			Log.d("resultPage", "gotoWeb");
		}
		catch(Exception ex)
		{
			Log.e("exception caught", ex.getLocalizedMessage());
		}
	}
	
	public void gotoWeb(View v)
	{
		try{
			Intent rsIntent = this.getIntent();
			resultDd = rsIntent.getParcelableExtra("toResult");
	//		resultDd = (DefineData)rsIntent.getParcelableExtra("resultDd");
			Log.d("resultPage", "gotoWeb");
		}
		catch(Exception ex)
		{
			Log.e("exception caught", ex.getLocalizedMessage());
		}
	}

}
