package org.nus.trailblaze.views;
//Author: Priyanka Jalan
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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.nus.trailblaze.R;
import org.nus.trailblaze.models.DiscussionThread;
import org.nus.trailblaze.models.ThreadPost;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class DiscussionThreadNewPostActivity extends AppCompatActivity {

    private static final int MAX_LENGTH = 100 ;
    //Declaring Widgets
    private ImageView threadPostImage;
    private EditText threadPostMessage;
    private Uri postImageUri = null;

    //Declaring Firebase
//    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion_thread_new_post);

        //Widget Typecast
        threadPostImage = (ImageView) findViewById(R.id.thread_post_image);
        threadPostMessage = (EditText) findViewById(R.id.thread_post_msg);

        //Initialize Firebase
//        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

    }

    public void imageUploadClickListener(View view){
        //Check and Handle Gallery access permission
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(DiscussionThreadNewPostActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                showToastMessage("Permission Denied");
                ActivityCompat.requestPermissions(DiscussionThreadNewPostActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
            }else{
                openGallery();
            }
        }else{
            openGallery();
        }
    }

    public void openGallery(){
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMinCropResultSize(512,512)
                .start(DiscussionThreadNewPostActivity.this);
    }

    public void showToastMessage(String message){
        Toast.makeText(DiscussionThreadNewPostActivity.this,message,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        DiscussionThreadNewPostActivity.super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                postImageUri = result.getUri();
                threadPostImage.setImageURI(postImageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public void addPostClickListener(View view){
        final String message = threadPostMessage.getText().toString();
        if(!TextUtils.isEmpty(message) && postImageUri != null){
            //Start Uploading to Storage
            final String postId = UUID.randomUUID().toString();
            String randomName = UUID.randomUUID().toString();
            StorageReference filePath = storageReference.child("post_images").child(randomName + ".jpg");
            filePath.putFile(postImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        //Store In Firestore
                        String downloadUri = task.getResult().getDownloadUrl().toString();

                        Map<String, Object> postMap = new HashMap<>();
                        postMap.put("id",postId);
                        postMap.put("url",downloadUri);
                        postMap.put("message",message);
                        postMap.put("createdDate",FieldValue.serverTimestamp());
                        postMap.put("userId","C0HJXoLJhyQWio5ybV8b9SfsU0J3");

                        firebaseFirestore.collection("discussion_threads").document("48mgr5JTsjwrryuIXQMB").collection("posts").add(postMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if(task.isSuccessful()){
                                    Intent discussionThreadIntent = new Intent(DiscussionThreadNewPostActivity.this, DiscussionThreadActivity.class);
                                    startActivity(discussionThreadIntent);
                                    finish();
                                }else{
                                    showToastMessage("Post add failed");
                                }
                            }
                        });

                    }else{
                        showToastMessage("Image Upload Failed");
                    }
                }
            });

        }
    }


}
