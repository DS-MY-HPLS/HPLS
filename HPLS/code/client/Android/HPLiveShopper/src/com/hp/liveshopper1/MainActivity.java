package com.hp.liveshopper1;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.hp.liveshopper.BuildConfig;
import com.hp.liveshopper.R;
import com.metaio.sdk.ARViewActivity;
import com.metaio.sdk.MetaioDebug;
import com.metaio.sdk.jni.ETRACKING_STATE;
import com.metaio.sdk.jni.EVISUAL_SEARCH_STATE;
import com.metaio.sdk.jni.IGeometry;
import com.metaio.sdk.jni.IMetaioSDKAndroid;
import com.metaio.sdk.jni.IMetaioSDKCallback;
import com.metaio.sdk.jni.IVisualSearchCallback;
import com.metaio.sdk.jni.TrackingValues;
import com.metaio.sdk.jni.TrackingValuesVector;
import com.metaio.sdk.jni.VisualSearchResponseVector;

public class MainActivity extends ARViewActivity {

	private MetaioSDKCallbackHandler mCallbackHandler;

	private VisualSearchCallbackHandler mVSCallback;

	boolean m_request;

	TrackingValues m_trackingValues;

	private final static String databaseID = "hpls";

	ImageButton torchBtn;
	ImageView HPLogo;
	boolean isLightOn = false;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.metaio);
		MetaioDebug.enableLogging(BuildConfig.DEBUG);
        
		mCallbackHandler = new MetaioSDKCallbackHandler();

		mVSCallback = new VisualSearchCallbackHandler();
		metaioSDK.registerVisualSearchCallback(mVSCallback);

		m_request = true;	
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();

		mCallbackHandler.delete();
		mCallbackHandler = null;

		mVSCallback.delete();
		mVSCallback = null;
	}

	@Override
	protected int getGUILayout() {
		// Attaching layout to the activity
		return R.layout.metaio;
	}

	@Override
	protected IMetaioSDKCallback getMetaioSDKCallbackHandler() {
		return mCallbackHandler;
	}

	@Override
	protected void loadContents() {
	}

	@Override
	public void onDrawFrame() {
		m_trackingValues = metaioSDK.getTrackingValues(1);
		
		// request new VisualSearch before rendering next frame
		if (m_request || !m_trackingValues.isTrackingState()) {
			metaioSDK.requestVisualSearch(databaseID, true);
			m_request = false;
		}
		super.onDrawFrame();
	}

	@Override
	protected void onGeometryTouched(IGeometry geometry) {
	}

	final class MetaioSDKCallbackHandler extends IMetaioSDKCallback {

		@Override
		public void onSDKReady() {
			
			
			// show GUI
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					mGUIView.setVisibility(View.VISIBLE);

					torchBtn = (ImageButton) findViewById(R.id.btnTorch);
					torchBtn.bringToFront();
					torchBtn.setVisibility(View.VISIBLE);
					
					HPLogo = (ImageView) findViewById(R.id.HPLogoBottom);
					HPLogo.bringToFront();
					HPLogo.setVisibility(View.VISIBLE);
					
					// a toast message to alert the user
					Toast toast = Toast
							.makeText(
									getApplicationContext(),
									"Please hold the camera to an image you want to perform visual search on.",
									Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					
					// Start visual search
					m_request = true;
				}
			});
		}

		@Override
		public void onTrackingEvent(TrackingValuesVector trackingValues) {
			if (trackingValues.size() > 0) {
				// if tracking is lost, request visual search
				if (trackingValues.get(0).getState() == ETRACKING_STATE.ETS_LOST) {
					MetaioDebug
							.log("Requesting a new visual search because tracking is lost...");
					m_request = true;
				}
			}
		}
	}

	final class VisualSearchCallbackHandler extends IVisualSearchCallback {
		@Override
		public void onVisualSearchResult(VisualSearchResponseVector response,
				int errorCode) {
			MetaioDebug.log("onVisualSearchResult: " + errorCode + ", "
					+ response.size());
			
			if (errorCode == 0 && response.size() > 0) {
				
				String metadata = response.get(0).getMetadata();
				MetaioDebug.log("onVisualSearchResult: getMetadata: " + metadata);
				
				if (metadata != null) {
					try {
						JSONObject json = new JSONObject(metadata);
						
						String ar_type = json.getString("ar_type");
						String title = json.getString("title");
						String url_video = json.getString("url_video");
						String url_website = json.getString("url_website");
						String url_website_short = json.getString("url_website_short");
						
						
						Intent nextScreen;
						if(ar_type != null && "prod".equals(ar_type)) {
							
							String code = json.getString("code");
							String price = json.getString("price");
							String description = json.getString("description");
							String url_datasheet = json.getString("url_datasheet");
							String url_locator = json.getString("url_locator");
							String url_images = json.getString("url_images");
						
							nextScreen = new Intent(getApplicationContext(), ProductActivity.class);
							nextScreen.putExtra("code", code);
							nextScreen.putExtra("title", title);
							nextScreen.putExtra("price", price);
							nextScreen.putExtra("description", description);
							nextScreen.putExtra("url_datasheet", url_datasheet);
							nextScreen.putExtra("url_video", url_video);
							nextScreen.putExtra("url_website", url_website);
							nextScreen.putExtra("url_website_short", url_website_short);
							nextScreen.putExtra("url_locator", url_locator);
							nextScreen.putExtra("url_images", url_images);
							
							MetaioDebug.log("onVisualSearchResult: nextScreen ProductActivity Intent");
						
						} else {
							
							nextScreen = new Intent(getApplicationContext(), AdvertActivity.class);
							nextScreen.putExtra("title", title);
							nextScreen.putExtra("url_video", url_video);
							nextScreen.putExtra("url_website", url_website);
							nextScreen.putExtra("url_website_short", url_website_short);
							
							MetaioDebug.log("onVisualSearchResult: nextScreen AdvertActivity Intent");
						}
						
						startActivity(nextScreen);
						
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					Log.e("ServiceHandler",
							"Couldn't get any data from the url");
				}

			} else {
				if (errorCode > 0)
					MetaioDebug.log(Log.ERROR, "Visual search error: "
							+ errorCode);

				// if visual search didn't succeed, request another round
				MetaioDebug
						.log("Requesting new visual search because search failed...");
				metaioSDK.requestVisualSearch(databaseID, true);
			}
		}

		@Override
		public void onVisualSearchStatusChanged(EVISUAL_SEARCH_STATE state) {
			MetaioDebug.log("The current visual search state is: " + state);
		}

	}
	
	public void toggleTorch(ImageButton btn) {
		if (isLightOn) {
			
			IMetaioSDKAndroid.startTorch(this);
			btn.setImageResource(R.drawable.ic_flash_off);
			isLightOn = false;

		} else {
			
			IMetaioSDKAndroid.stopTorch(this);
			btn.setImageResource(R.drawable.ic_flash_on);
			isLightOn = true;
		}
	}
	
	@Override
	public void onSurfaceChanged(int width, int height) {
		super.onSurfaceChanged(width, height);
		
		Camera camera = IMetaioSDKAndroid.getCamera(this);
        Parameters camParams  = camera.getParameters();
        camParams.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        camera.setParameters(camParams);
		
		torchBtn = (ImageButton) findViewById(R.id.btnTorch);
		torchBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				toggleTorch(torchBtn);
			}
		});
	}
	

}
