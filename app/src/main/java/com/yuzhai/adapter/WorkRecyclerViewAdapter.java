package com.yuzhai.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.yuzhai.bean.responseBean.SimpleOrderByTypeBean;
import com.yuzhai.config.IPConfig;
import com.yuzhai.yuzhaiwork.R;

import java.util.List;

/**
 * Created by Administrator on 2016/10/3.
 */
public class WorkRecyclerViewAdapter extends RecyclerView.Adapter<WorkRecyclerViewHolder> {
    private Context mContext;
    private Fragment mWorkFragment;
    private List<SimpleOrderByTypeBean.SimpleOrderBean> mOrders;
    private OnWorkItemClickListener mOnWorkItemClickListener;

    /*测试代码*/
//    private List<Map<String, Object>> mData;
    /*测试代码*/

//    public WorkRecyclerViewAdapter(Context context, List<Map<String, Object>> orders) {
//        this.mContext = context;
//        this.mData = orders;
//    }

    public WorkRecyclerViewAdapter(Fragment fragment, List<SimpleOrderByTypeBean.SimpleOrderBean> orders) {
        this.mWorkFragment = fragment;
        this.mContext = fragment.getContext();
        this.mOrders = orders;
    }

    public void setOnWorkItemClickListener(OnWorkItemClickListener onWorkItemClickListener) {
        this.mOnWorkItemClickListener = onWorkItemClickListener;
    }

    @Override
    public WorkRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WorkRecyclerViewHolder(LayoutInflater
                .from(mContext)
                .inflate(R.layout.category_work_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(WorkRecyclerViewHolder holder, final int position) {
            /*测试代码*/
//        holder.mTitle.setText((String) mData.get(position).get("title"));
//        holder.mDate.setText((String) mData.get(position).get("date"));
//        holder.mDeadline.setText((String) mData.get(position).get("limit"));
//        holder.mReward.setText((String) mData.get(position).get("price"));
//        holder.mImage.setImageResource((Integer) mData.get(position).get("image"));
            /*测试代码*/

            /*正常代码*/
        holder.mTitle.setText(mOrders.get(position).getTitle());
        holder.mDate.setText(mOrders.get(position).getDate());
        holder.mDeadline.setText(mOrders.get(position).getDeadline());
        holder.mReward.setText(mOrders.get(position).getReward());
        if (null != mOrders.get(position).getPicture() && 0 != mOrders.get(position).getPicture().size()) {
            Glide.with(mWorkFragment)
                    .load(IPConfig.image_addressPrefix + "/" + mOrders.get(position).getPicture().get(0).getImage())
                    .placeholder(R.drawable.default_image)
                    .error(R.drawable.default_image)
                    .into(holder.mImage);
        } else {
            holder.mImage.setImageResource(R.drawable.default_image);
        }
            /*正常代码*/

//        imageLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                    /*测试代码*/
////                insertImages(imageLayout, (List<Integer>) mData.get(position).get("images"));
//                    /*测试代码*/
//
//                    /*正常代码*/
////                    insertImages(imageLayout, mOrders.get(position).getPicture());
//                    /*正常代码*/
//                imageLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//            }
//        });
//
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnWorkItemClickListener) {
                    mOnWorkItemClickListener.onWorkItemClick(position);
                }
            }
        });
    }

//    private void insertImages(ViewGroup parent, List<Integer> imagesId) {
//        parent.removeAllViews();
//        int imageCount = imagesId.size();
//        int imageMargin = 15;
//        int imageHeight = 600;
//        int parentWidth = parent.getMeasuredWidth();
//
//        //获取需要显示的图片数量
//        if (imageCount < 0) {
//            throw new IllegalArgumentException("The image count can not less than 0");
//        } else if (imageCount > 3) {
//            //如果图片数量大于3，只显示3张
//            imageCount = 3;
//        }
//
//        //填充图片
//        for (int i = 0; i < imageCount; i++) {
//            //计算出每张图片的宽度
//            int eachWidth = (parentWidth - (imageCount - 1) * imageMargin) / imageCount;
//
//            ImageView imageView = new ImageView(mContext);
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(eachWidth, imageHeight / imageCount);
//            if (i != imageCount - 1) {
//                params.setMargins(0, 0, imageMargin, 0);
//            }
//
//            imageView.setLayoutParams(params);
//            imageView.setImageResource(imagesId.get(i));
//            parent.addView(imageView);
//        }
//    }

//    private void insertImages(ViewGroup parent, List<SimpleOrderByTypeBean.SimpleOrderBean.PicturesBean> imagePaths) {
//        int imageCount = imagePaths.size();
//        int imageMargin = 15;
//        int imageHeight = 600;
//        int parentWidth = parent.getMeasuredWidth();
//
//        //获取需要显示的图片数量
//        if (imageCount < 0) {
//            throw new IllegalArgumentException("The image count can not less than 0");
//        } else if (imageCount > 3) {
//            //如果图片数量大于3，只显示3张
//            imageCount = 3;
//        }
//
//        //填充图片
//        for (int i = 0; i < imageCount; i++) {
//            //计算出每张图片的宽度
//            int eachWidth = (parentWidth - (imageCount - 1) * imageMargin) / imageCount;
//
//            ImageView imageView = new ImageView(mContext);
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(eachWidth, imageHeight / imageCount);
//            if (i != imageCount - 1) {
//                params.setMargins(0, 0, imageMargin, 0);
//            }
//
//            ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView, R.drawable.default_image, R.drawable.default_image);
//            mImageLoader.get(IPConfig.image_addressPrefix + "/" + imagePaths.get(i).getImage(), listener, eachWidth, imageHeight);
//            Log.i("image_address", IPConfig.image_addressPrefix + "/" + imagePaths.get(i).getImage());
//
//            imageView.setLayoutParams(params);
//            parent.addView(imageView);
//        }
//    }

    @Override
    public int getItemCount() {
        /*正常代码*/
        return mOrders.size();
        /*正常代码*/

        /*测试代码*/
//        return mData.size();
        /*测试代码*/
    }

    public interface OnWorkItemClickListener {
        void onWorkItemClick(int position);
    }
}
