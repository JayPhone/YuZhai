package com.yuzhai.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.yuzhai.ui.MainActivity;
import com.yuzhai.view.AbstractSpinerAdapter;
import com.yuzhai.view.SpinerPopWindow;
import com.yuzhai.yuzhaiwork.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SubOrderFragment extends Fragment implements
        AbstractSpinerAdapter.IOnItemSelectListener {

    private GridView gridView1;
    private final int IMAGE_OPEN = 1;      //打开图片标记
    private final int GET_DATA = 2;           //获取处理后图片标记
    private final int TAKE_PHOTO = 3;       //拍照标记
    Activity activity;
    private Bitmap bmp;
    private String pathTakePhoto;              //拍照路径
    EditText ed;
    //存储Bmp图像
    private ArrayList<HashMap<String, Object>> imageItem;
    //适配器
    private SimpleAdapter simpleAdapter;

    private TextView mTView, mTView1;
    private ImageView mBtnDropDown, mBtnDropDown1;
    private List<String> wantodo = new ArrayList<String>();//你想要做的填充数组
    private List<String> linktime = new ArrayList<String>();//时间的填充数组
    private SpinerPopWindow mSpinerPopWindow;
    private SpinerPopWindow mSpinerPopWindow1;

    public SubOrderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sub_publish, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activity = getActivity();

        /******
         * 自定义的下拉菜单
         * mSpinerPopWindow是设置您想要做下拉菜单的自定义对象
         * mSpinerPopWindow1是设置限制时间下拉菜单自定义对象
         *
         *
         *
         */

        //获取id
        mTView = (TextView) activity.findViewById(R.id.tv_value);
        mTView1 = (TextView) activity.findViewById(R.id.tv_value1);
        mBtnDropDown = (ImageView) activity.findViewById(R.id.bt_dropdown);
        mBtnDropDown1 = (ImageView) activity.findViewById(R.id.bt_dropdown1);

        mTView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.tv_value:
                        showSpinWindow();
                        break;
                }
            }
        });

        mTView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.tv_value1:
                        showSpinWindow1();
                        break;
                }
            }
        });


        //为 想要做什么，限制时间， 填充数组
        String[] todo = getResources().getStringArray(R.array.wanttodo);
        for (int i = 0; i < todo.length; i++) {
            wantodo.add(todo[i]);
        }

        String[] lTime = getResources().getStringArray(R.array.linktime);
        for (int i = 0; i < lTime.length; i++) {
            linktime.add(lTime[i]);
        }


        //实例化对象
        mSpinerPopWindow = new SpinerPopWindow(activity);
        mSpinerPopWindow.refreshData(wantodo, 0);
        mSpinerPopWindow.setItemListener(new AbstractSpinerAdapter.IOnItemSelectListener() {
            @Override
            public void onItemClick(int pos) {
                setWantToDo(pos);
            }
        });

        mSpinerPopWindow1 = new SpinerPopWindow(activity);
        mSpinerPopWindow1.refreshData(linktime, 0);
        mSpinerPopWindow1.setItemListener(new AbstractSpinerAdapter.IOnItemSelectListener() {
            @Override
            public void onItemClick(int pos) {
                setLinktime(pos);
            }
        });


        /*****
         *图片添加处理
         *
         *
         *
         *
         *
         */


        //获取id
        gridView1 = (GridView) activity.findViewById(R.id.gridView1);

        //SimpleAdapter参数imageItem为数据源 R.layout.griditem_addpic为布局
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.gridview_addpic); //加号
        imageItem = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("itemImage", bmp);
        map.put("pathImage", "add_pic");
        imageItem.add(map);
        simpleAdapter = new SimpleAdapter(activity,
                imageItem, R.layout.griditem_addpic,
                new String[]{"itemImage"}, new int[]{R.id.imageView1});

        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data,
                                        String textRepresentation) {
                // TODO Auto-generated method stub
                if (view instanceof ImageView && data instanceof Bitmap) {
                    ImageView i = (ImageView) view;
                    i.setImageBitmap((Bitmap) data);
                    return true;
                }
                return false;
            }
        });
        gridView1.setAdapter(simpleAdapter);

        //设置gridView的监听事件
        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (imageItem.size() == 10) { //第一张为默认图片,满九张+一张默认即满
                    Toast.makeText(activity, "图片数9张已满", Toast.LENGTH_SHORT).show();
                } else if (position == 0) { //点击图片位置为+ 0对应0张图片
                    //Toast.makeText(MainActivity.this, "添加图片", Toast.LENGTH_SHORT).show();
                    AddImageDialog();
                } else {
//					DeleteDialog(position);
                    //Toast.makeText(MainActivity.this, "点击第" + (position + 1) + " 号图片",
                    //		Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    protected void AddImageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("添加图片");
        builder.setCancelable(true); //响应back按钮
        builder.setItems(new String[]{"本地相册选择", "手机相机添加", "取消选择图片"},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: //本地相册
                                dialog.dismiss();
                                Intent intent = new Intent(Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent, IMAGE_OPEN);
                                //通过onResume()刷新数据
                                break;
                            case 1: //手机相机
                                dialog.dismiss();
                                File outputImage = new File(Environment.getExternalStorageDirectory(), "suishoupai_image.jpg");
                                pathTakePhoto = outputImage.toString();
                                try {
                                    if (outputImage.exists()) {
                                        outputImage.delete();
                                    }
                                    outputImage.createNewFile();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
//                                imageUri = Uri.fromFile(outputImage);
//                                Intent intentPhoto = new Intent("android.media.action.IMAGE_CAPTURE"); //拍照
//                                intentPhoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//                                startActivityForResult(intentPhoto, TAKE_PHOTO);
                                break;
                            case 2: //取消添加
                                dialog.dismiss();
                                break;
                            default:
                                break;
                        }
                    }
                });
        //显示对话框
        builder.create().show();
    }


    private void showSpinWindow() {
        mSpinerPopWindow.setWidth(mTView.getWidth());
        mSpinerPopWindow.showAsDropDown(mTView);
    }

    private void showSpinWindow1() {
        mSpinerPopWindow1.setWidth(mTView.getWidth());
        mSpinerPopWindow1.showAsDropDown(mTView1);
    }

    private void setWantToDo(int pos) {
        if (pos >= 0 && pos <= wantodo.size()) {
            String value = wantodo.get(pos);
            mTView.setText(value);
        }
    }

    private void setLinktime(int pos) {
        if (pos >= 0 && pos <= linktime.size()) {
            String value = linktime.get(pos);
            mTView1.setText(value);
        }
    }


    @Override
    public void onItemClick(int pos) {

    }
}


