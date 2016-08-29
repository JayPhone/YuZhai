package com.yuzhai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuzhai.entry.responseBean.OrderByTypeBean;
import com.yuzhai.util.TypeUtil;
import com.yuzhai.yuzhaiwork.R;

import java.util.List;

/**
 * Created by Administrator on 2016/8/29.
 */
public class WorkListViewAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<OrderByTypeBean.OrderBean> mData;

    public WorkListViewAdapter(Context context, List<OrderByTypeBean.OrderBean> data) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        WorkViewHolder workViewHolder;
        if (convertView == null) {
            workViewHolder = new WorkViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.category_work_listview_item_layout, parent, false);
            workViewHolder.typeImage = (ImageView) convertView.findViewById(R.id.type_image);
            workViewHolder.title = (TextView) convertView.findViewById(R.id.name);
            workViewHolder.price = (TextView) convertView.findViewById(R.id.price);
            workViewHolder.date = (TextView) convertView.findViewById(R.id.date);
            workViewHolder.limit = (TextView) convertView.findViewById(R.id.limit);
            convertView.setTag(workViewHolder);
        } else {
            workViewHolder = (WorkViewHolder) convertView.getTag();
        }
        workViewHolder.typeImage.setImageResource(TypeUtil.getTypeImage(mData.get(position).getPublish().getType()));
        workViewHolder.title.setText(mData.get(position).getPublish().getTitle());
        workViewHolder.price.setText(mData.get(position).getPublish().getMoney());
        workViewHolder.date.setText(mData.get(position).getPublish().getDate());
        workViewHolder.limit.setText(mData.get(position).getPublish().getDeadline());
        return convertView;
    }
}
