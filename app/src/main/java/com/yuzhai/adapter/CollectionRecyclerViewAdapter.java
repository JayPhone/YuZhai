package com.yuzhai.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuzhai.yuzhaiwork.R;

/**
 * Created by Administrator on 2016/12/12.
 */

public class CollectionRecyclerViewAdapter extends RecyclerView.Adapter<CollectionRecyclerViewHolder> {
    private Context mContext;

    /**
     * 测试数据
     */
    private String[] titles = new String[]{
            "帮我做一个很厉害的APP，记住，是很厉害的，普通厉害的不要!!",
            "帮我做一个很厉害的APP，记住，是很厉害的，普通厉害的不要!!",
            "帮我做一个很厉害的APP，记住，是很厉害的，普通厉害的不要!!",
            "帮我做一个很厉害的APP，记住，是很厉害的，普通厉害的不要!!",
            "帮我做一个很厉害的APP，记住，是很厉害的，普通厉害的不要!!",
            "帮我做一个很厉害的APP，记住，是很厉害的，普通厉害的不要!!",
            "帮我做一个很厉害的APP，记住，是很厉害的，普通厉害的不要!!"
    };

    private String[] dates = new String[]{"2016-07-14", "2016-07-14", "2016-07-14", "2016-07-14", "2016-07-14", "2016-07-14", "2016-07-14"};

    private int[] typeImages = new int[]{R.drawable.test1, R.drawable.test2, R.drawable.test3, R.drawable.test4, R.drawable.test1, R.drawable.test2, R.drawable.test4};

    private String[] type = new String[]{"软件IT", "音乐制作", "平面设计", "视频拍摄", "游戏研发", "文案撰写", "金融会计"};

    public CollectionRecyclerViewAdapter(Context context) {
        mContext = context;
    }

    @Override
    public CollectionRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CollectionRecyclerViewHolder collectionRecyclerViewHolder = new CollectionRecyclerViewHolder(LayoutInflater.from(mContext).inflate(R.layout.collection_item_layout, parent, false));
        return collectionRecyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(CollectionRecyclerViewHolder holder, int position) {
        holder.mImage.setImageResource(typeImages[position]);
        holder.mType.setText(type[position]);
        holder.mTitle.setText(titles[position]);
        holder.mDate.setText(dates[position]);
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return typeImages.length;
    }
}
