package org.nus.trailblaze.views;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import org.nus.trailblaze.R;
import org.nus.trailblaze.dao.LearningTrailDao;
import org.nus.trailblaze.listeners.SignInFailureListener;
import org.nus.trailblaze.listeners.TrialSearchListener;
import org.nus.trailblaze.models.Participant;
import org.nus.trailblaze.models.User;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ParticipantJoin extends AppCompatActivity {

    private static Class next = TrailStationMainActivity.class;
    private Participant participant;
    private LearningTrailDao dao;

    @BindView(R.id.searchId)
    public EditText searchid;

    @BindView(R.id.searchPrgoressBar)
    public ProgressBar bar;

    @BindView(R.id.searchTrail)
    public Button searchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant_join);
        ButterKnife.bind(this);
        this.participant = Participant.fromUser((User) this.getIntent().getExtras().get("participant"));
        this.dao = new LearningTrailDao();
    }

    public void search(View v){
        String term = this.searchid.getText().toString();
        if(this.validate(term)){
            this.searchAndSend(term);
        }
        else {
            Log.d("[Alert/Debug]", "Invalid term");
        }

    }

    public boolean validate(String term){
        return term != null;
    }

    public void searchAndSend(String trailid){
        Log.d("trailid", trailid);
        this.bar.setVisibility(ProgressBar.VISIBLE);
        this.searchid.setEnabled(false);
        this.searchBtn.setEnabled(false);
        Task<QuerySnapshot> querySnapshotTask = this.dao.getTrailById(trailid)
                .addOnCompleteListener(new TrialSearchListener(this, ParticipantJoin.next, this.participant, trailid))
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        ParticipantJoin.this.bar.setVisibility(ProgressBar.INVISIBLE);
                        ParticipantJoin.this.searchid.setEnabled(true);
                        ParticipantJoin.this.searchBtn.setEnabled(true);
                    }
                });
    }

}
