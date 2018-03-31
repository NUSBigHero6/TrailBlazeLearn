package org.nus.trailblaze.views;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.nus.trailblaze.R;
import org.nus.trailblaze.fragments.FeedFragment;
import org.nus.trailblaze.models.ContributedItem;
import org.nus.trailblaze.models.Trainer;
import org.nus.trailblaze.models.User;

/**
 * Created by wengweichen on 14/3/18.
 */

public class TrailBlazaFeedActivity extends AppCompatActivity
        implements FeedFragment.OnPassItem {

    private static final String TAG = "TrailBlazaFeedActivity";
    private String userMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trail_blaze_feed);

        String trailId = (String) this.getIntent().getExtras().get("trailID");
        userMode = (String) this.getIntent().getExtras().get("userMode");

        //Initializing Fragments
        FeedFragment feedFragment = new FeedFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", trailId);
        bundle.putString("userMode", userMode);
        feedFragment.setArguments(bundle);

        replaceFragment(feedFragment);
    }

    private void replaceFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
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
        myIntent.putExtra("userMode", userMode);
        myIntent.putExtra("Item", item);
        startActivity(myIntent);
        // Following the documentation, right after starting the activity
        // we override the transition
        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
    }

}
