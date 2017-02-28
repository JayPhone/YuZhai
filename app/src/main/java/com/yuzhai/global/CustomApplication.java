package com.yuzhai.global;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;
import android.util.Log;

import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.yuzhai.dao.CookieOperate;
import com.yuzhai.dao.UserInfoOperate;
import com.yuzhai.util.InstantMessageHandler;

import org.litepal.LitePalApplication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.newim.BmobIM;
import cn.bmob.v3.Bmob;

/**
 * Created by Administrator on 2016/7/9.
 */
public class CustomApplication extends LitePalApplication {
    private UserInfoOperate mUserInfoOperate;
    private CookieOperate mCookieOperate;
    private boolean login = false;
    public static boolean isConnect = true;

    //Bmob后端云APPId
    public static final String BOMB_APP_ID = "d6c96dccb6148cc1b83b00b9676d6e43";
    //小米推送服务
    private static final String MI_APP_ID = "2882303761517547678";
    private static final String MI_APP_KEY = "5871754794678";
    // 此TAG在adb logcat中检索自己所需要的信息， 只需在命令行终端输入 adb logcat | grep
    public static final String TAG = "com.yuzhai.yuzhaiwork";

    @Override
    public void onCreate() {
        super.onCreate();
        mUserInfoOperate = new UserInfoOperate(this);
        mCookieOperate = new CookieOperate(this);

        // 注册push服务，注册成功后会向DemoMessageReceiver发送广播
        // 可以从DemoMessageReceiver的onCommandResult方法中MiPushCommandMessage对象参数中获取注册信息
        if (shouldInit()) {
            MiPushClient.registerPush(this, MI_APP_ID, MI_APP_KEY);
        }

        LoggerInterface newLogger = new LoggerInterface() {

            @Override
            public void setTag(String tag) {
                // ignore
            }

            @Override
            public void log(String content, Throwable t) {
                Log.d(TAG, content, t);
            }

            @Override
            public void log(String content) {
                Log.d(TAG, content);
            }
        };
        Logger.setLogger(this, newLogger);

        //NewIM初始化
        BmobIM.init(this);
        //注册消息接收器
        BmobIM.registerDefaultMessageHandler(new InstantMessageHandler(this));

    }

    //通过判断手机里的所有进程是否有这个App的进程
    //从而判断该App是否有打开
    private boolean shouldInit() {
        //通过ActivityManager我们可以获得系统里正在运行的activities
        //包括进程(Process)等、应用程序/包、服务(Service)、任务(Task)信息。
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();

        //获取本App的唯一标识
        int myPid = Process.myPid();
        //利用一个增强for循环取出手机里的所有进程
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            //通过比较进程的唯一标识和包名判断进程里是否存在该App
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    //用户信息操作
    public String getUserPhone() {
        return mUserInfoOperate.getUserPhone();
    }

    public String getPassword() {
        return mUserInfoOperate.getPassword();
    }

    public void addUserInfo(String userPhone, String password) {
        mUserInfoOperate.addUserInfo(userPhone, password);
    }

    public void clearUserInfo() {
        mUserInfoOperate.clearUserInfo();
    }

    public void setCookie(String cookie) {
        mCookieOperate.setCookie(cookie);
    }

    public String getCookie() {
        return mCookieOperate.getCookie();
    }

    public void clearCookie() {
        mCookieOperate.clearCookie();
    }

    public Map<String, String> generateHeaderMap() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", getCookie());
        return headers;
    }

    //登录操作
    public boolean isLogin() {
        return login;
    }

    public void setLoginState(boolean loginState) {
        this.login = loginState;
    }
}
