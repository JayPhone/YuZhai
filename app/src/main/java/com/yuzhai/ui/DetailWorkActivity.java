package com.yuzhai.ui;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.yuzhai.config.IPConfig;
import com.yuzhai.entry.responseBean.OrderPublishedBean;
import com.yuzhai.fragment.OrderPublishedFragment;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.http.CommonRequest;
import com.yuzhai.http.ParamsGenerateUtil;
import com.yuzhai.http.RequestQueueSingleton;
import com.yuzhai.util.JsonUtil;
import com.yuzhai.util.TypeUtil;
import com.yuzhai.view.UnRepeatToast;
import com.yuzhai.yuzhaiwork.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/15.
 */
public class DetailWorkActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView backImage;
    private TextView title;
    private TextView money;
    private TextView orderId;
    private TextView status;
    private TextView date;
    private TextView limit;
    private TextView tel;
    private TextView detailContent;
    private LinearLayout pictureLayout;
    private Button acceptButton;

    private CustomApplication mCustomApplication;
    private RequestQueue mRequestQueue;
    private String mJsonData;
    private OrderPublishedBean.OrderBean mOrderData;
    private List<OrderPublishedBean.OrderBean.PicturesBean> mPicPaths;
    private final String COOKIE = "cookie";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_detail);
        mCustomApplication = (CustomApplication) getApplication();
        mRequestQueue = RequestQueueSingleton.getInstance(this).getRequestQueue();

        //获取订单数据
        mJsonData = getIntent().getStringExtra(OrderPublishedFragment.DATA);
        mOrderData = JsonUtil.decodeByGson(mJsonData, OrderPublishedBean.OrderBean.class);
        initViews();
    }

    public void initViews() {
        backImage = (ImageView) findViewById(R.id.back_image);
        title = (TextView) findViewById(R.id.title);
        title.setText(mOrderData.getPublish().getTitle());
        money = (TextView) findViewById(R.id.money);
        money.setText(mOrderData.getPublish().getMoney());
        orderId = (TextView) findViewById(R.id.order_id);
        orderId.setText(mOrderData.getPublish().getPublishId());
        status = (TextView) findViewById(R.id.status);
        status.setText(mOrderData.getPublish().getCurrentstatus());
        date = (TextView) findViewById(R.id.date);
        date.setText(mOrderData.getPublish().getDate());
        limit = (TextView) findViewById(R.id.limit);
        limit.setText(mOrderData.getPublish().getDeadline());
        tel = (TextView) findViewById(R.id.tel);
        tel.setText(mOrderData.getPublish().getTel());
        detailContent = (TextView) findViewById(R.id.detail_content);
        detailContent.setText(mOrderData.getPublish().getDescript());
        pictureLayout = (LinearLayout) findViewById(R.id.pictures_layout);
        acceptButton = (Button) findViewById(R.id.accept_button);

        backImage.setOnClickListener(this);
        acceptButton.setOnClickListener(this);

        mPicPaths = mOrderData.getPictures();
        if (mPicPaths.size() != 0) {
            addImages(pictureLayout, mPicPaths);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //点击返回按钮
            case R.id.back_image:
                finish();
                break;

            //点击申请接收按钮
            case R.id.accept_button:
                sendApplyOrderRequest(mOrderData.getPublish().getPublishId());
                break;
        }
    }

    /**
     * 发送申请接收订单请求
     *
     * @param orderId 订单ID
     */
    public void sendApplyOrderRequest(String orderId) {
        //生成发送申请接收订单的参数集
        Map<String, String> params = ParamsGenerateUtil.generateApplyOrderParams(orderId);

        //创建发送申请接收订单的参数集
        CommonRequest applyOrderRequest = new CommonRequest(IPConfig.applyOrderAddress,
                generateHeaders(),
                params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String resp) {
                        Log.i("apply_order_resp", resp);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        UnRepeatToast.showToast(DetailWorkActivity.this, "服务器不务正业中");
                    }
                });

        //添加到请求队列
        mRequestQueue.add(applyOrderRequest);
    }

    /**
     * 动态添加图片到布局中
     *
     * @param picturesLayout 放置图片的布局
     * @param picPaths       图片路径集合
     */
    public void addImages(LinearLayout picturesLayout,
                          List<OrderPublishedBean.OrderBean.PicturesBean> picPaths) {
        Point p = new Point();
        WindowManager windowManager = getWindowManager();
        windowManager.getDefaultDisplay().getSize(p);
        for (int i = 0; i < picPaths.size(); i++) {
            ImageView imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((p.x - 30), 600);
            Log.i("width", (p.x - 60) + "");
            params.topMargin = 10;
            params.bottomMargin = 10;
            params.leftMargin = 15;
            params.rightMargin = 15;
            imageView.setLayoutParams(params);
            picturesLayout.addView(imageView);
            sendPictureRequest(imageView, picPaths.get(i).getPicturePath(), p.x);
        }
    }

    /**
     * 发送图片请求
     *
     * @param imageView 获取图片后显示的ImageView
     * @param path      图片的Url路径
     * @param width     图片宽度
     */
    public void sendPictureRequest(ImageView imageView, String path, int width) {
        int typeImage = TypeUtil.getTypeImage(mOrderData.getPublish().getType());
        ImageLoader imageLoader = RequestQueueSingleton.getInstance(this).getmImageLoader();
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView, typeImage, typeImage);
        imageLoader.get(IPConfig.addressPrefix + path, listener, (width - 30), 600);
    }

    /**
     * 生成请求头参数集
     *
     * @return 返回请求头参数集
     */
    public Map<String, String> generateHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put(COOKIE, mCustomApplication.getCookie());
        return headers;
    }
}
