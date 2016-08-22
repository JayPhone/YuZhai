package com.yuzhai.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yuzhai.config.IPConfig;
import com.yuzhai.config.RespParamsNameConfig;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.http.CommonRequest;
import com.yuzhai.http.ParamsGenerateUtil;
import com.yuzhai.http.RequestQueueSingleton;
import com.yuzhai.util.JsonUtil;
import com.yuzhai.view.UnRepeatToast;
import com.yuzhai.yuzhaiwork.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/27.
 */
public class PublishedListViewAdapter extends BaseAdapter implements View.OnClickListener {
    private CustomApplication customApplication;
    private RequestQueue requestQueue;
    private Context context;
    private LayoutInflater layoutInflater;
    private List<Map<String, Object>> data;
    private final String COOKIE = "cookie";
    private int mPosition;

    public PublishedListViewAdapter(Activity context, List<Map<String, Object>> data) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
        this.customApplication = (CustomApplication) context.getApplication();
        this.requestQueue = RequestQueueSingleton.getInstance(context).getRequestQueue();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.order_published_item_layout, parent, false);
            viewHolder.statusText = (TextView) convertView.findViewById(R.id.status);
            viewHolder.orderIdText = (TextView) convertView.findViewById(R.id.order_id);
            viewHolder.dateText = (TextView) convertView.findViewById(R.id.date);
            viewHolder.titleText = (TextView) convertView.findViewById(R.id.name);
            viewHolder.limitText = (TextView) convertView.findViewById(R.id.limit);
            viewHolder.priceText = (TextView) convertView.findViewById(R.id.price);
            viewHolder.typeImage = (ImageView) convertView.findViewById(R.id.type_image);
            viewHolder.cancelButton = (Button) convertView.findViewById(R.id.cancel_order);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.statusText.setText((String) data.get(position).get("currentstatu"));
        viewHolder.orderIdText.setText((String) data.get(position).get("publishId"));
        viewHolder.dateText.setText((String) data.get(position).get("date"));
        viewHolder.titleText.setText((String) data.get(position).get("name"));
        viewHolder.limitText.setText((String) data.get(position).get("limit"));
        viewHolder.priceText.setText((String) data.get(position).get("price"));
        viewHolder.typeImage.setImageResource((int) data.get(position).get("image"));

        viewHolder.cancelButton.setOnClickListener(this);
        mPosition = position;
        return convertView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_order:
                //显示取消订单对话框
                showCancelDialog();
                break;
        }
    }

    /**
     * 显示取消订单对话框
     */
    public void showCancelDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("取消订单");
        builder.setMessage("您确定要取消订单，如果该订单已经被接收，则需要赔付一定的金额，您还要继续吗?");
        builder.setPositiveButton("我要退", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //发送取消已发布订单请求
                sendCancelPublishedRequest((String) data.get(mPosition).get("publishId"));
            }
        });
        builder.setNegativeButton("先不退", null);
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
                        String responseCode = JsonUtil.decodeJson(resp, RespParamsNameConfig.CancelPublishedOrdersParam.CODE);

                        if (responseCode != null && responseCode.equals("1")) {
                            UnRepeatToast.showToast(context, "退单成功，请手动刷新");
                        }

                        if (responseCode != null && responseCode.equals("-1")) {
                            UnRepeatToast.showToast(context, "退单失败,请稍后再试");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        UnRepeatToast.showToast(context, "服务器睡着了");
                    }
                });

        //添加到请求队列
        requestQueue.add(cancelPublishedRequest);
    }

    public Map<String, String> generateHeaders() {
        //设置报头参数
        Map<String, String> headers = new HashMap<>();
        headers.put(COOKIE, customApplication.getCookie());
        return headers;
    }
}
