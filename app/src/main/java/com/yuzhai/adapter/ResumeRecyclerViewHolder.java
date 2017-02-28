package com.yuzhai.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.yuzhai.view.CircleImageView;
import com.yuzhai.yuzhaiwork.R;

/**
 * Created by Administrator on 2017/1/11.
 */

public class ResumeRecyclerViewHolder extends RecyclerView.ViewHolder {
    CardView mCardView;
    CircleImageView mHeaderImage;
    TextView mNameText;
    TextView mSexText;
    TextView mEducationText;
    TextView mTelText;

    public ResumeRecyclerViewHolder(View itemView) {
        super(itemView);
        mCardView = (CardView) itemView.findViewById(R.id.card_view);
        mHeaderImage = (CircleImageView) itemView.findViewById(R.id.header_image);
        mNameText = (TextView) itemView.findViewById(R.id.name);
        mSexText = (TextView) itemView.findViewById(R.id.sex);
        mEducationText = (TextView) itemView.findViewById(R.id.education);
        mTelText = (TextView) itemView.findViewById(R.id.status);
    }
}
