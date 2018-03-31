package org.nus.trailblaze.viewholders;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
//import com.bumptech.glide.annotation.GlideModule;
//import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.nus.trailblaze.R;
import org.nus.trailblaze.models.File;
import org.nus.trailblaze.models.Post;
import org.nus.trailblaze.models.ThreadPost;
import org.nus.trailblaze.views.DiscussionThreadActivity;

import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by priyanka on 30/3/2018.
 */
//@GlideModule
public class ThreadHolder extends RecyclerView.Adapter<ThreadHolder.ViewHolder> {

    public List<ThreadPost> threadList;
    public Context context;
    FirebaseFirestore firebaseFirestore;

    public ThreadHolder(List<ThreadPost> threadList){
        this.threadList = threadList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_thread_item,parent,false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String desc_data = threadList.get(position).getMessage();//from posts
        holder.setMessage(desc_data);//setting the description

        String image_url = threadList.get(position).getUrl();//from thread posts model
        holder.setThreadImage(image_url);//setting the image

        String user_id = threadList.get(position).getUserId();
        Log.i("USER INFO",user_id);
//        //Retrieve user data
//        firebaseFirestore.collection("users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                    Log.i("USER DATA", task.getResult().toString());
//            }
//        });


        try {
            long millisecond = threadList.get(position).getCreatedDate().getTime();
            String dateString = DateFormat.format("MM/dd/yyyy", new Date(millisecond)).toString();
            holder.setCreatedDate(dateString);
        } catch (Exception e) {

            Toast.makeText(context, "Exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public int getItemCount() {
        return threadList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private View myView;
        private TextView descView;
        private ImageView threadImageView;
        private TextView threadCreatedDate;
        private TextView threadUserName;
        private CircleImageView threadUserImage;

        //Constructor for View Holder
        public ViewHolder(View itemView) {
            super(itemView);
            myView = itemView;
        }

        public void setMessage(String descText){
            descView = myView.findViewById(R.id.thread_desc);
            descView.setText(descText);
        }

        public void setThreadImage(String downloadUri){
            threadImageView = myView.findViewById(R.id.thread_post_image);
            // Load the image using Glide
//            Glide.with(context)
//                    .load(downloadUri)
//                    .into(threadImageView);
        }

        public void setCreatedDate(String createdDate){
            threadCreatedDate = myView.findViewById(R.id.thread_date);
            threadCreatedDate.setText(createdDate);
        }

        public void setUserData(String name, String userImageUrl){

            threadUserName = myView.findViewById(R.id.thread_user_name);
            threadUserImage = myView.findViewById(R.id.thread_user_image);

            threadUserName.setText(name);


//            Glide.with(context).applyDefaultRequestOptions(placeholderOption).load(image).into(blogUserImage);

        }

    }
}
