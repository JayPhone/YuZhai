package com.yuzhai.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuzhai.yuzhaiwork.R;

/**
 * Created by 35429 on 2017/2/20.
 */

public class AppliedRecyclerViewHolder extends RecyclerView.ViewHolder {
    CardView mCardView;
    TextView mStatusText;
    TextView mOrderIdText;
    TextView mDateText;
    TextView mTitleText;
    TextView mDeadlineText;
    TextView mRewardText;
    ImageView mTypeImage;

    public AppliedRecyclerViewHolder(View itemView) {
        super(itemView);
        mCardView = (CardView) itemView.findViewById(R.id.card_view);
        mStatusText = (TextView) itemView.findViewById(R.id.status);
        mOrderIdText = (TextView) itemView.findViewById(R.id.order_id);
        mDateText = (TextView) itemView.findViewById(R.id.date);
        mTitleText = (TextView) itemView.findViewById(R.id.title);
        mDeadlineText = (TextView) itemView.findViewById(R.id.deadline);
        mRewardText = (TextView) itemView.findViewById(R.id.price);
        mTypeImage = (ImageView) itemView.findViewById(R.id.type_image);
    }
}
