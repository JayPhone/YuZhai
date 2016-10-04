package com.yuzhai.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yuzhai.view.UnRepeatToast;
import com.yuzhai.yuzhaiwork.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/3.
 */
public class WorkRecyclerViewAdapter extends RecyclerView.Adapter<WorkRecyclerViewHolder> {
    private Context mContext;
    private List<Map<String, Object>> mData;
    private String[] titles = new String[]{
            "帮我做一个很厉害的毕业设计，记住，是很厉害的，普通厉害的不要!!",
            "帮我做一个很厉害的毕业设计，记住，是很厉害的，普通厉害的不要!!",
            "帮我做一个很厉害的毕业设计，记住，是很厉害的，普通厉害的不要!!",
            "帮我做一个很厉害的毕业设计，记住，是很厉害的，普通厉害的不要!!"};
    private String[] dates = new String[]{"16-10-02", "16-10-03", "16-10-04", "16-10-05"};
    private String[] limits = new String[]{"5天", "15天", "20天", "50天"};
    private String[] prices = new String[]{"200", "300", "50", "100"};
    private int[] images = new int[]{R.drawable.test1, R.drawable.test2, R.drawable.test3, R.drawable.test4};

    public WorkRecyclerViewAdapter(Context context) {
        this.mContext = context;
        initData();
    }

    private void initData() {
        mData = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("title", titles[i]);
            map.put("date", dates[i]);
            map.put("limit", limits[i]);
            map.put("price", prices[i]);
            List<Integer> image = new ArrayList<>();
            for (int j = 0; j <= i - 1; j++) {
                image.add(images[j]);
            }
//            Log.i("data", image.toString());
            map.put("images", image);
            mData.add(map);
        }
//        Log.i("data", mData.toString());
    }

    @Override
    public WorkRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        WorkRecyclerViewHolder holder = new WorkRecyclerViewHolder(LayoutInflater.from(
                mContext).inflate(R.layout.category_work_listview_item_layout, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(WorkRecyclerViewHolder holder, final int position) {
        holder.mTitle.setText((String) mData.get(position).get("title"));
        holder.mDate.setText((String) mData.get(position).get("date"));
        holder.mLimit.setText((String) mData.get(position).get("limit"));
        holder.mPrice.setText((String) mData.get(position).get("price"));
        final LinearLayout imageLayout = holder.mImageLayout;
        imageLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                insertImages(imageLayout, (List<Object>) mData.get(position).get("images"));
                imageLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        holder.mWrapLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnRepeatToast.showToast(mContext, "点击了" + position);
            }
        });
    }

    private void insertImages(ViewGroup parent, List<Object> imagesId) {
        int imageCount = imagesId.size();
        int imageMargin = 15;
        int imageHeight = 600;
        int parentWidth = parent.getMeasuredWidth();

        //获取需要显示的图片数量
        if (imageCount < 0) {
            throw new IllegalArgumentException("The image count can not less than 0");
        } else if (imageCount > 3) {
            //如果图片数量大于3，只显示3张
            imageCount = 3;
        }

        //填充图片
        for (int i = 0; i < imageCount; i++) {
            //计算出每张图片的宽度
            int eachWidth = (parentWidth - (imageCount - 1) * imageMargin) / imageCount;

            ImageView imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(eachWidth, imageHeight / imageCount);
            if (i != imageCount - 1) {
                params.setMargins(0, 0, imageMargin, 0);
            }
            imageView.setLayoutParams(params);
            imageView.setImageResource((Integer) imagesId.get(i));
            parent.addView(imageView);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
