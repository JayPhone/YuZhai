package com.yuzhai.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.yuzhai.bean.responseBean.SimpleResumeByTypeBean;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.http.IPConfig;
import com.yuzhai.yuzhaiwork.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/11.
 */

public class ResumeRecyclerViewAdapter extends RecyclerView.Adapter<ResumeRecyclerViewHolder> {
    private Context mContext;
    private Fragment mResumeFragment;
    private List<SimpleResumeByTypeBean.SimpleResumeBean> mOrders;
    private ResumeRecyclerViewAdapter.OnResumeItemClickListener mOnResumeItemClickListener;

    /**
     * 测试数据
     */
    private int[] headerImages = new int[]{R.drawable.test1, R.drawable.test2, R.drawable.test3, R.drawable.test4, R.drawable.test2, R.drawable.test3};
    private String[] names = new String[]{"黄坚凤", "狂捷文", "炫金坤", "余志文", "堂弟会", "吕浩斌"};
    private String[] sexs = new String[]{"女性", "男性", "男性", "女性", "男性", "女性"};
    private String[] educations = new String[]{"初中", "高中", "专科", "本科", "硕士", "博士"};
    private String[] tels = new String[]{"12315616514", "16459872654", "14562317851", "16258942314", "15489632451", "18456295632"};
    private List<Map<String, Object>> testResumes;

    /**
     * 测试方法
     */
    private void textMethod() {
        Map<String, Object> map;
        testResumes = new ArrayList<>();
        for (int i = 0; i < headerImages.length; i++) {
            map = new HashMap<>();
            map.put("header", headerImages[i]);
            map.put("name", names[i]);
            map.put("education", educations[i]);
            map.put("sex", sexs[i]);
            map.put("tel", tels[i]);
            testResumes.add(map);
        }
    }

    public ResumeRecyclerViewAdapter(Fragment fragment, List<SimpleResumeByTypeBean.SimpleResumeBean> orders) {
        this.mResumeFragment = fragment;
        this.mContext = fragment.getContext();
        this.mOrders = orders;

        if (!CustomApplication.isConnect) {
            textMethod();
        }
    }

    public void setOnResumeItemClickListener(ResumeRecyclerViewAdapter.OnResumeItemClickListener onResumeItemClickListener) {
        this.mOnResumeItemClickListener = onResumeItemClickListener;
    }

    @Override
    public ResumeRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ResumeRecyclerViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.category_resume_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ResumeRecyclerViewHolder holder, final int position) {
        if (CustomApplication.isConnect) {
            holder.mNameText.setText(mOrders.get(position).getName());
            holder.mSexText.setText(mOrders.get(position).getSex());
            holder.mEducationText.setText(mOrders.get(position).getEducation());
            holder.mTelText.setText(mOrders.get(position).getContactNumber());
            Glide.with(mResumeFragment)
                    .load(IPConfig.image_addressPrefix + "/" + mOrders.get(position).getAvatar())
                    .placeholder(R.drawable.default_image)
                    .error(R.drawable.default_image)
                    .into(holder.mHeaderImage);
        } else {
            /**
             * 静态数据
             */
            holder.mNameText.setText((CharSequence) testResumes.get(position).get("name"));
            holder.mSexText.setText((CharSequence) testResumes.get(position).get("sex"));
            holder.mEducationText.setText((CharSequence) testResumes.get(position).get("education"));
            holder.mTelText.setText((CharSequence) testResumes.get(position).get("tel"));
            holder.mHeaderImage.setImageResource((Integer) testResumes.get(position).get("header"));
        }
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnResumeItemClickListener) {
                    mOnResumeItemClickListener.onResumeItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (CustomApplication.isConnect) {
            return mOrders.size();
        } else {
            /**
             * 静态数据
             */
            return testResumes.size();
        }
    }

    public interface OnResumeItemClickListener {
        void onResumeItemClick(int position);
    }
}
