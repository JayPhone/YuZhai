package com.yuzhai.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yuzhai.recyclerview.OnItemStateChangeListener;
import com.yuzhai.yuzhaiwork.R;

/**
 * Created by Administrator on 2016/9/29.
 */
public class CategoryRecyclerViewHolder extends RecyclerView.ViewHolder implements OnItemStateChangeListener {
    LinearLayout mFrameLayout;
    ImageView mImageView;
    TextView mTextView;

    public CategoryRecyclerViewHolder(View itemView) {
        super(itemView);
        mFrameLayout = (LinearLayout) itemView.findViewById(R.id.warp_layout);
        mImageView = (ImageView) itemView.findViewById(R.id.category_image);
        mTextView = (TextView) itemView.findViewById(R.id.category_text);
    }

    @Override
    public void onItemSelected() {
        mFrameLayout.setBackgroundResource(R.drawable.category_item_draged);
    }

    @Override
    public void onItemClear() {
        mFrameLayout.setBackgroundResource(R.color.color_white);
    }
}
