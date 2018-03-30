package org.nus.trailblaze.views;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import org.nus.trailblaze.R;
import org.nus.trailblaze.dao.AuthDao;
import org.nus.trailblaze.dao.LearningTrailDao;
import org.nus.trailblaze.dao.UserDao;
import org.nus.trailblaze.listeners.TrialSearchListener;
import org.nus.trailblaze.models.Participant;
import org.nus.trailblaze.models.User;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ParticipantJoin extends AppCompatActivity {

    private static Class next = TrailStationMainActivity.class;
    private Participant participant;
    private LearningTrailDao dao;
    private AuthDao authDao;

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
        this.authDao = new AuthDao();
        Toolbar mainToolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setTitle("Join Trail");
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

    protected void logout(){
        Intent logoutIntent = new Intent(this, RoleToggler.backToHome);
        this.authDao.signOut();
        startActivity(logoutIntent);
    }

    private void goToAccountSetupActivity(){
        Intent setupActivity = new Intent(this, SetupActivity.class);
        startActivity(setupActivity);
        finish();
    }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

}
