/**
 * Created by Liu Cao on 3/26/2018.
 */

package org.nus.trailblaze.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.nus.trailblaze.R;
import org.nus.trailblaze.adapters.FileHelper;
import org.nus.trailblaze.adapters.IntentHelper;
import org.nus.trailblaze.dao.ContributedItemDao;
import org.nus.trailblaze.models.Audio;
import org.nus.trailblaze.models.ContributedItem;
import org.nus.trailblaze.models.Location;
import org.nus.trailblaze.models.Participant;
import org.nus.trailblaze.models.TextDocument;
import org.nus.trailblaze.models.TrailStation;
import org.nus.trailblaze.models.User;

import java.util.Date;
import java.util.UUID;

public class ContributedItemMediaActivity extends AppCompatActivity {
    private Button btnUploadAudio;
    private ImageButton btnChooseAudio;
    private Uri filePath;
    private final int PICK_DOC_REQUEST = 71;
    private EditText editText_Desc;
    private TextView textView_Comment;

    //Participant p= new Participant("PT1","Participant (Green)","Green@test.com");
    private User user;
    Audio ao= new Audio(UUID.randomUUID().toString(),"My Audio","",1.0f,new Date(),"P" );
    String trailStationId;
    String learningTailId;
    ContributedItem ci;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contributed_item_media);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        //Initialize Views
        btnChooseAudio = (ImageButton) findViewById(R.id.btnChooseAudio);
        btnUploadAudio = (Button) findViewById(R.id.btnUploadAudio);
        editText_Desc=(EditText)findViewById(R.id.editText_Desc);
        textView_Comment=(TextView)findViewById(R.id.textView_Comment);

        Intent intent = getIntent();
        learningTailId = intent.getStringExtra("trailID");
        trailStationId = intent.getStringExtra("stationID");
        user=(User)intent.getSerializableExtra("user");
        btnChooseAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
                ci= new ContributedItem(UUID.randomUUID().toString(),user,new Date(),ao,editText_Desc.getText().toString(),trailStationId,learningTailId);
            }
        });

        btnUploadAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContributedItemDao ciDao= new ContributedItemDao(ContributedItemMediaActivity.this,ci);
                ciDao.SaveContributedItem(filePath,"audio");

            }
        });
    }

    private void chooseFile() {
        Intent intent= new Intent(Intent.ACTION_GET_CONTENT);
        intent=  IntentHelper.SetIntentType("audio/video",intent);
        startActivityForResult(Intent.createChooser(intent,"Choose Audio/Video"), PICK_DOC_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_DOC_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            ao.setMimeType(getContentResolver().getType(filePath));
            Cursor cursor = getContentResolver().query(filePath, null, null, null, null);
            FileHelper helper= new FileHelper(filePath,cursor);
            helper.SetFileProperty(ao);
            textView_Comment.setText(ao.getName());
        }
    }

}

