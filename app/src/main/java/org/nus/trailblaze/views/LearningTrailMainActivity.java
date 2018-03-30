package org.nus.trailblaze.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.nus.trailblaze.R;
import org.nus.trailblaze.TrailBlazeMainActivity;
import org.nus.trailblaze.adapters.LearningTrailFirestoreAdaptor;
import org.nus.trailblaze.listeners.ListItemClickListener;
import org.nus.trailblaze.models.LearningTrail;
import org.nus.trailblaze.models.Trainer;
import org.nus.trailblaze.models.User;
import org.nus.trailblaze.viewholders.LearningTrailHolder;

public class LearningTrailMainActivity extends AppCompatActivity implements ListItemClickListener {

    private static final String TAG = "LearningTrailActivity";
    public static final int CREATE_NEW_TRAIL = 1000;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirestoreRecyclerAdapter adapter;
    private FirebaseFirestore firestoreDB;
    private FirebaseAuth firebaseAuth;
    private Toolbar mainToolbar;

    private ProgressBar progressBar;
    private Button btnAddLearningTrail;
    private Trainer trainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.learning_trail_main);

        this.trainer = Trainer.fromUser((User) this.getIntent().getExtras().get("trainer"));

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mRecyclerView = (RecyclerView) findViewById(R.id.rvLearningTrail);
        btnAddLearningTrail = (Button) findViewById(R.id.btnAddLearningTrail);
        btnAddLearningTrail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LearningTrailMainActivity.this, SetLearningTrailActivity.class);
                intent.putExtra("trainer", Trainer.fromUser(LearningTrailMainActivity.this.trainer));
                startActivity(intent);
            }
        });

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        firestoreDB = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        //Account Settings Toolbar
        Toolbar mainToolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setTitle("Learning Trails");
        loadItemsList();
    }

    private void loadItemsList() {
        Query query = firestoreDB.collection("trails").whereEqualTo("trainer.id", this.trainer.getId());

        FirestoreRecyclerOptions<LearningTrail> response = new FirestoreRecyclerOptions.Builder<LearningTrail>()
                .setQuery(query, LearningTrail.class)
                .build();

        adapter = new LearningTrailFirestoreAdaptor(response, this, this.trainer) {
            @Override
            public void onDataChanged() {
                progressBar.setVisibility(View.GONE);
            }
        };

        adapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(adapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser == null){
            sendToLogin();
        }
        adapter.startListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onListItemClick(int position) {
        LearningTrail item = (LearningTrail) adapter.getItem(position);

        Intent intent = new Intent(this, ViewTrailStationActivity.class);
        intent.putExtra("trailID", item.getId());
        startActivity(intent);
    }

//    private class OptionsButtonViewHolder extends RecyclerView.ViewHolder {
//
//        public TextView LearningTrailName;
//        public Button buttonOptions;
//
//        public OptionsButtonViewHolder(View itemView) {
//            super(itemView);
//
//            LearningTrailName = (TextView) itemView.findViewById(R.id.tvLearningTrailName);
//            buttonOptions = (Button) itemView.findViewById(R.id.buttonOptions);
//        }
//    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.settings_menu:
                goToAccountSetupActivity();
                return true;
            case R.id.logout_menu:
                logout();
                return true;
            default:
                return false;
        }
    }

    private void logout(){
        firebaseAuth.signOut();
        sendToLogin();
    }
    private void sendToLogin(){
        Intent loginIntent = new Intent(LearningTrailMainActivity.this,TrailBlazeMainActivity.class);
        startActivity(loginIntent);
        finish();
    }
    private void goToAccountSetupActivity(){
        Intent setupActivity = new Intent(LearningTrailMainActivity.this,SetupActivity.class);
        startActivity(setupActivity);
        finish();
    }

}
