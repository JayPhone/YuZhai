package com.yuzhai.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yuzhai.config.IPConfig;
import com.yuzhai.entry.responseBean.CancelPublishedRespBean;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.http.CommonRequest;
import com.yuzhai.http.ParamsGenerateUtil;
import com.yuzhai.http.RequestQueueSingleton;
import com.yuzhai.util.JsonUtil;
import com.yuzhai.view.UnRepeatToast;
import com.yuzhai.yuzhaiwork.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/3.
 */
public class PublishedRecyclerViewAdapter extends RecyclerView.Adapter<PublishedRecyclerViewHolder> implements
        View.OnClickListener {
    private Activity mContext;
    private CustomApplication mCustomApplication;
    private RequestQueue mRequestQueue;
    private final String COOKIE = "cookie";

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

    public PublishedRecyclerViewAdapter(Activity context) {
        mContext = context;
        this.mCustomApplication = (CustomApplication) mContext.getApplication();
        this.mRequestQueue = RequestQueueSingleton.getInstance(context).getRequestQueue();
        initData();
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
    public PublishedRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PublishedRecyclerViewHolder holder = new PublishedRecyclerViewHolder(LayoutInflater.from(
                mContext).inflate(R.layout.order_published_item_layout, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(PublishedRecyclerViewHolder holder, final int position) {
        holder.mStatusText.setText((String) testData.get(position).get("status"));
        holder.mOrderIdText.setText((String) testData.get(position).get("orderId"));
        holder.mDateText.setText((String) testData.get(position).get("date"));
        holder.mTitleText.setText((String) testData.get(position).get("title"));
        holder.mLimitText.setText((String) testData.get(position).get("limit"));
        holder.mPriceText.setText((String) testData.get(position).get("price"));
        holder.mTypeImage.setImageResource((int) testData.get(position).get("typeImage"));
        holder.mCancelButton.setOnClickListener(this);
        holder.mWarpLayout.setOnClickListener(this);
        holder.mWarpLayout.setOnLongClickListener(new View.OnLongClickListener() {
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
        switch (v.getId()) {
            case R.id.cancel_order:
                //显示取消订单对话框
                showCancelDialog();
                break;
            case R.id.wrap_layout:
                UnRepeatToast.showToast(mContext, "被点击了");
                break;
        }
    }

    /**
     * 显示取消订单对话框
     */
    public void showCancelDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("取消订单");
        builder.setMessage("您确定要取消订单，如果该订单已经被接收，则需要赔付一定的金额，您还要继续吗?");
        builder.setPositiveButton("我要退", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //正常代码
                //发送取消已发布订单请求
//                sendCancelPublishedRequest(mData.get(mPosition).getPublish().getPublishId());

            }
        });
        builder.setNegativeButton("先不退", null);
        builder.create().show();
    }

    public void showDeleteOrderDialog(final int position) {
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

    /**
     * 发送取消已发布订单请求
     */
    public void sendCancelPublishedRequest(String publishId) {
        //获取取消已发布订单请求的参数集
        Map<String, String> params = ParamsGenerateUtil.generateCancelPublishedOrderParams(publishId);

        //创建取消已发布订单请求
        CommonRequest cancelPublishedRequest = new CommonRequest(IPConfig.cancelPublishedOrderAddress,
                generateHeaders(),
                params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String resp) {
                        Log.i("cancel_publish_resp", resp);

                        CancelPublishedRespBean cancelPublishedRespBean = JsonUtil.decodeByGson(resp, CancelPublishedRespBean.class);
                        String respCode = cancelPublishedRespBean.getCode();

                        if (respCode != null && respCode.equals("1")) {
                            UnRepeatToast.showToast(mContext, "退单成功，请手动刷新");
                        }

                        if (respCode != null && respCode.equals("-1")) {
                            UnRepeatToast.showToast(mContext, "退单失败,请稍后再试");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        UnRepeatToast.showToast(mContext, "服务器睡着了");
                    }
                });

        //添加到请求队列
        mRequestQueue.add(cancelPublishedRequest);
    }

    public Map<String, String> generateHeaders() {
        //设置报头参数
        Map<String, String> headers = new HashMap<>();
        headers.put(COOKIE, mCustomApplication.getCookie());
        return headers;
    }
}
