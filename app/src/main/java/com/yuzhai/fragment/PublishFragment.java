package com.yuzhai.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yuzhai.activity.LoginAndRegisterActivity;
import com.yuzhai.bean.requestBean.PublishBean;
import com.yuzhai.bean.responseBean.PublishOrderRespBean;
import com.yuzhai.http.IPConfig;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.http.FileUploadRequest;
import com.yuzhai.http.ParamsGenerateUtil;
import com.yuzhai.http.RequestQueueSingleton;
import com.yuzhai.util.BitmapUtil;
import com.yuzhai.util.FileUtil;
import com.yuzhai.util.GetPathUtil;
import com.yuzhai.util.JsonUtil;
import com.yuzhai.view.UnRepeatToast;
import com.yuzhai.yuzhaiwork.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static com.a.a.b.a.i.im;

/**
 * Created by Administrator on 2016/7/13.
 */
public class PublishFragment extends Fragment implements View.OnTouchListener, View.OnClickListener {
    private static final String TAG = "PublishFragment";
    private static final String TYPE = "type";

    //全局变量
    private CustomApplication mCustomApplication;
    private RequestQueue mRequestQueue;

    //组件引用
    private Toolbar mToolbar;
    private EditText mTitleEdit;
    private EditText mDescriptionEdit;
    private LinearLayout mImagesPreviewLayout;
    private ImageView mUploadImageView;
    private Spinner mTypeSpinner;
    private Spinner mDeadlineSpinner;
    private EditText mTelEdit;
    private EditText mRewardEdit;
    private Button mPublishButton;
    private ProgressDialog mProgressDialog;

    //数据引用
    private String[] typeTexts = new String[]{"请选择项目类型", "软件IT", "音乐制作", "平面设计", "视频拍摄", "游戏研发", "文案撰写", "金融会计"};
    private String[] dateTexts = new String[]{"请选择预期时长", "7天", "15天", "30天", "365天"};

    private String imagePath;
    private List<String> imagePathsList;
    private List<File> imageFilesList;
    private PublishBean publishBean;
    private int imageMargin = 15;

    //Activity请求码
    private final int CAMERA_REQUEST = 1;
    private final int IMAGE_PICK_REQUEST = 2;

    public static PublishFragment newInstance() {

        Bundle args = new Bundle();

        PublishFragment fragment = new PublishFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_publish, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCustomApplication = (CustomApplication) getActivity().getApplication();
        mRequestQueue = RequestQueueSingleton.getRequestQueue(getActivity());
        imageFilesList = new ArrayList<>();
        imagePathsList = new ArrayList<>();
        initViews();
    }

    public void initViews() {
        //查找引用
        mToolbar = (Toolbar) getView().findViewById(R.id.publish_toolbar);
        mToolbar.setTitle("发布需求");
        mToolbar.inflateMenu(R.menu.publish_menu);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer);
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        mTitleEdit = (EditText) getView().findViewById(R.id.need_title);
        mDescriptionEdit = (EditText) getView().findViewById(R.id.need_content);
        mImagesPreviewLayout = (LinearLayout) getView().findViewById(R.id.images_preview);
        mUploadImageView = (ImageView) getView().findViewById(R.id.upload_image);
        mTelEdit = (EditText) getView().findViewById(R.id.contact);
        mRewardEdit = (EditText) getView().findViewById(R.id.reward);
        mPublishButton = (Button) getView().findViewById(R.id.publish_button);

        //设置触摸监听器
        mDescriptionEdit.setOnTouchListener(this);

        //设置点击监听器
        mUploadImageView.setOnClickListener(this);
        mPublishButton.setOnClickListener(this);

        //初始化两个选择器
        initTypeSpinner();
        initDateSpinner();
    }

    //对话框显示图片的添加方式
    public void addImageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("添加图片");
        builder.setItems(new String[]{"手机相机添加", "本地相册选择"},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                //每次只能上传5张图片
                                if (imagePathsList.size() < 5) {
                                    Log.i(TAG, "count" + imagePathsList.size() + "");
                                    checkCameraPermission();
                                } else {
                                    UnRepeatToast.showToast(getActivity(), "一次最多只能上传5张图片");
                                }
                                break;

                            case 1:
                                if (imagePathsList.size() < 5) {
                                    Log.i(TAG, "count" + imagePathsList.size() + "");
                                    showImagePick();
                                } else {
                                    UnRepeatToast.showToast(getActivity(), "一次最多只能上传5张图片");
                                }
                                break;
                        }
                    }
                });
        builder.create().show();
    }

    /**
     * 检测是否拥有相机权限
     */
    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);
        } else {
            showCamera();
        }
    }

    /**
     * 打开相机
     */
    public void showCamera() {
        imagePath = FileUtil.createFilePath(FileUtil.NEED_IMAGE);
        Log.i("path", imagePath);
        Intent intent_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent_camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(imagePath)));
        startActivityForResult(intent_camera, CAMERA_REQUEST);
    }

    /**
     * 打开图片选择器
     */
    public void showImagePick() {
        Intent intent_imagePick = new Intent(Intent.ACTION_PICK);
        intent_imagePick.setType("image/*");
        //是否按比例缩放
        intent_imagePick.putExtra("scale", "true");
        //图片输出大小
        intent_imagePick.putExtra("outputX", 640);
        intent_imagePick.putExtra("outputY", 640);
        //图片输出格式
        intent_imagePick.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent_imagePick.putExtra("return-data", false);
        startActivityForResult(intent_imagePick, IMAGE_PICK_REQUEST);
    }

    /**
     * 当用户拒绝授予相机权限时调用
     */
    private void showOnDenienCameraPermission() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("权限申请");
        builder.setMessage("请到设置 - 应用 - 御宅工作 - 权限中开启相机权限，以正常使用头像上传，需求图片上传等功能");
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent_getPermission = new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent_getPermission);
            }
        });
        builder.setNegativeButton("取消", null).create().show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    int imageLayoutWidth = mImagesPreviewLayout.getMeasuredWidth();
                    int imageWidth = (imageLayoutWidth - (imageMargin * 6)) / 5;
                    int imageHeight = imageWidth;
                    ImageView needImage = new ImageView(getActivity());
                    needImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(imageWidth, imageHeight);
                    params.setMargins(imageMargin, 0, 0, 0);
                    needImage.setLayoutParams(params);
                    mImagesPreviewLayout.addView(needImage);
                    needImage.setImageBitmap(BitmapUtil.decodeSampledBitmapFromFile(imagePath, imageWidth, imageHeight));
                    //添加当前图片路径到List
                    imagePathsList.add(imagePath);
                    Log.i(TAG, "paths" + imagePathsList.toString());
                }
                break;
            case IMAGE_PICK_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    int imageLayoutWidth = mImagesPreviewLayout.getMeasuredWidth();
                    int imageWidth = (imageLayoutWidth - (imageMargin * 6)) / 5;
                    int imageHeight = imageWidth;
                    ImageView needImage = new ImageView(getActivity());
                    needImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(imageWidth, imageHeight);
                    params.setMargins(imageMargin, 0, 0, 0);
                    needImage.setLayoutParams(params);
                    mImagesPreviewLayout.addView(needImage);
                    Uri uri = data.getData();
                    imagePath = GetPathUtil.getImageAbsolutePath(getActivity(), uri);
                    needImage.setImageBitmap(BitmapUtil.decodeSampledBitmapFromFile(imagePath, imageWidth, imageHeight));
                    //添加当前图片路径到List
                    imagePathsList.add(imagePath);
                    Log.i(TAG, "paths:" + imagePathsList.toString());
                }
                break;
        }
    }

    /**
     * 初始化类型选择器
     */
    public void initTypeSpinner() {
        mTypeSpinner = (Spinner) getView().findViewById(R.id.type_spinner);
        List<Map<String, String>> types = new ArrayList<>();
        Map<String, String> type;

        for (int i = 0; i < typeTexts.length; i++) {
            type = new HashMap<>();
            type.put(TYPE, typeTexts[i]);
            types.add(type);
        }

        SimpleAdapter adapter = new SimpleAdapter(
                getActivity(),
                types,
                R.layout.publish_spinner_item_layout,
                new String[]{TYPE},
                new int[]{R.id.type_item}
        );
        mTypeSpinner.setAdapter(adapter);
    }

    /**
     * 初始化期限选择器
     */
    public void initDateSpinner() {
        mDeadlineSpinner = (Spinner) getView().findViewById(R.id.date_spinner);
        List<Map<String, String>> types = new ArrayList<>();
        Map<String, String> type;

        for (String dateText : dateTexts) {
            type = new HashMap<>();
            type.put(TYPE, dateText);
            types.add(type);
        }

        SimpleAdapter adapter = new SimpleAdapter(
                getActivity(),
                types,
                R.layout.publish_spinner_item_layout,
                new String[]{TYPE},
                new int[]{R.id.type_item}
        );
        mDeadlineSpinner.setAdapter(adapter);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.need_content:
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.upload_image:
                addImageDialog();
                break;

            //点击发布按钮
            case R.id.publish_button:
                if (!mCustomApplication.isLogin()) {
                    Snackbar sb = Snackbar.make(v, "您尚未登陆，请登录后再发布需求", Snackbar.LENGTH_LONG);
                    sb.setAction("登录", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent login_register = new Intent(getActivity(), LoginAndRegisterActivity.class);
                            startActivity(login_register);
                        }
                    }).show();
                } else {
                    //显示处理图片对话框
                    showProgressDialog("正在处理图片");
                    //压缩图片
                    compressImage(imagePathsList);
                }
                break;
        }
    }

    /**
     * 发送发布订单请求
     *
     * @param title       需求标题
     * @param description 需求内容
     * @param type        需求类型
     * @param deadline    需求期限
     * @param tel         联系电话
     * @param reward      项目金额
     */
    public void sendPublishOrderRequest(String title,
                                        String description,
                                        String type,
                                        String deadline,
                                        String tel,
                                        String reward,
                                        List<File> imageFiles) {
        Log.i(TAG, "request" + imageFiles.toString());

        if (checkPublishData(title, description, imagePathsList, type, deadline, tel, reward)) {
            //获取请求参数
            Map<String, String> params = ParamsGenerateUtil.generatePublishOrderParams(title,
                    description,
                    type,
                    deadline,
                    tel,
                    reward);

            Log.i(TAG, "publish_params:" + params.toString());

            //创建请求
            FileUploadRequest publishOrderRequest = new FileUploadRequest(getContext(),
                    IPConfig.publishOrderAddress,
                    mCustomApplication.generateHeaderMap(),
                    params,
                    "files",
                    imageFiles,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String resp) {
                            Log.i(TAG, "publish_order_resp:" + resp);
                            PublishOrderRespBean publishOrderRespBean =
                                    JsonUtil.decodeByGson(resp, PublishOrderRespBean.class);
                            if (publishOrderRespBean.getCode().equals("1")) {
                                dismissProgressDialog();
                                clearAll();
                                //清空图片路径
                                imagePathsList.clear();
                                //清空文件
                                imageFilesList.clear();
                                UnRepeatToast.showToast(getActivity(), "发布成功");
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            dismissProgressDialog();
                            UnRepeatToast.showToast(getActivity(), "服务器不务正业中");
                        }
                    });
            //添加到请求队列
            mRequestQueue.add(publishOrderRequest);
        }
    }

    /**
     * 显示对话框
     */
    public void showProgressDialog(String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage(message);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.show();
        } else {
            mProgressDialog.setMessage(message);
            mProgressDialog.show();
        }
    }

    /**
     * 取消显示正在发布对话框
     */
    public void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    /**
     * 清空所有数据
     */
    public void clearAll() {
        mTitleEdit.getText().clear();
        mDescriptionEdit.getText().clear();
        mImagesPreviewLayout.removeAllViews();
        mTypeSpinner.setSelection(0);
        mDeadlineSpinner.setSelection(0);
        mTelEdit.getText().clear();
        mRewardEdit.getText().clear();
    }

    /**
     * 用于校验发布订单操作的数据
     *
     * @param title          需求标题
     * @param description    需求内容
     * @param imagePathsList
     * @param type           需求类型
     * @param deadline       需求期限
     * @param tel            联系电话
     * @param reward         项目金额
     * @return 如果填写的数据其中一项或全部不正确，返回false，否则返回true
     */
    public boolean checkPublishData(String title,
                                    String description,
                                    List<String> imagePathsList,
                                    String type,
                                    String deadline,
                                    String tel,
                                    String reward) {

        if (title.equals("")) {
            UnRepeatToast.showToast(getActivity(), "需求标题不能为空");
            return false;
        }

        if (description.equals("")) {
            UnRepeatToast.showToast(getActivity(), "需求内容不能为空");
            return false;
        }

        if (imagePathsList.size() == 0) {
            UnRepeatToast.showToast(getActivity(), "至少需要上传一张图片");
            return false;
        }

        if (type.equals("请选择项目类型")) {
            UnRepeatToast.showToast(getActivity(), "请选择项目类型");
            return false;
        }

        if (deadline.equals("请选择预期时长")) {
            UnRepeatToast.showToast(getActivity(), "请选择预期时长");
            return false;
        }

        if (tel.equals("")) {
            UnRepeatToast.showToast(getActivity(), "联系电话不能为空");
            return false;
        }

        if (tel.length() != 11) {
            UnRepeatToast.showToast(getActivity(), "手机号码长度应为11位");
            return false;
        }

        if (reward.equals("")) {
            UnRepeatToast.showToast(getActivity(), "项目金额不能为空");
            return false;
        }

        //校验成功后，保存填写的信息
        publishBean = new PublishBean(title, description, type, deadline, tel, reward);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showCamera();
                } else {
                    showOnDenienCameraPermission();
                }
                break;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.i(TAG, "UserVisible:" + isVisibleToUser);
    }

    /**
     * 压缩图片
     */
    private void compressImage(List<String> imagePathsList) {
        for (int i = 0; i < imagePathsList.size(); i++) {
            Luban.get(getContext())
                    .load(new File(imagePathsList.get(i)))                     //传人要压缩的图片
                    .putGear(Luban.THIRD_GEAR)      //设定压缩档次，默认三挡
                    .setCompressListener(new OnCompressListener() { //设置回调
                        @Override
                        public void onStart() {
                            // TODO 压缩开始前调用，可以在方法内启动 loading UI
                        }

                        @Override
                        public void onSuccess(File file) {
                            imageFilesList.add(file);
                            Log.i(TAG, "compress" + imageFilesList.toString());
                            canSendRequest();
                        }

                        @Override
                        public void onError(Throwable e) {
                            // TODO 当压缩过去出现问题时调用
                        }
                    }).launch();    //启动压缩
        }
    }

    /**
     * 当选择的图片全部压缩完成后发送请求
     */
    private void canSendRequest() {
        if (imageFilesList.size() == imagePathsList.size()) {
            //关闭处理图片对话框
            dismissProgressDialog();
            //显示正在发布对话框
            showProgressDialog("正在发布");
            //选择的项目类型
            String type = mTypeSpinner.getSelectedItem().toString();
            type = type.substring(6, type.length() - 1);
            //选择的项目期限
            String deadLine = mDeadlineSpinner.getSelectedItem().toString();
            deadLine = deadLine.substring(6, deadLine.length() - 1);
            //发送发布订单请求
            sendPublishOrderRequest(mTitleEdit.getText().toString(),
                    mDescriptionEdit.getText().toString(),
                    type,
                    deadLine,
                    mTelEdit.getText().toString(),
                    mRewardEdit.getText().toString(),
                    imageFilesList);
        }
    }
}
