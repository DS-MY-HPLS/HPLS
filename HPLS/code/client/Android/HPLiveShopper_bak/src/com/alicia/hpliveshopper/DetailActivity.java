package com.alicia.hpliveshopper;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Timer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.alicia.defineData.DefineData;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class DetailActivity extends Activity {

	private TextView txtCode;
	private TextView txtTitle;
	private TextView txtPrice;
	private TextView txtDescription;
	private Button btnDatasheet;
	private ImageView imgView1;
	private ImageView imgView2;
	private ImageView imgView3;
	private VideoView videoView;
	String videoUrl;
	private Button btnBuynow;
	private Button btnReseller;
	
	DefineData resultDd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_detail);

		txtCode = (TextView) findViewById(R.id.txtCode);
		txtTitle = (TextView) findViewById(R.id.txtTitle);
		txtPrice = (TextView) findViewById(R.id.txtPrice);
		txtDescription = (TextView) findViewById(R.id.txtDescription);
		btnDatasheet = (Button) findViewById(R.id.btnDatasheet);
		btnBuynow = (Button) findViewById(R.id.btnBuynow);
		btnReseller = (Button) findViewById(R.id.btnReseller);

		imgView1 = (ImageView) findViewById(R.id.ImageView1);
		imgView2 = (ImageView) findViewById(R.id.ImageView2);
		imgView3 = (ImageView) findViewById(R.id.ImageView3);

		videoView = (VideoView) findViewById(R.id.VideoView);

//		Intent i = getIntent();
		// Receiving the Data
		Intent rsIntent = this.getIntent();
		resultDd = (DefineData)rsIntent.getParcelableExtra("toNextScreenDd");
		String code = resultDd.getCode();
		String title = resultDd.getTitle();
		String price = resultDd.getPrice();
		String description = resultDd.getDescription();
		String url_images = resultDd.getUrl_images();
		final String url_datasheet = resultDd.getUrl_datasheet();
		final String url_video = resultDd.getUrl_video();
		final String url_buynow = resultDd.getUrl_buynow();
		final String url_reseller = resultDd.getUrl_reseller();

		// Displaying Received data
		txtCode.setText(code);
		txtTitle.setText(title);
		txtPrice.setText(price);
		txtDescription.setText(Html.fromHtml(description));

		if (url_images != null && "".equalsIgnoreCase(url_images)) {
			imgView1.setVisibility(View.INVISIBLE);
			imgView2.setVisibility(View.INVISIBLE);
			imgView3.setVisibility(View.INVISIBLE);
		} else {
			String[] images = url_images.split("\\|");
			imgView1.getLayoutParams().width = 300;
			imgView1.getLayoutParams().height = 300;
			BitmapWorkerTask task1 = new BitmapWorkerTask(imgView1);
			task1.execute(images[0]);
			if (images.length == 3) {
				imgView2.getLayoutParams().width = 300;
				imgView2.getLayoutParams().height = 300;
				BitmapWorkerTask task2 = new BitmapWorkerTask(imgView2);
				task2.execute(images[1]);
				imgView3.getLayoutParams().width = 300;
				imgView3.getLayoutParams().height = 300;
				BitmapWorkerTask task3 = new BitmapWorkerTask(imgView3);
				task3.execute(images[2]);
			} else {
				imgView2.setVisibility(View.INVISIBLE);
				imgView3.setVisibility(View.INVISIBLE);
			}
		}

		if (url_video != null && "".equalsIgnoreCase(url_video)) {
			videoView.setVisibility(View.INVISIBLE);
		} else {
			new VideoAsyncTask().execute(url_video);
		}

		if (url_datasheet != null && "".equalsIgnoreCase(url_datasheet)) {
			btnDatasheet.setVisibility(View.INVISIBLE);
		}
		if (url_buynow != null && "".equalsIgnoreCase(url_buynow)) {
			btnBuynow.setVisibility(View.INVISIBLE);
		}
		if (url_reseller != null && "".equalsIgnoreCase(url_reseller)) {
			btnReseller.setVisibility(View.INVISIBLE);
		}

		btnDatasheet.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
						.parse(url_datasheet));
				startActivity(browserIntent);
			}
		});

		btnBuynow.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
						.parse(url_buynow));
				startActivity(browserIntent);
			}
		});

		btnReseller.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
						.parse(url_reseller));
				startActivity(browserIntent);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		// if (id == R.id.action_settings) {
		// return true;
		// }
		return super.onOptionsItemSelected(item);
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
			progressDialog = ProgressDialog.show(DetailActivity.this, "",
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

	private class VideoAsyncTask extends AsyncTask<String, Void, Void> {
		ProgressDialog progressDialog;
		private String data;
		
		@Override
		protected void onPreExecute() {
			progressDialog = ProgressDialog.show(DetailActivity.this, "",
					"Loading...", true);
		}

		@Override
		protected Void doInBackground(String... params) {
			data = params[0];
			
			try {
				videoUrl = getUrlVideoRTSP(data);
			} catch (Exception e) {
				Log.e("Login Soap Calling in Exception", e.toString());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			progressDialog.dismiss();

			videoView.setVideoURI(Uri.parse(videoUrl));
			MediaController mc = new MediaController(DetailActivity.this);
			videoView.setMediaController(mc);
			videoView.requestFocus();
			videoView.start();
			mc.show();
		}

	}

	public static String getUrlVideoRTSP(String urlYoutube) {
		try {
			String gdy = "http://gdata.youtube.com/feeds/api/videos/";
			DocumentBuilder documentBuilder = DocumentBuilderFactory
					.newInstance().newDocumentBuilder();
			String id = extractYoutubeId(urlYoutube);
			URL url = new URL(gdy + id);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			Document doc = documentBuilder.parse(connection.getInputStream());
			Element el = doc.getDocumentElement();
			NodeList list = el.getElementsByTagName("media:content");// /media:content
			String cursor = urlYoutube;
			for (int i = 0; i < list.getLength(); i++) {
				Node node = list.item(i);
				if (node != null) {
					NamedNodeMap nodeMap = node.getAttributes();
					HashMap<String, String> maps = new HashMap<String, String>();
					for (int j = 0; j < nodeMap.getLength(); j++) {
						Attr att = (Attr) nodeMap.item(j);
						maps.put(att.getName(), att.getValue());
					}
					if (maps.containsKey("yt:format")) {
						String f = maps.get("yt:format");
						if (maps.containsKey("url")) {
							cursor = maps.get("url");
						}
						if (f.equals("1"))
							return cursor;
					}
				}
			}
			return cursor;
		} catch (Exception ex) {
			Log.e("Get Url Video RTSP Exception======>>", ex.toString());
		}
		return urlYoutube;

	}

	protected static String extractYoutubeId(String url)
			throws MalformedURLException {
		String id = null;
		try {
			String query = new URL(url).getQuery();
			if (query != null) {
				String[] param = query.split("&");
				for (String row : param) {
					String[] param1 = row.split("=");
					if (param1[0].equals("v")) {
						id = param1[1];
					}
				}
			} else {
				if (url.contains("embed")) {
					id = url.substring(url.lastIndexOf("/") + 1);
				}
			}
		} catch (Exception ex) {
			Log.e("Exception", ex.toString());
		}
		return id;
	}
}
