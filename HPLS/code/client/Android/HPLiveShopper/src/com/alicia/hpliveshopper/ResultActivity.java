package com.alicia.hpliveshopper;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;

import com.alicia.defineData.DefineData;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.os.Build;

public class ResultActivity extends Activity {
	
	DefineData resultDd;
	static String rootDirectory = "hplsVisualSearch";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result_screen);
		Log.d("resultPage", "oncreate");
		
		try{
			Intent rsIntent = this.getIntent();
			resultDd = (DefineData)rsIntent.getParcelableExtra("toNextScreenDd");			
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
			ImageButton ibVideo = (ImageButton)findViewById(R.id.ibVideo);
			if(resultDd.getUrl_video().isEmpty()){
				ibVideo.setVisibility(View.INVISIBLE);
			}
			
			DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
			int width = displayMetrics.widthPixels / 2;
			int height = displayMetrics.heightPixels;
			
			RelativeLayout rlLocation = (RelativeLayout)findViewById(R.id.rlLocation);
			rlLocation.getLayoutParams().width = width - 20;
			
			RelativeLayout rlSpec = (RelativeLayout)findViewById(R.id.rlSpec);
			rlSpec.getLayoutParams().width = width - 20;
			
			LinearLayout lLRelatedPicture = (LinearLayout)findViewById(R.id.lLRelatedPic);
			lLRelatedPicture.getLayoutParams().height = width;
			
			Log.d("width", "width " + width);
			Log.d("height", "height " + height);
			
			Log.d("image", "url_images " + resultDd.getUrl_images());
			
			if(resultDd.getUrl_images() != null || "".equalsIgnoreCase(resultDd.getUrl_images()))
			{
				String[] images = resultDd.getUrl_images().split("\\|");
				Log.d("image length", "length = " + images.length);
				for(int i = 0; i < images.length; i ++)
				{
					Log.d("i = ", "i = " + i);
					if(i == 0)
					{
						ImageView ivBlueBackground = (ImageView)findViewById(R.id.ivBlueBackground);
						ivBlueBackground.getLayoutParams().width = width;
						ivBlueBackground.getLayoutParams().height = width;
						
						ImageView ivWhiteBackground = (ImageView)findViewById(R.id.ivWhiteBackground);
						int whiteWidth = width - 20;
						ivWhiteBackground.getLayoutParams().width = whiteWidth;
						ivWhiteBackground.getLayoutParams().height = whiteWidth;
						
						ImageView ivImageProduct = (ImageView)findViewById(R.id.ivImageProduct);
						int productWidth = whiteWidth - 20;
						ivImageProduct.setBackgroundColor(Color.WHITE);
						ivImageProduct.getLayoutParams().width = productWidth;
						ivImageProduct.getLayoutParams().height = productWidth;
						BitmapWorkerTask task1 = new BitmapWorkerTask(ivImageProduct);
						task1.execute(images[0]);
					}
					
					else
					{
						ImageView iv = new ImageView(this);
//						iv.getLayoutParams().width = width;
//						iv.getLayoutParams().height = width;
						lLRelatedPicture.addView(iv);
						BitmapWorkerTask task2 = new BitmapWorkerTask(iv);
						task2.execute(images[i]);
					}
				}
			}
			
			TextView tvProdName = (TextView)findViewById(R.id.tvProdName);
			tvProdName.setText(resultDd.getTitle());
			
			TextView tvDescription = (TextView)findViewById(R.id.tvDescription);
			tvDescription.setText(resultDd.getDescription());
			
			TextView tvPrice = (TextView)findViewById(R.id.tvPrice);
			tvPrice.setText(resultDd.getPrice());
			
			ScrollView scVertical = (ScrollView)findViewById(R.id.scVertical);
			final ScrollView scHorizontal = (ScrollView)findViewById(R.id.scHorizontal);
			
			scVertical.setOnTouchListener(new View.OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					scHorizontal.getParent().requestDisallowInterceptTouchEvent(false);
					return false;
				}
			});
			
			scHorizontal.setOnTouchListener(new View.OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					v.getParent().requestDisallowInterceptTouchEvent(true);
					return false;
				}
			});
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
				
			case R.id.ibLocation:
				urlLink = resultDd.getUrl_reseller();
				break;
				
			case R.id.ibSpec:
				urlLink = resultDd.getUrl_datasheet();
				break;
				
			case R.id.btnVideo:
				urlLink = resultDd.getUrl_video();
				break;
				
			case R.id.ibVideo:
				urlLink = resultDd.getUrl_video();
				break;
				
			case R.id.ivImageProduct:
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
			Log.d("urlLink", "urlLink " + urlLink);
			if(urlLink != null && !urlLink.isEmpty())
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

		// Once complete, see if ImageView is still around and set bitmap.
		@Override
		protected void onPostExecute(Bitmap bitmap) {
			progressDialog.dismiss();
			if (imageViewReference != null && bitmap != null) {
				final ImageView imageView = imageViewReference.get();
				if (imageView != null) {
					imageView.setImageBitmap(bitmap);

				}
			}
		}
	}
}
