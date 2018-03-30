package org.nus.trailblaze.dao;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.nus.trailblaze.models.LearningTrail;
import org.nus.trailblaze.models.Trainer;

/**
 * Created by plasmashadow on 3/24/18.
 */

public class LearningTrailDao {

    private static final String TRAIL_COLLECTION = "trails";
    private CollectionReference ref;
    private FirebaseFirestore db;
    private Context current;
    private LearningTrail learningTrail;

    public LearningTrailDao(CollectionReference ref){
        this.ref = ref;
    }

    public LearningTrailDao(Context current, LearningTrail learningTrail){
        this.ref = FirebaseFirestore.getInstance().collection(LearningTrailDao.TRAIL_COLLECTION);
        this.db= FirebaseFirestore.getInstance();
        this.current=current;
        this.learningTrail = learningTrail;
    }

    public LearningTrailDao(){
        this.ref = FirebaseFirestore.getInstance().collection(LearningTrailDao.TRAIL_COLLECTION);
    }

    public void SaveLearningTrail(String documentID){

        if (documentID == null){
            db.collection(TRAIL_COLLECTION).document().set(learningTrail)

                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(current, "Learning Trail Saved",
                                    Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(current, "ERROR" + e.toString(),
                                    Toast.LENGTH_SHORT).show();
                            Log.d("TAG", e.toString());
                        }
                    });
        }
        else {
            db.collection(TRAIL_COLLECTION).document(documentID).set(learningTrail)

                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(current, "Learning Trail Saved",
                                    Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(current, "ERROR" + e.toString(),
                                    Toast.LENGTH_SHORT).show();
                            Log.d("TAG", e.toString());
                        }
                    });
        }
    }

    public Task<QuerySnapshot> getTrailById(String trail){
        return  this.ref.whereEqualTo("name", trail).get();
    }

    public Task<QuerySnapshot> getTrails(Trainer trainer){
        return this.ref.whereEqualTo("trainer_id", trainer.getId()).get();
    }

    public Task<Void> createNewTrail(LearningTrail trail){
        return this.ref.document(trail.getId()).set(trail);
    }

    public Task<Void> deleteTrail(LearningTrail trail){
        return this.ref.document(trail.getId()).delete();
    }

}
