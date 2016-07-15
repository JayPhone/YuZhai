package com.yuzhai.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.yuzhai.yuzhaiwork.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/13.
 */
public class PublishFragment extends Fragment {
    private Activity mainActivity;
    private Spinner typeSpinner;
    private Spinner dateSpinner;
    private String[] typeTexts = new String[]{"软件IT", "音乐制作", "平面设计", "视频拍摄", "游戏研发", "文案撰写", "金融会计"};
    private String[] dateTexts = new String[]{"7天", "15天", "30天", "365天"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainActivity = getActivity();
        return inflater.inflate(R.layout.fragment_publish, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initTypeSpinner();
        initDateSpinner();
    }

    public void initTypeSpinner() {
        typeSpinner = (Spinner) mainActivity.findViewById(R.id.type_spinner);
        List<Map<String, String>> types = new ArrayList<>();
        Map<String, String> type;

        for (int i = 0; i < typeTexts.length; i++) {
            type = new HashMap<>();
            type.put("type", typeTexts[i]);
            types.add(type);
        }

        SimpleAdapter adapter = new SimpleAdapter(
                getActivity(),
                types,
                R.layout.publish_spinner_item_layout,
                new String[]{"type"},
                new int[]{R.id.type_item}
        );
        typeSpinner.setAdapter(adapter);
    }

    public void initDateSpinner() {
        dateSpinner = (Spinner) mainActivity.findViewById(R.id.date_spinner);
        List<Map<String, String>> types = new ArrayList<>();
        Map<String, String> type;

        for (int i = 0; i < dateTexts.length; i++) {
            type = new HashMap<>();
            type.put("type", dateTexts[i]);
            types.add(type);
        }

        SimpleAdapter adapter = new SimpleAdapter(
                getActivity(),
                types,
                R.layout.publish_spinner_item_layout,
                new String[]{"type"},
                new int[]{R.id.type_item}
        );
        dateSpinner.setAdapter(adapter);
    }
}
