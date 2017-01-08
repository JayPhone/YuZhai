package com.yuzhai.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuzhai.yuzhaiwork.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/13.
 */

public class AcceptedRecyclerViewAdapter extends RecyclerView.Adapter<AcceptedRecyclerViewHolder> implements
        View.OnClickListener {
    private Activity mContext;
    private OnAcceptedItemClickListener mOnAcceptedItemClickListener;

    //测试代码
    private List<Map<String, Object>> testData;
    //订单图标类型
    private int[] typeImages = new int[]{R.drawable.test1, R.drawable.test2, R.drawable.test3, R.drawable.test4, R.drawable.test1, R.drawable.test2, R.drawable.test4};
    //订单状态
    private String[] status = new String[]{"已接", "待接", "完成", "已接", "待接", "完成", "完成"};
    //订单日期
    private String[] dates = new String[]{"2016-07-14", "2016-07-14", "2016-07-14", "2016-07-14", "2016-07-14", "2016-07-14", "2016-07-14"};
    //订单名称
    private String[] names = new String[]{
            "帮我做一个很厉害的毕业设计，记住，是很厉害的，普通厉害的不要!!",
            "帮我做一个很厉害的毕业设计，记住，是很厉害的，普通厉害的不要!!",
            "帮我做一个很厉害的毕业设计，记住，是很厉害的，普通厉害的不要!!",
            "帮我做一个很厉害的毕业设计，记住，是很厉害的，普通厉害的不要!!",
            "帮我做一个很厉害的毕业设计，记住，是很厉害的，普通厉害的不要!!",
            "帮我做一个很厉害的毕业设计，记住，是很厉害的，普通厉害的不要!!",
            "帮我做一个很厉害的毕业设计，记住，是很厉害的，普通厉害的不要!!"
    };
    //订单金额
    private String[] prices = new String[]{"100", "150", "200", "250", "300", "350", "400"};
    private String[] limits = new String[]{"5天", "10天", "15天", "20天", "25天", "30天", "35天"};
    private String[] orderId = new String[]{"1954656112", "1954656112", "1954656112", "1954656112", "1954656112", "1954656112", "1954656112"};

    public AcceptedRecyclerViewAdapter(Activity context) {
        mContext = context;
        initData();
    }

    public void setOnAcceptedItemClickListener(OnAcceptedItemClickListener onAcceptedItemClickListener) {
        this.mOnAcceptedItemClickListener = onAcceptedItemClickListener;
    }

    private void initData() {
        testData = new ArrayList<>();
        for (int i = 0; i < typeImages.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("typeImage", typeImages[i]);
            map.put("status", status[i]);
            map.put("date", dates[i]);
            map.put("title", names[i]);
            map.put("limit", limits[i]);
            map.put("price", prices[i]);
            map.put("orderId", orderId[i]);
            testData.add(map);
        }
    }

    @Override
    public AcceptedRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AcceptedRecyclerViewHolder holder = new AcceptedRecyclerViewHolder(LayoutInflater.from(
                mContext).inflate(R.layout.order_accepted_item_layout, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(AcceptedRecyclerViewHolder holder, final int position) {
        holder.mStatusText.setText((String) testData.get(position).get("status"));
        holder.mOrderIdText.setText((String) testData.get(position).get("orderId"));
        holder.mDateText.setText((String) testData.get(position).get("date"));
        holder.mTitleText.setText((String) testData.get(position).get("title"));
        holder.mLimitText.setText((String) testData.get(position).get("limit"));
        holder.mPriceText.setText((String) testData.get(position).get("price"));
        holder.mTypeImage.setImageResource((int) testData.get(position).get("typeImage"));
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnAcceptedItemClickListener) {
                    mOnAcceptedItemClickListener.onAcceptedItemClick(position);
                }
            }
        });
        holder.mCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDeleteOrderDialog(position);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return testData.size();
    }

    @Override
    public void onClick(View v) {
    }


    /**
     * 显示取消订单对话框
     */
    private void showCancelDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("取消接单");
        builder.setMessage("您确定要取消订单，你所接收的订单在未完成的情况下退单，需要赔付给发布方你所交纳的违约金，您还要继续吗?");
        builder.setPositiveButton("我要退", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("先不退", null);
        builder.create().show();
    }

    private void showDeleteOrderDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("删除订单");
        builder.setMessage("您确定要删除订单，订单删除后将不能恢复，您还要继续吗?");
        builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                notifyItemRemoved(position);
            }
        });
        builder.setNegativeButton("算了", null);
        builder.create().show();
    }

    public interface OnAcceptedItemClickListener {
        void onAcceptedItemClick(int position);
    }
}
