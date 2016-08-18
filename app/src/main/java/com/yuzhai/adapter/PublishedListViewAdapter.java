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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yuzhai.config.IPConfig;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.http.CommonRequest;
import com.yuzhai.util.JsonUtil;
import com.yuzhai.yuzhaiwork.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/27.
 */
public class PublishedListViewAdapter extends BaseAdapter {
    private CustomApplication customApplication;
    private RequestQueue requestQueue;
    private Context context;
    private LayoutInflater layoutInflater;
    List<Map<String, Object>> datas;

    public PublishedListViewAdapter(Activity context, List<Map<String, Object>> datas) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.datas = datas;
        this.customApplication = (CustomApplication) context.getApplication();
        this.requestQueue = customApplication.getRequestQueue();
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
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
            convertView = layoutInflater.inflate(R.layout.order_published_item_layout, null);
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
        viewHolder.statusText.setText((String) datas.get(position).get("currentstatu"));
        viewHolder.orderIdText.setText((String) datas.get(position).get("publishId"));
        viewHolder.dateText.setText((String) datas.get(position).get("date"));
        viewHolder.titleText.setText((String) datas.get(position).get("name"));
        viewHolder.limitText.setText((String) datas.get(position).get("limit"));
        viewHolder.priceText.setText((String) datas.get(position).get("price"));
        viewHolder.typeImage.setImageResource((int) datas.get(position).get("image"));
        viewHolder.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("取消订单");
                builder.setMessage("您确定要取消订单，如果该订单已经被接，则需要赔付一定的金额，您还要继续吗?");
                builder.setPositiveButton("我要退", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CommonRequest cancelPublishOrderRequest = new CommonRequest(Request.Method.POST, IPConfig.cancelPublishedOrderAddress, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                Log.i("cancelPublishOrderRes", s);
                                if (JsonUtil.decodeJson(s, "code").equals("1")) {
                                    Toast.makeText(context, "退单成功，请手动刷新", Toast.LENGTH_SHORT).show();
                                } else if (JsonUtil.decodeJson(s, "code").equals("-1")) {
                                    Toast.makeText(context, "退单失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {

                            }
                        });
                        cancelPublishOrderRequest.setRequestHeaders(createHeaders());
                        cancelPublishOrderRequest.setRequestParams(createParams((String) datas.get(position).get("publishId")));
                        requestQueue.add(cancelPublishOrderRequest);
                    }
                });
                builder.setNegativeButton("先不退", null);
                builder.create().show();
            }
        });
        return convertView;
    }

    //生成请求参数
    public Map<String, String> createParams(String publishId) {
        Map<String, String> params = new HashMap<>();
        params.put("publishId", publishId);
        return params;
    }

    public Map<String, String> createHeaders() {
        //设置报头参数
        Map<String, String> headers = new HashMap<>();
        headers.put("cookie", customApplication.getCookie());
        return headers;
    }
}
