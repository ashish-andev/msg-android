package io.msgapp.android;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.graphics.drawable.DrawableCompat;

/**
 * Created by matheus on 11/13/15.
 */
public class Util {

    // android:tint is only available on API 21+
    public static void tint(Context context, int drawableId, int colorId) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Drawable drawable = DrawableCompat.wrap(context.getResources().getDrawable(drawableId));
            DrawableCompat.setTint(drawable, context.getResources().getColor(colorId));
        }
    }
}
