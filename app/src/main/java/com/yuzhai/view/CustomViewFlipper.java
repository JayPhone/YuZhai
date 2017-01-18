package com.yuzhai.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.yuzhai.listener.OnFlipperGesture;
import com.yuzhai.yuzhaiwork.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/11.
 */
public class CustomViewFlipper extends ViewFlipper {
    private OnFlipListener onFlipListener;
    private OnItemClickListener onItemClickListener;
    private GestureDetector mFlipperDetector;
    private Context mContext;
    private ViewGroup mDisallowInterceptView;

    public CustomViewFlipper(Context context) {
        super(context);
    }

    public CustomViewFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        mFlipperDetector = new GestureDetector(context, new OnFlipperGesture(this));
    }

    /**
     * 设置切换监听
     *
     * @param onFlipListener
     */
    public void setOnFlipListener(OnFlipListener onFlipListener) {
        this.onFlipListener = onFlipListener;
    }

    /**
     * 设置点击监听
     *
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 使用本地资源设置图片内容
     *
     * @param imagesList
     */
    public void setImageResources(List<Integer> imagesList) {
        if (imagesList != null) {
            addImageViewsByResource(imagesList);
        }
    }

    /**
     * 使用网络Url获取图片内容
     *
     * @param prefix
     * @param imageUrlsList
     */
    public void setImageUrls(String prefix, List<String> imageUrlsList) {
        if (imageUrlsList != null) {
            List<String> tempList = new ArrayList<>();
            String tempUrl;
            for (int i = 0; i < imageUrlsList.size(); i++) {
                tempUrl = prefix + "/" + imageUrlsList.get(i);
                tempList.add(tempUrl);
            }
            addImageViewsByUrl(tempList);
        }
    }

    /**
     * 根据传入的图片数量生成相对应数量的ImageView
     *
     * @param imagesList
     */
    public void addImageViewsByResource(List<Integer> imagesList) {
        for (int i = 0; i < imagesList.size(); i++) {
            ImageView imageView = new ImageView(mContext);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(params);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageResource(imagesList.get(i));
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onItemClickListener) {
                        onItemClickListener.onItemClick(getDisplayedChild());
                    }
                }
            });
            this.addView(imageView);
        }
    }

    /**
     * 根据传入的图片Url生成相对应数量的ImageView
     *
     * @param imageUrlsList
     */
    public void addImageViewsByUrl(List<String> imageUrlsList) {
        for (int i = 0; i < imageUrlsList.size(); i++) {
            ImageView imageView = new ImageView(mContext);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(params);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            Glide.with(mContext)
                    .load(imageUrlsList.get(i))
                    .placeholder(R.drawable.default_image)
                    .error(R.drawable.default_image)
                    .into(imageView);

            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onItemClickListener) {
                        onItemClickListener.onItemClick(getDisplayedChild());
                    }
                }
            });
            this.addView(imageView);
        }
    }

    @Override
    public void showPrevious() {
        super.showPrevious();
        if (onFlipListener != null) {
            onFlipListener.onShowPrevious(this);
        }
    }

    @Override
    public void showNext() {
        super.showNext();
        if (onFlipListener != null) {
            onFlipListener.onShowNext(this);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //Log.i("viewFlipper-dispatch", ev.getAction() + "");
        //禁止父View拦截触摸事件
        if (ev.getAction() == MotionEvent.ACTION_DOWN && null != mDisallowInterceptView) {
            mDisallowInterceptView.requestDisallowInterceptTouchEvent(true);
        }

        //如果检测到的是快速甩动，则由viewFlipper去处理
        //传递触摸事件给子View，如果子View没有消费事件，则由ViewFlipper的onTouch和onTouchEvent去处理
        return mFlipperDetector.onTouchEvent(ev) || super.dispatchTouchEvent(ev);
    }

    public void setDisallowInterceptViewGroup(ViewGroup disallowInterceptViewGroup) {
        mDisallowInterceptView = disallowInterceptViewGroup;
    }

    public interface OnFlipListener {
        void onShowPrevious(CustomViewFlipper flipper);

        void onShowNext(CustomViewFlipper flipper);
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
