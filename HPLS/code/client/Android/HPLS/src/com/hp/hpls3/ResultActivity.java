package com.hp.hpls3;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;

import com.hp.hpls3.defineData.DefineData;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.os.Build;

public class ResultActivity extends Activity {
	
	DefineData resultDd;
	static String rootDirectory = "hplsVisualSearch";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activiry_result);
		Log.d("resultPage", "oncreate");
		
		try{
			Intent rsIntent = this.getIntent();
			resultDd = (DefineData)rsIntent.getParcelableExtra("toResult");			
			initializeActivity();
			Log.d("resultPage", "onCreateEnd");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			Log.e("exception caught", ex.getLocalizedMessage());
		}
	}
	
	public void initializeActivity()
	{
		try
		{
			ImageView ivProduct = (ImageView)findViewById(R.id.ivProduct);
			if(resultDd.getUrl_images() != null || "".equalsIgnoreCase(resultDd.getUrl_images()))
			{
//				String[] images = resultDd.getUrl_images().split("\\|");
//				Log.d("image", images[0]);
//				ivProduct.getLayoutParams().width = 250;
//				ivProduct.getLayoutParams().height = 250;
//				BitmapWorkerTask mainPic = new BitmapWorkerTask( ivProduct );
//				mainPic.execute(images[0]);
			}
			
			TextView tvProdName = (TextView)findViewById(R.id.tvProdName);
			tvProdName.setText(resultDd.getTitle());
			
			TextView tvDescription = (TextView)findViewById(R.id.tvDescription);
			tvDescription.setText(resultDd.getDescription());
			
			TextView tvPrice = (TextView)findViewById(R.id.tvPrice);
			tvPrice.setText(resultDd.getPrice());
		}
		catch(Exception ex){
			ex.printStackTrace();
			Log.e("assetManager", ex.getLocalizedMessage());
		}
	}
	
	public void gotoWeb(View v)
	{
		String urlLink = null;
		try
		{
			switch (v.getId()) {
			
			case R.id.btnBuy:
				urlLink = resultDd.getUrl_buynow();
				break;
				
			case R.id.btnLocation:
				urlLink = resultDd.getUrl_reseller();
				break;
				
			case R.id.btnSpec:
				urlLink = resultDd.getUrl_datasheet();
				break;
				
			case R.id.btnVideo:
				urlLink = resultDd.getUrl_video();
				break;

			default:
				break;
			}
			Log.d("resultPage", "gotoWeb");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			Log.e("exception caught", ex.getLocalizedMessage());
		}
		finally
		{
			Log.d("urlLink", urlLink);
			if(urlLink != null || !urlLink.isEmpty())
			{
				Uri uri = Uri.parse( urlLink );
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
		}
	}
	
	private class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {

		ProgressDialog progressDialog;
		private final WeakReference<ImageView> imageViewReference;
		private String data;

		public BitmapWorkerTask(ImageView imageView) {
			// Use a WeakReference to ensure the ImageView can be garbage
			// collected
			imageViewReference = new WeakReference<ImageView>(imageView);
		}

		@Override
		protected void onPreExecute() {
			progressDialog = ProgressDialog.show(ResultActivity.this, "",
					"Loading...", true);
		}

		// Decode image in background.
		@Override
		protected Bitmap doInBackground(String... params) {
			data = params[0];
			try {
				return BitmapFactory.decodeStream((InputStream) new URL(data)
						.getContent());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	}
}
