package com.hp.liveshopper1;

import java.net.MalformedURLException;
import java.net.URL;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;
import com.hp.liveshopper.R;
 

public class AdvertActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

	private YouTubePlayer YPlayer;
    private static final String YoutubeDeveloperKey = "AIzaSyBn2EyBp_UjIxTaRCF9O3aUYbR9pSwe2GQ";
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    String title;
    String url_video;
    String url_website;
    String url_website_short;
    		
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advert);
 
        Intent i = getIntent();
		// Receiving the Data
        title = i.getStringExtra("title");
        url_video = i.getStringExtra("url_video");
		url_website = i.getStringExtra("url_website");
		url_website_short = i.getStringExtra("url_website_short");
		
        YouTubePlayerView youTubeView = (YouTubePlayerView) findViewById(R.id.videoView);
        youTubeView.initialize(YoutubeDeveloperKey, this);
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_advert_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// action with ID action_refresh was selected
		case R.id.action_share:
			Intent sendIntent = new Intent();
			sendIntent.setAction(Intent.ACTION_SEND);
			sendIntent.putExtra(Intent.EXTRA_TEXT, title + " | " + url_website_short);
			sendIntent.setType("text/plain");
			startActivity(sendIntent);
			break;
		// action with ID action_settings was selected
		case R.id.action_info:
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
					.parse(url_website));
			startActivity(browserIntent);
			break;
		default:
			break;
		}

		return true;
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
        return (YouTubePlayerView) findViewById(R.id.videoView);
    }
 
    @Override
    public void onInitializationSuccess(Provider provider,
            YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {
            YPlayer = player;
            /*
             * Now that this variable YPlayer is global you can access it
             * throughout the activity, and perform all the player actions like
             * play, pause and seeking to a position by code.
             */
            try {
            	YPlayer.loadVideo(extractYoutubeId(url_video));
            } catch (Exception ex) {
    			Log.e("Get Url Video Exception======>>", ex.toString());
    		}
        }
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
