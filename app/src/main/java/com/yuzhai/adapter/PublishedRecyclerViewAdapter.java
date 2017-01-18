package com.yuzhai.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuzhai.bean.responseBean.OrderPublishedBean;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.util.TypeUtil;
import com.yuzhai.yuzhaiwork.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/3.
 */
public class PublishedRecyclerViewAdapter extends RecyclerView.Adapter<PublishedRecyclerViewHolder> {
    private Context mContext;
    private Fragment mPublishedFragment;
    private List<OrderPublishedBean.OrderBean> mOrder;
    private OnPublishedItemClickListener mOnPublishedItemClickListener;

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
    private List<Map<String, Object>> testPublishedOrders;

    /**
     * 测试方法
     */
    private void textMethod() {
        Map<String, Object> map;
        testPublishedOrders = new ArrayList<>();
        for (int i = 0; i < typeImages.length; i++) {
            map = new HashMap<>();
            map.put("type", typeImages[i]);
            map.put("status", status[i]);
            map.put("date", dates[i]);
            map.put("title", titles[i]);
            map.put("price", prices[i]);
            map.put("limit", limits[i]);
            map.put("orderId", orderIds[i]);
            testPublishedOrders.add(map);
        }
    }

    public PublishedRecyclerViewAdapter(Fragment fragment, List<OrderPublishedBean.OrderBean> order) {
        mPublishedFragment = fragment;
        mContext = fragment.getContext();
        mOrder = order;

        if (!CustomApplication.isConnect) {
            textMethod();
        }
    }

    public void setOnPublishedItemClickListener(OnPublishedItemClickListener onPublishedItemClickListener) {
        mOnPublishedItemClickListener = onPublishedItemClickListener;
    }


    @Override
    public PublishedRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PublishedRecyclerViewHolder holder = new PublishedRecyclerViewHolder(LayoutInflater.from(
                mContext).inflate(R.layout.order_published_item_layout, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(PublishedRecyclerViewHolder holder, final int position) {
        if (CustomApplication.isConnect) {
            holder.mStatusText.setText(mOrder.get(position).getStatus());
            holder.mOrderIdText.setText(mOrder.get(position).getOrderID());
            holder.mDateText.setText(mOrder.get(position).getDate());
            holder.mTitleText.setText(mOrder.get(position).getTitle());
            holder.mDeadlineText.setText(mOrder.get(position).getDeadline());
            holder.mRewardText.setText(mOrder.get(position).getReward());
            holder.mTypeImage.setImageResource(TypeUtil.getTypeImage(mOrder.get(position).getType()));
        } else {
            holder.mStatusText.setText((CharSequence) testPublishedOrders.get(position).get("status"));
            holder.mOrderIdText.setText((CharSequence) testPublishedOrders.get(position).get("orderId"));
            holder.mDateText.setText((CharSequence) testPublishedOrders.get(position).get("date"));
            holder.mTitleText.setText((CharSequence) testPublishedOrders.get(position).get("title"));
            holder.mDeadlineText.setText((CharSequence) testPublishedOrders.get(position).get("limit"));
            holder.mRewardText.setText((CharSequence) testPublishedOrders.get(position).get("price"));
            holder.mTypeImage.setImageResource((Integer) testPublishedOrders.get(position).get("type"));
        }


        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnPublishedItemClickListener) {
                    mOnPublishedItemClickListener.onPublishedItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (CustomApplication.isConnect) {
            return mOrder.size();
        } else {
            return testPublishedOrders.size();
        }
    }

    /**
     * 显示取消订单对话框
     */
//    public void showCancelDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//        builder.setTitle("取消订单");
//        builder.setMessage("您确定要取消订单，如果该订单已经被接收，则需要赔付一定的金额，您还要继续吗?");
//        builder.setPositiveButton("我要退", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                正常代码
//                发送取消已发布订单请求
//                sendCancelPublishedRequest(mData.get(mPosition).getPublish().getPublishId());
//
//            }
//        });
//        builder.setNegativeButton("先不退", null);
//        builder.create().show();
//    }

    /**
     * 发送取消已发布订单请求
     */
//    public void sendCancelPublishedRequest(String publishId, String token) {
//        //获取取消已发布订单请求的参数集
//        Map<String, String> params = ParamsGenerateUtil.generateCancelPublishedOrderParams(publishId, token);
//
//        //创建取消已发布订单请求
//        CommonRequest cancelPublishedRequest = new CommonRequest(IPConfig.cancelPublishedOrderAddress,
//                null,
//                params,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String resp) {
//                        Log.i("cancel_publish_resp", resp);
//
//                        CancelPublishedRespBean cancelPublishedRespBean = JsonUtil.decodeByGson(resp, CancelPublishedRespBean.class);
//                        String respCode = cancelPublishedRespBean.getCode();
//
//                        if (respCode != null && respCode.equals("1")) {
//                            UnRepeatToast.showToast(mContext, "退单成功，请手动刷新");
//                        }
//
//                        if (respCode != null && respCode.equals("-1")) {
//                            UnRepeatToast.showToast(mContext, "退单失败,请稍后再试");
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        UnRepeatToast.showToast(mContext, "服务器睡着了");
//                    }
//                });
//
//        //添加到请求队列
//        mRequestQueue.add(cancelPublishedRequest);
//    }

    public interface OnPublishedItemClickListener {
        void onPublishedItemClick(int position);
    }
}
