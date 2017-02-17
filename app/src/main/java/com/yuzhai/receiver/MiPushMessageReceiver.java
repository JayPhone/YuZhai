package com.yuzhai.receiver;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.yuzhaiwork.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MiPushMessageReceiver extends PushMessageReceiver {

    private String mRegId;
    private long mResultCode = -1;
    private String mReason;
    private String mCommand;
    private String mMessage;
    private String mTopic;
    private String mAlias;
    private String mUserAccount;
    private String mStartTime;
    private String mEndTime;

    //onReceivePassThroughMessage 方法用来接收服务器向客户端发送的透传消息。
    public void onReceivePassThroughMessage(Context context, MiPushMessage message) {
        //打印消息方便测试
        Log.i("onReceivePassThrough", "透传消息到达了");
        Log.i("onReceivePassThrough", "透传消息是" + message.toString());
    }

    //onNotificationMessageClicked 方法用来接收服务器向客户端发送的通知消息，
    //这个回调方法会在用户手动点击通知后触发。
    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage message) {
        //打印消息方便测试
        Log.i("onNotificationClicked", "用户点击了通知消息");
        Log.i("onNotificationClicked", "通知消息是" + message.toString());
        Log.i("onNotificationClicked", "点击后,会进入应用");
    }

    //方法用来接收服务器向客户端发送的通知消息，这个回调方法是在通知消息到达客户端时触发。
    //另外应用在前台时不弹出通知的通知消息到达客户端也会触发这个回调函数。
    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage message) {
        //打印消息方便测试
        Log.i("onNotificationArrived", "通知消息到达了");
        Log.i("onNotificationArrived", "通知消息是" + message.toString());
    }

    //onCommandResult 方法用来接收客户端向服务器发送命令后的响应结果。
    @Override
    public void onCommandResult(Context context, MiPushCommandMessage message) {
        String command = message.getCommand();
        Log.i("onCommandResult", "command message:" + command);

        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {

                //打印信息便于测试注册成功与否
                Log.i("onCommandResult", "注册成功");

            } else {
                Log.i("onCommandResult", "注册失败");
            }
        }

    }

    //onReceiveRegisterResult 方法用来接收客户端向服务器发送注册命令后的响应结果。
    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage message) {
        String command = message.getCommand();
        Log.i("onReceiveRegisterResult", "command message:" + command);

        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                Log.i("regId", message.getCommandArguments().get(0));
                //打印日志：注册成功
                Log.i("onReceiveRegisterResult", "注册成功");
            } else {
                //打印日志：注册失败
                Log.i("onReceiveRegisterResult", "注册失败");
            }
        } else {
            System.out.println("其他情况" + message.getReason());
        }
    }

}
