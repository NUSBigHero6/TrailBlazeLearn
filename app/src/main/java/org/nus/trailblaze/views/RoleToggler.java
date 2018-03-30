package org.nus.trailblaze.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.nus.trailblaze.R;
import org.nus.trailblaze.TrailBlazeMainActivity;
import org.nus.trailblaze.dao.AuthDao;
import org.nus.trailblaze.models.Participant;
import org.nus.trailblaze.models.Trainer;
import org.nus.trailblaze.models.User;

import butterknife.ButterKnife;

public class RoleToggler extends AppCompatActivity {

    private static final Class backToHome = TrailBlazeMainActivity.class;
    private static final Class trainerView = LearningTrailMainActivity.class;
    private static final Class participantView = ParticipantJoin.class;

    private AuthDao dao;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_toggler);
        ButterKnife.bind(this);
        dao = new AuthDao();
         //assuming the screen wouldn't be reached without an intent.
        user = (User) this.getIntent().getExtras().get("user");

        //Account Settings Toolbar
        Toolbar mainToolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setTitle(user.getName());

    }

    protected void onTrainer(View view){

        Intent trainerIntent = new Intent(this, RoleToggler.trainerView);
        trainerIntent.putExtra("trainer", Trainer.fromUser(this.user));
        startActivity(trainerIntent);
    }

    protected void onParticipant(View view){
        Intent participantIntent = new Intent(this, RoleToggler.participantView);
        participantIntent.putExtra("participant", Participant.fromUser(this.user));
        startActivity(participantIntent);
    }

    protected void logout(){
        Intent logoutIntent = new Intent(this, RoleToggler.backToHome);
        this.dao.signOut();
        startActivity(logoutIntent);
    }

    private void goToAccountSetupActivity(){
        Intent setupActivity = new Intent(RoleToggler.this, SetupActivity.class);
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
