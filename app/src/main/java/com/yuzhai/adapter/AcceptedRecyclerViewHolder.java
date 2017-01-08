package com.yuzhai.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuzhai.yuzhaiwork.R;

/**
 * Created by Administrator on 2016/10/13.
 */

public class AcceptedRecyclerViewHolder extends RecyclerView.ViewHolder {
    CardView mCardView;
    TextView mStatusText;
    TextView mOrderIdText;
    TextView mDateText;
    TextView mTitleText;
    TextView mLimitText;
    TextView mPriceText;
    ImageView mTypeImage;

    public AcceptedRecyclerViewHolder(View itemView) {
        super(itemView);
        mCardView = (CardView) itemView.findViewById(R.id.card_view);
        mStatusText = (TextView) itemView.findViewById(R.id.tel);
        mOrderIdText = (TextView) itemView.findViewById(R.id.order_id);
        mDateText = (TextView) itemView.findViewById(R.id.date);
        mTitleText = (TextView) itemView.findViewById(R.id.title);
        mLimitText = (TextView) itemView.findViewById(R.id.deadline);
        mPriceText = (TextView) itemView.findViewById(R.id.price);
        mTypeImage = (ImageView) itemView.findViewById(R.id.type_image);
    }
}
