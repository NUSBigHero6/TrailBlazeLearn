package org.nus.trailblaze.views;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import org.nus.trailblaze.R;
import org.nus.trailblaze.models.ContributedItem;

/**
 * Created by wengweichen on 14/3/18.
 */

public class TrailBlazaFeedActivity extends FragmentActivity
        implements FeedFragment.OnPassItem {

    private static final String TAG = "TrailBlazaFeedActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trail_blaze_feed);

    }

    @Override
    public void passItem(ContributedItem item) {
        Intent myIntent;

        if(item.getFile().getMimeType().indexOf("audio") > -1){
            myIntent = new Intent(TrailBlazaFeedActivity.this,
                    AudioPlayerActivity.class);

        }
        else{
            myIntent = new Intent(TrailBlazaFeedActivity.this,
                    TrailBlazeItemViewerActivity.class);

        }
        myIntent.putExtra("Item", item);
        startActivity(myIntent);
        // Following the documentation, right after starting the activity
        // we override the transition
        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
    }

}
