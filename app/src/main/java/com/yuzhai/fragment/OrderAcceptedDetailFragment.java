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

public class OrderAcceptedDetailFragment extends Fragment {
    private TextView mTitle;
    private TextView mPrice;
    private TextView mStatus;
    private TextView mLimit;
    private TextView mDate;
    private TextView mTel;
    private TextView mContent;
    private IndicatedViewFlipper mImages;

    private CustomApplication mCustomApplication;
    private RequestQueue mRequestQueue;

    private List<Integer> imagesList = new ArrayList<>();

    public static OrderAcceptedDetailFragment newInstance() {
//        Bundle args = new Bundle();
        OrderAcceptedDetailFragment fragment = new OrderAcceptedDetailFragment();
//        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_accepted_detail, container, false);
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
        mPrice = (TextView) getView().findViewById(R.id.price);
        mStatus = (TextView) getView().findViewById(R.id.tel);
        mLimit = (TextView) getView().findViewById(R.id.deadline);
        mDate = (TextView) getView().findViewById(R.id.date);
        mTel = (TextView) getView().findViewById(R.id.tel);
        mContent = (TextView) getView().findViewById(R.id.description);
        mImages = (IndicatedViewFlipper) getView().findViewById(R.id.image_flipper);
        mImages.setDisallowInterceptViewGroup((ViewGroup) mImages.getParent().getParent().getParent().getParent());
    }

    private void initData() {
        mImages.setFlipperImageResources(imagesList);
    }
}
