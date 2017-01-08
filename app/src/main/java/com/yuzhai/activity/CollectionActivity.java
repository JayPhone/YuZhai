package com.yuzhai.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.yuzhai.adapter.CollectionRecyclerViewAdapter;
import com.yuzhai.yuzhaiwork.R;

/**
 * Created by Administrator on 2016/11/7.
 */

public class CollectionActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        mToolbar = (Toolbar) findViewById(R.id.collection_toolbar);
        mToolbar.setNavigationIcon(R.drawable.back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolbar.setTitle("我的收藏");

        mRecyclerView = (RecyclerView) findViewById(R.id.collection_recyclerView);
        mRecyclerView.setAdapter(new CollectionRecyclerViewAdapter(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
