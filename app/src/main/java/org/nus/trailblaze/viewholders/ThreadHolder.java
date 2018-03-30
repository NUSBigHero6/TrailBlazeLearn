package org.nus.trailblaze.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.nus.trailblaze.R;
import org.nus.trailblaze.models.File;
import org.nus.trailblaze.models.Post;
import org.nus.trailblaze.models.ThreadPost;

import java.util.List;

/**
 * Created by priyanka on 30/3/2018.
 */

public class ThreadHolder extends RecyclerView.Adapter<ThreadHolder.ViewHolder> {

    public List<ThreadPost> threadList;
//    public Context context;

    public ThreadHolder(List<ThreadPost> threadList){
        this.threadList = threadList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_thread_item,parent,false);
//        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String desc_data = threadList.get(position).getMessage();//from posts
        holder.setMessage(desc_data);//setting the description
    }

    @Override
    public int getItemCount() {
        return threadList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private View myView;
        private TextView descView;

        //Constructor for View Holder
        public ViewHolder(View itemView) {
            super(itemView);
            myView = itemView;
        }

        public void setMessage(String descText){
            descView = myView.findViewById(R.id.thread_desc);
            descView.setText(descText);
        }


    }
}
