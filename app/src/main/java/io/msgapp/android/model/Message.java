package io.msgapp.android.model;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by matheus on 12/25/15.
 */
public class Message extends RealmObject {
    @Ignore public final static int STATUS_LOCAL = 1;
    @Ignore public final static int STATUS_SENT = 2;
    @Ignore public final static int STATUS_DELIVERED = 3;
    @Ignore public final static int STATUS_READ = 4;

    private int placeholder;

    public int getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(int placeholder) {
        this.placeholder = placeholder;
    }

    public Message() {}

}
