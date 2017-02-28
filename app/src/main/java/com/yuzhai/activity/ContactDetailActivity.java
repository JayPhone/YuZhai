package com.yuzhai.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yuzhai.adapter.ContactDetailRecyclerViewAdapter;
import com.yuzhai.bean.innerBean.ContactMsgBean;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.yuzhaiwork.R;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.listener.MessageListHandler;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.newim.listener.MessagesQueryListener;
import cn.bmob.newim.listener.ObseverListener;
import cn.bmob.newim.notification.BmobNotificationManager;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by 35429 on 2017/2/10.
 */

public class ContactDetailActivity extends AppCompatActivity implements
        SwipeRefreshLayout.OnRefreshListener,
        ObseverListener,
        MessageListHandler {
    private static final String TAG = "ContactDetailActivity";
    private static final String BOTTOM = "bottom";
    private static final String TOP = "top";

    private List<BmobIMMessage> mUpdateMsgList;
    private List<BmobIMMessage> mInitMsgList = new ArrayList<>();
    private CustomApplication mCustomApplication;
    private Toolbar mToolbar;
    private EditText mSendEdit;
    private Button mSendButton;
    private SwipeRefreshLayout mContactDetailSrl;
    private RecyclerView mContactDetailRecyclerView;
    private ContactDetailRecyclerViewAdapter mContactDetailRecyclerViewAdapter;
    private BmobIMConversation mConversation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCustomApplication = (CustomApplication) getApplication();
        setContentView(R.layout.activity_contact_detail);
        //获取BmobIMConversation对象，启动会话的查询，删除等功能
        mConversation = BmobIMConversation.obtain(BmobIMClient.getInstance(), (BmobIMConversation) getIntent().getExtras().getSerializable("c"));
        initView();
        initData();
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.contact_detail_toolbar);
        mToolbar.setTitle(mConversation.getConversationTitle());
        mToolbar.setNavigationIcon(R.drawable.back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mContactDetailSrl = (SwipeRefreshLayout) findViewById(R.id.contact_detail_refresh);
        //设置下拉刷新监听
        mContactDetailSrl.setOnRefreshListener(this);
        //设置刷新样式
        mContactDetailSrl.setColorSchemeResources(R.color.mainColor);

        mContactDetailRecyclerView = (RecyclerView) findViewById(R.id.contact_recycler);
        mContactDetailRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mContactDetailRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mContactDetailRecyclerViewAdapter = new ContactDetailRecyclerViewAdapter(mInitMsgList, mCustomApplication);
        mContactDetailRecyclerView.setAdapter(mContactDetailRecyclerViewAdapter);

        mSendEdit = (EditText) findViewById(R.id.send_edt);
        mSendButton = (Button) findViewById(R.id.send_btn);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sendText = mSendEdit.getText().toString();
                if (!TextUtils.isEmpty(sendText)) {
                    BmobIMTextMessage msg = new BmobIMTextMessage();
                    msg.setContent(mSendEdit.getText().toString());
                    mConversation.sendMessage(msg, new MessageSendListener() {
                        @Override
                        public void onStart(BmobIMMessage msg) {
                            super.onStart(msg);
                            Log.i(TAG, msg.toString());
                        }

                        @Override
                        public void done(BmobIMMessage msg, BmobException e) {
                            Log.i(TAG, msg.toString() + " ");
                            mUpdateMsgList = new ArrayList<>();
                            mUpdateMsgList.add(msg);
                            updateMsg(mUpdateMsgList, TOP);
                            mSendEdit.setText("");
                            mContactDetailRecyclerView.scrollToPosition(mInitMsgList.size() - 1);
                            if (e != null) {
                                Log.i(TAG, "发送错误:" + e.getMessage());
                            }
                        }
                    });
                } else {
                    Toast.makeText(ContactDetailActivity.this, "发送消息不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initData() {
        queryMessage(null);
    }

    @Override
    public void onRefresh() {
        if (mInitMsgList != null && mInitMsgList.size() > 0) {
            queryMessage(mInitMsgList.get(0));
        } else {
            setRefreshState(false);
        }
    }

    /**
     * 查询历史消息
     *
     * @param msg
     */
    private void queryMessage(BmobIMMessage msg) {
        //首次加载，可设置msg为null，
        //下拉刷新的时候，可用消息表的第一个msg作为刷新的起始时间点，默认按照消息时间的降序排列，limit由开发者控制
        mConversation.queryMessages(msg, 10, new MessagesQueryListener() {
            @Override
            public void done(List<BmobIMMessage> list, BmobException e) {
                if (e == null) {
                    if (null != list && list.size() > 0) {
                        for (BmobIMMessage message : list) {
                            Log.i(TAG, message.toString());
                        }
                        //更新数据
                        updateMsg(list, BOTTOM);
                    } else {
                        setRefreshState(false);
                    }
                } else {
                    Log.i(TAG, "error:" + e.getMessage() + "(" + e.getErrorCode() + ")");
                }
            }
        });
    }

    /**
     * 插入数据
     *
     * @param messageList
     * @param orientation
     */
    private void updateMsg(List<BmobIMMessage> messageList, String orientation) {
        if (orientation.equals(BOTTOM)) {
            mInitMsgList.addAll(0, messageList);
        } else if (orientation.equals(TOP)) {
            mInitMsgList.addAll(messageList);
        }
        mContactDetailRecyclerViewAdapter.notifyDataSetChanged();
        setRefreshState(false);
    }

    @Override
    protected void onResume() {
        //锁屏期间的收到的未读消息需要添加到聊天界面中
        addUnReadMessage();
        //添加页面消息监听器
        BmobIM.getInstance().addMessageListHandler(this);
        // 有可能锁屏期间，在聊天界面出现通知栏，这时候需要清除通知
        BmobNotificationManager.getInstance(this).cancelNotification();
        super.onResume();
    }

    @Override
    public void onMessageReceive(List<MessageEvent> list) {
        Log.i(TAG, "聊天页面接收到消息：" + list.size());
        //当注册页面消息监听时候，有消息（包含离线消息）到来时会回调该方法
        for (int i = 0; i < list.size(); i++) {
            addMessageToChat(list.get(i));
        }
    }

    /**
     * 添加未读的通知栏消息到聊天界面
     */
    private void addUnReadMessage() {
        List<MessageEvent> cache = BmobNotificationManager.getInstance(this).getNotificationCacheList();
        if (cache.size() > 0) {
            int size = cache.size();
            for (int i = 0; i < size; i++) {
                MessageEvent event = cache.get(i);
                addMessageToChat(event);
            }
        }
    }

    /**
     * 添加消息到聊天界面中
     *
     * @param event
     */
    private void addMessageToChat(MessageEvent event) {
        BmobIMMessage msg = event.getMessage();
        if (mConversation != null && mConversation.getConversationId().equals(event.getConversation().getConversationId()) //如果是当前会话的消息
                && !msg.isTransient()) {//并且不为暂态消息
            mUpdateMsgList = new ArrayList<>();
            mUpdateMsgList.add(msg);
            updateMsg(mUpdateMsgList, TOP);
            //更新该会话下面的已读状态
            mConversation.updateReceiveStatus(msg);
            mContactDetailRecyclerView.scrollToPosition(mInitMsgList.size() - 1);
        } else {
            Log.i(TAG, "不是与当前聊天对象的消息");
        }
    }

    @Override
    protected void onPause() {

        //移除页面消息监听器
        BmobIM.getInstance().removeMessageListHandler(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        //更新此会话的所有消息为已读状态
        if (mConversation != null) {
            mConversation.updateLocalCache();
        }
        super.onDestroy();
    }

    /**
     * 设置是否显示刷新状态
     *
     * @param state 刷新状态
     */
    public void setRefreshState(Boolean state) {
        mContactDetailSrl.setRefreshing(state);
    }

}
