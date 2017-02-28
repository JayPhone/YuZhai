package com.yuzhai.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuzhai.activity.ContactDetailActivity;
import com.yuzhai.adapter.ContactRecyclerViewAdapter;
import com.yuzhai.bean.responseBean.ContactPrivateConversationBean;
import com.yuzhai.recyclerview.DividerGridItemDecoration;
import com.yuzhai.yuzhaiwork.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;

/**
 * Created by Administrator on 2016/12/24.
 */

public class ContactFragment extends Fragment implements
        ContactRecyclerViewAdapter.OnContactItemClickListener,
        SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "ContactFragment";
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mContactSrl;
    private ContactRecyclerViewAdapter mContactRecyclerViewAdapter;
    private List<ContactPrivateConversationBean> mInitConversationList = new ArrayList<>();

    public static ContactFragment newInstance() {
        Bundle args = new Bundle();
        ContactFragment fragment = new ContactFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventBus.getDefault().register(this);
        initView();
        initData();
    }

    private void initView() {
        mToolbar = (Toolbar) getView().findViewById(R.id.contact_toolbar);
        mToolbar.setTitle("最近联系");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer);
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        mContactSrl = (SwipeRefreshLayout) getView().findViewById(R.id.contact_refresh);
        //设置下拉刷新监听
        mContactSrl.setOnRefreshListener(this);
        //设置刷新样式
        mContactSrl.setColorSchemeResources(R.color.mainColor);

        mRecyclerView = (RecyclerView) getView().findViewById(R.id.contact_recyclerView);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(getActivity()));
        mContactRecyclerViewAdapter = new ContactRecyclerViewAdapter(this, mInitConversationList);
        mRecyclerView.setAdapter(mContactRecyclerViewAdapter);
        mContactRecyclerViewAdapter.setOnContactItemClickListener(this);
    }

    private void initData() {
        List<ContactPrivateConversationBean> conversationList = getConversations();
        if (conversationList != null && conversationList.size() > 0) {
            for (ContactPrivateConversationBean item : conversationList) {
                Log.i(TAG, "conversation:" + item.toString());
            }
            updateMsg(conversationList);
        }
    }

    /**
     * 获取会话列表的数据
     *
     * @return
     */
    private List<ContactPrivateConversationBean> getConversations() {
        //添加会话
        List<ContactPrivateConversationBean> conversationList = new ArrayList<>();
        conversationList.clear();
        List<BmobIMConversation> list = BmobIM.getInstance().loadAllConversation();
        if (list != null && list.size() > 0) {
            for (BmobIMConversation item : list) {
                switch (item.getConversationType()) {
                    case 1://私聊
                        conversationList.add(new ContactPrivateConversationBean(item));
                        break;
                    default:
                        break;
                }
            }
        }
        //重新排序
        Collections.sort(conversationList);
        return conversationList;
    }

    /**
     * 插入数据
     *
     * @param messageList
     */
    private void updateMsg(List<ContactPrivateConversationBean> messageList) {
        mInitConversationList.clear();
        mInitConversationList.addAll(messageList);
        mContactRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        queryConversation();
    }

    @Override
    public void onContactItemClick(int position) {
        BmobIMConversation c = mInitConversationList.get(position).getConversation();
        //进入详细聊天界面
        Intent contactDetail = new Intent(getActivity(), ContactDetailActivity.class);
        contactDetail.putExtra("c", c);
        startActivity(contactDetail);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.i(TAG, "UserVisible:" + isVisibleToUser);
    }

    /**
     * 运行时刷新数据
     */
    @Override
    public void onResume() {
        super.onResume();
        queryConversation();
    }

    private void queryConversation() {
        setRefreshState(true);
        updateMsg(getConversations());
        setRefreshState(false);
    }

    /**
     * 注册离线消息接收事件
     *
     * @param event
     */
    @Subscribe
    public void onEventMainThread(OfflineMessageEvent event) {
        //重新刷新列表
        updateMsg(getConversations());
        Log.i(TAG, "offline_event:" + event.getEventMap().toString());
    }

    /**
     * 注册消息接收事件
     *
     * @param event 1、与用户相关的由开发者自己维护，SDK内部只存储用户信息
     *              2、开发者获取到信息后，可调用SDK内部提供的方法更新会话
     */
    @Subscribe
    public void onEventMainThread(MessageEvent event) {
        //重新获取本地消息并刷新列表
        updateMsg(getConversations());
        Log.i(TAG, "event:" + event.getConversation().getMessages());
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        //清理导致内存泄露的资源
        BmobIM.getInstance().clear();
        super.onDestroy();
    }

    /**
     * 设置是否显示刷新状态
     *
     * @param state 刷新状态
     */
    public void setRefreshState(Boolean state) {
        mContactSrl.setRefreshing(state);
    }
}
