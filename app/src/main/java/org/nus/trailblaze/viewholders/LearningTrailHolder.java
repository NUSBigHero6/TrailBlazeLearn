package org.nus.trailblaze.viewholders;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListPopupWindow;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.nus.trailblaze.R;
import org.nus.trailblaze.adapters.LearningTrailFirestoreAdaptor;
import org.nus.trailblaze.dao.LearningTrailDao;
import org.nus.trailblaze.listeners.ListItemClickListener;
import org.nus.trailblaze.models.LearningTrail;
import org.nus.trailblaze.views.SetLearningTrailActivity;

import static android.content.ContentValues.TAG;

/**
 * Created by kooc on 3/20/2018.
 */

public class LearningTrailHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public static View itemView;
    private TextView LearningTrailName;
    private Button btnOptions;
    private RecyclerView learningTrailView;
    private ListItemClickListener listener;
    private Context context;
    private LearningTrailDao trailDao;

    FirebaseFirestore db;

    public LearningTrailHolder(final Context context, final View itemView, ListItemClickListener listener, final DocumentSnapshot docSnapshot) {
        super(itemView);
        itemView.setOnClickListener(this);

        this.listener = listener;
        this.context = context;
        this.learningTrailView = (RecyclerView) itemView.findViewById(R.id.rvLearningTrail);
        this.LearningTrailName = (TextView) itemView.findViewById(R.id.tvLearningTrailName);
        this.btnOptions = (Button) itemView.findViewById(R.id.btnOptions);

        db = FirebaseFirestore.getInstance();
        trailDao = new LearningTrailDao(db.collection("trails"));
    }

    public void bind(final LearningTrail trail) {
        Log.d("Item", trail.getId());
        LearningTrailName.setText(trail.getName());

        btnOptions.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                PopupMenu menu = new PopupMenu(LearningTrailHolder.this.context, v);
                menu.inflate(R.menu.options_menu);

                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Log.d("Item", trail.getId());
                        switch (item.getItemId()) {
                            case R.id.itemDelete:
                                trailDao.deleteTrail(trail);
                                break;
                            case R.id.itemEdit:

                                Intent intent = new Intent(context.getApplicationContext(), SetLearningTrailActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString(SetLearningTrailActivity.DOCUMENTID, trail.getId());
                                Log.d("[ID-d]", String.valueOf(trail.getId()));
                                bundle.putString(SetLearningTrailActivity.NAMEVALUE, trail.getName());
                                Log.d("[ID-n]", String.valueOf(trail.getName()));
                                intent.putExtras(bundle);
                                context.startActivity(intent);

                                break;
                        }
                        return false;
                    }
                });
                menu.show();
            }
        });
    }

    public void onClick(View view) {
        int clickedPosition = getAdapterPosition();
        listener.onListItemClick(clickedPosition);
    }
}
