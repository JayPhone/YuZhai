package com.yuzhai.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yuzhai.yuzhaiwork.R;

/**
 * Created by Administrator on 2016/10/3.
 */
public class WorkRecyclerViewHolder extends RecyclerView.ViewHolder {
    RelativeLayout mWrapLayout;
    LinearLayout mImageLayout;
    TextView mTitle;
    TextView mDate;
    TextView mLimit;
    TextView mPrice;

    public WorkRecyclerViewHolder(View itemView) {
        super(itemView);
        mWrapLayout = (RelativeLayout) itemView.findViewById(R.id.wrap_layout);
        mTitle = (TextView) itemView.findViewById(R.id.title_text);
        mDate = (TextView) itemView.findViewById(R.id.date_content);
        mLimit = (TextView) itemView.findViewById(R.id.limit_content);
        mPrice = (TextView) itemView.findViewById(R.id.price_content);
        mImageLayout = (LinearLayout) itemView.findViewById(R.id.image_layout);
    }
}
