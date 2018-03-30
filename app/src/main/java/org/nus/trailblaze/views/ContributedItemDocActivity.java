/**
 * Created by Liu Cao on 3/20/2018.
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
import android.webkit.MimeTypeMap;
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
import org.nus.trailblaze.models.ContributedItem;
import org.nus.trailblaze.models.File;
import org.nus.trailblaze.models.Location;
import org.nus.trailblaze.models.Participant;
import org.nus.trailblaze.models.TextDocument;
import org.nus.trailblaze.models.TrailStation;
import org.nus.trailblaze.models.User;
import java.util.Date;
import java.util.UUID;

public class ContributedItemDocActivity extends AppCompatActivity  {
    private Button btnUpload;
    private ImageButton btnChoose;
    private Uri filePath;
    private final int PICK_DOC_REQUEST = 71;
    private EditText editText_Description;
    private TextView textView_Comment;
    //Get Participant from context
    Participant p= new Participant("PT1","Participant (Green)","Green@test.com");
    TextDocument td= new TextDocument("Doc1","Document (PDF/Text)","Test@Url",1.0f,new Date(),"PDF/TXT" );

    String trailStationId="TrailStationId1";
    String learingTrailId="LearningTrailId1";
    ContributedItem ci;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contributed_item_doc);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        //Set toolbar text as TrailStation Id
        //Initialize Views
        btnChoose = (ImageButton) findViewById(R.id.btnChoose);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        editText_Description=(EditText)findViewById(R.id.editText_Description);
        textView_Comment=(TextView)findViewById(R.id.textView_Comment);

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
                ci= new ContributedItem("ContributedItem_7",p,new Date(),td,
                        editText_Description.getText().toString(),trailStationId,learingTrailId);

            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContributedItemDao ciDao= new ContributedItemDao(ContributedItemDocActivity.this,ci);
                ciDao.SaveContributedItem(filePath,"document");
                //Return trail station page
                startActivity(new Intent(getApplicationContext(), ContributedItemMainActivity.class));
                finish();
            }
        });
    }

    private void chooseFile()
    {
        Intent intent= new Intent(Intent.ACTION_GET_CONTENT);
        intent= IntentHelper.SetIntentType("document",intent);
        startActivityForResult(Intent.createChooser(intent,"Choose File"), PICK_DOC_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_DOC_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            td.setMimeType(getContentResolver().getType(filePath));
            Cursor cursor = getContentResolver().query(filePath, null, null, null, null);
            FileHelper helper= new FileHelper(filePath,cursor);
            helper.SetFileProperty(td);
            textView_Comment.setText(td.getName());
        }
    }

}

