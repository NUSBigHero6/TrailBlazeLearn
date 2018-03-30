package org.nus.trailblaze.listeners;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;

/**
 * Created by plasmashadow on 3/21/18.
 */

public class SignInFailureListener implements OnFailureListener {

    private Activity activity;

    public SignInFailureListener(Activity act){
        this.activity = act;
    }


    @Override
    public void onFailure(@NonNull Exception e) {
        Log.d("Failure", e.toString());
        Toast.makeText(this.activity, e.toString(), Toast.LENGTH_SHORT);
    }
}