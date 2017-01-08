package com.yuzhai.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuzhai.yuzhaiwork.R;

/**
 * Created by Administrator on 2016/12/25.
 */

public class ContactRecyclerViewAdapter extends RecyclerView.Adapter<ContactRecyclerViewHolder> {
    private String[] userNames = new String[]{
            "较大是的",
            "是肯定是的",
            "索迪斯是",
            "搜狗更多",
            "你不惹",
            "你从额",
            "瓶外传",
            "怎么办",
            "索迪斯是",
            "搜狗更多",
            "你不惹",
            "你从额",
    };
    private int[] headers = new int[]{
            R.drawable.test1, R.drawable.test2, R.drawable.test3, R.drawable.test4,
            R.drawable.test1, R.drawable.test2, R.drawable.test3, R.drawable.test4,
            R.drawable.test1, R.drawable.test2, R.drawable.test3, R.drawable.test4};

    public ContactRecyclerViewAdapter() {

    }

    @Override
    public ContactRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContactRecyclerViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.contact_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ContactRecyclerViewHolder holder, int position) {
        holder.contactLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });
        holder.userheaderImage.setImageResource(headers[position]);
        holder.userNameText.setText(userNames[position]);
    }

    @Override
    public int getItemCount() {
        return headers.length;
    }
}
