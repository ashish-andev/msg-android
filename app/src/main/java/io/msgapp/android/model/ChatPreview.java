package io.msgapp.android.model;

/**
 * Created by matheus on 11/16/15.
 */
public class ChatPreview {
    public String avatarUrl;
    public int userId;
    public String userName;
    public String messagePreview;
    public String messageWhen;
    public int status;

    // TODO: move these constants to a more suitable class
    public final static int STATUS_LOCAL = 1;
    public final static int STATUS_SENT = 2;
    public final static int STATUS_DELIVERED = 3;
    public final static int STATUS_READ = 4;

    public ChatPreview(String avatarUrl, int userId, String userName, String messagePreview, String messageWhen,
                       int status) {
        this.avatarUrl = avatarUrl;
        this.userId = userId;
        this.userName  = userName;
        this.messagePreview = messagePreview;
        this.messageWhen = messageWhen;
        this.status = status;
    }
}
