package com.yuzhai.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yuzhai.adapter.ContactRecyclerViewAdapter;
import com.yuzhai.recyclerview.DividerGridItemDecoration;
import com.yuzhai.yuzhaiwork.R;

/**
 * Created by Administrator on 2016/12/24.
 */

public class ContactFragment extends Fragment {
    private TextView mTitle;
    private RecyclerView mRecyclerView;
    private ContactRecyclerViewAdapter mContactRecyclerViewAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mTitle = (TextView) getView().findViewById(R.id.title_text);
        mTitle.setText("最近联系");

        mRecyclerView = (RecyclerView) getView().findViewById(R.id.contact_recyclerView);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(getActivity()));
        mContactRecyclerViewAdapter = new ContactRecyclerViewAdapter();
        mRecyclerView.setAdapter(mContactRecyclerViewAdapter);
    }
}
