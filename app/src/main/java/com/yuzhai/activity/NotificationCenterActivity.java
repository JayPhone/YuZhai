package com.yuzhai.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.yuzhai.adapter.NotificationRecyclerViewAdapter;
import com.yuzhai.bean.innerBean.NotificationDBBean;
import com.yuzhai.bean.responseBean.OrderAcceptedDetailBean;
import com.yuzhai.bean.responseBean.OrderPublishedBean;
import com.yuzhai.fragment.OrderAcceptedFragment;
import com.yuzhai.fragment.OrderPublishedFragment;
import com.yuzhai.yuzhaiwork.R;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpHeaders.ACCEPT;
import static org.litepal.crud.DataSupport.findAll;

/**
 * Created by 35429 on 2017/2/18.
 */

public class NotificationCenterActivity extends AppCompatActivity implements
        NotificationRecyclerViewAdapter.OnNotificationClickListener,
        SwipeRefreshLayout.OnRefreshListener,
        Toolbar.OnMenuItemClickListener {
    private static final String TAG = "NotificationCenterActiv";
    private final static String PUBLISH = "publish";
    private final static String APPLY = "apply";
    private final static String RECEIVE = "receive";

    private Toolbar mToolbar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private NotificationRecyclerViewAdapter mNotificationRecyclerViewAdapter;
    private List<NotificationDBBean> mInitNotifications = new ArrayList<>();
    private List<NotificationDBBean> mUpdateNotifications;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_center);
        initView();
        initData();
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.notification_toolbar);
        mToolbar.inflateMenu(R.menu.notification_center_menu);
        mToolbar.setOnMenuItemClickListener(this);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.notification_refresh);
        //设置下拉刷新监听
        mSwipeRefreshLayout.setOnRefreshListener(this);
        //设置刷新样式
        mSwipeRefreshLayout.setColorSchemeResources(R.color.mainColor);
        mRecyclerView = (RecyclerView) findViewById(R.id.notification_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mNotificationRecyclerViewAdapter = new NotificationRecyclerViewAdapter(mInitNotifications);
        mNotificationRecyclerViewAdapter.setOnNotificationClickListener(this);
        mRecyclerView.setAdapter(mNotificationRecyclerViewAdapter);
    }

    private void initData() {
        setRefreshState(true);
        mUpdateNotifications = DataSupport.findAll(NotificationDBBean.class);
        if (mUpdateNotifications.size() != 0) {
            updateData(mUpdateNotifications);
        } else {
            setRefreshState(false);
        }
    }

    @Override
    public void onRefresh() {
        setRefreshState(true);
        mUpdateNotifications = DataSupport.findAll(NotificationDBBean.class);
        //当前数据库有更新
        if (mUpdateNotifications.size() > mInitNotifications.size()) {
            mUpdateNotifications = DataSupport
                    .limit(mInitNotifications.size())
                    .offset(mUpdateNotifications.size() - 1)
                    .find(NotificationDBBean.class);
            updateData(mUpdateNotifications);
        } else {
            setRefreshState(false);
        }
    }

    /**
     * 更新订单数据
     *
     * @param newNotifications 新获取的订单数据集
     */
    public void updateData(List<NotificationDBBean> newNotifications) {
        for (int i = newNotifications.size() - 1; i >= 0; i--) {
            //将获取的新数据插入到数据集
            mInitNotifications.add(newNotifications.get(i));
        }
        //通知recyclerView插入数据
        mNotificationRecyclerViewAdapter.notifyItemRangeInserted(0, newNotifications.size());
        //recyclerView滚动到顶部
        mRecyclerView.smoothScrollToPosition(0);
        setRefreshState(false);
    }

    @Override
    public void onNotificationClick(String type, String orderId) {
        if (type.equals(PUBLISH)) {//打开发布详细页面
            Intent orderPublishedDetail = new Intent(this, OrdersPublishedActivity.class);
            orderPublishedDetail.putExtra(OrderPublishedFragment.ORDER_ID, orderId);
            startActivity(orderPublishedDetail);
        } else if (type.equals(RECEIVE)) {//打开接收详细页面
            Intent orderAcceptedDetail = new Intent(this, OrdersAcceptedActivity.class);
            orderAcceptedDetail.putExtra(OrderAcceptedFragment.ORDER_ID, orderId);
            startActivity(orderAcceptedDetail);
        } else if (type.equals(APPLY)) {//打开申请详细页面
            Intent orderAppliedDetail = new Intent(this, OrdersAppliedActivity.class);
            orderAppliedDetail.putExtra(OrderAcceptedFragment.ORDER_ID, orderId);
            startActivity(orderAppliedDetail);
        }
    }

    /**
     * 清空所有数据
     */
    public void deleteAll() {
        mNotificationRecyclerViewAdapter.notifyItemRangeRemoved(0, mInitNotifications.size());
        mInitNotifications.clear();
    }

    /**
     * 设置是否显示刷新状态
     *
     * @param state 刷新状态
     */
    public void setRefreshState(Boolean state) {
        mSwipeRefreshLayout.setRefreshing(state);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                showDeleteAllNotificationDialog();
                break;
        }
        return false;
    }

    private void showDeleteAllNotificationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("删除所有通知");
        builder.setMessage("确认要删除所有通知吗，删除后将不能恢复，请谨慎操作！");
        builder.setPositiveButton("我确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //删除数据库中的所有通知数据
                DataSupport.deleteAll(NotificationDBBean.class);
                //清空界面
                deleteAll();
            }
        });
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }
}
