package org.nus.trailblaze.viewholders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import org.nus.trailblaze.listeners.ListItemClickListener;
import org.nus.trailblaze.models.LearningTrail;

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
    private LearningTrailFirestoreAdaptor adaptor;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public LearningTrailHolder(final Context context, final View itemView, ListItemClickListener listener, final DocumentSnapshot docSnapshot) {
        super(itemView);
        this.listener = listener;
        this.context = context;
        this.learningTrailView = (RecyclerView) itemView.findViewById(R.id.rvLearningTrail);
        this.LearningTrailName = (TextView) itemView.findViewById(R.id.tvLearningTrailName);
        this.btnOptions = (Button) itemView.findViewById(R.id.btnOptions);


        this.btnOptions.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                PopupMenu menu = new PopupMenu(LearningTrailHolder.this.context, v);
                menu.inflate(R.menu.options_menu);

//                DocumentSnapshot ds = adaptor.getSnapshots().getSnapshot(LearningTrailHolder.super.getAdapterPosition());
//                final String documentID = ds.getId();

                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        //Not always working...
                        //Delete the first row will crash
                        final String documentID = docSnapshot.getReference().getId();
                        Log.d("[DocID]", String.valueOf(documentID));
                        switch (item.getItemId()) {
                            case R.id.itemDelete:
                                db.collection("trails").document(documentID)
                                        .delete();
                                break;
                            case R.id.itemEdit:

                                //todo: Intent back to SetLearningTrailActivity and pass the documentID

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
