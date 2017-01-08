package com.yuzhai.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yuzhai.yuzhaiwork.R;

/**
 * Created by Administrator on 2016/10/3.
 */
public class WorkRecyclerViewHolder extends RecyclerView.ViewHolder {
    CardView mCardView;
    RelativeLayout mWrapLayout;
    ImageView mImage;
    TextView mTitle;
    TextView mDate;
    TextView mDeadline;
    TextView mReward;

    public WorkRecyclerViewHolder(View itemView) {
        super(itemView);
        mCardView = (CardView) itemView.findViewById(R.id.card_view);
        mWrapLayout = (RelativeLayout) itemView.findViewById(R.id.wrap_layout);
        mTitle = (TextView) itemView.findViewById(R.id.title_text);
        mDate = (TextView) itemView.findViewById(R.id.date_content);
        mDeadline = (TextView) itemView.findViewById(R.id.limit_content);
        mReward = (TextView) itemView.findViewById(R.id.price_content);
        mImage = (ImageView) itemView.findViewById(R.id.image);
    }
}
