package com.yuzhai.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuzhai.yuzhaiwork.R;

/**
 * Created by Administrator on 2016/6/10.
 */
public class OrderFragment extends Fragment {
    Activity activity;
    ImageView downImage;
    private int bmpW;// 动画图片宽度
    private int offset = 0;// 动画图片偏移量
    private TextView textView1, textView2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = getActivity();

        InitImageView();


    }

    /**
     * 初始化动画，这个就是页卡滑动时，下面的横线也滑动的效果
     */


    private void InitImageView() {
        //获取id
        downImage = (ImageView) activity.findViewById(R.id.downImage);
        bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.line).getWidth();// 获取图片宽度
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;// 获取分辨率宽度
        offset = (screenW / 2 - bmpW) / 2;// 计算偏移量
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        downImage.setImageMatrix(matrix);// 设置动画初始位置
    }


    private void IninTextView(){
        //获取id
        textView1 = (TextView)activity.findViewById(R.id.textview1);
        textView2 = (TextView)activity.findViewById(R.id.textview2);


    }
}
