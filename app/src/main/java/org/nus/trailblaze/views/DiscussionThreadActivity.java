package org.nus.trailblaze.views;
//Author : Priyanka Jalan

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.nus.trailblaze.R;
import org.nus.trailblaze.models.Post;
import org.nus.trailblaze.models.ThreadPost;
import org.nus.trailblaze.viewholders.ThreadHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class DiscussionThreadActivity extends AppCompatActivity {

    private RecyclerView threadListView;
    private List<ThreadPost> threadList;

    private FirebaseFirestore firebaseFirestore;
    private ThreadHolder threadHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion_thread);

        //Thread Toolbar
        Toolbar setupToolbar = findViewById(R.id.threadToolbar);
        setupToolbar.setTitle("Discussion Thread");
        setupToolbar.setSubtitle("Semakau-2017");

        threadListView = findViewById(R.id.threadListView);
        threadList = new ArrayList<>();

        threadHolder = new ThreadHolder(threadList);
        threadListView.setLayoutManager(new LinearLayoutManager(getParent()));
        threadListView.setAdapter(threadHolder);

        firebaseFirestore = FirebaseFirestore.getInstance();

         firebaseFirestore.collection("discussion_threads").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                     for(DocumentChange doc: documentSnapshots.getDocumentChanges()){
                         //Handle Added thread post
                         if(doc.getType() == DocumentChange.Type.ADDED){
                             ThreadPost threadPost = doc.getDocument().toObject(ThreadPost.class);
                             threadList.add(threadPost);
                             threadHolder.notifyDataSetChanged();
                         }
                     }
            }
        });
    }

}
