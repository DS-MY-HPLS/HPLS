// Copyright 2007-2014 metaio GmbH. All rights reserved.
package com.hp.hpls3;


import android.view.View;

import com.metaio.sdk.ARELActivity;


public class ARELViewActivity extends ARELActivity 
{
	@Override
	protected int getGUILayout() 
	{
		return R.layout.arel;
	}
	
	public void onButtonClick(View v)
	{
		finish();
	}
 
}
