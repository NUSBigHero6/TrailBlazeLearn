package org.nus.trailblaze.listeners;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import org.nus.trailblaze.models.Participant;
import org.nus.trailblaze.models.User;
import org.nus.trailblaze.views.ParticipantJoin;

/**
 * Created by plasmashadow on 3/30/18.
 */

public class TrialSearchListener implements OnCompleteListener<QuerySnapshot> {

    private ParticipantJoin activity;
    private Class nextActivity;
    private User user;
    private String trailID;

    public TrialSearchListener(ParticipantJoin activity, Class next, User user, String trialid){
        this.activity = activity;
        this.nextActivity = next;
        this.user = user;
        this.trailID = trialid;
    }


    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {
        Log.d("[TASK]", String.valueOf(task.isComplete()));
        if(task.isSuccessful()){
            QuerySnapshot result = task.getResult();
            Log.d("query_result", String.valueOf(result.isEmpty()));
            if(!result.isEmpty()){
                Intent intent = new Intent(this.activity, this.nextActivity);
                intent.putExtra("userMode", "participant");
                intent.putExtra("participant", Participant.fromUser(this.user));
                intent.putExtra("trailID", this.trailID);
                // lines are needed for back navigation.
                this.activity.searchBtn.setEnabled(true);
                this.activity.searchid.setEnabled(true);
                this.activity.bar.setVisibility(ProgressBar.INVISIBLE);
                this.activity.startActivity(intent);
            }
            else {
                Toast.makeText(this.activity, "Learning trail not found", Toast.LENGTH_SHORT).show();
                this.activity.searchBtn.setEnabled(true);
                this.activity.searchid.setEnabled(true);
                this.activity.bar.setVisibility(ProgressBar.INVISIBLE);
            }
        }
        else {
            Toast.makeText(this.activity, "Something wrong", Toast.LENGTH_SHORT).show();
        }
    }
}