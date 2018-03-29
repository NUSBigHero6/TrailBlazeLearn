package org.nus.trailblaze.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.nus.trailblaze.R;
import org.nus.trailblaze.listeners.ListItemClickListener;
import org.nus.trailblaze.models.LearningTrail;
import org.nus.trailblaze.models.Trainer;
import org.nus.trailblaze.viewholders.LearningTrailHolder;

/**
 * Created by kooc on 3/23/2018.
 */

public class LearningTrailFirestoreAdaptor extends FirestoreRecyclerAdapter<LearningTrail, LearningTrailHolder> {

    private static final String TAG = FeedFirestoreAdapter.class.getSimpleName();
    final private ListItemClickListener mOnClickListener;
    private View itemView;
    private DocumentSnapshot docSnapshot;
    private Trainer trainer;

    public LearningTrailFirestoreAdaptor(FirestoreRecyclerOptions<LearningTrail> response,
                                        ListItemClickListener listener, Trainer trainer) {
        super(response);
        mOnClickListener = listener;
        this.trainer = trainer;
    }

    public LearningTrailHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.learning_trail_row;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        itemView = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        LearningTrailHolder viewHolder = new LearningTrailHolder(context, itemView,
                mOnClickListener, this.trainer);

        return viewHolder;

    }

    public void onBindViewHolder(@NonNull final LearningTrailHolder viewHolder, int index, @NonNull LearningTrail model) {

        docSnapshot = getSnapshots().getSnapshot(viewHolder.getAdapterPosition());

        model.setId(docSnapshot.getId());
        viewHolder.bind(model);
    }

    @Override
    public void onError(FirebaseFirestoreException e) {
        Log.e("error", e.getMessage());
    }
}