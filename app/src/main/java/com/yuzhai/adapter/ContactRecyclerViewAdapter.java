package com.yuzhai.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.yuzhai.bean.responseBean.ContactPrivateConversationBean;
import com.yuzhai.http.IPConfig;
import com.yuzhai.util.TimeUtil;
import com.yuzhai.yuzhaiwork.R;

import java.util.List;

/**
 * Created by Administrator on 2016/12/25.
 */

public class ContactRecyclerViewAdapter extends RecyclerView.Adapter<ContactRecyclerViewHolder> {
    private List<ContactPrivateConversationBean> mContactConversationList;
    private OnContactItemClickListener mOnContactItemClickListener;
    private Fragment mFragment;

    public ContactRecyclerViewAdapter(Fragment fragment, List<ContactPrivateConversationBean> contactConversationList) {
        mFragment = fragment;
        mContactConversationList = contactConversationList;
    }

    public void setOnContactItemClickListener(OnContactItemClickListener onContactItemClickListener) {
        this.mOnContactItemClickListener = onContactItemClickListener;
    }

    @Override
    public ContactRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContactRecyclerViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.contact_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ContactRecyclerViewHolder holder, final int position) {
        holder.contactLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnContactItemClickListener) {
                    mOnContactItemClickListener.onContactItemClick(position);
                }
            }
        });
        Glide.with(mFragment)
                .load(IPConfig.image_addressPrefix + mContactConversationList.get(position).getAvatar())
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .into(holder.userheaderImage);
        holder.userNameText.setText(mContactConversationList.get(position).getName());
        holder.contentText.setText(mContactConversationList.get(position).getLastMessageContent());
        holder.timeText.setText(TimeUtil.getChatTime(false, mContactConversationList.get(position).getLastMessageTime()));
        //查询指定未读消息数
        long unread = mContactConversationList.get(position).getUnReadCount();
        if (unread > 0) {
            holder.unReadText.setVisibility(View.VISIBLE);
            holder.unReadText.setText(String.valueOf(unread));
        } else {
            holder.unReadText.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mContactConversationList.size();
    }

    public interface OnContactItemClickListener {
        void onContactItemClick(int position);
    }
}
