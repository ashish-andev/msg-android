package io.msgapp.android.model;

import io.realm.RealmObject;

/**
 * Created by matheus on 11/16/15.
 */
public class ChatPreview extends RealmObject {
    private String avatarUrl;
    private long userId;
    private String userName;
    private String messagePreview;
    private String messageWhen;
    private int status;

    public ChatPreview(String avatarUrl, int userId, String userName, String messagePreview, String messageWhen,
                       int status) {
        this.avatarUrl = avatarUrl;
        this.userId = userId;
        this.userName  = userName;
        this.messagePreview = messagePreview;
        this.messageWhen = messageWhen;
        this.status = status;
    }

    public ChatPreview() {}

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessagePreview() {
        return messagePreview;
    }

    public void setMessagePreview(String messagePreview) {
        this.messagePreview = messagePreview;
    }

    public String getMessageWhen() {
        return messageWhen;
    }

    public void setMessageWhen(String messageWhen) {
        this.messageWhen = messageWhen;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
