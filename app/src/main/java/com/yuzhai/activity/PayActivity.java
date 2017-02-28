package com.yuzhai.activity;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yuzhai.bean.innerBean.PayRoleBean;
import com.yuzhai.fragment.OrderPublishedProcessFragment;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.yuzhaiwork.R;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import c.b.BP;
import c.b.PListener;

/**
 * Created by Administrator on 2016/12/26.
 */

public class PayActivity extends AppCompatActivity {
    private static final String TAG = "PayActivity";
    private static final String PAY_ROLE = "pay_role";
    private static final String PRICE = "price";
    private static final String PAY_DESCRIPTION = "pay_description";

    // 此为微信支付插件的官方最新版本号,请在更新时留意更新说明
    int PLUGINVERSION = 7;

    private Toolbar mToolbar;
    private TextView mReward;
    private TextView mPayDescription;
    private RelativeLayout mAliPayLayout;
    private RelativeLayout mWxPayLayout;
    private RadioButton mAliPayRadio;
    private RadioButton mWxPayRadio;
    private Button mPayTypeButton;

    private ProgressDialog dialog;

    private String mPayRole;
    private String mPayDescriptionText;
    private String mPriceText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        //初始化支付接口
        BP.init(CustomApplication.BOMB_APP_ID);

        mPayDescriptionText = getIntent().getStringExtra(PAY_DESCRIPTION);
        mPriceText = getIntent().getStringExtra(PRICE);
        mPayRole = getIntent().getStringExtra(PAY_ROLE);

        initView();
        initData();
    }

    private void initData() {
        mReward.setText("￥" + mPriceText);
        mPayDescription.setText(mPayDescriptionText);
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.pay_toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mReward = (TextView) findViewById(R.id.reward);
        mPayDescription = (TextView) findViewById(R.id.pay_description);

        mAliPayRadio = (RadioButton) findViewById(R.id.choice_alipay);
        mAliPayRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWxPayRadio.setChecked(false);
            }
        });

        mWxPayRadio = (RadioButton) findViewById(R.id.choice_wxpay);
        mWxPayRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAliPayRadio.setChecked(false);
            }
        });

        mAliPayLayout = (RelativeLayout) findViewById(R.id.alipay_layout);
        mAliPayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAliPayRadio.setChecked(true);
                mWxPayRadio.setChecked(false);
            }
        });

        mWxPayLayout = (RelativeLayout) findViewById(R.id.wxpay_layout);
        mWxPayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAliPayRadio.setChecked(false);
                mWxPayRadio.setChecked(true);
            }
        });

        mPayTypeButton = (Button) findViewById(R.id.pay_type);
        mPayTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAliPayRadio.isChecked()) {//支付宝支付
                    pay(true);
                } else if (mWxPayRadio.isChecked()) {//微信支付
                    pay(false);
                }
            }
        });
    }

    /**
     * 调用支付
     *
     * @param alipayOrWechatPay 支付类型，true为支付宝支付,false为微信支付
     */
    private void pay(final boolean alipayOrWechatPay) {
        if (alipayOrWechatPay) {
            if (!checkPackageInstalled("com.eg.android.AlipayGphone",
                    "https://www.alipay.com")) { // 支付宝支付要求用户已经安装支付宝客户端
                Toast.makeText(PayActivity.this, "请安装支付宝客户端", Toast.LENGTH_SHORT)
                        .show();
                return;
            }
        } else {
            if (checkPackageInstalled("com.tencent.mm", "http://weixin.qq.com")) {// 需要用微信支付时，要安装微信客户端，然后需要插件
                // 有微信客户端，看看有无微信支付插件
                int pluginVersion = BP.getPluginVersion(this);
                if (pluginVersion < PLUGINVERSION) {// 为0说明未安装支付插件,
                    // 否则就是支付插件的版本低于官方最新版
                    Toast.makeText(
                            PayActivity.this,
                            pluginVersion == 0 ? "监测到本机尚未安装支付插件,无法进行支付,请先安装插件(无流量消耗)"
                                    : "监测到本机的支付插件不是最新版,最好进行更新,请先更新插件(无流量消耗)",
                            Toast.LENGTH_SHORT).show();

                    installBmobPayPlugin("bp.db");
                    return;
                }
            } else {// 没有安装微信
                Toast.makeText(PayActivity.this, "请安装微信客户端", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        showDialog("正在获取订单...\nSDK版本号:" + BP.getPaySdkVersion());

        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName cn = new ComponentName("com.bmob.app.sport",
                    "com.bmob.app.sport.wxapi.BmobActivity");
            intent.setComponent(cn);
            this.startActivity(intent);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        BP.pay("订单支付", mPayDescriptionText, Double.valueOf(mPriceText), alipayOrWechatPay, new PListener() {

            // 因为网络等原因,支付结果未知(小概率事件),出于保险起见稍后手动查询
            @Override
            public void unknow() {
                Toast.makeText(PayActivity.this, "支付结果未知,请稍后手动查询", Toast.LENGTH_SHORT)
                        .show();
            }

            // 支付成功,如果金额较大请手动查询确认
            @Override
            public void succeed() {
                Toast.makeText(PayActivity.this, "支付成功!", Toast.LENGTH_SHORT).show();
                hideDialog();
                //发送消息到请求打开支付页面的页面
                EventBus.getDefault().post(new PayRoleBean(mPayRole));
                finish();
            }

            // 无论成功与否,返回订单号
            @Override
            public void orderId(String orderId) {
                // 此处应该保存订单号,比如保存进数据库等,以便以后查询
                showDialog("获取订单成功!请等待跳转到支付页面~");
            }

            // 支付失败,原因可能是用户中断支付操作,也可能是网络原因
            @Override
            public void fail(int code, String reason) {

                // 当code为-2,意味着用户中断了操作
                // code为-3意味着没有安装BmobPlugin插件
                if (code == -3) {
                    Toast.makeText(
                            PayActivity.this,
                            "监测到你尚未安装支付插件,无法进行支付,请先安装插件(已打包在本地,无流量消耗),安装结束后重新支付",
                            Toast.LENGTH_SHORT).show();
                    installBmobPayPlugin("bp.db");
                } else {
                    Toast.makeText(PayActivity.this, "支付中断!", Toast.LENGTH_SHORT)
                            .show();
                    Log.i(TAG, "pay status is fail, error code is \n"
                            + code + " ,reason is " + reason + "\n\n");
                }
                hideDialog();
            }
        });

    }

    /**
     * 检查某包名应用是否已经安装
     *
     * @param packageName 包名
     * @param browserUrl  如果没有应用市场，去官网下载
     * @return
     */
    private boolean checkPackageInstalled(String packageName, String browserUrl) {
        try {
            // 检查是否有支付宝客户端
            getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            // 没有安装支付宝，跳转到应用市场
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=" + packageName));
                startActivity(intent);
            } catch (Exception ee) {// 连应用市场都没有，用浏览器去支付宝官网下载
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(browserUrl));
                    startActivity(intent);
                } catch (Exception eee) {
                    Toast.makeText(PayActivity.this,
                            "您的手机上没有没有应用市场也没有浏览器，我也是醉了，你去想办法安装支付宝/微信吧",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
        return false;
    }

    /**
     * 安装assets里的apk文件
     *
     * @param fileName
     */
    void installBmobPayPlugin(String fileName) {
        try {
            InputStream is = getAssets().open(fileName);
            File file = new File(Environment.getExternalStorageDirectory()
                    + File.separator + fileName + ".apk");
            if (file.exists())
                file.delete();
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
            fos.close();
            is.close();

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://" + file),
                    "application/vnd.android.package-archive");
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDialog(String message) {
        try {
            if (dialog == null) {
                dialog = new ProgressDialog(this);
                dialog.setCancelable(true);
            }
            dialog.setMessage(message);
            dialog.show();
        } catch (Exception e) {
            // 在其他线程调用dialog会报错
        }
    }

    private void hideDialog() {
        if (dialog != null && dialog.isShowing())
            try {
                dialog.dismiss();
            } catch (Exception e) {
            }
    }
}
