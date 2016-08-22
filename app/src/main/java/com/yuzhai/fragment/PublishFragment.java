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
import android.support.v4.app.Fragment;
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
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yuzhai.config.IPConfig;
import com.yuzhai.entry.PublishEntry;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.http.FileUploadRequest;
import com.yuzhai.util.BitmapUtil;
import com.yuzhai.util.CheckData;
import com.yuzhai.util.FileUtil;
import com.yuzhai.util.GetPathUtil;
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
    private Activity mainActivity;
    private CustomApplication customApplication;
    private RequestQueue requestQueue;

    //组件引用
    private EditText needTitleEdit;
    private EditText needContentEdit;
    private LinearLayout imagesPreviewLayout;
    private ImageView uploadImageView;
    private Spinner typeSpinner;
    private Spinner dateSpinner;
    private EditText contactEdit;
    private EditText moneyEdit;
    private Button publishButton;

    //数据引用
    private String[] typeTexts = new String[]{"请选择项目类型", "软件IT", "音乐制作", "平面设计", "视频拍摄", "游戏研发", "文案撰写", "金融会计"};
    private String[] dateTexts = new String[]{"请选择预期时长", "7天", "15天", "30天", "365天"};

    private String imagePath;
    private List<String> imagePathsList;
    private List<ImageView> imageViewsList;
    private PublishEntry publishEntry;

    //Activity请求码
    private final int CAMERA_PEQUEST = 1;
    private final int IMAGEPICK_PEQUEST = 2;

    private ProgressDialog progressDialog;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainActivity = getActivity();
        return inflater.inflate(R.layout.fragment_publish, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        customApplication = (CustomApplication) mainActivity.getApplication();
        requestQueue = customApplication.getRequestQueue();
        imagePathsList = new ArrayList<>();
        imageViewsList = new ArrayList<>();
        initViews();
    }

    public void initViews() {
        //查找引用
        needTitleEdit = (EditText) mainActivity.findViewById(R.id.need_title);
        needContentEdit = (EditText) mainActivity.findViewById(R.id.need_content);
        imagesPreviewLayout = (LinearLayout) mainActivity.findViewById(R.id.images_preview);
        uploadImageView = (ImageView) mainActivity.findViewById(R.id.upload_image);
        contactEdit = (EditText) mainActivity.findViewById(R.id.contact);
        moneyEdit = (EditText) mainActivity.findViewById(R.id.money);
        publishButton = (Button) mainActivity.findViewById(R.id.publish_button);

        //设置触摸监听器
        needContentEdit.setOnTouchListener(this);

        //设置点击监听器
        uploadImageView.setOnClickListener(this);
        publishButton.setOnClickListener(this);

        //初始化两个选择器
        initTypeSpinner();
        initDateSpinner();
    }

    //对话框显示图片的添加方式
    public void addImageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
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
                                    Toast.makeText(mainActivity, "一次最多只能上传5张图片", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case 1:
                                if (imagePathsList.size() < 5) {
                                    Log.i("count", imagePathsList.size() + "");
                                    showImagePick();
                                } else {
                                    Toast.makeText(mainActivity, "一次最多只能上传5张图片", Toast.LENGTH_SHORT).show();
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
        startActivityForResult(intent_camera, CAMERA_PEQUEST);
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
        startActivityForResult(intent_imagePick, IMAGEPICK_PEQUEST);
    }

    //当用户选择不再提示后的处理
    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void onCameraNeverAskAgain() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
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
            case CAMERA_PEQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    ImageView needImage = new ImageView(mainActivity);
                    needImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(180, 180);
                    params.rightMargin = 10;
                    needImage.setImageBitmap(BitmapUtil.decodeSampledBitmapFromFile(imagePath, 150, 150));
                    imagesPreviewLayout.addView(needImage, params);
                    //添加当前图片路径到List
                    imagePathsList.add(imagePath);
                    //添加当前图片到List
                    imageViewsList.add(needImage);
                    Log.i("paths", imagePathsList.toString());
                }
                break;
            case IMAGEPICK_PEQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    ImageView needImage = new ImageView(mainActivity);
                    needImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(180, 180);
                    params.rightMargin = 10;
                    Uri uri = data.getData();
                    imagePath = GetPathUtil.getImageAbsolutePath(mainActivity, uri);
                    needImage.setImageBitmap(BitmapUtil.decodeSampledBitmapFromFile(imagePath, 150, 150));
                    imagesPreviewLayout.addView(needImage, params);
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
        typeSpinner = (Spinner) mainActivity.findViewById(R.id.type_spinner);
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
        typeSpinner.setAdapter(adapter);
    }

    //初始化期限选择器
    public void initDateSpinner() {
        dateSpinner = (Spinner) mainActivity.findViewById(R.id.date_spinner);
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
        dateSpinner.setAdapter(adapter);
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
            case R.id.publish_button:
                if (customApplication.isLogin() == false) {
                    Toast.makeText(mainActivity, "您尚未登陆，请登录后再发布需求", Toast.LENGTH_SHORT).show();
                } else {
                    //校验数据
                    //当返回true时表示所有填写的参数均符合格式
                    if (checkData()) {
                        FileUploadRequest fileUploadRequest = new FileUploadRequest(IPConfig.publishAddress, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                Log.i("response", s);
                                progressDialog.dismiss();
                                Toast.makeText(mainActivity, "发布成功", Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                Toast.makeText(mainActivity, "服务器无响应，请稍后再试", Toast.LENGTH_SHORT).show();
                            }
                        }, "images", createUploadFile(), createParams());

                        //添加cookie
                        fileUploadRequest.setmHeaders(createHeaders());
                        requestQueue.add(fileUploadRequest);
                        progressDialog = new ProgressDialog(mainActivity);
                        progressDialog.setMessage("正在发布");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.show();
                    }
                }
                break;
        }
    }

    /*校验数据
     *如果填写的数据不正确，返回false
     * 如果填写的数据全部都正确，返回true
     */
    public boolean checkData() {
        //获取填写的内容
        String title = needTitleEdit.getText().toString();
        String content = needContentEdit.getText().toString();
        String type = typeSpinner.getSelectedItem().toString();
        type = type.substring(6, type.length() - 1);
        String date = dateSpinner.getSelectedItem().toString();
        date = date.substring(6, date.length() - 1);
        String contact = contactEdit.getText().toString();
        String money = moneyEdit.getText().toString();

        /*CheckData类用于校验填写的数据*/
        if (CheckData.isEmpty(title)) {
            Toast.makeText(mainActivity, "需求标题不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (CheckData.isEmpty(content)) {
            Toast.makeText(mainActivity, "需求内容不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (type.equals("请选择项目类型")) {
            Toast.makeText(mainActivity, "请选择项目类型", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (date.equals("请选择预期时长")) {
            Toast.makeText(mainActivity, "请选择预期时长", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (CheckData.isEmpty(contact)) {
            Toast.makeText(mainActivity, "联系电话不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!CheckData.matchLength(contact, 11)) {
            Toast.makeText(mainActivity, "手机号码长度应为11位", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (CheckData.isEmpty(money)) {
            Toast.makeText(mainActivity, "项目金额不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        //校验成功后，保存填写的信息
        publishEntry = new PublishEntry(title, content, type, date, contact, money);
        return true;
    }

    //生成图片文件List
    public List<File> createUploadFile() {
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

    public Map<String, String> createHeaders() {
        //设置请求参数
        Map<String, String> headers = new HashMap<>();
        headers.put("cookie", customApplication.getCookie());
        return headers;
    }

    //生成请求参数
    public Map<String, String> createParams() {
        Map<String, String> params = new HashMap<>();
        params.put("title", publishEntry.getTitle());
        params.put("descript", publishEntry.getDescript());
        params.put("type", publishEntry.getType());
        params.put("deadline", publishEntry.getDeadline());
        params.put("tel", publishEntry.getTel());
        params.put("money", publishEntry.getMoney());
        return params;
    }

    //权限许可情况调用
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: 将权限处理委托给生成的方法
        PublishFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
