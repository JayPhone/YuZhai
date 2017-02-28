package com.yuzhai.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yuzhai.activity.ShowImageActivity;
import com.yuzhai.bean.responseBean.OrderAcceptedDetailBean;
import com.yuzhai.bean.responseBean.OrderAppliedDetailBean;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.http.IPConfig;
import com.yuzhai.util.JsonUtil;
import com.yuzhai.view.CustomViewFlipper;
import com.yuzhai.view.IndicatedViewFlipper;
import com.yuzhai.yuzhaiwork.R;

import java.util.ArrayList;
import java.util.List;

import static com.yuzhai.activity.WorkDetailActivity.IMAGE_URL;

/**
 * Created by 35429 on 2017/2/21.
 */

public class OrderAppliedDetailFragment extends Fragment {
    private TextView mTitle;
    private TextView mReward;
    private TextView mOrderId;
    private TextView mStatus;
    private TextView mDeadline;
    private TextView mDate;
    private TextView mTel;
    private TextView mDescription;
    private IndicatedViewFlipper mImageFlipper;

    private OrderAppliedDetailBean.OrderInfoBean mOrder;
    private static final String ORDER = "order";

    public static OrderAppliedDetailFragment newInstance(String order) {
        Bundle args = new Bundle();
        args.putString(ORDER, order);
        OrderAppliedDetailFragment fragment = new OrderAppliedDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 测试方法
     */
    private void testMethod() {
        List<Integer> imagesList = new ArrayList<>();
        imagesList.add(R.drawable.test1);
        imagesList.add(R.drawable.test2);
        imagesList.add(R.drawable.test3);
        imagesList.add(R.drawable.test4);
        mImageFlipper.setFlipperImageResources(imagesList);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_applied_detail, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initViews();
        if (CustomApplication.isConnect) {
            initData();
        } else {
            testMethod();
        }
    }

    private void initViews() {
        mOrderId = (TextView) getView().findViewById(R.id.id);
        mTitle = (TextView) getView().findViewById(R.id.title);
        mReward = (TextView) getView().findViewById(R.id.price);
        mStatus = (TextView) getView().findViewById(R.id.status);
        mDeadline = (TextView) getView().findViewById(R.id.deadline);
        mDate = (TextView) getView().findViewById(R.id.date);
        mTel = (TextView) getView().findViewById(R.id.tel);
        mDescription = (TextView) getView().findViewById(R.id.description);
        mImageFlipper = (IndicatedViewFlipper) getView().findViewById(R.id.image_flipper);
        mImageFlipper.setDisallowInterceptViewGroup((ViewGroup) mImageFlipper.getParent().getParent().getParent().getParent());
        mImageFlipper.setOnItemClickListener(new CustomViewFlipper.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (CustomApplication.isConnect) {
                    Intent showImage = new Intent(getActivity(), ShowImageActivity.class);
                    showImage.putExtra(IMAGE_URL, mOrder.getPictures().get(position).getImage());
                    Log.i("url", mOrder.getPictures().get(position).getImage());
                    startActivity(showImage);
                }
            }
        });
    }

    /**
     * 初始化组件数据
     */
    private void initData() {
        mOrder = JsonUtil.decodeByGson(getArguments().getString(ORDER), OrderAppliedDetailBean.class).getDetailedOrder();
        updateData(mOrder);
    }

    /**
     * 更新数据
     *
     * @param detailOrder
     */
    public void updateData(final OrderAppliedDetailBean.OrderInfoBean detailOrder) {
        //设置订单号
        mOrderId.setText(detailOrder.getOrderID());
        //设置订单标题
        mTitle.setText(detailOrder.getTitle());
        //设置订单状态
        mStatus.setText(detailOrder.getStatus());
        //设置订单期限
        mDeadline.setText(detailOrder.getDeadline());
        //设置订单发布日期
        mDate.setText(detailOrder.getDate());
        //设置发布方联系电话
        mTel.setText(detailOrder.getTel());
        //设置订单详细描述
        mDescription.setText(detailOrder.getDescription());
        //设置订单金额
        mReward.setText(detailOrder.getReward());
        //设置订单图片
        setFlipperImages(detailOrder.getPictures());
    }

    /**
     * 设置imageFlipper的图片
     *
     * @param imageList
     */
    private void setFlipperImages(List<OrderAppliedDetailBean.OrderInfoBean.PicturesBean> imageList) {
        List<String> imagePaths = new ArrayList<>();
        for (int i = 0; i < imageList.size(); i++) {
            imagePaths.add(imageList.get(i).getImage());
        }
        mImageFlipper.setFlipperImageUrls(IPConfig.image_addressPrefix, imagePaths);
    }
}
