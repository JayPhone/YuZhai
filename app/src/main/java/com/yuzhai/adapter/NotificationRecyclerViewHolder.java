package com.yuzhai.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuzhai.yuzhaiwork.R;

/**
 * Created by 35429 on 2017/2/19.
 */

public class NotificationRecyclerViewHolder extends RecyclerView.ViewHolder {
    TextView timeText;
    TextView notificationTitle;
    ImageView notificationImage;
    TextView notificationMessage;
    CardView wrapCardView;

    public NotificationRecyclerViewHolder(View itemView) {
        super(itemView);
        timeText = (TextView) itemView.findViewById(R.id.time_text);
        notificationTitle = (TextView) itemView.findViewById(R.id.notification_title);
        notificationImage = (ImageView) itemView.findViewById(R.id.notification_image);
        notificationMessage = (TextView) itemView.findViewById(R.id.notification_text);
        wrapCardView = (CardView) itemView.findViewById(R.id.card_view);
    }
}
