package com.yuzhai.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.yuzhai.adapter.ContactDetailRecyclerViewAdapter;
import com.yuzhai.bean.innerBean.ContactMsgBean;
import com.yuzhai.yuzhaiwork.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 35429 on 2017/2/10.
 */

public class ContactDetailActivity extends AppCompatActivity {
    private List<ContactMsgBean> contactMsgBeanList = new ArrayList<>();
    private Toolbar mToolbar;
    private EditText mSendEdit;
    private Button mSendButton;
    private RecyclerView mRecyclerView;
    private ContactDetailRecyclerViewAdapter mContactDetailRecyclerViewAdapter;

    private String mUserName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);
        mUserName = getIntent().getStringExtra("user_name");
        initMsg();
        initView();
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.contact_detail_toolbar);
        mToolbar.setTitle(mUserName);
        mToolbar.setNavigationIcon(R.drawable.back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.contact_recycler);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mContactDetailRecyclerViewAdapter = new ContactDetailRecyclerViewAdapter(contactMsgBeanList);
        mRecyclerView.setAdapter(mContactDetailRecyclerViewAdapter);

        mSendEdit = (EditText) findViewById(R.id.send_edt);
        mSendButton = (Button) findViewById(R.id.send_btn);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactMsgBean msg = new ContactMsgBean(mSendEdit.getText().toString(), ContactMsgBean.TYPE_SENT);
                updateMsg(msg);
                mSendEdit.setText("");
            }
        });
    }

    private void initMsg() {
        ContactMsgBean contactMsgBean1 = new ContactMsgBean("接收消息接收消息接收消息", ContactMsgBean.TYPE_RECEIVED);
        ContactMsgBean contactMsgBean2 = new ContactMsgBean("接收消息接收消息接收消息", ContactMsgBean.TYPE_SENT);
        ContactMsgBean contactMsgBean3 = new ContactMsgBean("发送消息发送消息发送消息发送消息发送消息", ContactMsgBean.TYPE_RECEIVED);
        ContactMsgBean contactMsgBean4 = new ContactMsgBean("接收消息接收消息接收消息接收消息接收消息", ContactMsgBean.TYPE_SENT);
        ContactMsgBean contactMsgBean5 = new ContactMsgBean("发送消息发送消息发送消息发送消息发送消息发送消息发送消息发送消息发送消息发送消息发送消息发送消息", ContactMsgBean.TYPE_RECEIVED);
        ContactMsgBean contactMsgBean6 = new ContactMsgBean("接收消息接收消息接收消息接收消息接收消息接收消息接收消息接收消息接收消息接收消息接收消息接收消息", ContactMsgBean.TYPE_SENT);
        ContactMsgBean contactMsgBean7 = new ContactMsgBean("接收消息接收消息接收消息", ContactMsgBean.TYPE_RECEIVED);
        ContactMsgBean contactMsgBean8 = new ContactMsgBean("接收消息接收消息接收消息", ContactMsgBean.TYPE_SENT);
        contactMsgBeanList.add(contactMsgBean1);
        contactMsgBeanList.add(contactMsgBean2);
        contactMsgBeanList.add(contactMsgBean3);
        contactMsgBeanList.add(contactMsgBean4);
        contactMsgBeanList.add(contactMsgBean5);
        contactMsgBeanList.add(contactMsgBean6);
        contactMsgBeanList.add(contactMsgBean7);
        contactMsgBeanList.add(contactMsgBean8);
    }

    private void updateMsg(ContactMsgBean msg) {
        contactMsgBeanList.add(msg);
        mContactDetailRecyclerViewAdapter.notifyItemInserted(contactMsgBeanList.size() - 1);
        mRecyclerView.scrollToPosition(contactMsgBeanList.size() - 1);
    }
}
