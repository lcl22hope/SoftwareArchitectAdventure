package com.adventure.learning.yzlibrary.log;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.adventure.learning.yzlibrary.util.DisplayUtil;

/**
 * ViewLogPrinter 的辅助类
 * 1. 控制ViewLogPriinter的显示和隐藏
 * */
public class ViewLogPrinterProvider {
    private FrameLayout roorView;
    private View floatingView;
    private boolean isOpen;
    private FrameLayout logView;
    private RecyclerView recyclerView;

    public ViewLogPrinterProvider(FrameLayout roorView, RecyclerView recyclerView) {
        this.roorView = roorView;
        this.recyclerView = recyclerView;
    }

    private static final String TAG_FLOATING_VIEW = "floating_view";
    private static final String TAG_LOG_VIEW = "log_view";
    /**
     * 显示悬浮窗：界面中有才添加
     * */
    public void showFloatingView() {
        //如果界面中已经添加了 FloatingView 悬浮窗
        if (roorView.findViewWithTag(TAG_FLOATING_VIEW) != null) {
            return;
        }
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM | Gravity.END;
        View floatingView = genFloatingView();
        floatingView.setTag(TAG_FLOATING_VIEW);
        floatingView.setBackgroundColor(Color.GRAY);
        floatingView.setAlpha(0.8f);
        params.bottomMargin = DisplayUtil.dp2px(100, roorView.getResources());
        roorView.addView(genFloatingView(), params);
    }

    /**
     * 隐藏悬浮窗
     * */
    public void closeFloatingView() {
        isOpen = false;
        roorView.removeView(genFloatingView());
    }

    private View genFloatingView() {
        if (floatingView != null) {
            return floatingView;
        }
        TextView textView = new TextView(recyclerView.getContext());
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isOpen) {
                    showLogView();
                }
            }
        });
        textView.setText("YZLogger");
        return floatingView = textView;
    }

    private void showLogView() {
        if (roorView.findViewWithTag(TAG_LOG_VIEW) != null) {
            return;
        }
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.dp2px(160, roorView.getResources()));
        params.gravity = Gravity.BOTTOM;
        View logView = genLogView();
        logView.setTag(TAG_LOG_VIEW);
        roorView.addView(genLogView(), params);
        isOpen = true;
    }

    private View genLogView() {
        if (logView != null) {
            return logView;
        }
        FrameLayout logView = new FrameLayout(roorView.getContext());
        logView.setBackgroundColor(Color.LTGRAY);
        logView.addView(recyclerView);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.END;
        TextView closeView = new TextView(roorView.getContext());
        closeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               closeLogView();
            }
        });
        closeView.setText("Close");
        logView.addView(closeView, params);
        return this.logView = logView;
    }

    /**
     * 关闭LogView
     * */
    private void closeLogView() {
        isOpen = false;
        roorView.removeView(logView);
    }

}
