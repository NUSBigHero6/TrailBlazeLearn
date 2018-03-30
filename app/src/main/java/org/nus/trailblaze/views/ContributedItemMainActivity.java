package org.nus.trailblaze.views;
//Author: priyanka

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import org.nus.trailblaze.R;
import org.nus.trailblaze.fragments.FeedFragment;
import org.nus.trailblaze.models.ContributedItem;

import java.util.Date;

public class ContributedItemMainActivity  extends FragmentActivity
        implements FeedFragment.OnPassItem{

    //Declaring Fragments
    private FeedFragment feedFragment;

    private Toolbar itemToolbar;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contributed_item_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        firebaseAuth = FirebaseAuth.getInstance();

        //Account Settings Toolbar
        Toolbar itemToolbar = findViewById(R.id.itemToolbar);
        itemToolbar.setTitle("Contributed Item");

        //Initializing Fragments
        feedFragment = new FeedFragment();

        replaceFragment(feedFragment);

    }
    //Handle Bottom navigation menu clicks
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_docs:
                    startActivity(new Intent(getApplicationContext(),ContributedItemDocActivity.class));
                    return true;
                case R.id.menu_photos:
                    startActivity(new Intent(getApplicationContext(),ContributedItemImageActivity.class));
                    return true;
                case R.id.menu_audio:
                    startActivity(new Intent(getApplicationContext(),ContributedItemMediaActivity.class));
                    return true;
            }
            return false;
        }
    };

    private void replaceFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.item_main_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void passItem(ContributedItem item) {
        Log.d("ACTIVITY", item.getFile().getMimeType());

        Intent myIntent;

        if(item.getFile().getMimeType().indexOf("audio") > -1){
            myIntent = new Intent(ContributedItemMainActivity.this,
                    AudioPlayerActivity.class);

        }
        else{
            myIntent = new Intent(ContributedItemMainActivity.this,
                    TrailBlazeItemViewerActivity.class);

        }
        myIntent.putExtra("Item", item);
        startActivity(myIntent);
        // Following the documentation, right after starting the activity
        // we override the transition
        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
    }

    public void goToThreadListener(View view){
        Intent threadIntent = new Intent(ContributedItemMainActivity.this,DiscussionThreadActivity.class);
        startActivity(threadIntent);
        finish();
    }

}
