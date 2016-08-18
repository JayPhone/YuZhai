package com.yuzhai.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.yuzhai.config.IPConfig;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.ui.IdentityAuthenActivity;
import com.yuzhai.ui.LoginActivity;
import com.yuzhai.ui.SetUpActivity;
import com.yuzhai.ui.UserInfoActivity;
import com.yuzhai.util.BitmapCache;
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
    private TextView userName = null;

    //其他引用
    private List<Map<String, Object>> items = null;
    private CustomApplication customApplication;
    private RequestQueue requestQueue;
    private String userHeadURL;
    private String userNameStr;
    IntentFilter headFilter;
    IntentFilter nameFilter;

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
        requestQueue = customApplication.getRequestQueue();
        if (customApplication.isLogin()) {
            if (getArguments() != null) {
                if (getArguments().getString("userHead") != null) {
                    userHeadURL = getArguments().getString("userHead");
                    Log.i("userHead", userHeadURL);
                }
                if (getArguments().getString("userName") != null) {
                    userNameStr = getArguments().getString("userName");
                    Log.i("userName", userNameStr);
                }
            }
            mainView = inflater.inflate(R.layout.fragment_menu_login, container, false);
        } else {
            mainView = inflater.inflate(R.layout.fragment_menu_login_no, container, false);
        }
        return mainView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        headFilter = new IntentFilter("yzgz.broadcast.replace.head");
        nameFilter = new IntentFilter("yzgz.broadcast.replace.name");
        mainActivity.registerReceiver(headReceiver, headFilter);
        mainActivity.registerReceiver(nameReceiver, nameFilter);
        super.onActivityCreated(savedInstanceState);
        //如果已经登录
        if (customApplication.isLogin()) {
            //添加数据
            items = addItems(imageID, itemName, null);
            //获取头像组件
            userHeader = (ImageView) mainActivity.findViewById(R.id.head_image);
            if (userHeadURL != null) {
                ImageLoader imageLoader = new ImageLoader(requestQueue, new BitmapCache());
                ImageLoader.ImageListener listener = ImageLoader.getImageListener(userHeader, R.drawable.head_default, R.drawable.head_default);
                imageLoader.get(IPConfig.addressPrefix + userHeadURL, listener, 200, 200);
            }
            userHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("userPhone", customApplication.getUserPhone());
                    Intent intent = new Intent();
                    intent.setClass(mainActivity, UserInfoActivity.class);
                    if (userHeadURL != null) {
                        bundle.putString("userHead", IPConfig.addressPrefix + userHeadURL);
                    }
                    if (userNameStr != null) {
                        bundle.putString("userName", userNameStr);
                    }
                    intent.putExtras(bundle);
                    mainActivity.startActivity(intent);
                }
            });

            userName = (TextView) mainActivity.findViewById(R.id.user_name);
            if (userNameStr != null) {
                userName.setText(userNameStr);
            } else {
                userName.setText(customApplication.getUserPhone());
            }

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

        if (customApplication.isLogin()) {
            menuItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            break;
                        case 1:
                            break;
                        case 2:
                            Intent intent_inentityAuthen = new Intent();
                            intent_inentityAuthen.setClass(mainActivity, IdentityAuthenActivity.class);
                            startActivity(intent_inentityAuthen);
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

    BroadcastReceiver headReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String headPath = intent.getStringExtra("userHead");
            Log.i("path", headPath);
            ImageLoader imageLoader = new ImageLoader(requestQueue, new BitmapCache());
            ImageLoader.ImageListener listener = ImageLoader.getImageListener(userHeader, R.drawable.head_default, R.drawable.head_default);
            imageLoader.get(IPConfig.addressPrefix + headPath, listener, 200, 200);
            userHeadURL = headPath;
        }
    };

    BroadcastReceiver nameReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String tempUserName = intent.getStringExtra("userName");
            Log.i("userName", tempUserName);
            userNameStr = tempUserName;
            userName.setText(tempUserName);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        mainActivity.unregisterReceiver(headReceiver);
        mainActivity.unregisterReceiver(nameReceiver);
    }
}
