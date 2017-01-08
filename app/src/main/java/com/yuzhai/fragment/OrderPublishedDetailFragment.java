package com.yuzhai.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.http.RequestQueueSingleton;
import com.yuzhai.view.IndicatedViewFlipper;
import com.yuzhai.yuzhaiwork.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/14.
 */

public class OrderPublishedDetailFragment extends Fragment {
    private TextView mTitle;
    private TextView mContent;
    private IndicatedViewFlipper mImages;
    private TextView mPrice;
    private TextView mOrderId;
    private TextView mStatus;
    private TextView mLimit;
    private TextView mDate;
    private TextView mTel;


    private CustomApplication mCustomApplication;
    private RequestQueue mRequestQueue;

    private List<Integer> imagesList = new ArrayList<>();

    public static OrderPublishedDetailFragment newInstance() {
//        Bundle args = new Bundle();
        OrderPublishedDetailFragment fragment = new OrderPublishedDetailFragment();
//        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_published_detail, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCustomApplication = (CustomApplication) getActivity().getApplication();
        mRequestQueue = RequestQueueSingleton.getRequestQueue(getActivity());

        imagesList.add(R.drawable.test1);
        imagesList.add(R.drawable.test2);
        imagesList.add(R.drawable.test3);
        imagesList.add(R.drawable.test4);

        initViews();
        initData();
    }

    private void initViews() {
        mTitle = (TextView) getView().findViewById(R.id.title);
        mContent = (TextView) getView().findViewById(R.id.description);
        mPrice = (TextView) getView().findViewById(R.id.price);
        mOrderId = (TextView) getView().findViewById(R.id.id);
        mStatus = (TextView) getView().findViewById(R.id.tel);
        mLimit = (TextView) getView().findViewById(R.id.deadline);
        mDate = (TextView) getView().findViewById(R.id.date);
        mTel = (TextView) getView().findViewById(R.id.call);
        mImages = (IndicatedViewFlipper) getView().findViewById(R.id.image_flipper);

        mImages.setDisallowInterceptViewGroup((ViewGroup) mImages.getParent().getParent().getParent().getParent());
//        Log.i("parent", mImages.getParent().getParent().getParent().getParent() + "");
    }

    private void initData() {
        mImages.setFlipperImageResources(imagesList);
//        mTitle.setText();
//        mContent.setText();
//        mPrice.setText();
//        mOrderId.setText();
//        mStatus.setText();
//        mLimit.setText();
//        mDate.setText();
//        mTel.setText();
    }
}
