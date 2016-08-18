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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.yuzhai.config.IPConfig;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.http.CommonRequest;
import com.yuzhai.util.BitmapCache;
import com.yuzhai.util.JsonUtil;
import com.yuzhai.yuzhaiwork.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/15.
 */
public class DetailWorkActivity extends AppCompatActivity {
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
    private CustomApplication customApplication;
    private RequestQueue requestQueue;
    private String dataString;
    private int type;
    private Map<String, Object> datas;
    private List<String> picturesPath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_detail);
        customApplication = (CustomApplication) getApplication();
        requestQueue = customApplication.getRequestQueue();
        dataString = getIntent().getStringExtra("data");
        type = getIntent().getIntExtra("type", -1);
        datas = JsonUtil.decodeResponseForJobDetail(dataString);
        picturesPath = (List<String>) datas.get("pictures");
        Log.i("pictures", picturesPath.toString());
        Log.i("datas", datas.toString());
        initViews();
    }

    public void initViews() {
        backImage = (ImageView) findViewById(R.id.back_image);
        title = (TextView) findViewById(R.id.title);
        title.setText((String) datas.get("name"));
        money = (TextView) findViewById(R.id.money);
        money.setText((String) datas.get("price"));
        orderId = (TextView) findViewById(R.id.order_id);
        orderId.setText((String) datas.get("publishId"));
        status = (TextView) findViewById(R.id.status);
        status.setText((String) datas.get("currentstatu"));
        date = (TextView) findViewById(R.id.date);
        date.setText((String) datas.get("date"));
        limit = (TextView) findViewById(R.id.limit);
        limit.setText((String) datas.get("limit"));
        tel = (TextView) findViewById(R.id.tel);
        tel.setText((String) datas.get("tel"));
        detailContent = (TextView) findViewById(R.id.detail_content);
        detailContent.setText((String) datas.get("descript"));
        pictureLayout = (LinearLayout) findViewById(R.id.pictures_layout);
        if (picturesPath.size() != 0) {
            addImages(pictureLayout, picturesPath);

        }
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        acceptButton = (Button) findViewById(R.id.accept_button);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonRequest commonRequest = new CommonRequest(Request.Method.POST, IPConfig.applyAddress, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.i("response", s);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });
                Map<String, String> params = new HashMap<>();
                params.put("orderId", (String) datas.get("publishId"));
                commonRequest.setRequestParams(params);
                Map<String, String> headers = new HashMap<>();
                headers.put("cookie", customApplication.getCookie());
                commonRequest.setRequestHeaders(headers);
                requestQueue.add(commonRequest);
            }
        });
    }

    public void addImages(LinearLayout pictures, List<String> picturesPath) {
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
            pictures.addView(imageView);
            pictureRequest(imageView, picturesPath.get(i), p.x);
        }
    }

    public void pictureRequest(ImageView imageView, String path, int width) {
        ImageLoader imageLoader = new ImageLoader(requestQueue, new BitmapCache());
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView, type, type);
        imageLoader.get(IPConfig.addressPrefix + path, listener, (width - 30), 600);
    }
}
