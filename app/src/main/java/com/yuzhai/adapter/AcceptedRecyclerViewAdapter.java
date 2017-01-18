package com.yuzhai.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuzhai.bean.responseBean.OrderAcceptedBean;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.util.TypeUtil;
import com.yuzhai.yuzhaiwork.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/13.
 */

public class AcceptedRecyclerViewAdapter extends RecyclerView.Adapter<AcceptedRecyclerViewHolder> {
    private Context mContext;
    private Fragment mAcceptedFragment;
    private List<OrderAcceptedBean.OrderBean> mOrder;
    private OnAcceptedItemClickListener mOnAcceptedItemClickListener;

    /**
     * 测试数据
     */
    private int[] typeImages = new int[]{R.drawable.test1, R.drawable.test2, R.drawable.test3, R.drawable.test4};
    private String[] status = new String[]{"已接", "待接", "完成", "已接"};
    private String[] dates = new String[]{"2016-07-14", "2016-07-14", "2016-07-14", "2016-07-14"};
    private String[] titles = new String[]{
            "帮我做一个很厉害的APP，记住，是很厉害的，普通厉害的不要!!",
            "帮我做一个很厉害的APP，记住，是很厉害的，普通厉害的不要!!",
            "帮我做一个很厉害的APP，记住，是很厉害的，普通厉害的不要!!",
            "帮我做一个很厉害的APP，记住，是很厉害的，普通厉害的不要!!",
    };
    private String[] prices = new String[]{"100", "150", "200", "250"};
    private String[] limits = new String[]{"5天", "10天", "15天", "20天"};
    private String[] orderIds = new String[]{"1954656112", "1954656112", "1954656112", "1954656112"};
    private List<Map<String, Object>> testAcceptedOrders;

    /**
     * 测试方法
     */
    private void textMethod() {
        Map<String, Object> map;
        testAcceptedOrders = new ArrayList<>();
        for (int i = 0; i < typeImages.length; i++) {
            map = new HashMap<>();
            map.put("type", typeImages[i]);
            map.put("status", status[i]);
            map.put("date", dates[i]);
            map.put("title", titles[i]);
            map.put("price", prices[i]);
            map.put("limit", limits[i]);
            map.put("orderId", orderIds[i]);
            testAcceptedOrders.add(map);
        }
    }

    public AcceptedRecyclerViewAdapter(Fragment fragment, List<OrderAcceptedBean.OrderBean> order) {
        mContext = fragment.getContext();
        mAcceptedFragment = fragment;
        mOrder = order;

        if (!CustomApplication.isConnect) {
            textMethod();
        }
    }

    public void setOnAcceptedItemClickListener(OnAcceptedItemClickListener onAcceptedItemClickListener) {
        this.mOnAcceptedItemClickListener = onAcceptedItemClickListener;
    }

    @Override
    public AcceptedRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AcceptedRecyclerViewHolder holder = new AcceptedRecyclerViewHolder(LayoutInflater.from(
                mContext).inflate(R.layout.order_accepted_item_layout, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(AcceptedRecyclerViewHolder holder, final int position) {
        if (CustomApplication.isConnect) {
            holder.mStatusText.setText(mOrder.get(position).getStatus());
            holder.mOrderIdText.setText(mOrder.get(position).getOrderID());
            holder.mDateText.setText(mOrder.get(position).getDate());
            holder.mTitleText.setText(mOrder.get(position).getTitle());
            holder.mDeadlineText.setText(mOrder.get(position).getDeadline());
            holder.mRewardText.setText(mOrder.get(position).getReward());
            holder.mTypeImage.setImageResource(TypeUtil.getTypeImage(mOrder.get(position).getType()));
        } else {
            holder.mStatusText.setText((CharSequence) testAcceptedOrders.get(position).get("status"));
            holder.mOrderIdText.setText((CharSequence) testAcceptedOrders.get(position).get("orderId"));
            holder.mDateText.setText((CharSequence) testAcceptedOrders.get(position).get("date"));
            holder.mTitleText.setText((CharSequence) testAcceptedOrders.get(position).get("title"));
            holder.mDeadlineText.setText((CharSequence) testAcceptedOrders.get(position).get("limit"));
            holder.mRewardText.setText((CharSequence) testAcceptedOrders.get(position).get("price"));
            holder.mTypeImage.setImageResource((Integer) testAcceptedOrders.get(position).get("type"));
        }

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnAcceptedItemClickListener) {
                    mOnAcceptedItemClickListener.onAcceptedItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (CustomApplication.isConnect) {
            return mOrder.size();
        } else {
            return testAcceptedOrders.size();
        }
    }

//
//    /**
//     * 显示取消订单对话框
//     */
//    private void showCancelDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//        builder.setTitle("取消接单");
//        builder.setMessage("您确定要取消订单，你所接收的订单在未完成的情况下退单，需要赔付给发布方你所交纳的违约金，您还要继续吗?");
//        builder.setPositiveButton("我要退", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });
//        builder.setNegativeButton("先不退", null);
//        builder.create().show();
//    }
//
//    private void showDeleteOrderDialog(final int position) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//        builder.setTitle("删除订单");
//        builder.setMessage("您确定要删除订单，订单删除后将不能恢复，您还要继续吗?");
//        builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                notifyItemRemoved(position);
//            }
//        });
//        builder.setNegativeButton("算了", null);
//        builder.create().show();
//    }

    public interface OnAcceptedItemClickListener {
        void onAcceptedItemClick(int position);
    }
}
