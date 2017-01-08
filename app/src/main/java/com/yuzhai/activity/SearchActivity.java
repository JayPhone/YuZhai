package com.yuzhai.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.yuzhai.yuzhaiwork.R;

public class SearchActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private ListView historyRecordLv;
    private Button deleteHistoryRecord;
    private String[] data = new String[]{"软件IT", "音乐制作", "平面设计", "视频拍摄", "游戏研发", "文档撰写", "金融会计"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mToolbar = (Toolbar) findViewById(R.id.search_toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
        historyRecordLv = (ListView) findViewById(R.id.history_list);
        historyRecordLv.setAdapter(arrayAdapter);
        deleteHistoryRecord = (Button) findViewById(R.id.delete_button);
    }
}
