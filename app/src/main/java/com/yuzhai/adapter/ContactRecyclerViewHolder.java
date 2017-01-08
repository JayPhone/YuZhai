package com.yuzhai.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yuzhai.view.CircleImageView;
import com.yuzhai.yuzhaiwork.R;

/**
 * Created by Administrator on 2016/12/25.
 */

public class ContactRecyclerViewHolder extends RecyclerView.ViewHolder {
    RelativeLayout contactLayout;
    CircleImageView userheaderImage;
    TextView userNameText;
    TextView contentText;
    TextView timeText;

    public ContactRecyclerViewHolder(View itemView) {
        super(itemView);
        contactLayout = (RelativeLayout) itemView.findViewById(R.id.contact_layout);
        userheaderImage = (CircleImageView) itemView.findViewById(R.id.user_header);
        userNameText = (TextView) itemView.findViewById(R.id.user_name);
        contentText = (TextView) itemView.findViewById(R.id.contact_content);
        timeText = (TextView) itemView.findViewById(R.id.received_time);
    }
}
