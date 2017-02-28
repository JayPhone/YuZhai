package com.yuzhai.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuzhai.bean.innerBean.ContactMsgBean;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.yuzhaiwork.R;

import java.util.List;

import cn.bmob.newim.bean.BmobIMMessage;

/**
 * Created by 35429 on 2017/2/9.
 */

public class ContactDetailRecyclerViewAdapter extends RecyclerView.Adapter<ContactDetailRecyclerViewHolder> {
    private List<BmobIMMessage> mBmobIMMessageList;
    private CustomApplication mCustomApplication;

    public ContactDetailRecyclerViewAdapter(List<BmobIMMessage> bmobIMMessageList, CustomApplication customApplication) {
        mBmobIMMessageList = bmobIMMessageList;
        mCustomApplication = customApplication;
    }

    @Override
    public ContactDetailRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContactDetailRecyclerViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_detail_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ContactDetailRecyclerViewHolder holder, int position) {
        BmobIMMessage bmobIMMessage = mBmobIMMessageList.get(position);
        if (!bmobIMMessage.getFromId().equals(mCustomApplication.getUserPhone())) {
            holder.sendLayout.setVisibility(View.GONE);
            holder.recLayout.setVisibility(View.VISIBLE);
            holder.recText.setText(bmobIMMessage.getContent());
        } else if (bmobIMMessage.getFromId().equals(mCustomApplication.getUserPhone())) {
            holder.recLayout.setVisibility(View.GONE);
            holder.sendLayout.setVisibility(View.VISIBLE);
            holder.sendText.setText(bmobIMMessage.getContent());
        }
    }

    @Override
    public int getItemCount() {
        return mBmobIMMessageList.size();
    }
}
