package org.nus.trailblaze.adapters;

import android.content.Context;
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
import org.nus.trailblaze.models.TrailStation;
import org.nus.trailblaze.models.Trainer;
import org.nus.trailblaze.viewholders.TrailStationHolder;

/**
 * Created by AswathyLeelakumari on 24/3/2018.
 */

public class TrailStationFirestoreAdapter extends FirestoreRecyclerAdapter<TrailStation, TrailStationHolder> {

    private static final String TAG = FeedFirestoreAdapter.class.getSimpleName();
    final private ListItemClickListener mOnClickListener;
    private View itemView;
    private DocumentSnapshot docSnapshot;
    private Trainer trainer;


    public TrailStationFirestoreAdapter(FirestoreRecyclerOptions<TrailStation> response,
                                        ListItemClickListener listener, Trainer trainer) {
        super(response);
        mOnClickListener = listener;
        this.trainer = trainer;
    }

    public TrailStationHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.trailstation_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        itemView = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        TrailStationHolder viewHolder = new TrailStationHolder(context, itemView, mOnClickListener, this.trainer);


        return viewHolder;
    }

    public void onBindViewHolder(TrailStationHolder viewHolder, int index, TrailStation model) {

        docSnapshot = getSnapshots().getSnapshot(viewHolder.getAdapterPosition());
        model.setId(docSnapshot.getId());
        viewHolder.bind(model);
    }

    @Override
    public void onError(FirebaseFirestoreException e) {
        Log.e("error", e.getMessage());
    }
}
