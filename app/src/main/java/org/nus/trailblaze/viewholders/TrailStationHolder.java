package org.nus.trailblaze.viewholders;

import android.app.Activity;
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
import org.nus.trailblaze.dao.TrailStationDao;
import org.nus.trailblaze.listeners.ListItemClickListener;
import org.nus.trailblaze.models.TrailStation;
import org.nus.trailblaze.models.Trainer;
import org.nus.trailblaze.views.SetTrailStationActivity;
import org.nus.trailblaze.views.TrailStationMainActivity;


/**
 * Created by AswathyLeelakumari on 24/3/2018.
 */

public class TrailStationHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public static View itemView;
    private TextView stationName;
    private Button btnUpdate;
    private RecyclerView trailStationView;
    private ListItemClickListener listener;
    private Context context;
    private TrailStationDao stationDao;
    private Trainer trainer;


    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public TrailStationHolder(final Context context,final View itemView,
                              ListItemClickListener listener, Trainer trainer) {
        super(itemView);
        itemView.setOnClickListener(this);

        this.listener = listener;
        this.context = context;

        Intent intent = ((Activity) context).getIntent();
        String userMode = intent.getStringExtra("userMode");

        trailStationView = (RecyclerView) itemView.findViewById(R.id.rv_trail_station_list);
        stationName = (TextView) itemView.findViewById(R.id.station_name);
        btnUpdate = (Button) itemView.findViewById(R.id.btnUpdateOptions);

        if (userMode.equals("trainer"))
        {
            btnUpdate.setVisibility(View.VISIBLE); //SHOW the button
        }

        this.trainer = trainer;
        stationDao = new TrailStationDao(db.collection("stations"));
    }

        public void bind(final TrailStation station) {
            Log.d("Item", station.getId());
            stationName.setText(station.getName());

            btnUpdate.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(final View v) {
                    PopupMenu menu = new PopupMenu(TrailStationHolder.this.context, v);
                    menu.inflate(R.menu.options_menu);

                    menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            Log.d("Item", station.getId());
                            switch (item.getItemId()) {
                                case R.id.itemDelete:
                                    stationDao.deleteStation(station.getId());
                                    break;
                                case R.id.itemEdit:
                                    Intent intent = new Intent(context.getApplicationContext(), SetTrailStationActivity.class);
                                    Bundle bundle = new Bundle();

                                    bundle.putString(SetTrailStationActivity.DOCUMENTID, station.getId());
                                    bundle.putString(SetTrailStationActivity.SEQVALUE, station.getSequence());
                                    bundle.putString(SetTrailStationActivity.NAMEVALUE, station.getName());
                                    bundle.putString(SetTrailStationActivity.INSTRVALUE, station.getInstruction());
                                    intent.putExtras(bundle);

                                    //intent.putExtra("trainer", Trainer.fromUser(TrailStationHolder.this.trainer));
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
        Log.d("add activity", "next");
    }

}
