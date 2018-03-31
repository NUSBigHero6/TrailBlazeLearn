package org.nus.trailblaze.views;
//Author: priyanka

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.nus.trailblaze.R;
import org.nus.trailblaze.fragments.FeedFragment;
import org.nus.trailblaze.models.ContributedItem;

import java.util.HashMap;
import java.util.Map;


public class ContributedItemMainActivity  extends AppCompatActivity implements FeedFragment.OnPassItem{

    //Declaring Fragments
    private FeedFragment feedFragment;

    private Toolbar itemToolbar;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private  String stationId;
    private  String trailId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contributed_item_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        //Account Settings Toolbar
        Toolbar itemToolbar = findViewById(R.id.itemToolbar);
        itemToolbar.setTitle("Contributed Item");


        stationId = this.getIntent().getStringExtra("stationID");
        trailId= getIntent().getStringExtra("trailID");

        Log.d("SENDING/TRIAL", stationId);
        //Initializing Fragments
        feedFragment = new FeedFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", stationId);
        bundle.putString("userMode", "participant");
        feedFragment.setArguments(bundle);

        replaceFragment(feedFragment);


    }
    //Handle Bottom navigation menu clicks
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_docs:
                    Intent dIntent=new Intent(getApplicationContext(),ContributedItemDocActivity.class);
                    dIntent.putExtra("trailID", trailId);
                    dIntent.putExtra("stationID", stationId);

                    startActivity(dIntent);
                    return true;
                case R.id.menu_photos:
                    Intent pIntent=new Intent(getApplicationContext(),ContributedItemImageActivity.class);
                    pIntent.putExtra("trailID", trailId);
                    pIntent.putExtra("stationID", stationId);

                    startActivity(pIntent);
                    return true;
                case R.id.menu_audio:
                    Intent aIntent=new Intent(getApplicationContext(),ContributedItemMediaActivity.class);
                    aIntent.putExtra("trailID", trailId);
                    aIntent.putExtra("stationID", stationId);
                    startActivity(aIntent);
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
        myIntent.putExtra("stationID", this.stationId);
        startActivity(myIntent);
        // Following the documentation, right after starting the activity
        // we override the transition
        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
    }

    public void goToThreadListener(View view){
        final String stationID = getIntent().getStringExtra("stationID");
        Map<String, Object> threadMap = new HashMap<>();
        threadMap.put("id", stationID + "_thread");
        threadMap.put("stationID",stationID);
        if(!TextUtils.isEmpty(stationID)){
            //Search if any thead exists with station ID
            firebaseFirestore.collection("discussion_threads").document(stationID + "_thread").addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                    Intent threadIntent = new Intent(ContributedItemMainActivity.this,DiscussionThreadActivity.class);
                    threadIntent.putExtra("stationID",stationID);
                    threadIntent.putExtra("threadID",stationID + "_thread");
                    startActivity(threadIntent);
                }
            });
        }else{
            Log.i("STATION ID","No stationID");
        }

    }

    public void showToastMessage(String message){
        Toast.makeText(ContributedItemMainActivity.this,message,Toast.LENGTH_LONG).show();
    }


}
