package org.nus.trailblaze.viewholders;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.nus.trailblaze.R;
import org.nus.trailblaze.listeners.ListItemClickListener;
import org.nus.trailblaze.models.TrailStation;

/**
 * Created by AswathyLeelakumari on 24/3/2018.
 */

public class TrailStationHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView stationId;
    private TextView stationName;
    private RecyclerView trailStationView;
    private ListItemClickListener listener;

    public TrailStationHolder(View itemView, ListItemClickListener listener) {
        super(itemView);
        this.listener = listener;

        itemView.setOnClickListener(this);

        trailStationView = (RecyclerView) itemView.findViewById(R.id.rv_trail_station_list);
        stationId = (TextView) itemView.findViewById(R.id.station_id);
        stationName = (TextView) itemView.findViewById(R.id.station_name);
    }

    public void bind(TrailStation item) {
        stationId.setText(item.getId());
        stationName.setText(item.getName());
    }

    public void onClick(View view) {
        int clickedPosition = getAdapterPosition();
        listener.onListItemClick(clickedPosition);
        Log.d("add activity", "next");
    }
}
