package org.nus.trailblaze.views;

import android.content.Intent;
import android.os.Bundle;
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
import java.util.UUID;

import org.nus.trailblaze.R;
import org.nus.trailblaze.dao.LearningTrailDao;
import org.nus.trailblaze.models.LearningTrail;
import org.nus.trailblaze.models.Trainer;
import org.nus.trailblaze.models.User;

/**
 * Created by priyanka on 15/3/2018.
 */

public class SetLearningTrailActivity extends AppCompatActivity implements View.OnClickListener {

    private String trailcode;
    private TextView titlecode;
    private EditText et;
    private Button btn;
    private String ymd;
    public final static String DOCUMENTID ="org.nus.trailblaze.docID";
    public final static String NAMEVALUE = "org.nus.trailblaze.nameID";
    private String documentID;
    private String nameValue;
    private LearningTrail learningTrail;
    private String trailName;
    private Trainer trainer;
    private Button btnManageStation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_learning_trail);

        titlecode = findViewById(R.id.trailCodeDisplay);
        btn = (Button) findViewById(R.id.newTrail);
        btn.setOnClickListener(this);
        et = (EditText) findViewById(R.id.trailCode);
        btnManageStation = (Button) findViewById(R.id.btnManageStation);
        btnManageStation.setVisibility(Button.INVISIBLE);
        btnManageStation.setOnClickListener(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        nameValue = bundle.getString(NAMEVALUE);
        documentID = bundle.getString(DOCUMENTID);

        Date date = new Date();
        ymd = (String) DateFormat.format("yyMMdd", date);
        titlecode.setText(ymd);

        if (nameValue != null) {
            titlecode.setText(nameValue);
            nameValue = nameValue.substring(nameValue.lastIndexOf("-") + 1);
            et.setText(nameValue);
        }

        this.trainer = Trainer.fromUser((User) this.getIntent().getExtras().get("trainer"));

        et.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                trailcode = titlecode.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {
                trailName = et.getText().toString();
                trailName = ymd + "-" + trailName;
                titlecode.setText(trailName);
            }

            @Override
            public void afterTextChanged(Editable s) {
                trailcode = titlecode.getText().toString();
            }
        });
    }

    @Override
    public void onClick (View view) {

        switch (view.getId()) {

            case R.id.newTrail:
                save();
                break;

            case R.id.btnManageStation:
                goToStationActivity();
                break;

            default:
                break;
        }

    }

    private void save() {
        if (trailcode == null){
            Toast.makeText(SetLearningTrailActivity.this, "Please enter a Trail Code",
                    Toast.LENGTH_SHORT).show();
        }
        else {

            //Generate ID for trailID
            String trailID = UUID.randomUUID().toString();
            learningTrail = new LearningTrail(trailID, new Date(), trailcode, trainer);
            LearningTrailDao learningTrailDao = new LearningTrailDao(SetLearningTrailActivity.this, learningTrail);
            learningTrailDao.SaveLearningTrail(documentID);

            Intent intent = new Intent(getApplicationContext(), LearningTrailMainActivity.class);
            intent.putExtra("trainer", Trainer.fromUser(this.trainer));
            startActivity(intent);
        }
    }

    private void goToStationActivity() {
        Intent intent = new Intent(this, TrailStationMainActivity.class);
        intent.putExtra("trailID", trailName);
        intent.putExtra("userMode", "trainer");
        intent.putExtra("trainer", Trainer.fromUser(this.trainer));
        startActivity(intent);
    }
}
