package org.nus.trailblaze.views;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;

import org.nus.trailblaze.R;
import org.nus.trailblaze.models.Participant;
import org.nus.trailblaze.models.Trainer;
import org.nus.trailblaze.models.User;

public class RoleToggler extends Activity {

    private static final Class trainerView = TrailBlazaFeedActivity.class;
    private static final Class participantView = ParticipantJoin.class;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_toggler);

        // assuming the screen wouldn't be reached without an intent.
        user = (User) this.getIntent().getExtras().get("user");
    }

    protected void onTrainer(View view){
        Intent trainerIntent = new Intent(this, TrailMapActivity.class);
        trainerIntent.putExtra("trainer", Trainer.fromUser(this.user));
        startActivity(trainerIntent);
    }

    protected void onParticipant(View view){
        Intent participantIntent = new Intent(this, RoleToggler.participantView);
        participantIntent.putExtra("participant", Participant.fromUser(this.user));
        startActivity(participantIntent);
    }

}
