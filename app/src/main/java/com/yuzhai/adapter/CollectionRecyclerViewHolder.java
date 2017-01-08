package com.yuzhai.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuzhai.yuzhaiwork.R;

/**
 * Created by Administrator on 2016/12/12.
 */

public class CollectionRecyclerViewHolder extends RecyclerView.ViewHolder {
    CardView mCardView;
    ImageView mImage;
    TextView mType;
    TextView mTitle;
    TextView mDate;

    public CollectionRecyclerViewHolder(View itemView) {
        super(itemView);
        mCardView = (CardView) itemView.findViewById(R.id.card_view);
        mImage = (ImageView) itemView.findViewById(R.id.image_view);
        mType = (TextView) itemView.findViewById(R.id.type);
        mTitle = (TextView) itemView.findViewById(R.id.title);
        mDate = (TextView) itemView.findViewById(R.id.date);
    }
}
