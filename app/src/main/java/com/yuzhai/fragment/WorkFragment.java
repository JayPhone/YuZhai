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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yuzhai.adapter.WorkListViewAdapter;
import com.yuzhai.config.IPConfig;
import com.yuzhai.entry.responseBean.OrderByTypeBean;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.http.CommonRequest;
import com.yuzhai.http.ParamsGenerateUtil;
import com.yuzhai.http.RequestQueueSingleton;
import com.yuzhai.ui.DetailWorkActivity;
import com.yuzhai.util.JsonUtil;
import com.yuzhai.util.TypeUtil;
import com.yuzhai.view.UnRepeatToast;
import com.yuzhai.yuzhaiwork.R;

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
    private List<OrderByTypeBean.OrderBean> mOrders;
    private int mType = 0;
    private final static String TYPE = "type";
    private final String COOKIE = "cookie";

    /**
     * 测试数据
     */
    //订单图标类型
    private int[] typeImages = new int[]{R.drawable.it, R.drawable.music, R.drawable.design, R.drawable.movie, R.drawable.game, R.drawable.write, R.drawable.calculate};
    //订单日期
    private String[] dates = new String[]{"2016-07-14", "2016-07-14", "2016-07-14", "2016-07-14", "2016-07-14", "2016-07-14", "2016-07-14"};
    //订单名称
    private String[] names = new String[]{
            "【LOGO设计】千树设计主管标志设计商标设计/设计名称",
            "【LOGO设计】千树设计主管标志设计商标设计/设计名称",
            "【LOGO设计】千树设计主管标志设计商标设计/设计名称",
            "【LOGO设计】千树设计主管标志设计商标设计/设计名称",
            "【LOGO设计】千树设计主管标志设计商标设计/设计名称",
            "【LOGO设计】千树设计主管标志设计商标设计/设计名称",
            "【LOGO设计】千树设计主管标志设计商标设计/设计名称"
    };
    //订单金额
    private String[] prices = new String[]{"100", "150", "200", "250", "300", "350", "400"};
    private String[] limit = new String[]{"5天", "10天", "15天", "20天", "25天", "30天", "35天"};
    private List<Map<String, Object>> works;

    /**
     * 获取WorkFragment实例
     *
     * @return WorkFragment实例
     */
    public static WorkFragment newInstance() {
        Bundle data = new Bundle();
//        data.putInt(TYPE, type);
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
//        mType = getArguments().getInt(TYPE);
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
        sendOrdersByTypeRequest(TypeUtil.getTypeText(mType));
//        works = new ArrayList<>();
//        for (int i = 0; i < typeImages.length; i++) {
//            Map<String, Object> item = new HashMap<>();
//            item.put("typeImage", typeImages[i]);
//            item.put("title", names[i]);
//            item.put("price", prices[i]);
//            item.put("date", dates[i]);
//            item.put("limit", limit[i]);
//            works.add(item);
//        }
//        SimpleAdapter simpleAdapter = new SimpleAdapter(mMainActivity,
//                works,
//                R.layout.category_work_listview_item_layout,
//                new String[]{"typeImage", "title", "price", "date", "limit"},
//                new int[]{R.id.type_image, R.id.title, R.id.price, R.id.date, R.id.limit});
//        mWorkLv.setAdapter(simpleAdapter);
    }

    @Override
    public void onRefresh() {
        sendOrdersByTypeRequest(TypeUtil.getTypeText(mType));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.work_listview:
                //解析出订单数据
                String orderJson = JsonUtil.codeByGson(mOrders.get(position));
                Log.i("work_orders", orderJson);

                //打开详细界面
                Intent detailActivity = new Intent(mMainActivity, DetailWorkActivity.class);
                detailActivity.putExtra("data", orderJson);
                startActivity(detailActivity);
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
                        mOrders = JsonUtil.decodeByGson(resp, OrderByTypeBean.class).getOrder();
                        mWorkLv.setAdapter(new WorkListViewAdapter(mMainActivity, mOrders));
                        setRefreshState(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        UnRepeatToast.showToast(mMainActivity, "服务器不务正业中");
                        setRefreshState(false);
                    }
                });

        //添加到请求队列
        mRequestQueue.add(ordersByTypeRequest);
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
