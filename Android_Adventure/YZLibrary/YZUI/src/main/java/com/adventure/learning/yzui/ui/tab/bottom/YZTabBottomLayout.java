package com.adventure.learning.yzui.ui.tab.bottom;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.adventure.learning.yzlibrary.log.Logger;
import com.adventure.learning.yzlibrary.util.DisplayUtil;
import com.adventure.learning.yzui.R;
import com.adventure.learning.yzui.ui.tab.common.YZTabLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class YZTabBottomLayout extends FrameLayout implements YZTabLayout<YZTabBottom, YZTabBottomInfo<?>> {

    private List<OnTabSelectedListener<YZTabBottomInfo<?>>> tabSelectedListeners = new ArrayList<>();

    private YZTabBottomInfo<?> selectedInfo;

    public float getTabAlpha() {
        return bottomAlpha;
    }

    public void setTabAlpha(float tabAlpha) {
        this.bottomAlpha = tabAlpha;
    }

    public float getTabHeight() {
        return tabBottomHeight;
    }

    public void setTabHeight(float tabHeight) {
        this.tabBottomHeight = tabHeight;
    }

    public float getBottomLineHeight() {
        return bottomLineHeight;
    }

    public void setBottomLineHeight(float bottomLineHeight) {
        this.bottomLineHeight = bottomLineHeight;
    }

    public String getBottomLineColor() {
        return bottomLineColor;
    }

    public void setBottomLineColor(String bottomLineColor) {
        this.bottomLineColor = bottomLineColor;
    }

    private float bottomAlpha = 1f;
    /**
     * TabBottom 高度
     * */
    private float tabBottomHeight = 50f;
    /**
     * TabBottom 的头部线条高度
     * */
    private float bottomLineHeight = 0.5f;
    /**
     * TabBottom 的头部线条颜色
     * */
    private String bottomLineColor = "#dfe0e1";

    private List<YZTabBottomInfo<?>> infoList;

    public YZTabBottomLayout(@NonNull Context context) {
        super(context);
    }

    public YZTabBottomLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public YZTabBottomLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public YZTabBottomLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * 通过 data 找对应的 TabBottom
     * @param info
     * @return
     */
    @Override
    public YZTabBottom findTab(@NonNull YZTabBottomInfo<?> info) {
        ViewGroup vg = findViewWithTag(TAG_TAB_BOTTOM);
        for (int i = 0; i < vg.getChildCount(); i++) {
           View child = vg.getChildAt(i);
           if (child instanceof YZTabBottom) {
               YZTabBottom tab = (YZTabBottom) child;
               if (tab.getTabInfo() == info) {
                   return tab;
               }
           }
        }
        return null;
    }

    /**
     * @param listener
     */
    @Override
    public void addTabSelectedChangeListener(OnTabSelectedListener<YZTabBottomInfo<?>> listener) {
        tabSelectedListeners.add(listener);
    }

    /**
     * @param defaultInfo
     */
    @Override
    public void defaultSelected(@NonNull YZTabBottomInfo<?> defaultInfo) {
        onTabSelected(defaultInfo);
    }

    private static final String TAG_TAB_BOTTOM = "TAG_TAB_BOTTOM";
    /**
     * @param infoList
     */
    @Override
    public void inflateInfo(@NonNull List<YZTabBottomInfo<?>> infoList) {
        if (infoList.isEmpty()) {
            return;
        }
        this.infoList = infoList;
        // 移除之前已经添加的View
        // 数组第0个元素是界面中间的内容，因此需要从1开始
        for (int i = getChildCount()-1; i > 0; i--) {
            removeViewAt(i);
        }
        selectedInfo = null;
        addBackground();
        //清除之前添加的 TabBottom listener, Tips：java foreach remove 问题
        Iterator<OnTabSelectedListener<YZTabBottomInfo<?>>> iterator = tabSelectedListeners.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() instanceof YZTabBottom) {
                iterator.remove();
            }
        }

        int height = DisplayUtil.dp2px(tabBottomHeight, getResources());
        // 如果使用 LinearLayout 当动态改变 tabBottom height 时， gravity 会失效，因此使用 FrameLayout
        FrameLayout fy = new FrameLayout(getContext());
        fy.setTag(TAG_TAB_BOTTOM);
        int width = DisplayUtil.getDisplayWidthInPx(getContext()) / infoList.size();
        for (int i = 0; i < infoList.size(); i++) {
            YZTabBottomInfo<?> info = infoList.get(i);
            LayoutParams params = new LayoutParams(width, height);
            params.gravity = Gravity.BOTTOM;
            params.leftMargin = i * width;

            YZTabBottom tabBottom = new YZTabBottom(getContext());
            tabSelectedListeners.add(tabBottom);
            tabBottom.setTabInfo(info);
            fy.addView(tabBottom, params);
            tabBottom.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onTabSelected(info);
                }
            });
        }

        LayoutParams flParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        flParams.gravity = Gravity.BOTTOM;
        addBottomLine();
        addView(fy, flParams);
    }

    private void addBottomLine(){
        View bottomLine = new View(getContext());
        bottomLine.setBackgroundColor(Color.parseColor(bottomLineColor));

        LayoutParams bottomLineParams = new LayoutParams(LayoutParams.MATCH_PARENT, DisplayUtil.dp2px(bottomLineHeight, getResources()));
        bottomLineParams.gravity = Gravity.BOTTOM;
        bottomLineParams.bottomMargin = DisplayUtil.dp2px(tabBottomHeight - bottomLineHeight, getResources());
        addView(bottomLine, bottomLineParams);
        bottomLine.setAlpha(bottomAlpha);
    }

    private void onTabSelected(@NonNull YZTabBottomInfo<?> nextInfo) {
        for (OnTabSelectedListener<YZTabBottomInfo<?>> listener : tabSelectedListeners) {
            listener.onTabSelectedChange(infoList.indexOf(nextInfo), selectedInfo, nextInfo);
        }
        this.selectedInfo = nextInfo;
    }

    private void addBackground() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.yz_bottom_layout_bg, null);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, DisplayUtil.dp2px(tabBottomHeight, getResources()));
        params.gravity = Gravity.BOTTOM;
        addView(view, params);
        view.setAlpha(bottomAlpha);
    }
}
