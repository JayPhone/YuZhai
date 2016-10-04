package com.yuzhai.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yuzhai.yuzhaiwork.R;

/**
 * Created by Administrator on 2016/10/3.
 */
public class PublishedRecyclerViewHolder extends RecyclerView.ViewHolder {
    LinearLayout mWarpLayout;
    TextView mStatusText;
    TextView mOrderIdText;
    TextView mDateText;
    TextView mTitleText;
    TextView mLimitText;
    TextView mPriceText;
    ImageView mTypeImage;
    Button mCancelButton;

    public PublishedRecyclerViewHolder(View itemView) {
        super(itemView);
        mWarpLayout = (LinearLayout) itemView.findViewById(R.id.wrap_layout);
        mStatusText = (TextView) itemView.findViewById(R.id.status);
        mOrderIdText = (TextView) itemView.findViewById(R.id.order_id);
        mDateText = (TextView) itemView.findViewById(R.id.date);
        mTitleText = (TextView) itemView.findViewById(R.id.title);
        mLimitText = (TextView) itemView.findViewById(R.id.limit);
        mPriceText = (TextView) itemView.findViewById(R.id.price);
        mTypeImage = (ImageView) itemView.findViewById(R.id.type_image);
        mCancelButton = (Button) itemView.findViewById(R.id.cancel_order);
    }
}
