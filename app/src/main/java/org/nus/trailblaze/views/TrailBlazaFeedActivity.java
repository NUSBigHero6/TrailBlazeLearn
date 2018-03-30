package org.nus.trailblaze.views;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.os.Bundle;
import android.util.Log;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.nus.trailblaze.R;
import org.nus.trailblaze.adapters.FeedFirestoreAdapter;
import org.nus.trailblaze.listeners.ListItemClickListener;
import org.nus.trailblaze.models.ContributedItem;

/**
 * Created by wengweichen on 14/3/18.
 */

public class TrailBlazaFeedActivity extends Activity
        implements ListItemClickListener {

    private static final String TAG = "TrailBlazaFeedActivity";

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirestoreRecyclerAdapter adapter;
    private FirebaseFirestore firestoreDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trail_blaze_feed);
        mRecyclerView = (RecyclerView) findViewById(R.id.tb_feed_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        firestoreDB = FirebaseFirestore.getInstance();

        loadItemsList();
    }

    private void loadItemsList() {
        Query query = firestoreDB.collection("contributed_items");

        FirestoreRecyclerOptions<ContributedItem> response = new FirestoreRecyclerOptions.Builder<ContributedItem>()
                .setQuery(query, ContributedItem.class)
                .build();

        adapter = new FeedFirestoreAdapter(response, this);

        adapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onListItemClick(int position) {
        ContributedItem item = (ContributedItem) adapter.getItem(position);
        Log.d("ACTIVITY", item.getFile().getMimeType());

        //temporary comment
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
