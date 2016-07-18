package com.yuzhai.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.yuzhai.util.BitmapUtil;
import com.yuzhai.yuzhaiwork.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by Administrator on 2016/7/13.
 */
@RuntimePermissions
public class PublishFragment extends Fragment implements View.OnTouchListener {
    private Activity mainActivity;
    private EditText needContent;
    private ImageView uploadImage;
    private Spinner typeSpinner;
    private Spinner dateSpinner;
    private String[] typeTexts = new String[]{"软件IT", "音乐制作", "平面设计", "视频拍摄", "游戏研发", "文案撰写", "金融会计"};
    private String[] dateTexts = new String[]{"7天", "15天", "30天", "365天"};
    private int CAMERA_PEQUEST = 1;
    private int GALLERY_PEQUEST = 2;
    private List<String> imagesPath;
    private String imagePath;
    private LinearLayout imagesPreview;
    private List<ImageView> imageViews;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainActivity = getActivity();
        return inflater.inflate(R.layout.fragment_publish, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        imagesPath = new ArrayList<>();
        imageViews = new ArrayList<>();
        initViews();
        initTypeSpinner();
        initDateSpinner();
    }

    public void initViews() {
        imagesPreview = (LinearLayout) mainActivity.findViewById(R.id.images_preview);
        needContent = (EditText) mainActivity.findViewById(R.id.need_content);
        needContent.setOnTouchListener(this);
        uploadImage = (ImageView) mainActivity.findViewById(R.id.upload_image);
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImageDialog();
            }
        });
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
                                if (imagesPath.size() < 5) {
                                    Log.i("count", imagesPath.size() + "");
                                    PublishFragmentPermissionsDispatcher.showCameraWithCheck(PublishFragment.this);
                                } else {
                                    Toast.makeText(mainActivity, "一次最多只能上传5张图片", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case 1:
                                showGallery();
                                break;
                        }
                    }
                });
        builder.create().show();
    }

    public void showGallery() {
        Intent intent_gallery = new Intent(Intent.ACTION_PICK);
        intent_gallery.setType("image/*");
        startActivityForResult(intent_gallery, GALLERY_PEQUEST);
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    public void showCamera() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            //保存图片的路径
            String out_file_path = Environment.getExternalStorageDirectory() + "/YuZhai/needImage";
            //创建图片路径文件夹
            File dir = new File(out_file_path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            imagePath = out_file_path + "/hdIMG_" + new DateFormat().format("yyyyMMdd_HHmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
            Log.i("path", imagePath);
            Intent intent_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent_camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(imagePath)));
            startActivityForResult(intent_camera, CAMERA_PEQUEST);
        } else {
            Log.i("错误", "请确认已经插入SD卡");
        }
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
        if (requestCode == CAMERA_PEQUEST && resultCode == Activity.RESULT_OK) {
            ImageView needImage = new ImageView(mainActivity);
            needImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(180, 180);
            params.rightMargin = 10;
            needImage.setImageBitmap(BitmapUtil.decodeSampledBitmapFromFile(imagePath, 150, 150));
            imagesPreview.addView(needImage, params);
            //添加当前图片路径到List
            imagesPath.add(imagePath);
            //添加当前图片到List
            imageViews.add(needImage);
            Log.i("paths", imagesPath.toString());
        }
    }

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

    //权限许可情况调用
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: 将权限处理委托给生成的方法
        PublishFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
