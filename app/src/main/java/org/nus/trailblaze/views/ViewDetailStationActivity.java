package org.nus.trailblaze.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.nus.trailblaze.R;
import org.nus.trailblaze.dao.TrailStationDao;
import org.nus.trailblaze.listeners.ListItemClickListener;

/**
 * Created by AswathyLeelakumari on 30/3/2018.
 */

public class ViewDetailStationActivity extends AppCompatActivity implements ListItemClickListener {

    private static final Class goDiscThread = DiscussionThreadActivity.class;

    private FirebaseFirestore firestoreDB;
    private FirebaseAuth firebaseAuth;
    private TrailStationDao dao;

    private TextView stnLocation;
    private TextView stnInstrn;
    private Button btnDiscThread;
    private String trailId;
    private String stnHeader;
    private String stnId;
    private String locName;
    private String instrn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_detail_station);

        stnLocation = (TextView) findViewById(R.id.stn_location);
        stnInstrn = (TextView) findViewById(R.id.stn_instrn);
        btnDiscThread = findViewById(R.id.btnGotoDiscThread);

        btnDiscThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent discIntent = new Intent(ViewDetailStationActivity.this, ViewDetailStationActivity.goDiscThread);
                discIntent.putExtra("trailID", trailId);
                discIntent.putExtra("stationID", stnId);
                //newStnIntent.putExtra("trainer", Trainer.fromUser(TrailStationMainActivity.this.trainer));
                startActivity(discIntent);
            }
        });

        Intent intent = getIntent();
        trailId = intent.getStringExtra("trailID");
        stnId = intent.getStringExtra("stationID");
        locName = intent.getStringExtra("location");
        instrn = intent.getStringExtra("instructions");
        stnHeader = trailId + " Details";

        stnLocation.setText(locName);
        stnInstrn.setText(instrn);

        Toolbar viewToolbar = (Toolbar) findViewById(R.id.viewToolbar);
        viewToolbar.setTitle(stnHeader);
    }

    public void onListItemClick(int position) {}
}
