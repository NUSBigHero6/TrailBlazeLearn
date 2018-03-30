
package org.nus.trailblaze.views;

        import android.app.Activity;
        import android.content.Intent;
        import android.os.Bundle;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;

        import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
        import com.firebase.ui.firestore.FirestoreRecyclerOptions;
        import com.google.firebase.firestore.FirebaseFirestore;
        import com.google.firebase.firestore.Query;

        import org.nus.trailblaze.R;
        import org.nus.trailblaze.adapters.TrailStationFirestoreAdapter;
        import org.nus.trailblaze.listeners.ListItemClickListener;
        import org.nus.trailblaze.models.Participant;
        import org.nus.trailblaze.models.TrailStation;
        import org.nus.trailblaze.models.Trainer;
        import org.nus.trailblaze.models.User;

/**
 * Created by AswathyLeelakumari on 24/3/2018.
 */

public class TrailStationMainActivity extends Activity implements ListItemClickListener {

    private static final String TAG = "TrailStationMainActivity";
    private static final Class setStationView = SetTrailStationActivity.class;

    private RecyclerView mRecyclerView;
    private Button mBtnAddStation;
    private Button btnUpdate;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirestoreRecyclerAdapter adapter;
    private FirebaseFirestore firestoreDB;
    private Trainer trainer;
    private Participant participant;
    private String trailID;
    private String userMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trail_station_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_trail_station_list);
        btnUpdate = (Button) findViewById(R.id.btnUpdateOptions);
        mBtnAddStation = (Button) findViewById(R.id.btn_add_trail_station);

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


        Intent intent = getIntent();
        trailID = intent.getStringExtra("trailID");
        userMode = intent.getStringExtra("userMode");


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

        loadTrialStations();


    }

    private void loadTrialStations() {
        Query query = firestoreDB.collection("stations").whereEqualTo("trailId",trailID).orderBy("sequence");
        Log.d("query bundle", String.valueOf(query));

        FirestoreRecyclerOptions<TrailStation> response = new FirestoreRecyclerOptions.Builder<TrailStation>()
                .setQuery(query, TrailStation.class)
                .build();

        adapter = new TrailStationFirestoreAdapter(response, this, this.trainer);

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
        TrailStation item = (TrailStation) adapter.getItem(position);
        Log.d("stations activity", item.getName());

    }


    public void btnAddStation(View view) {
        Log.d("redirecting activity","new station");

    }
}
