package org.nus.trailblaze.views;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import org.nus.trailblaze.R;
import org.nus.trailblaze.dao.TrailStationDao;
import org.nus.trailblaze.models.LearningTrail;
import org.nus.trailblaze.models.Location;
import org.nus.trailblaze.models.TrailStation;
import org.nus.trailblaze.models.Trainer;
import org.nus.trailblaze.models.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by AswathyLeelakumari on 24/3/2018.
 */


public class SetTrailStationActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mSaveBtn;
    private EditText mInstrn;
    private EditText mName;
    private EditText mSeq;
    private FirebaseFirestore mFireStore;
    public final static String DOCUMENTID ="org.nus.trailblaze.docID";
    public final static String SEQVALUE ="org.nus.trailblaze.seqVal";
    public final static String NAMEVALUE = "org.nus.trailblaze.nameVal";
    public final static String INSTRVALUE = "org.nus.trailblaze.instrVal";
    public final static String LOCVALUE = "org.nus.trailblaze.locVal";
    Map<String, String> stationMap = new HashMap<>();
    private TrailStation trailStation;
    private Location location;
    private String documentID;
    private String nameValue;
    private String locValue;
    private String instrValue;
    private  String seqValue;
    private String trailId;
    private Trainer trainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_trail_station);

        mFireStore = FirebaseFirestore.getInstance();

        mName = (EditText) findViewById(R.id.trail_name);
        mInstrn = (EditText) findViewById(R.id.trail_instructions);
        mSeq = (EditText) findViewById(R.id.trail_sequence);
        mSaveBtn = (Button) findViewById(R.id.btn_save_station);
        mSaveBtn.setOnClickListener(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        nameValue = bundle.getString(NAMEVALUE);
        seqValue = bundle.getString(SEQVALUE);
        documentID = bundle.getString(DOCUMENTID);
        instrValue = bundle.getString(INSTRVALUE);
        locValue = bundle.getString(LOCVALUE);

        trailId = intent.getStringExtra("trailID");

        //this.trainer = Trainer.fromUser((User) this.getIntent().getExtras().get("trainer"));


        if (nameValue != null) {
            nameValue = nameValue.substring(nameValue.lastIndexOf("-") + 1);
            mName.setText(nameValue);
        }
        if (instrValue != null) {
            instrValue = instrValue.substring(instrValue.lastIndexOf("-") + 1);
            mInstrn.setText(instrValue);
        }
        if (seqValue != null) {
            seqValue = seqValue.substring(seqValue.lastIndexOf("-") + 1);
            mSeq.setText(seqValue);
        }
        if (documentID != null) {
            Log.d("docId not null", documentID);
        } else {
            Date date = new Date();
            documentID = (String) DateFormat.format("yyMMddhhmmss", date);
        }


        Toolbar stnToolbar = (Toolbar) findViewById(R.id.stnEditToolbar);
        setSupportActionBar(stnToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        //autocompleteFragment.getView().setBackgroundColor(Color.BLUE);
        EditText et = (EditText) autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input);
        et.setHint("Search Location");
        et.setTextSize(17.0f);
        if(locValue != null) {
            et.setText(locValue);
        }

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                location = new Location(place.getLatLng().longitude, place.getLatLng().latitude,
                        place.getName().toString());

                Log.i("Place", "Place: " + location.getName());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("Place", "An error occurred: " + status);
            }
        });
    }

    public void onClick (View view) {
        String stationName = mName.getText().toString();
        String stationInstrn = mInstrn.getText().toString();
        String stationSeq = mSeq.getText().toString();

        trailStation = new TrailStation(documentID, location, stationName, stationInstrn, stationSeq, trailId);
        TrailStationDao trailStationDao = new TrailStationDao(SetTrailStationActivity.this, trailStation);
        trailStationDao.SaveTrailStation(documentID)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        SetTrailStationActivity.this.finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("[DEBUG/FAIL]", e.toString());
            }
        });
    }



}
