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
import com.yuzhai.global.CustomApplication;
import com.yuzhai.http.CommonRequest;
import com.yuzhai.http.ParamsGenerateUtil;
import com.yuzhai.http.RequestQueueSingleton;
import com.yuzhai.util.BitmapCache;
import com.yuzhai.util.JsonUtil;
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

    private int[] defaultImage = new int[]{R.drawable.it, R.drawable.music, R.drawable.design, R.drawable.movie, R.drawable.game, R.drawable.write, R.drawable.calculate};
    private CustomApplication mCustomApplication;
    private RequestQueue mRequestQueue;
    private String dataString;
    private int type;
    private Map<String, Object> data;
    private List<String> picturesPath;
    private final String COOKIE = "cookie";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_detail);
        mCustomApplication = (CustomApplication) getApplication();
        mRequestQueue = RequestQueueSingleton.getInstance(this).getRequestQueue();
        dataString = getIntent().getStringExtra("data");
        type = getIntent().getIntExtra("type", -1);
        data = JsonUtil.decodeResponseForJobDetail(dataString);
        picturesPath = (List<String>) data.get("pictures");
        Log.i("pictures", picturesPath.toString());
        Log.i("datas", data.toString());
        initViews();
    }

    public void initViews() {
        backImage = (ImageView) findViewById(R.id.back_image);
        title = (TextView) findViewById(R.id.title);
        title.setText((String) data.get("name"));
        money = (TextView) findViewById(R.id.money);
        money.setText((String) data.get("price"));
        orderId = (TextView) findViewById(R.id.order_id);
        orderId.setText((String) data.get("publishId"));
        status = (TextView) findViewById(R.id.status);
        status.setText((String) data.get("currentstatu"));
        date = (TextView) findViewById(R.id.date);
        date.setText((String) data.get("date"));
        limit = (TextView) findViewById(R.id.limit);
        limit.setText((String) data.get("limit"));
        tel = (TextView) findViewById(R.id.tel);
        tel.setText((String) data.get("tel"));
        detailContent = (TextView) findViewById(R.id.detail_content);
        detailContent.setText((String) data.get("descript"));
        pictureLayout = (LinearLayout) findViewById(R.id.pictures_layout);
        acceptButton = (Button) findViewById(R.id.accept_button);

        if (picturesPath.size() != 0) {
            addImages(pictureLayout, picturesPath);

        }

        backImage.setOnClickListener(this);
        acceptButton.setOnClickListener(this);
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
                sendApplyOrderRequest((String) data.get("publishId"));
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
                        UnRepeatToast.showToast(DetailWorkActivity.this, "服务器睡着了");
                    }
                });

        //添加到请求队列
        mRequestQueue.add(applyOrderRequest);
    }

    /**
     * 动态添加图片到布局中
     *
     * @param picturesLayout 放置图片的布局
     * @param picturesPath   图片路径集合
     */
    public void addImages(LinearLayout picturesLayout, List<String> picturesPath) {
        Point p = new Point();
        WindowManager windowManager = getWindowManager();
        windowManager.getDefaultDisplay().getSize(p);
        for (int i = 0; i < picturesPath.size(); i++) {
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
            sendPictureRequest(imageView, picturesPath.get(i), p.x);
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
        ImageLoader imageLoader = new ImageLoader(mRequestQueue, new BitmapCache());
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView, type, type);
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
