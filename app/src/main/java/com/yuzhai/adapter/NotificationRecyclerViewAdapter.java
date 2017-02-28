package com.yuzhai.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuzhai.bean.innerBean.NotificationDBBean;
import com.yuzhai.yuzhaiwork.R;

import java.util.List;

import static com.xiaomi.push.service.am.m;
import static com.xiaomi.push.service.am.p;
import static org.apache.http.HttpHeaders.ACCEPT;

/**
 * Created by 35429 on 2017/2/19.
 */

public class NotificationRecyclerViewAdapter extends RecyclerView.Adapter<NotificationRecyclerViewHolder> {
    private List<NotificationDBBean> mNotificationDBBeenList;
    private OnNotificationClickListener mOnNotificationClickListener;
    private final static String PUBLISH = "publish";
    private final static String RECEIVE = "receive";
    private final static String APPLY = "apply";

    /**
     * 测试数据
     */
//    private String[] notificationTime = new String[]{"2017-02-17", "2017-02-18", "2017-02-19", "2017-02-20"};
//    private int[] notificationImages = new int[]{R.drawable.published, R.drawable.accepted, R.drawable.accepted, R.drawable.published};
//    private String[] notificationMessage = new String[]
//            {
//                    "您有新的订单信息，请点击查看并进行后续处理",
//                    "您有新的订单信息，请点击查看并进行后续处理",
//                    "您有新的订单信息，请点击查看并进行后续处理",
//                    "您有新的订单信息，请点击查看并进行后续处理"};
    public NotificationRecyclerViewAdapter(List<NotificationDBBean> notificationDBBeanList) {
        mNotificationDBBeenList = notificationDBBeanList;
    }

    public void setOnNotificationClickListener(OnNotificationClickListener onNotificationClickListener) {
        mOnNotificationClickListener = onNotificationClickListener;
    }

    @Override
    public NotificationRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NotificationRecyclerViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.notification_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(NotificationRecyclerViewHolder holder, final int position) {
        if (mNotificationDBBeenList.get(position).getType().equals(PUBLISH)) {
            holder.notificationImage.setImageResource(R.drawable.published);
        } else if (mNotificationDBBeenList.get(position).getType().equals(RECEIVE)) {
            holder.notificationImage.setImageResource(R.drawable.accepted);
        } else if (mNotificationDBBeenList.get(position).getType().equals(APPLY)) {
            holder.notificationImage.setImageResource(R.drawable.applied);
        }
        holder.notificationTitle.setText(mNotificationDBBeenList.get(position).getTitle());
        holder.timeText.setText(mNotificationDBBeenList.get(position).getDate());
        holder.notificationMessage.setText(mNotificationDBBeenList.get(position).getDescription());
        holder.wrapCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnNotificationClickListener != null) {
                    mOnNotificationClickListener.onNotificationClick(
                            mNotificationDBBeenList.get(position).getType(),
                            mNotificationDBBeenList.get(position).getOrderId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNotificationDBBeenList.size();
    }

    public interface OnNotificationClickListener {
        void onNotificationClick(String type, String orderId);
    }
}
