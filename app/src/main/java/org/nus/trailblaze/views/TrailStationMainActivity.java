
package org.nus.trailblaze.views;

        import android.app.Activity;
        import android.content.Intent;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.support.v7.widget.Toolbar;
        import android.util.Log;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.Button;
        import android.widget.ProgressBar;
        import android.widget.TextView;

        import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
        import com.firebase.ui.firestore.FirestoreRecyclerOptions;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.firestore.FirebaseFirestore;
        import com.google.firebase.firestore.Query;

        import org.nus.trailblaze.R;
        import org.nus.trailblaze.TrailBlazeMainActivity;
        import org.nus.trailblaze.adapters.TrailStationFirestoreAdapter;
        import org.nus.trailblaze.listeners.ListItemClickListener;
        import org.nus.trailblaze.models.Participant;
        import org.nus.trailblaze.models.TrailStation;
        import org.nus.trailblaze.models.Trainer;
        import org.nus.trailblaze.models.User;

/**
 * Created by AswathyLeelakumari on 24/3/2018.
 */

public class TrailStationMainActivity extends AppCompatActivity implements ListItemClickListener {

    private static final String TAG = "TrailStationMainActivity";
    private static final Class setStationView = SetTrailStationActivity.class;

    private RecyclerView mRecyclerView;
    private Button mBtnAddStation;
    private Button btnUpdate;
    private ProgressBar progressBar;
    private TextView noResultTextView;

    private RecyclerView.LayoutManager mLayoutManager;
    private FirestoreRecyclerAdapter adapter;
    private FirebaseFirestore firestoreDB;
    private FirebaseAuth firebaseAuth;
    private Trainer trainer;
    private String trailID;
    private String userMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trail_station_main);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        noResultTextView = (TextView) findViewById(R.id.ts_noResult);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_trail_station_list);
        btnUpdate = (Button) findViewById(R.id.btnUpdateOptions);
        mBtnAddStation = (Button) findViewById(R.id.btn_add_trail_station);

        Intent intent = getIntent();
        trailID = intent.getStringExtra("trailID");
        userMode = intent.getStringExtra("userMode");


        mBtnAddStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newStnIntent = new Intent(TrailStationMainActivity.this, TrailStationMainActivity.setStationView);
                newStnIntent.putExtra("trailID", trailID);
                //newStnIntent.putExtra("trainer", Trainer.fromUser(TrailStationMainActivity.this.trainer));
                startActivity(newStnIntent);
            }
        });

       // this.trainer = Trainer.fromUser((User) this.getIntent().getExtras().get("trainer"));

        if (userMode.equals("trainer"))
        {
            mBtnAddStation.setVisibility(View.VISIBLE); //SHOW the button

        }

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        firestoreDB = FirebaseFirestore.getInstance();

        //Account Settings Toolbar
        Toolbar trailToolbar = (Toolbar) findViewById(R.id.trailToolbar);
        setSupportActionBar(trailToolbar);
        getSupportActionBar().setTitle(trailID);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadTrialStations();

    }

    private void loadTrialStations() {
        Query query = firestoreDB.collection("stations").whereEqualTo("trailId",trailID).orderBy("sequence");

        final FirestoreRecyclerOptions<TrailStation> response = new FirestoreRecyclerOptions.Builder<TrailStation>()
                .setQuery(query, TrailStation.class)
                .build();

        adapter = new TrailStationFirestoreAdapter(response, this, this.trainer) {
            @Override
            public void onDataChanged() {
                progressBar.setVisibility(View.GONE);
                noResultTextView.setVisibility((response.getSnapshots().size() == 0 ? View.VISIBLE : View.GONE));
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
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onListItemClick(int position) {
        TrailStation item = (TrailStation) adapter.getItem(position);
        Class intentClass = ViewDetailStationActivity.class;

        Intent intent = new Intent(getApplicationContext(), DiscussionThreadActivity.class);
        if (userMode.equals("participant")) {
            intent = new Intent(getApplicationContext(), ViewDetailStationActivity.class);
            intent.putExtra("location",item.getLocation().getName());
            intent.putExtra("instructions",item.getInstruction());
        }
        intent.putExtra("trailID", trailID);
        intent.putExtra("stationID",item.getId());
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(TrailStationMainActivity.this, RoleToggler.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("user", Trainer.fromUser(this.trainer));
                startActivity(intent);
                finish();
                return true;
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
        Intent loginIntent = new Intent(TrailStationMainActivity.this,TrailBlazeMainActivity.class);
        startActivity(loginIntent);
        finish();
    }

    private void goToAccountSetupActivity(){
        Intent setupActivity = new Intent(TrailStationMainActivity.this,SetupActivity.class);
        startActivity(setupActivity);
        finish();
    }
}
