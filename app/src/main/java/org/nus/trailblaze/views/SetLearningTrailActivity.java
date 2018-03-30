package org.nus.trailblaze.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import org.nus.trailblaze.R;

/**
 * Created by priyanka on 15/3/2018.
 */

public class SetLearningTrailActivity extends AppCompatActivity implements View.OnClickListener {

    private String trailcode;
    private TextView titlecode;
    private EditText et;
    private Button btn;
    private String ymd;
    public static final String NAME = "name";
    private static final String COLLECTION = "trails";
    public final static String DOCUMENTID ="org.nus.trailblaze.docID";
    public final static String NAMEVALUE = "org.nus.trailblaze.nameID";
    private String documentID;
    private String nameValue;

    Map<String, Object> trailDataMap = new HashMap<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_learning_trail);
        titlecode = findViewById(R.id.trailCodeDisplay);
        btn = (Button) findViewById(R.id.newTrail);
        btn.setOnClickListener(this);
        et = (EditText) findViewById(R.id.trailCode);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        nameValue = bundle.getString(NAMEVALUE);
        documentID = bundle.getString(DOCUMENTID);

        if (nameValue != null) {
            nameValue = nameValue.substring(nameValue.lastIndexOf("-") + 1);
            et.setText(nameValue);
        }

        Date date = new Date();
        String day          = (String) DateFormat.format("dd", date);
        String monthNumber  = (String) DateFormat.format("MM", date);
        String year         = (String) DateFormat.format("yy", date);

        ymd = year + monthNumber + day;
        titlecode.setText(ymd);

        et.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                trailcode = titlecode.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {
                String onTextChanged = et.getText().toString();
                onTextChanged = ymd + "-" + onTextChanged;
                titlecode.setText(onTextChanged);
                trailcode = titlecode.getText().toString();
                trailDataMap.put(NAME, trailcode);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick (View view) {

        if (trailcode == null){
            Toast.makeText(SetLearningTrailActivity.this, "Please enter a Trail Code",
                    Toast.LENGTH_SHORT).show();
        }
        else if (documentID == null) {
            db.collection(COLLECTION).document().set(trailDataMap)

                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(SetLearningTrailActivity.this, "Learning Trail Saved",
                                    Toast.LENGTH_SHORT).show();

                            Intent i = new Intent(getApplicationContext(), LearningTrailMainActivity.class);
                            startActivity(i);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SetLearningTrailActivity.this, "ERROR" + e.toString(),
                                    Toast.LENGTH_SHORT).show();
                            Log.d("TAG", e.toString());
                        }
                    });
        }
    }
}
