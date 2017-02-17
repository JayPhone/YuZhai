package com.yuzhai.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuzhai.bean.innerBean.ContactMsgBean;
import com.yuzhai.yuzhaiwork.R;

import java.util.List;

/**
 * Created by 35429 on 2017/2/9.
 */

public class ContactDetailRecyclerViewAdapter extends RecyclerView.Adapter<ContactDetailRecyclerViewHolder> {
    private List<ContactMsgBean> mMsgList;

    public ContactDetailRecyclerViewAdapter(List<ContactMsgBean> msgList) {
        mMsgList = msgList;
    }

    @Override
    public ContactDetailRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContactDetailRecyclerViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_detail_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ContactDetailRecyclerViewHolder holder, int position) {
        ContactMsgBean msg = mMsgList.get(position);
        if (msg.getType() == ContactMsgBean.TYPE_RECEIVED) {
            holder.sendLayout.setVisibility(View.GONE);
            holder.recLayout.setVisibility(View.VISIBLE);
            holder.recText.setText(msg.getContent());
        } else if (msg.getType() == ContactMsgBean.TYPE_SENT) {
            holder.recLayout.setVisibility(View.GONE);
            holder.sendLayout.setVisibility(View.VISIBLE);
            holder.sendText.setText(msg.getContent());
        }
    }

    @Override
    public int getItemCount() {
        return mMsgList.size();
    }
}
