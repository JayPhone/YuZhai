package com.yuzhai.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.yuzhai.activity.LoginActivity;
import com.yuzhai.bean.requestBean.PublishBean;
import com.yuzhai.http.IPConfig;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.http.FileUploadRequest;
import com.yuzhai.http.ParamsGenerateUtil;
import com.yuzhai.http.RequestQueueSingleton;
import com.yuzhai.util.BitmapUtil;
import com.yuzhai.util.FileUtil;
import com.yuzhai.util.GetPathUtil;
import com.yuzhai.view.UnRepeatToast;
import com.yuzhai.yuzhaiwork.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by Administrator on 2016/7/13.
 */
@RuntimePermissions
public class PublishFragment extends Fragment implements View.OnTouchListener, View.OnClickListener {
    //全局变量
    private Activity mMainActivity;
    private CustomApplication mCustomApplication;
    private RequestQueue mRequestQueue;

    //组件引用
    private Toolbar mToolbar;
    private TextView mTitleText;
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
    private List<ImageView> imageViewsList;
    private PublishBean publishBean;
    private int imageMargin = 15;

    //Activity请求码
    private final int CAMERA_REQUEST = 1;
    private final int IMAGE_PICK_REQUEST = 2;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMainActivity = getActivity();
        return inflater.inflate(R.layout.fragment_publish, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCustomApplication = (CustomApplication) mMainActivity.getApplication();
        mRequestQueue = RequestQueueSingleton.getRequestQueue(mMainActivity);
        imagePathsList = new ArrayList<>();
        imageViewsList = new ArrayList<>();
        initViews();
    }

    public void initViews() {
        //查找引用
        mToolbar = (Toolbar) getView().findViewById(R.id.publish_toolbar);
        mToolbar.inflateMenu(R.menu.publish_menu);

        mTitleText = (TextView) getView().findViewById(R.id.title_text);
        mTitleText.setText("发布需求");
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
        AlertDialog.Builder builder = new AlertDialog.Builder(mMainActivity);
        builder.setTitle("添加图片");
        builder.setItems(new String[]{"手机相机添加", "本地相册选择"},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                //每次只能上传5张图片
                                if (imagePathsList.size() < 5) {
                                    Log.i("count", imagePathsList.size() + "");
                                    PublishFragmentPermissionsDispatcher.showCameraWithCheck(PublishFragment.this);
                                } else {
                                    UnRepeatToast.showToast(mMainActivity, "一次最多只能上传5张图片");
                                }
                                break;

                            case 1:
                                if (imagePathsList.size() < 5) {
                                    Log.i("count", imagePathsList.size() + "");
                                    showImagePick();
                                } else {
                                    UnRepeatToast.showToast(mMainActivity, "一次最多只能上传5张图片");
                                }
                                break;
                        }
                    }
                });
        builder.create().show();
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    public void showCamera() {
        imagePath = FileUtil.createFilePath(FileUtil.NEED_IMAGE);
        Log.i("path", imagePath);
        Intent intent_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent_camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(imagePath)));
        startActivityForResult(intent_camera, CAMERA_REQUEST);
    }

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

    //当用户选择不再提示后的处理
    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void onCameraNeverAskAgain() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mMainActivity);
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
                    ImageView needImage = new ImageView(mMainActivity);
                    needImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(imageWidth, imageHeight);
                    params.setMargins(imageMargin, 0, 0, 0);
                    needImage.setLayoutParams(params);
                    mImagesPreviewLayout.addView(needImage);
                    needImage.setImageBitmap(BitmapUtil.decodeSampledBitmapFromFile(imagePath, imageWidth, imageHeight));
                    //添加当前图片路径到List
                    imagePathsList.add(imagePath);
                    //添加当前图片到List
                    imageViewsList.add(needImage);
                    Log.i("paths", imagePathsList.toString());
                }
                break;
            case IMAGE_PICK_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    int imageLayoutWidth = mImagesPreviewLayout.getMeasuredWidth();
                    int imageWidth = (imageLayoutWidth - (imageMargin * 6)) / 5;
                    int imageHeight = imageWidth;
                    ImageView needImage = new ImageView(mMainActivity);
                    needImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(imageWidth, imageHeight);
                    params.setMargins(imageMargin, 0, 0, 0);
                    needImage.setLayoutParams(params);
                    mImagesPreviewLayout.addView(needImage);
                    Uri uri = data.getData();
                    imagePath = GetPathUtil.getImageAbsolutePath(mMainActivity, uri);
                    needImage.setImageBitmap(BitmapUtil.decodeSampledBitmapFromFile(imagePath, imageWidth, imageHeight));
                    //添加当前图片路径到List
                    imagePathsList.add(imagePath);
                    //添加当前图片到List
                    imageViewsList.add(needImage);
                    Log.i("paths", imagePathsList.toString());
                }
                break;
        }
    }

    //初始化类型选择器
    public void initTypeSpinner() {
        mTypeSpinner = (Spinner) getView().findViewById(R.id.type_spinner);
        List<Map<String, String>> types = new ArrayList<>();
        Map<String, String> type;

        for (int i = 0; i < typeTexts.length; i++) {
            type = new HashMap<>();
            type.put("type", typeTexts[i]);
            types.add(type);
        }

        SimpleAdapter adapter = new SimpleAdapter(
                getActivity(),
                types,
                R.layout.publish_spinner_item_layout,
                new String[]{"type"},
                new int[]{R.id.type_item}
        );
        mTypeSpinner.setAdapter(adapter);
    }

    //初始化期限选择器
    public void initDateSpinner() {
        mDeadlineSpinner = (Spinner) getView().findViewById(R.id.date_spinner);
        List<Map<String, String>> types = new ArrayList<>();
        Map<String, String> type;

        for (int i = 0; i < dateTexts.length; i++) {
            type = new HashMap<>();
            type.put("type", dateTexts[i]);
            types.add(type);
        }

        SimpleAdapter adapter = new SimpleAdapter(
                getActivity(),
                types,
                R.layout.publish_spinner_item_layout,
                new String[]{"type"},
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
            //点击返回按钮
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
                            Intent login = new Intent(mMainActivity, LoginActivity.class);
                            startActivity(login);
                        }
                    }).show();
                } else {
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
                            mCustomApplication.getToken());
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
                                        String token) {

        if (checkPublishData(title, description, imagePathsList, type, deadline, tel, reward)) {
            //显示正在发布对话框
            showProgressDialog();
            //获取请求参数
            Map<String, String> params = ParamsGenerateUtil.generatePublishOrderParams(title,
                    description,
                    type,
                    deadline,
                    tel,
                    reward);

            Log.i("publish_params", params.toString());

            //创建请求
            FileUploadRequest publishOrderRequest = new FileUploadRequest(IPConfig.publishOrderAddress + "?token=" + token, null, params, "images", generateUploadFile(), new Response.Listener<String>() {
                @Override
                public void onResponse(String resp) {
                    Log.i("publish_order_resp", resp);
                    dismissProgressDialog();
                    clearAll();
                    UnRepeatToast.showToast(mMainActivity, "发布成功");
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    dismissProgressDialog();
                    UnRepeatToast.showToast(mMainActivity, "服务器不务正业中");
                }
            });

            //添加到请求队列
            mRequestQueue.add(publishOrderRequest);
        }
    }

    /**
     * 显示正在发布对话框
     */
    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mMainActivity);
            mProgressDialog.setMessage("正在发布");
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.show();
        } else {
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
            UnRepeatToast.showToast(mMainActivity, "需求标题不能为空");
            return false;
        }

        if (description.equals("")) {
            UnRepeatToast.showToast(mMainActivity, "需求内容不能为空");
            return false;
        }

        if (imagePathsList.size() == 0) {
            UnRepeatToast.showToast(mMainActivity, "至少需要上传一张图片");
            return false;
        }

        if (type.equals("请选择项目类型")) {
            UnRepeatToast.showToast(mMainActivity, "请选择项目类型");
            return false;
        }

        if (deadline.equals("请选择预期时长")) {
            UnRepeatToast.showToast(mMainActivity, "请选择预期时长");
            return false;
        }

        if (tel.equals("")) {
            UnRepeatToast.showToast(mMainActivity, "联系电话不能为空");
            return false;
        }

        if (tel.length() != 11) {
            UnRepeatToast.showToast(mMainActivity, "手机号码长度应为11位");
            return false;
        }

        if (reward.equals("")) {
            UnRepeatToast.showToast(mMainActivity, "项目金额不能为空");
            return false;
        }

        //校验成功后，保存填写的信息
        publishBean = new PublishBean(title, description, type, deadline, tel, reward);
        return true;
    }

    /**
     * 生成图片文件List
     *
     * @return 返回List
     */
    public List<File> generateUploadFile() {
        List<File> filesList = new ArrayList<>();
        if (imagePathsList.size() == 0) {
            return null;
        } else {
            for (int i = 0; i < imagePathsList.size(); i++) {
                filesList.add(new File(imagePathsList.get(i)));
            }
            return filesList;
        }
    }

    //权限许可情况调用
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: 将权限处理委托给生成的方法
        PublishFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
