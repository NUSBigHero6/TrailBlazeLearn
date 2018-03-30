package org.nus.trailblaze.views;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.nus.trailblaze.R;

import java.util.HashMap;
import java.util.Map;


public class NewTrailStationActivity extends AppCompatActivity {

    private Button mSaveBtn;
    private EditText mInstrn;
    private EditText mName;
    private FirebaseFirestore mFireStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_trail_station);

        mFireStore = FirebaseFirestore.getInstance();

        mName = (EditText) findViewById(R.id.trail_name);
        mInstrn = (EditText) findViewById(R.id.trail_instructions);
        mSaveBtn = (Button) findViewById(R.id.btn_save_station);

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stationName = mName.getText().toString();
                String stationInstrn = mInstrn.getText().toString();

                Map<String, String> stationMap = new HashMap<>();
                stationMap.put("name", stationName);
                stationMap.put("id", "2");
                stationMap.put("instruction", stationInstrn);

                mFireStore.collection("stations").add(stationMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(NewTrailStationActivity.this,"Station added to Firstore",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String err = e.getMessage();
                        Toast.makeText(NewTrailStationActivity. this, "Error: "+ err,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

}
