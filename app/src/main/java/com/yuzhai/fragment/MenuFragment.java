package com.yuzhai.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.yuzhai.global.CustomApplication;
import com.yuzhai.ui.LoginActivity;
import com.yuzhai.ui.SetUpActivity;
import com.yuzhai.ui.UserInfoActivity;
import com.yuzhai.yuzhaiwork.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/20.
 */
public class MenuFragment extends Fragment {
    //组件引用
    private Activity mainActivity = null;
    private ListView menuItems = null;
    private View mainView = null;
    private Button login_register = null;
    private ImageView userHeader = null;
    private TextView verifyRealName = null;

    //其他引用
    private List<Map<String, Object>> items = null;
    private CustomApplication customApplication;

    //数据
    int[] imageID = new int[]{
            R.drawable.it,
            R.drawable.music,
            R.drawable.design,
            R.drawable.movie,
            R.drawable.game,
            R.drawable.write
    };

    String[] itemName = new String[]{
            "收藏",
            "简历投放",
            "实名认证",
            "软件设置",
            "关于我们",
            "退出系统"
    };

    //按登录与否加载布局
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //获取fragment的宿主Activity
        mainActivity = getActivity();
        customApplication = (CustomApplication) mainActivity.getApplication();
        if (customApplication.isLOGIN())
            mainView = inflater.inflate(R.layout.fragment_menu_login, container, false);
        else
            mainView = inflater.inflate(R.layout.fragment_menu_login_no, container, false);
        return mainView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //如果已经登录
        if (customApplication.isLOGIN()) {
            //添加数据
            items = addItems(imageID, itemName, null);
            //获取头像组件
            userHeader = (ImageView) mainActivity.findViewById(R.id.head_image);
            userHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(mainActivity, UserInfoActivity.class);
                    mainActivity.startActivity(intent);
                }
            });
            verifyRealName = (TextView) mainActivity.findViewById(R.id.verify_real_name);
            verifyRealName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        } else {
            //添加数据
            items = addItems(imageID, itemName, new int[]{1, 2});
            //获取登录和注册按钮
            login_register = (Button) mainActivity.findViewById(R.id.menu_login_register);
            login_register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(mainActivity, LoginActivity.class);
                    mainActivity.startActivity(intent);
                }
            });
        }
        initListView(items);
    }

    public void initListView(List<Map<String, Object>> items) {
        //给ListView设置设配器
        menuItems = (ListView) mainActivity.findViewById(R.id.menu_items);
        SimpleAdapter adapter = new SimpleAdapter(mainActivity,
                items,
                R.layout.menu_items_listview_layout,
                new String[]{"icon", "text"},
                new int[]{R.id.item_image, R.id.item_text}
        );
        menuItems.setAdapter(adapter);

        if (customApplication.isLOGIN()) {
            menuItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            break;
                        case 1:
                            break;
                        case 2:
                            break;
                        case 3:
                            Intent intent_setUp = new Intent();
                            intent_setUp.setClass(mainActivity, SetUpActivity.class);
                            startActivity(intent_setUp);
                            break;
                        case 4:
                            break;
                        case 5:
                            break;
                    }
                }
            });
        } else {
            menuItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            break;
                        case 1:
                            Intent intent_setUp = new Intent();
                            intent_setUp.setClass(mainActivity, SetUpActivity.class);
                            startActivity(intent_setUp);
                            break;
                        case 2:
                            break;
                        case 3:
                            break;
                    }
                }
            });
        }
    }

    //往ListView添加数据
    public List<Map<String, Object>> addItems(int[] images, String[] texts, int[] ignoreItem) {
        List<Map<String, Object>> datas = items = new ArrayList<>();
        boolean flag = false;
        for (int i = 0; i < images.length; i++) {
            if (ignoreItem != null) {
                //查找忽略数组中的项，如果找到，把flag置为true
                for (int j = 0; j < ignoreItem.length; j++) {
                    if (i == ignoreItem[j]) {
                        flag = true;
                        break;
                    }
                }
                //如果flag为true，跳出当前循环并讲flag置为false
                if (flag == true) {
                    flag = false;
                    continue;
                }
            }
            HashMap data = new HashMap();
            data.put("icon", imageID[i]);
            data.put("text", itemName[i]);
            datas.add(data);
        }
        return datas;
    }
}
