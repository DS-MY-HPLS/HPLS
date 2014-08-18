package com.alicia.hpliveshopper;

import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alicia.defineData.DefineData;
import com.hp.androidFeature.AndroidFeatures;
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

public class MainActivity extends ARViewActivity{

	private MetaioSDKCallbackHandler mCallbackHandler;
	private VisualSearchCallbackHandler mVSCallback;

	boolean m_request;
	TrackingValues m_trackingValues;
	
	private HorizontalScrollView hsvPic;
	private LinearLayout llPic;
	private Animation righttoleftinvisible;
	private Animation righttoleftvisible;
    private Animation lefttorightvisible;
    private Animation lefttorightinvisible;

	private final static String databaseID = "hpls";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.visual_search);
		MetaioDebug.enableLogging(BuildConfig.DEBUG);
		
		hsvPic = (HorizontalScrollView)mGUIView.findViewById(R.id.hsvPic);
		llPic = (LinearLayout)mGUIView.findViewById(R.id.llPic);
		
		new Handler().post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try
				{
					AssetManager assetManager = getAssets();
					String[] files = assetManager.list("pictures");
					String[] locale = assetManager.getLocales();
					
//					File file = new File("/assests/pictures");
//					String[] files = file.list();

					for(final String fileName : files)
					{
						// To load image
						InputStream ims = null;
				        try 
				        {
				            // get input stream
				            ims = assetManager.open("pictures/" + fileName);
				 
				            // create drawable from stream
				            Drawable d = Drawable.createFromStream(ims, fileName);
				 
				            ImageView iv = new ImageView(MainActivity.this);
				            iv.setImageDrawable(d);
				            iv.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									AndroidFeatures.showToastMethod(fileName);
								}
							});
//				            lLtestAnimation.addView(iv);
				            llPic.addView(iv);
				        }
				        catch(Exception ex) 
				        {
				        	System.out.println(ex.toString());
				        	AndroidFeatures.showToastMethod("failed");
				        }
					}
					
					
//					AndroidFeatures.showToastMethod("file finish "  + files.length);
					Log.d(INPUT_SERVICE, files.toString());
				}
				catch(Exception ex)
				{
					AndroidFeatures.showToastMethod("file failed");
				}
			}
		});
		
		righttoleftinvisible = AnimationUtils.loadAnimation( MainActivity.this, R.anim.righttoleftinvisible );
		righttoleftvisible = AnimationUtils.loadAnimation( MainActivity.this, R.anim.righttoleftvisible );
		lefttorightvisible = AnimationUtils.loadAnimation( MainActivity.this, R.anim.lefttorightvisible );
		lefttorightinvisible = AnimationUtils.loadAnimation( MainActivity.this, R.anim.lefttorightinvisible );
		
		righttoleftvisible.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				llPicVisible();
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				
			}
		});
		
		righttoleftinvisible.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
					llPicVisible();
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
		});
		
		lefttorightvisible.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
					llPicVisible();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
		});
		
		lefttorightinvisible.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				llPicVisible();
			}
		});

		mCallbackHandler = new MetaioSDKCallbackHandler();

		mVSCallback = new VisualSearchCallbackHandler();
		metaioSDK.registerVisualSearchCallback(mVSCallback);

		m_request = true;
	}
	
	private void llPicVisible()
	{
		if(hsvPic.getVisibility() == View.INVISIBLE)
			hsvPic.setVisibility(View.VISIBLE);
		else
			hsvPic.setVisibility(View.INVISIBLE);
	}
	
	private float fingerX = 0;

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch(event.getAction() & MotionEvent.ACTION_MASK)
		{
			case MotionEvent.ACTION_DOWN:
				fingerX = event.getX();
				MetaioDebug.log("fingerY: " + fingerX);
				break;
				
			case MotionEvent.ACTION_UP:
				float dx = event.getX() - fingerX;
				if(dx < 0)	//swipe left
				{
					MetaioDebug.log("LEFT fingerY: " + fingerX);
					
					new Handler().post(new Runnable() {
						
						@Override
						public void run() {
							if(hsvPic.getVisibility() == View.INVISIBLE)
								hsvPic.startAnimation(lefttorightvisible);
							else
								hsvPic.startAnimation(lefttorightinvisible);
						}
					});
				}
				else
				{
					MetaioDebug.log("RIGHT fingerY: " + fingerX);
					
					new Handler().post(new Runnable() {
						
						@Override
						public void run() {
							if(hsvPic.getVisibility() == View.VISIBLE)
							{
								hsvPic.startAnimation(righttoleftinvisible);
							}
							else
							{
								hsvPic.startAnimation(righttoleftvisible);
							}
							
						}
					});
				}
				break;
				
			default:
				break;
		}
		
		return true;
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
				
				final float visualScore = response.get(0).getVisualSearchScore();
				MetaioDebug.log("VisualSearchScore: " + visualScore);
				
				String metadata = response.get(0).getMetadata();
				MetaioDebug.log("onVisualSearchResult: getMetadata: " + metadata);
				
				if (metadata != null) {
					try {
						
						DefineData toNextScreenDd = new DefineData(metadata);						
						Intent nextScreen = new Intent(getApplicationContext(), ResultActivity.class);
						nextScreen.putExtra("toNextScreenDd", (Parcelable)toNextScreenDd);
						startActivity(nextScreen);
						
					} catch (Exception e) {
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
