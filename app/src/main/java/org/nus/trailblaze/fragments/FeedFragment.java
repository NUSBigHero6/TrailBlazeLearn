package org.nus.trailblaze.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.nus.trailblaze.R;
import org.nus.trailblaze.adapters.FeedFirestoreAdapter;
import org.nus.trailblaze.listeners.ListItemClickListener;
import org.nus.trailblaze.models.ContributedItem;

public class FeedFragment extends Fragment
        implements ListItemClickListener {

    private RecyclerView mRecyclerView;
    private FirestoreRecyclerAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseFirestore firestoreDB;
    private OnPassItem mPassItem;

    private ProgressBar progressBar;
    private TextView noResultTextView;
    private String filterId;
    private String userModeText;

    public interface OnPassItem{
        public void passItem(ContributedItem item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        filterId = getArguments().getString("id");
        userModeText = getArguments().getString("userMode");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mRecyclerView = (RecyclerView) getView().findViewById(R.id.tb_feed_view);
        progressBar = (ProgressBar) getView().findViewById(R.id.progress_bar);
        noResultTextView = (TextView) getView().findViewById(R.id.tv_noResult);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        firestoreDB = FirebaseFirestore.getInstance();

        loadItemsList();
    }

    private void loadItemsList() {

        String filterIdField = "trailStationId";

        if(userModeText.equals("trainer"))
            filterIdField = "learningTrailId";

        Log.d("filterIdField", filterIdField + " " + String.valueOf(userModeText == "trainer") + " " + filterId);
        Query query =  firestoreDB.collection("contributed_items")
                                    .whereEqualTo(filterIdField, filterId);

        final FirestoreRecyclerOptions<ContributedItem> response = new FirestoreRecyclerOptions.Builder<ContributedItem>()
                .setQuery(query, ContributedItem.class)
                .build();

        adapter = new FeedFirestoreAdapter(response, this) {
            @Override
            public void onDataChanged() {
                progressBar.setVisibility(View.GONE);
                noResultTextView.setVisibility((response.getSnapshots().size() == 0 ? View.VISIBLE : View.GONE));
            }
        };

        adapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mPassItem = (OnPassItem) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onListItemClick(int position) {
        // Send the event to the host activity
        mPassItem.passItem((ContributedItem)adapter.getItem(position));
    }
}
