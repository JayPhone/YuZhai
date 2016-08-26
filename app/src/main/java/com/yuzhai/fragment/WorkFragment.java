package com.yuzhai.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yuzhai.config.IPConfig;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.http.CommonRequest;
import com.yuzhai.http.ParamsGenerateUtil;
import com.yuzhai.http.RequestQueueSingleton;
import com.yuzhai.ui.DetailWorkActivity;
import com.yuzhai.util.JsonUtil;
import com.yuzhai.view.UnRepeatToast;
import com.yuzhai.yuzhaiwork.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/22.
 */
public class WorkFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        AdapterView.OnItemClickListener {

    private SwipeRefreshLayout mWorkSrl;
    private ListView mWorkLv;
    private Activity mMainActivity;

    private CustomApplication mCustomApplication;
    private RequestQueue mRequestQueue;
    private String mWorkResponse;
    private List<Map<String, Object>> mOrders;
    private int mType;
    private final static String TYPE = "type";
    private final String COOKIE = "cookie";

    private String[] typeArray = new String[]{"软件IT", "音乐制作", "平面设计", "视频拍摄", "游戏研发", "文案撰写", "金融会计"};

    /**
     * 获取WorkFragment实例
     *
     * @param type 项目类型
     * @return WorkFragment实例
     */
    public static WorkFragment newInstance(int type) {
        Bundle data = new Bundle();
        data.putInt(TYPE, type);
        WorkFragment workFragment = new WorkFragment();
        workFragment.setArguments(data);
        return workFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.category_viewpager_work_layout, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMainActivity = getActivity();
        mCustomApplication = (CustomApplication) mMainActivity.getApplication();
        mRequestQueue = RequestQueueSingleton.getInstance(mMainActivity).getRequestQueue();
        mType = getArguments().getInt(TYPE);
        //初始化控件
        initViews();
        //初始化数据
        initData();
    }

    /**
     * 初始化组件
     */
    public void initViews() {
        mWorkSrl = (SwipeRefreshLayout) mMainActivity.findViewById(R.id.work_refresh);
        mWorkLv = (ListView) mMainActivity.findViewById(R.id.work_listview);

        //设置下拉刷新监听
        mWorkSrl.setOnRefreshListener(this);
        //设置刷新样式
        mWorkSrl.setColorSchemeResources(R.color.mainColor);
        //设置ListView的子项点击监听
        mWorkLv.setOnItemClickListener(this);
    }

    /**
     * 初始化数据
     */
    public void initData() {
        setRefreshState(true);
        sendOrdersByTypeRequest(typeArray[mType]);
    }

    @Override
    public void onRefresh() {
        setRefreshState(true);
        sendOrdersByTypeRequest(typeArray[mType]);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.work_listview:
                try {
                    //解析出订单数据
                    JSONArray jsonArray = JsonUtil.decodeToJsonArray(mWorkResponse, "order");
                    String orders = jsonArray.get(position).toString();
                    Log.i("work_orders", orders);

                    //打开详细界面
                    Intent detailActivity = new Intent(mMainActivity, DetailWorkActivity.class);
                    detailActivity.putExtra("data", orders);
                    detailActivity.putExtra("type", (int) mOrders.get(position).get("image"));
                    startActivity(detailActivity);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * 发送通过类型查询订单请求
     *
     * @param type 项目类型
     */
    public void sendOrdersByTypeRequest(String type) {
        //获取通过类型查询订单请求的参数集
        Map<String, String> params = ParamsGenerateUtil.generateOrdersByTypeParams(type);

        //创建通过类型查询订单请求
        CommonRequest ordersByTypeRequest = new CommonRequest(IPConfig.ordersByTypeAddress,
                generateHeaders(),
                params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String resp) {
                        Log.i("work_fragment_resp", resp);
                        mWorkResponse = resp;
                        mOrders = JsonUtil.decodeResponseForJob(resp, mType);
                        mWorkLv.setAdapter(createWorkAdapter(mOrders));
                        setRefreshState(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        UnRepeatToast.showToast(mMainActivity, "服务器睡着了");
                    }
                });

        //添加到请求队列
        mRequestQueue.add(ordersByTypeRequest);
    }

    /**
     * 生成ListView的适配器
     *
     * @param orders 订单数据
     * @return 返回ListView的适配器
     */
    public SimpleAdapter createWorkAdapter(List<Map<String, Object>> orders) {
        SimpleAdapter adapter = new SimpleAdapter(
                mMainActivity,
                orders,
                R.layout.category_work_listview_item_layout,
                new String[]{"date", "image", "name", "price", "limit"},
                new int[]{R.id.date, R.id.type_image, R.id.name, R.id.price, R.id.limit}
        );
        return adapter;
    }

    /**
     * 设置是否显示刷新状态
     *
     * @param state 刷新状态
     */
    public void setRefreshState(Boolean state) {
        mWorkSrl.setRefreshing(state);
    }

    /**
     * 生成请求头参数集
     *
     * @return 返回请求头参数集
     */
    public Map<String, String> generateHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put(COOKIE, mCustomApplication.getCookie());
        return headers;
    }
}
