package com.adventure.learning.yzui.ui.tab.common;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import java.util.List;

public interface YZTabLayout<Tab extends ViewGroup, D> {
    Tab findTab(@NonNull D data);

    void addTabSelectedChangeListener(OnTabSelectedListener<D> listener);

    void defaultSelected(@NonNull D defaultInfo);

    void inflateInfo(@NonNull List<D> data);

    interface OnTabSelectedListener<D> {
        void onTabSelectedChange(int index, D prevInfo, D nextInfo);
    }
}
