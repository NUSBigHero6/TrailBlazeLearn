package org.nus.trailblaze.dao;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.nus.trailblaze.models.ContributedItem;
import org.nus.trailblaze.views.ContributedItemDocActivity;

/**
 * Created by liu.cao on 26/3/2018.
 */

public class ContributedItemDao {
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseFirestore db;
    private Context current;
    private ContributedItem ci;
    public ContributedItemDao(Context current,ContributedItem ci)
    {
        this.storage = FirebaseStorage.getInstance();
        this.storageReference = storage.getReference();
        this.db= FirebaseFirestore.getInstance();
        this.current=current;
        this.ci=ci;
    }
    public void SaveContributedItem(Uri uri, String type)
    {
        if(uri != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(current);
            progressDialog.setTitle("Saving...");
            progressDialog.show();
            StorageReference ref = storageReference.child(type+"/"+ ci.getFile().getName().toString());
            ref.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            ci.getFile().setUrl(downloadUrl.toString());
                            db.collection("contributed_items").document(ci.getId()).set(ci);
                            progressDialog.dismiss();
                            Toast.makeText(current, "Saved Successfully", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(current, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Saved "+(int)progress+"%");
                        }
                    });
        }
    }
    public void Dispose()
    {
        storage=null;
        storageReference=null;
        db=null;
        ci=null;
    }
}
