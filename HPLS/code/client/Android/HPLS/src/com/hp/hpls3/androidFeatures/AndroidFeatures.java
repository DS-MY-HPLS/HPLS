package com.hp.hpls3.androidFeatures;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class AndroidFeatures {
	
	private static String message;
	private static Context context;
	
	public AndroidFeatures()
	{
		
	}
	
	public AndroidFeatures(Context context)
	{
		AndroidFeatures.context = context;
	}
	
	public AndroidFeatures(Context context, String message)
	{
		AndroidFeatures.context = context;
		AndroidFeatures.message = message;
	}
	
	public static void showToastMethod(String message) {
		AndroidFeatures.message = message;
		showToastMethod();
	}
	
	public static void showToastMethod() {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
  }
}
