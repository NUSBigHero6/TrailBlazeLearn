package org.nus.trailblaze.viewholders;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import org.nus.trailblaze.R;
import org.nus.trailblaze.listeners.ListItemClickListener;
import org.nus.trailblaze.models.ContributedItem;

/**
 * Created by wengweichen on 14/3/18.
 */

public class FeedViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener{
    CardView feedCard;
    TextView tbParticipantName;
    ImageView tbFeedIcon;
    ListItemClickListener listener;

    public FeedViewHolder(View itemView, ListItemClickListener listener) {
        super(itemView);
        this.listener = listener;

        itemView.setOnClickListener(this);

        feedCard = (CardView)itemView.findViewById(R.id.tb_feed_card);
        tbParticipantName = (TextView) itemView.findViewById(R.id.tb_feed_item);
        tbFeedIcon = (ImageView) itemView.findViewById(R.id.tb_feed_icon);
    }

    public void bind(ContributedItem item) {
        tbParticipantName.setText(item.getDescription());
        tbFeedIcon.setImageResource(getIconId(item.getFile().getMimeType()));
    }

    public void onClick(View view) {
        int clickedPosition = getAdapterPosition();
        listener.onListItemClick(clickedPosition);
    }

    private int getIconId(String mimeType) {
        int iconId = 0;
        switch (mimeType)
        {
            case "image/png":
            case "image/jpg":
            case "image/jpeg":
            case "image/gif":
                iconId = R.drawable.ic_image_black_24dp;
                break;
            case "video/mp4":
                iconId = R.drawable.ic_video_library_black_24dp;
                break;
            case "application/pdf":
                iconId = R.drawable.ic_picture_as_pdf_black_24dp;
                break;
            case "text/plain":
                iconId = R.drawable.ic_text_format_black_24dp;
                break;
            case "audio/mpeg":
                iconId = R.drawable.ic_audiotrack_black_24dp;
                break;
        }

        return iconId;
    }
}
