package com.alicia.hpliveshopper;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.metaio.sdk.ARViewActivity;
import com.metaio.sdk.MetaioDebug;
import com.metaio.sdk.jni.ETRACKING_STATE;
import com.metaio.sdk.jni.EVISUAL_SEARCH_STATE;
import com.metaio.sdk.jni.IGeometry;
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.visual_search);
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
		return R.layout.visual_search;
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
						String code = json.getString("code");
						String title = json.getString("title");
						String price = json.getString("price");
						String description = json.getString("description");
						String url_datasheet = json.getString("url_datasheet");
						String url_video = json.getString("url_video");
						String url_buynow = json.getString("url_buynow");
						String url_reseller = json.getString("url_reseller");
						String url_images = json.getString("url_images");
						
						Intent nextScreen = new Intent(getApplicationContext(), DetailActivity.class);
						nextScreen.putExtra("code", code);
						nextScreen.putExtra("title", title);
						nextScreen.putExtra("price", price);
						nextScreen.putExtra("description", description);
						nextScreen.putExtra("url_datasheet", url_datasheet);
						nextScreen.putExtra("url_video", url_video);
						nextScreen.putExtra("url_buynow", url_buynow);
						nextScreen.putExtra("url_reseller", url_reseller);
						nextScreen.putExtra("url_images", url_images);
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

}
