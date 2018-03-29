package org.nus.trailblaze.views;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import org.nus.trailblaze.R;
import org.nus.trailblaze.TrailBlazeMainActivity;
import org.nus.trailblaze.dao.AuthDao;
import org.nus.trailblaze.models.Participant;
import org.nus.trailblaze.models.Trainer;
import org.nus.trailblaze.models.User;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RoleToggler extends Activity {

    private static final Class backToHome = TrailBlazeMainActivity.class;
    private static final Class trainerView = LearningTrailMainActivity.class;
    private static final Class participantView = ParticipantJoin.class;

    private AuthDao dao;
    private User user;

    @BindView(R.id.userEmailDisplay)
    TextView email;

    @BindView(R.id.usernameDisplay)
    TextView username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_toggler);
        ButterKnife.bind(this);
        dao = new AuthDao();
        // assuming the screen wouldn't be reached without an intent.
        user = (User) this.getIntent().getExtras().get("user");
        this.email.setText(user.getEmail());
        this.username.setText(user.getName());
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

    protected void onLogout(View view){
        dao.signOut();
        Intent intent = new Intent(this, RoleToggler.backToHome);
        startActivity(intent);
    }

}
