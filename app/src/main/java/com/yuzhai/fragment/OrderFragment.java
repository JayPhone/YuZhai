package com.yuzhai.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yuzhai.adapter.OrderViewPagerAdapter;
import com.yuzhai.config.IPConfig;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.http.CommonRequest;
import com.yuzhai.util.JsonUtil;
import com.yuzhai.view.OrderViewPager;
import com.yuzhai.yuzhaiwork.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/10.
 */
public class OrderFragment extends Fragment {
    private Activity mainActivity;
    private List<View> orderViews;
    private OrderViewPagerAdapter orderViewPagerAdapter;
    private TextView titlePublished;
    private TextView titleAccpeted;
    private ImageView cursorImageView;
    private OrderViewPager orderViewPager;
    private SwipeRefreshLayout publishedOrderRefresh;
    private SwipeRefreshLayout acceptedOrderRefresh;
    private ListView publishedListView;
    private ListView acceptedListView;
    private View publishView;
    private View acceptView;
    private Button publishedCancle;

    private Bitmap cursor;
    private int currentItem;
    private Animation translateAnimation;
    private int offSet;
    private int cursorWidth;
    private Matrix matrix;

    private List<Map<String, Object>> publishedOrders;
    private List<Map<String, Object>> acceptedOrders;

    private CustomApplication customApplication;
    private RequestQueue requestQueue;

    private ProgressDialog progressDialog;

    private View publishedItemView;

    //订单图标类型
    private int[] typeImages = new int[]{R.drawable.it, R.drawable.music, R.drawable.design, R.drawable.movie, R.drawable.game, R.drawable.write, R.drawable.calculate};
    //订单状态
    private String[] status = new String[]{"已接", "待接", "完成", "已接", "待接", "完成", "完成"};
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainActivity = getActivity();
        customApplication = (CustomApplication) mainActivity.getApplication();
        requestQueue = customApplication.getRequestQueue();
        initViews();
        initViewPagerView();
    }

    //初始化组件
    public void initViews() {
        cursorImageView = (ImageView) mainActivity.findViewById(R.id.cursor);
        orderViewPager = (OrderViewPager) mainActivity.findViewById(R.id.order_viewPager);
        titlePublished = (TextView) mainActivity.findViewById(R.id.title_published);
        titleAccpeted = (TextView) mainActivity.findViewById(R.id.title_accepted);
        titlePublished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderViewPager.setCurrentItem(0);
            }
        });
        titleAccpeted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderViewPager.setCurrentItem(1);
            }
        });
    }

    //添加viewPager的页面
    public void initViewPagerView() {
        //添加viewPager的页面
        orderViews = new ArrayList<>();
        publishView = mainActivity.getLayoutInflater().inflate(R.layout.order_viewpager_published_layout, null);
        acceptView = mainActivity.getLayoutInflater().inflate(R.layout.order_viewpager_accepted_layout, null);
        orderViews.add(publishView);
        orderViews.add(acceptView);
        //创建viewPager的适配器
        orderViewPagerAdapter = new OrderViewPagerAdapter(orderViews);
        orderViewPager.setOffscreenPageLimit(2);
        orderViewPager.setAdapter(orderViewPagerAdapter);
        orderViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (position) {
                    case 0:
                        if (currentItem == 1) {
                            translateAnimation = new TranslateAnimation(offSet * 2 + cursorWidth, 0, 0, 0);
                            translateAnimation.setDuration(150);
                            translateAnimation.setFillAfter(true);
                            cursorImageView.startAnimation(translateAnimation);
                        }
                        break;
                    case 1:
                        if (currentItem == 0) {
                            translateAnimation = new TranslateAnimation(0, offSet * 2 + cursorWidth, 0, 0);
                            translateAnimation.setDuration(150);
                            translateAnimation.setFillAfter(true);
                            cursorImageView.startAnimation(translateAnimation);
                        }
                        break;
                }
                currentItem = position;
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        initCursor();
        initPublishedPage();
        initAcceptedPage();
    }

    //初始化游标
    public void initCursor() {
        matrix = new Matrix();
        cursor = BitmapFactory.decodeResource(getResources(), R.drawable.line);
        cursorWidth = cursor.getWidth();
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        offSet = (displayMetrics.widthPixels - 2 * cursorWidth) / 4;
        matrix.setTranslate(offSet, 0);
        cursorImageView.setImageMatrix(matrix);
        currentItem = 0;
    }

    //初始化发布页面
    public void initPublishedPage() {
        publishedOrderRefresh = (SwipeRefreshLayout) publishView.findViewById(R.id.published_order_refresh);
        publishedItemView = mainActivity.getLayoutInflater().inflate(R.layout.order_published_item_layout, null);
        publishedCancle = (Button) publishedItemView.findViewById(R.id.cancel_order);
        publishedCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mainActivity, "hello", Toast.LENGTH_SHORT).show();
//                AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
//                builder.setTitle("取消订单");
//                builder.setMessage("您确定要取消订单，如果该订单已经被接，则需要赔付一定的金额，您还要继续吗?");
//                builder.setPositiveButton("我要退", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//                builder.setNegativeButton("先不退", null);
//                builder.create().show();
            }
        });
        publishedListView = (ListView) publishView.findViewById(R.id.published_listview);
        publishedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(mainActivity, "hello", Toast.LENGTH_SHORT).show();
            }
        });
        CommonRequest commonRequest = new CommonRequest(Request.Method.POST, IPConfig.orderPublishedAddress, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.i("response", s);
                progressDialog.dismiss();
                publishedOrders = JsonUtil.decodeResponseForPublished(s);
                publishedListView.setAdapter(createPublishedAdapter(publishedOrders));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        commonRequest.setmHeaders(createHeaders());
        requestQueue.add(commonRequest);
        progressDialog = new ProgressDialog(mainActivity);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("数据加载中");
        progressDialog.show();

        publishedOrderRefresh.setColorSchemeResources(R.color.mainColor);
        publishedOrderRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                CommonRequest commonRequest = new CommonRequest(Request.Method.POST, IPConfig.orderPublishedAddress, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.i("response", s);
                        publishedOrders = JsonUtil.decodeResponseForPublished(s);
                        publishedListView.setAdapter(createPublishedAdapter(publishedOrders));
                        publishedOrderRefresh.setRefreshing(false);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });
                commonRequest.setmHeaders(createHeaders());
                requestQueue.add(commonRequest);
            }
        });
    }

    //初始化接收页面
    public void initAcceptedPage() {
        acceptedOrderRefresh = (SwipeRefreshLayout) acceptView.findViewById(R.id.accepted_order_refresh);
        acceptedListView = (ListView) acceptView.findViewById(R.id.accepted_listview);
        acceptedOrders = new ArrayList<>();
        for (int i = 0; i < typeImages.length; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("statu", status[i]);
            item.put("image", typeImages[i]);
            item.put("date", dates[i]);
            item.put("name", names[i]);
            item.put("price", prices[i]);
            acceptedOrders.add(item);
        }
        final SimpleAdapter adapter = new SimpleAdapter(
                mainActivity,
                acceptedOrders,
                R.layout.order_accepted_item_layout,
                new String[]{"statu", "date", "image", "name", "price"},
                new int[]{R.id.status, R.id.date, R.id.type_image, R.id.name, R.id.price}
        );
        acceptedListView.setAdapter(adapter);
        acceptedOrderRefresh.setColorSchemeResources(R.color.mainColor);
        acceptedOrderRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                acceptedListView.setAdapter(adapter);
                acceptedOrderRefresh.setRefreshing(false);
            }
        });
    }

    public SimpleAdapter createPublishedAdapter(List<Map<String, Object>> publishedOrders) {
        SimpleAdapter adapter = new SimpleAdapter(
                mainActivity,
                publishedOrders,
                R.layout.order_published_item_layout,
                new String[]{"currentstatu", "date", "image", "name", "price", "publishId", "limit"},
                new int[]{R.id.status, R.id.date, R.id.type_image, R.id.name, R.id.price, R.id.order_id, R.id.limit}
        );
        return adapter;
    }

    public Map<String, String> createHeaders() {
        //设置请求参数
        Map<String, String> headers = new HashMap<>();
        headers.put("cookie", customApplication.getCookie());
        return headers;
    }
}
