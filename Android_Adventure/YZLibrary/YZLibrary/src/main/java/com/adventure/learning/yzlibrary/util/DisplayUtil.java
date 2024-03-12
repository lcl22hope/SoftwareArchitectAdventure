package com.adventure.learning.yzlibrary.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;
import android.view.WindowMetrics;

import androidx.annotation.NonNull;

public class DisplayUtil {
    public static int dp2px(float dp, Resources resources) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
    }

    public static int getDisplayWidthInPx(@NonNull Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Use WindowMetrics for API 30 and above
            WindowMetrics windowMetrics = wm.getCurrentWindowMetrics();
            return windowMetrics.getBounds().width();
        } else {
            // Use DisplayMetrics for API 29 and below
            DisplayMetrics displayMetrics = new DisplayMetrics();
            context.getDisplay().getMetrics(displayMetrics);
            return displayMetrics.widthPixels;
        }
    }

    public static int getDisplayHeightInPx(@NonNull Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Use WindowMetrics for API 30 and above
            WindowMetrics windowMetrics = wm.getCurrentWindowMetrics();
            return windowMetrics.getBounds().height();
        } else {
            // Use DisplayMetrics for API 29 and below
            DisplayMetrics displayMetrics = new DisplayMetrics();
            context.getDisplay().getMetrics(displayMetrics);
            return displayMetrics.heightPixels;
        }
    }
}
