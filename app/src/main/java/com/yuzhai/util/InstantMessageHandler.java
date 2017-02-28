package com.yuzhai.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yuzhai.activity.MainActivity;
import com.yuzhai.bean.responseBean.ContactUserDataBean;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.http.CommonRequest;
import com.yuzhai.http.IPConfig;
import com.yuzhai.http.ParamsGenerateUtil;
import com.yuzhai.http.RequestQueueSingleton;
import com.yuzhai.view.UnRepeatToast;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Map;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.BmobIMMessageHandler;
import cn.bmob.newim.notification.BmobNotificationManager;

import static com.xiaomi.push.service.am.s;

/**
 * Created by 35429 on 2017/2/17.
 */

public class InstantMessageHandler extends BmobIMMessageHandler {
    private static final String TAG = "InstantMessageHandler";
    private static final String NOTIFICATION = "notification";
    private static final String CONTACT = "contact";
    private Context mContext;

    public InstantMessageHandler(Context context) {
        mContext = context;
    }

    @Override
    public void onMessageReceive(final MessageEvent event) {
        //当接收到服务器发来的消息时，此方法被调用
        Log.i(TAG, "有人发送消息:" + event.getMessage());
        //处理消息
        excuteMessage(event);
    }

    @Override
    public void onOfflineReceive(final OfflineMessageEvent event) {
        //每次调用connect方法时会查询一次离线消息，如果有，此方法会被调用
        Map<String, List<MessageEvent>> map = event.getEventMap();
        Log.i(TAG, "离线消息属于" + map.size() + "个用户");
        //挨个检测下离线消息所属的用户的信息是否需要更新
        for (Map.Entry<String, List<MessageEvent>> entry : map.entrySet()) {
            List<MessageEvent> list = entry.getValue();
            int size = list.size();
            for (int i = 0; i < size; i++) {
                excuteMessage(list.get(i));
            }
        }
    }

    /**
     * 处理消息
     *
     * @param event
     */
    private void excuteMessage(MessageEvent event) {
        //检测用户信息是否需要更新
        BmobIMConversation conversation = event.getConversation();
        BmobIMUserInfo info = event.getFromUserInfo();
        BmobIMMessage msg = event.getMessage();
        String username = info.getName();
        String objectId = info.getUserId();
        String avatar = info.getAvatar();
        String title = conversation.getConversationTitle();
        Log.i(TAG, "object_id:" + objectId);
        Log.i(TAG, "user_name:" + username);
        Log.i(TAG, "avatar:" + avatar);
        Log.i(TAG, "title:" + title);
        //第一步：判断是否需要更新。判断依据：sdk内部，对于单聊会话来说，是用objectId来表示新会话的会话标题的，因此需要比对用户名和会话标题，两者不一样，则需要更新用户信息。
        if (!username.equals(title)) {
            //更新用户资料
            sendQueryContactUserInfoRequest(event, objectId);
        }
        if (BmobNotificationManager.getInstance(mContext).isShowNotification()) {//如果需要显示通知栏，SDK提供以下两种显示方式：
            Intent pendingIntent = new Intent(mContext, MainActivity.class);
            //通知主页面打开最近联系页面
            pendingIntent.putExtra(NOTIFICATION, CONTACT);
            pendingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            //多个用户的多条消息合并成一条通知：有XX个联系人发来了XX条消息
            BmobNotificationManager.getInstance(mContext).showNotification(event, pendingIntent);
        } else {//直接发送消息事件
            Log.i(TAG, "当前处于应用内，发送event");
            EventBus.getDefault().post(event);
        }
    }

    /**
     * 发送查看用户信息的请求
     *
     * @param event
     * @param userAccount
     */
    private void sendQueryContactUserInfoRequest(final MessageEvent event, String userAccount) {
        //获取查询聊天用户信息的参数集
        Map<String, String> params = ParamsGenerateUtil.generateQueryContactUserInfo(userAccount);
        Log.i(TAG, "query_contact_user_info_param:" + params);

        CommonRequest queryContactUserInfo = new CommonRequest(
                mContext,
                IPConfig.queryContactUserInfoAddress,
                ((CustomApplication) mContext).generateHeaderMap(),
                params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String resp) {
                        Log.i(TAG, resp);
                        //更新用户信息
                        ContactUserDataBean contactUserDataBean = JsonUtil.decodeByGson(resp, ContactUserDataBean.class);
                        BmobIMUserInfo bmobIMUserInfo = new BmobIMUserInfo(contactUserDataBean.getUserId(),
                                contactUserDataBean.getName(),
                                contactUserDataBean.getAvatar());
                        BmobIM.getInstance().updateUserInfo(bmobIMUserInfo);
                        //更新会话信息
                        BmobIMConversation conversation = event.getConversation();
                        conversation.setConversationTitle(contactUserDataBean.getName());
                        conversation.setConversationIcon(contactUserDataBean.getAvatar());
                        BmobIM.getInstance().updateConversation(conversation);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        UnRepeatToast.showToast(mContext, "服务器不务正业中");
                    }
                });

        RequestQueueSingleton.getRequestQueue(mContext).add(queryContactUserInfo);
    }
}
