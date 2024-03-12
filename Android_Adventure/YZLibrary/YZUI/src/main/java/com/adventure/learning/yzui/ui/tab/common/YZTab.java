package com.adventure.learning.yzui.ui.tab.common;

import androidx.annotation.NonNull;
import androidx.annotation.Px;

/**
 * YZTab 对外的接口
 * */
public interface YZTab<D> extends YZTabLayout.OnTabSelectedListener<D> {
    void setTabInfo(@NonNull D data);
    /**
     * 动态修改某个 item 的大小
     *
     * @param height
     * */
    void resetHeight(@Px int height);
}
