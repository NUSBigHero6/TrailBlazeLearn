package org.nus.trailblaze.viewholders;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.nus.trailblaze.R;
import org.nus.trailblaze.adapters.LearningTrailFirestoreAdaptor;
import org.nus.trailblaze.listeners.ListItemClickListener;
import org.nus.trailblaze.models.LearningTrail;
import org.nus.trailblaze.views.SetLearningTrailActivity;

/**
 * Created by kooc on 3/20/2018.
 */

public class LearningTrailHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

    private TextView LearningTrailName;
    private Button btnOptions;
    private ListItemClickListener listener;
    private Context context;
    private LearningTrailFirestoreAdaptor adaptor;
    private String documentID;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public LearningTrailHolder(final Context context, final View itemView, ListItemClickListener listener, final DocumentSnapshot docSnapshot) {
        super(itemView);
        this.listener = listener;
        this.context = context;

        itemView.setOnClickListener(this);

        this.LearningTrailName = (TextView) itemView.findViewById(R.id.tvLearningTrailName);
        this.btnOptions = (Button) itemView.findViewById(R.id.btnOptions);

        this.btnOptions.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                PopupMenu menu = new PopupMenu(LearningTrailHolder.this.context, v);
                menu.inflate(R.menu.options_menu);
                //documentID = docSnapshot.getId();

//                DocumentSnapshot ds = adaptor.getSnapshots().getSnapshot(LearningTrailHolder.super.getAdapterPosition());
//                final String documentID = ds.getId();

                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        //Not always working...
                        //Delete the first row will crash
                        documentID = docSnapshot.getId();
                        //Log.d("[DocID]", String.valueOf(documentID));
                        final String nameValue = docSnapshot.getData().get(SetLearningTrailActivity.NAME).toString();
                        switch (item.getItemId()) {
                            case R.id.itemDelete:
                                db.collection("trails").document(documentID)
                                        .delete();
                                break;
                            case R.id.itemEdit:
                                Intent intent = new Intent(context.getApplicationContext(), SetLearningTrailActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString(SetLearningTrailActivity.DOCUMENTID, documentID);
                                Log.d("[ID-d]", String.valueOf(documentID));
                                bundle.putString(SetLearningTrailActivity.NAMEVALUE, nameValue);
                                Log.d("[ID-n]", String.valueOf(nameValue));
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

    public void bind(LearningTrail item) {
        LearningTrailName.setText(item.getName());
    }

    public void onClick(View view) {
        int clickedPosition = getAdapterPosition();
        listener.onListItemClick(clickedPosition);
    }
}
