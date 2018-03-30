package org.nus.trailblaze.dao;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.nus.trailblaze.models.TrailStation;
import org.nus.trailblaze.models.Trainer;

/**
 * Created by AswathyLeelakumari on 30/3/2018.
 */

public class TrailStationDao {

    private static final String COLLECTION = "stations";
    private CollectionReference ref;
    private FirebaseFirestore db;
    private Context current;
    private TrailStation trailStation;

    public TrailStationDao(CollectionReference ref){
        this.ref = ref;
    }

    public TrailStationDao(Context current, TrailStation trailStation){
        this.ref = FirebaseFirestore.getInstance().collection(TrailStationDao.COLLECTION);
        this.db= FirebaseFirestore.getInstance();
        this.current=current;
        this.trailStation = trailStation;
    }

    public void SaveTrailStation(String documentID) {
        db.collection(COLLECTION).document(documentID).set(trailStation).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(current,"Station changes saved",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String err = e.getMessage();
                Toast.makeText(current, "Error: "+ err,Toast.LENGTH_SHORT).show();
                Log.d("SaveTrailStation Error:", e.toString());
            }
        });
    }


    public Task<Void> deleteStation(String documentID){
        return this.ref.document(documentID).delete();
    }
}
