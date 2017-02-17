package com.yuzhai.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yuzhai.activity.ContactDetailActivity;
import com.yuzhai.adapter.ContactRecyclerViewAdapter;
import com.yuzhai.recyclerview.DividerGridItemDecoration;
import com.yuzhai.yuzhaiwork.R;

/**
 * Created by Administrator on 2016/12/24.
 */

public class ContactFragment extends Fragment implements ContactRecyclerViewAdapter.OnContactItemClickListener {
    private static final String TAG = "ContactFragment";
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private ContactRecyclerViewAdapter mContactRecyclerViewAdapter;

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

        initView();
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
        mRecyclerView = (RecyclerView) getView().findViewById(R.id.contact_recyclerView);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(getActivity()));
        mContactRecyclerViewAdapter = new ContactRecyclerViewAdapter();
        mRecyclerView.setAdapter(mContactRecyclerViewAdapter);
        mContactRecyclerViewAdapter.setOnContactItemClickListener(this);
    }

    @Override
    public void onContactItemClick(int position, String userName) {
        Intent contactDetail = new Intent(getActivity(), ContactDetailActivity.class);
        contactDetail.putExtra("user_name", userName);
        startActivity(contactDetail);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.i(TAG, "UserVisible:" + isVisibleToUser);
    }
}
