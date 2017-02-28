package com.yuzhai.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.yuzhai.bean.responseBean.SimpleOrderByTypeBean;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.http.IPConfig;
import com.yuzhai.yuzhaiwork.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/3.
 */
public class WorkRecyclerViewAdapter extends RecyclerView.Adapter<WorkRecyclerViewHolder> {
    private Context mContext;
    private Fragment mWorkFragment;
    private List<SimpleOrderByTypeBean.SimpleOrderBean> mOrders;
    private OnWorkItemClickListener mOnWorkItemClickListener;

    /**
     * 测试数据
     */
    private String[] titles = new String[]{
            "帮我做一个很厉害的APP，记住，是很厉害的，普通厉害的不要!!",
            "帮我做一个很厉害的APP，记住，是很厉害的，普通厉害的不要!!",
            "帮我做一个很厉害的APP，记住，是很厉害的，普通厉害的不要!!",
            "帮我做一个很厉害的APP，记住，是很厉害的，普通厉害的不要!!"};
    private String[] dates = new String[]{"16-10-02", "16-10-03", "16-10-04", "16-10-05"};
    private String[] limits = new String[]{"5天", "15天", "20天", "50天"};
    private String[] prices = new String[]{"200", "300", "50", "100"};
    private int[] images = new int[]{R.drawable.test1, R.drawable.test2, R.drawable.test3, R.drawable.test4};
    private List<Map<String, Object>> testOrders;

    /**
     * 测试方法
     */
    private void textMethod() {
        Map<String, Object> map;
        testOrders = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            map = new HashMap<>();
            map.put("image", images[i]);
            map.put("notificationTitle", titles[i]);
            map.put("date", dates[i]);
            map.put("limit", limits[i]);
            map.put("price", prices[i]);
            testOrders.add(map);
        }
    }

    public WorkRecyclerViewAdapter(Fragment fragment, List<SimpleOrderByTypeBean.SimpleOrderBean> testOrders) {
        this.mWorkFragment = fragment;
        this.mContext = fragment.getContext();
        this.mOrders = testOrders;

        if (!CustomApplication.isConnect) {
            textMethod();
        }
    }

    public void setOnWorkItemClickListener(OnWorkItemClickListener onWorkItemClickListener) {
        this.mOnWorkItemClickListener = onWorkItemClickListener;
    }

    @Override
    public WorkRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WorkRecyclerViewHolder(LayoutInflater
                .from(mContext)
                .inflate(R.layout.category_work_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(WorkRecyclerViewHolder holder, final int position) {
        if (CustomApplication.isConnect) {
            holder.mTitle.setText(mOrders.get(position).getTitle());
            holder.mDate.setText(mOrders.get(position).getDate());
            holder.mDeadline.setText(mOrders.get(position).getDeadline());
            holder.mReward.setText(mOrders.get(position).getReward());
            if (null != mOrders.get(position).getPicture() && 0 != mOrders.get(position).getPicture().size()) {
                Glide.with(mWorkFragment)
                        .load(IPConfig.image_addressPrefix + "/" + mOrders.get(position).getPicture().get(0).getImage())
                        .placeholder(R.drawable.default_image)
                        .error(R.drawable.default_image)
                        .into(holder.mImage);
            } else {
                holder.mImage.setImageResource(R.drawable.default_image);
            }
        } else {
            holder.mTitle.setText((String) testOrders.get(position).get("notificationTitle"));
            holder.mDate.setText((String) testOrders.get(position).get("date"));
            holder.mDeadline.setText((String) testOrders.get(position).get("limit"));
            holder.mReward.setText((String) testOrders.get(position).get("price"));
            holder.mImage.setImageResource((Integer) testOrders.get(position).get("image"));
        }


        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnWorkItemClickListener) {
                    mOnWorkItemClickListener.onWorkItemClick(position);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        if (CustomApplication.isConnect) {
            return mOrders.size();
        } else {
            return testOrders.size();
        }
    }

    public interface OnWorkItemClickListener {
        void onWorkItemClick(int position);
    }
}
