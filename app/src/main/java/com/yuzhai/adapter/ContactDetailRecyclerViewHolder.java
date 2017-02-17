package com.yuzhai.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yuzhai.view.CircleImageView;
import com.yuzhai.yuzhaiwork.R;

/**
 * Created by 35429 on 2017/2/9.
 */

public class ContactDetailRecyclerViewHolder extends RecyclerView.ViewHolder {
    RelativeLayout recLayout;
    RelativeLayout sendLayout;
    CircleImageView recHeader;
    CircleImageView sendHeader;
    TextView recText;
    TextView sendText;

    public ContactDetailRecyclerViewHolder(View itemView) {
        super(itemView);
        recLayout = (RelativeLayout) itemView.findViewById(R.id.rec_layout);
        sendLayout = (RelativeLayout) itemView.findViewById(R.id.send_layout);
        recHeader = (CircleImageView) itemView.findViewById(R.id.rec_header);
        sendHeader = (CircleImageView) itemView.findViewById(R.id.send_header);
        recText = (TextView) itemView.findViewById(R.id.rec_text);
        sendText = (TextView) itemView.findViewById(R.id.send_text);
    }
}
