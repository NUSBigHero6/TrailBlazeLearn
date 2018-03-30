/**
 * Created by Liu Cao on 3/22/2018.
 */

package org.nus.trailblaze.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import org.nus.trailblaze.models.Location;
import org.nus.trailblaze.models.Participant;
import org.nus.trailblaze.models.Photo;
import org.nus.trailblaze.models.TextDocument;
import org.nus.trailblaze.models.TrailStation;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

public class ContributedItemImageActivity extends AppCompatActivity {
    private Button  btnSave;
    private ImageButton btnChoose;
    private ImageView imageView;
    private  EditText editText_Desc;
    private Uri filePath;
    Participant p= new Participant("PT1","Participant (Green)","Green@test.com");
    Photo po= new Photo("Photo1","My Photo","",1.0f,new Date(),"" );
    String trailStationId="TrailStationId1";
    String learningTrailId="learingTrailId1";
    ContributedItem ci;
    private final int PICK_IMAGE_REQUEST = 71;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contributed_item_image);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
         btnChoose = (ImageButton) findViewById(R.id.btnChoose);
         btnSave = (Button) findViewById(R.id.btnSave);
         imageView = (ImageView) findViewById(R.id.imgView);
         editText_Desc=(EditText)findViewById(R.id.editText_Desc);

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
                ci= new ContributedItem("ContributedItem_3",p,new Date(),po,editText_Desc.getText().toString(),trailStationId,learningTrailId);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContributedItemDao ciDao= new ContributedItemDao(ContributedItemImageActivity.this,ci);
                ciDao.SaveContributedItem(filePath,"image");
                //Return trail station page
                startActivity(new Intent(getApplicationContext(), ContributedItemMainActivity.class));
                finish();
            }
        });
    }
    private void chooseImage()
    {
        Intent intent= new Intent(Intent.ACTION_GET_CONTENT);
        intent=  IntentHelper.SetIntentType("image",intent);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            po.setMimeType(getContentResolver().getType(filePath));
            Cursor cursor = getContentResolver().query(filePath, null, null, null, null);
            FileHelper helper= new FileHelper(filePath,cursor);
            helper.SetFileProperty(po);

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
