package com.hp.liveshopper1;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.PlayerStyle;
import com.google.android.youtube.player.YouTubePlayerView;
import com.hp.liveshopper.R;

public class ProductActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

	private YouTubePlayer YPlayer;
    private static final String YoutubeDeveloperKey = "AIzaSyBn2EyBp_UjIxTaRCF9O3aUYbR9pSwe2GQ";
    private static final int RECOVERY_DIALOG_REQUEST = 1;
	
	private TextView txtCode;
	private TextView txtTitle;
	private TextView txtPrice;
	private TextView txtDescription;
	private ImageView imgView0;
	private ImageView imgView1;
	private ImageView imgView2;
	private ImageView imgView3;
	private ImageView iconVideo;
	private TextView lblVideo;
	//private VideoView videoView;
	String videoUrl;
	String url_video;
	private Button btnWebsite;
	private Button btnDatasheet;
	private Button btnAccessories;
	private Button btnLocator;
	private ImageButton btnShare;
	private ImageButton btnEmail;
	private ImageButton btnCall;
	
	int screen_width;
	int screen_height;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product);		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		screen_width = displayMetrics.widthPixels;
		screen_height = displayMetrics.heightPixels;

		txtCode = (TextView) findViewById(R.id.txtCode);
		txtTitle = (TextView) findViewById(R.id.txtTitle);
		txtPrice = (TextView) findViewById(R.id.txtPrice);
		txtDescription = (TextView) findViewById(R.id.txtDescription);
		btnAccessories = (Button) findViewById(R.id.btnAccessories);
		btnDatasheet = (Button) findViewById(R.id.btnDatasheet);
		btnWebsite = (Button) findViewById(R.id.btnWebsite);
		btnLocator = (Button) findViewById(R.id.btnLocator);
		btnShare = (ImageButton) findViewById(R.id.btnShare);
		btnEmail = (ImageButton) findViewById(R.id.btnEmail);
		btnCall = (ImageButton) findViewById(R.id.btnCall);

		imgView0 = (ImageView) findViewById(R.id.imageView0);
		imgView1 = (ImageView) findViewById(R.id.imageView1);
		imgView2 = (ImageView) findViewById(R.id.imageView2);
		imgView3 = (ImageView) findViewById(R.id.imageView3);

		iconVideo = (ImageView) findViewById(R.id.iconVideo);
		lblVideo = (TextView) findViewById(R.id.lblVideo);
		//videoView = (VideoView) findViewById(R.id.videoView);

		Intent i = getIntent();
		// Receiving the Data
		String code = i.getStringExtra("code");
		final String title = i.getStringExtra("title");
		String price = i.getStringExtra("price");
		String description = i.getStringExtra("description");
		String url_images = i.getStringExtra("url_images");
		final String url_datasheet = i.getStringExtra("url_datasheet");
		url_video = i.getStringExtra("url_video");
		final String url_website = i.getStringExtra("url_website");
		final String url_website_short = i.getStringExtra("url_website_short");
		final String url_locator = i.getStringExtra("url_locator");
		

		// Displaying Received data
		txtCode.setText(code);
		txtTitle.setText(title);
		txtPrice.setText(price);
		txtDescription.setText(Html.fromHtml(description));

		if (url_images != null && "".equalsIgnoreCase(url_images)) {
			imgView0.setVisibility(View.INVISIBLE);
			imgView1.setVisibility(View.INVISIBLE);
			imgView2.setVisibility(View.INVISIBLE);
			imgView3.setVisibility(View.INVISIBLE);
		} else {
			String[] images = url_images.split("\\|");
			
			BitmapWorkerTask task0 = new BitmapWorkerTask(imgView0, (int) (screen_width * 0.5), (int) (screen_height * 0.5));
			task0.execute(images[0]);
			
			if (images.length == 3) {
				
				BitmapWorkerTask task1 = new BitmapWorkerTask(imgView1, screen_width, screen_height);
				task1.execute(images[0]);
				
				BitmapWorkerTask task2 = new BitmapWorkerTask(imgView2, screen_width, screen_height);
				task2.execute(images[1]);

				BitmapWorkerTask task3 = new BitmapWorkerTask(imgView3, screen_width, screen_height);
				task3.execute(images[2]);
				
			} else {
				imgView1.setVisibility(View.INVISIBLE);
				imgView2.setVisibility(View.INVISIBLE);
				imgView3.setVisibility(View.INVISIBLE);
			}
			
		}
/*
		if (url_video != null && "".equalsIgnoreCase(url_video)) {
			lblVideo.setVisibility(View.INVISIBLE);
			iconVideo.setVisibility(View.INVISIBLE);
			//videoView.setVisibility(View.INVISIBLE);
			
		} else {
			//new VideoAsyncTask().execute(url_video);
		}
*/		
		YouTubePlayerView youTubeView = (YouTubePlayerView) findViewById(R.id.youtubeView);
        youTubeView.initialize(YoutubeDeveloperKey, this);

		if (url_datasheet != null && "".equalsIgnoreCase(url_datasheet)) {
			btnDatasheet.setVisibility(View.INVISIBLE);			
		}
		if (url_website != null && "".equalsIgnoreCase(url_website)) {
			btnWebsite.setVisibility(View.INVISIBLE);
		}
		if (url_locator != null && "".equalsIgnoreCase(url_locator)) {
			btnLocator.setVisibility(View.INVISIBLE);
		}

		
		btnShare.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				
				Intent sendIntent = new Intent();
				sendIntent.setAction(Intent.ACTION_SEND);
				sendIntent.putExtra(Intent.EXTRA_TEXT, title + " | " + url_website_short);
				sendIntent.setType("text/plain");
				startActivity(sendIntent);
			}
		});
		
		btnEmail.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				String url_email = getResources().getString(R.string.url_email);
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
						.parse(url_email));
				startActivity(browserIntent);
			}
		});
		
		btnCall.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				String call_number = getResources().getString(R.string.call_number);
				Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:" + call_number));
				startActivity(callIntent);
			}
		});
		
		btnAccessories.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				String url_accessories = getResources().getString(R.string.url_accessories);
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
						.parse(url_accessories));
				startActivity(browserIntent);
			}
		});
		
		btnDatasheet.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
						.parse(url_datasheet));
				startActivity(browserIntent);
			}
		});

		btnWebsite.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
						.parse(url_website));
				startActivity(browserIntent);
			}
		});

		btnLocator.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
						.parse(url_locator));
				startActivity(browserIntent);
			}
		});

	}

	@Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
            YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format(
                    "There was an error initializing the YouTubePlayer",
                    errorReason.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(YoutubeDeveloperKey, this);
        }
    }
 
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtubeView);
    }
 
	public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
	      boolean wasRestored) {
	    if (!wasRestored) {
	    	try {
	    		PlayerStyle style = PlayerStyle.MINIMAL;
	    		player.setPlayerStyle(style);
	    		player.cueVideo(extractYoutubeId(url_video));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
	    }
	  }

	  
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_product_actions, menu);
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
		private int screen_width;
		private int screen_height;

		public BitmapWorkerTask(ImageView imageView, int screen_width, int screen_height) {
			// Use a WeakReference to ensure the ImageView can be garbage
			// collected
			imageViewReference = new WeakReference<ImageView>(imageView);
			this.screen_width = screen_width;
			this.screen_height = screen_height;
		}

		@Override
		protected void onPreExecute() {
			progressDialog = ProgressDialog.show(ProductActivity.this, "",
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
					
					int original_width = bitmap.getWidth();
				    int original_height = bitmap.getHeight();
				    
				    int new_width = original_width;
				    int new_height = original_height;
				    
				    if (original_width > this.screen_width) {
				        //scale width to fit
				        new_width = this.screen_width;
				        //scale height to maintain aspect ratio
				        new_height = (new_width * original_height) / original_width;
				    }

				    // then check if we need to scale even with the new height
				    if (new_height > this.screen_height) {
				        //scale height to fit instead
				        new_height = this.screen_height;
				        //scale width to maintain aspect ratio
				        new_width = (new_height * original_width) / original_height;
				    }
				    
				    imageView.getLayoutParams().width = new_width;
				    imageView.getLayoutParams().height = new_height;
				    
				}
			}
		}
	}

	private class VideoAsyncTask extends AsyncTask<String, Void, Void> {
		ProgressDialog progressDialog;
		private String data;
		
		@Override
		protected void onPreExecute() {
			progressDialog = ProgressDialog.show(ProductActivity.this, "",
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

			//videoView.setVideoURI(Uri.parse(videoUrl));
			MediaController mc = new MediaController(ProductActivity.this);
			//videoView.setMediaController(mc);
			//videoView.getLayoutParams().width = screen_width;
			//videoView.requestFocus();
			//videoView.start();
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

