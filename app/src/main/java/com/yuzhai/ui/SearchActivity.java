package com.yuzhai.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.yuzhai.yuzhaiwork.R;

public class SearchActivity extends AppCompatActivity {
    ImageView backImage;
    ListView historyRecordListview;
    Button deleteHistoryRecord;
    String[] datas = new String[]{"数据", "数据", "数据", "数据", "数据", "数据", "数据", "数据", "数据", "数据", "数据", "数据", "数据", "数据"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        backImage = (ImageView) findViewById(R.id.back_image);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datas);
        historyRecordListview = (ListView) findViewById(R.id.history_record_listview);
        historyRecordListview.setAdapter(arrayAdapter);
        deleteHistoryRecord = (Button) findViewById(R.id.delete_history_record);
//        deleteHistoryRecord.setVisibility(View.INVISIBLE);
    }
}
