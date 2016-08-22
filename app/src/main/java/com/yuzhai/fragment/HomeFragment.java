package com.yuzhai.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.yuzhai.ui.AdvertiseActivity;
import com.yuzhai.ui.CategoryActivity;
import com.yuzhai.ui.MainActivity;
import com.yuzhai.ui.SearchActivity;
import com.yuzhai.view.CategoryGridView;
import com.yuzhai.view.PointViewFlipper;
import com.yuzhai.yuzhaiwork.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/10.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {
    private Activity mainActivity;
    //滚动面板下方的圆点面板
    private LinearLayout pointPanel;
    //滚动面板下方的圆点
    private TextView point_1, point_2, point_3, point_4;
    //圆点数组
    private TextView[] points;
    //搜索框
    private TextView searchView;
    //滚动面板的图片
    private ImageView image_1, image_2, image_3, image_4;
    //用于弹出个人信息面板
    private ImageView personImage;
    //滚动面板
    private PointViewFlipper picturePanel;
    //类别面板
    private CategoryGridView category;
    //类别面板设配器
    private SimpleAdapter categoryAdapter;
    //类别面板数据容器
    private List<Map<String, Object>> categoryData;
    //类别面板分类图片
    private int[] imageId = new int[]{R.drawable.it, R.drawable.music, R.drawable.design, R.drawable.movie, R.drawable.game, R.drawable.write, R.drawable.calculate};
    //类别面板分类标题
    private String[] categoryTexts = new String[]{"软件IT", "音乐制作", "平面设计", "视频拍摄", "游戏研发", "文案撰写", "金融会计"};

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //初始化数据
        initValues();
        MainActivity.menu.addIgnoredView(picturePanel);
    }

    private void initValues() {
        mainActivity = getActivity();
        //初始化标题栏
        initToolbar(mainActivity);
        //初始化焦点图
        initBanner(mainActivity);
        //初始化分类面板
        initCategory(mainActivity);
    }

    //初始化标题栏
    public void initToolbar(final Activity mainActivity) {
        personImage = (ImageView) mainActivity.findViewById(R.id.person_image);
        personImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.menu.showMenu();
            }
        });
        searchView = (TextView) mainActivity.findViewById(R.id.search_view);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent search_intent = new Intent();
                search_intent.setClass(mainActivity, SearchActivity.class);
                startActivity(search_intent);
            }
        });
    }

    //初始化焦点图
    public void initBanner(Activity mainActivity) {
        //初始化下面的圆点面板
        pointPanel = (LinearLayout) mainActivity.findViewById(R.id.point_panel);
        point_1 = (TextView) pointPanel.findViewById(R.id.point_1);
        point_2 = (TextView) pointPanel.findViewById(R.id.point_2);
        point_3 = (TextView) pointPanel.findViewById(R.id.point_3);
        point_4 = (TextView) pointPanel.findViewById(R.id.point_4);
        points = new TextView[]{point_1, point_2, point_3, point_4};

        //初始化焦点图面板
        picturePanel = (PointViewFlipper) mainActivity.findViewById(R.id.fli);
        picturePanel.setInAnimation(getActivity(), android.R.anim.fade_in);
        picturePanel.setOutAnimation(getActivity(), android.R.anim.fade_out);
        picturePanel.setOnFlipListener(flipListener);

        //初始化图片内容
        image_1 = (ImageView) mainActivity.findViewById(R.id.image_1);
        image_2 = (ImageView) mainActivity.findViewById(R.id.image_2);
        image_3 = (ImageView) mainActivity.findViewById(R.id.image_3);
        image_4 = (ImageView) mainActivity.findViewById(R.id.image_4);

        image_1.setImageResource(R.drawable.test1);
        image_2.setImageResource(R.drawable.test2);
        image_3.setImageResource(R.drawable.test3);
        image_4.setImageResource(R.drawable.test4);

        image_1.setOnClickListener(this);
        image_2.setOnClickListener(this);
        image_3.setOnClickListener(this);
        image_4.setOnClickListener(this);

        point_1.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.buttom_style_choose));
    }

    //初始化分类面板
    public void initCategory(Activity mainActivity) {
        category = (CategoryGridView) mainActivity.findViewById(R.id.category);
        category.setFocusable(false);
        categoryData = new ArrayList<>();
        for (int i = 0; i < imageId.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("image", imageId[i]);
            map.put("text", categoryTexts[i]);
            categoryData.add(map);
        }
        categoryAdapter = new SimpleAdapter(getActivity(), categoryData, R.layout.home_category_cell_layout, new String[]{"image", "text"}, new int[]{R.id.category_image, R.id.category_text});
        category.setAdapter(categoryAdapter);
        category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent_category = new Intent(getActivity(), CategoryActivity.class);
                intent_category.putExtra("title", position);
                startActivity(intent_category);
            }
        });
    }

    @Override
    public void onClick(View v) {
        int viewID = v.getId();
        switch (viewID) {
            case R.id.image_1:
                Intent intent_advertise1 = new Intent(getActivity(), AdvertiseActivity.class);
                startActivity(intent_advertise1);
                break;
            case R.id.image_2:
                Intent intent_advertise2 = new Intent(getActivity(), AdvertiseActivity.class);
                startActivity(intent_advertise2);
                break;
            case R.id.image_3:
                Intent intent_advertise3 = new Intent(getActivity(), AdvertiseActivity.class);
                startActivity(intent_advertise3);
                break;
            case R.id.image_4:
                Intent intent_advertise4 = new Intent(getActivity(), AdvertiseActivity.class);
                startActivity(intent_advertise4);
                break;
        }
    }

    //图片切换监听器
    private PointViewFlipper.OnFlipListener flipListener = new PointViewFlipper.OnFlipListener() {
        //图片切换到下一张
        @Override
        public void onShowPrevious(PointViewFlipper flipper) {
            int id = flipper.getDisplayedChild();
            points[id].setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.buttom_style_choose));
            for (int i = 0; i < points.length; i++) {
                if (id == i)
                    continue;
                else
                    points[i].setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.buttom_style));
            }
        }

        //图片切换到上一张
        @Override
        public void onShowNext(PointViewFlipper flipper) {
            int id = flipper.getDisplayedChild();
            points[id].setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.buttom_style_choose));
            for (int i = 0; i < points.length; i++) {
                if (id == i)
                    continue;
                else
                    points[i].setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.buttom_style));
            }
        }
    };

}
