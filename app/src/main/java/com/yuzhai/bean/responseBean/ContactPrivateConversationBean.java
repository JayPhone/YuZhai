package com.yuzhai.bean.responseBean;

import android.text.TextUtils;

import com.yuzhai.yuzhaiwork.R;

import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMConversationType;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMMessageType;

/**
 * 私聊会话
 * Created by Administrator on 2016/5/25.
 */
public class ContactPrivateConversationBean extends ContactConversationBean {

    private BmobIMConversation conversation;
    private BmobIMMessage lastMsg;

    public ContactPrivateConversationBean(BmobIMConversation conversation) {
        this.conversation = conversation;
        cType = BmobIMConversationType.setValue(conversation.getConversationType());
        cId = conversation.getConversationId();
        if (cType == BmobIMConversationType.PRIVATE) {
            cName = conversation.getConversationTitle();
            if (TextUtils.isEmpty(cName)) cName = cId;
        } else {
            cName = "未知会话";
        }
        List<BmobIMMessage> msgs = conversation.getMessages();
        if (msgs != null && msgs.size() > 0) {
            lastMsg = msgs.get(0);
        }
    }

    public BmobIMConversation getConversation() {
        return conversation;
    }

    public BmobIMMessage getLastMsg() {
        return lastMsg;
    }

    @Override
    public void readAllMessages() {
        conversation.updateLocalCache();
    }

    @Override
    public Object getAvatar() {
        if (cType == BmobIMConversationType.PRIVATE) {
            String avatar = conversation.getConversationIcon();
            if (TextUtils.isEmpty(avatar)) {//头像为空，使用默认头像
                return R.mipmap.ic_launcher;
            } else {
                return avatar;
            }
        } else {
            return R.mipmap.ic_launcher;
        }
    }

    @Override
    public String getLastMessageContent() {
        if (lastMsg != null) {
            String content = lastMsg.getContent();
            if (lastMsg.getMsgType().equals(BmobIMMessageType.TEXT.getType()) || lastMsg.getMsgType().equals("agree")) {
                return content;
            } else if (lastMsg.getMsgType().equals(BmobIMMessageType.IMAGE.getType())) {
                return "[图片]";
            } else if (lastMsg.getMsgType().equals(BmobIMMessageType.VOICE.getType())) {
                return "[语音]";
            } else if (lastMsg.getMsgType().equals(BmobIMMessageType.LOCATION.getType())) {
                return "[位置]";
            } else if (lastMsg.getMsgType().equals(BmobIMMessageType.VIDEO.getType())) {
                return "[视频]";
            } else {//开发者自定义的消息类型，需要自行处理
                return "[未知]";
            }
        } else {//防止消息错乱
            return "";
        }
    }

    @Override
    public long getLastMessageTime() {
        if (lastMsg != null) {
            return lastMsg.getCreateTime();
        } else {
            return 0;
        }
    }

    @Override
    public int getUnReadCount() {
        return (int) BmobIM.getInstance().getUnReadCount(conversation.getConversationId());
    }
}
