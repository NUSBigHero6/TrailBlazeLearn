//Author: Priyanka Jalan
package org.nus.trailblaze.views;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.nus.trailblaze.R;
import org.nus.trailblaze.TrailBlazeMainActivity;
import org.nus.trailblaze.models.User;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity {

    private EditText setupNameText;
    private CircleImageView setupImage;
    private Uri mainImageURI = null;

    //Declaring Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        //Initialize Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        //Account Settings Toolbar
        Toolbar setupToolbar = (Toolbar) findViewById(R.id.setupToolbar);
        setupToolbar.setTitle("Account Settings");

        //Widget Typecast
        setupNameText = (EditText) findViewById(R.id.user_name_text);
        setupImage = (CircleImageView) findViewById(R.id.setup_image);
        setupImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check and Handle Gallery access permission
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(ContextCompat.checkSelfPermission(SetupActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(SetupActivity.this,"Permission Denied",Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(SetupActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                    }else{
                        Toast.makeText(SetupActivity.this,"Permission Granted",Toast.LENGTH_LONG).show();
                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .start(SetupActivity.this);
                    }
                }else{
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .start(SetupActivity.this);
                }
            }
        });
    }


    public void saveSettingsListener(View view) {
        final String user_name = setupNameText.getText().toString();
        if (!TextUtils.isEmpty(user_name) && mainImageURI != null) {
            final String user_id = firebaseAuth.getCurrentUser().getUid();
            final String user_email = firebaseAuth.getCurrentUser().getEmail();
            //Store image in Storage
            StorageReference image_path = storageReference.child("profile_images").child(user_id + ".jpg");
            image_path.putFile(mainImageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        Uri download_uri = task.getResult().getDownloadUrl();
                        //Prepare User Object
                        Map<String, String> userMap = new HashMap<>();
                        userMap.put("id", user_id);
                        userMap.put("name", user_name);
                        userMap.put("email",user_email);
                        userMap.put("image", download_uri.toString());

//                        User userObject = new User(user_id, user_name, user_email, download_uri.toString());

                        //Save to Firestore
                        firebaseFirestore.collection("users").document(user_id).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(SetupActivity.this, "Settings are Updated", Toast.LENGTH_LONG).show();
                                } else {
                                    String errorMessage = task.getException().getMessage();
                                    Toast.makeText(SetupActivity.this, "Error : " + errorMessage, Toast.LENGTH_LONG).show();
                                }
                            }
                        });//END Firestore
                    } else {
                        String errorMessage = task.getException().getMessage();
                        Toast.makeText(SetupActivity.this, "Error : " + errorMessage, Toast.LENGTH_LONG).show();
                    }
                }
            });//END Listener
        }
    }
    public void logoutListener(View view){
        firebaseAuth.signOut();
        Intent loginIntent = new Intent(SetupActivity.this, TrailBlazeMainActivity.class);
        startActivity(loginIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        SetupActivity.super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mainImageURI = result.getUri();
                setupImage.setImageURI(mainImageURI);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
