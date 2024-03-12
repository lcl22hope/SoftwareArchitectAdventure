package com.adventure.learning.yzui.ui.tab.bottom;

import android.graphics.Bitmap;

import androidx.fragment.app.Fragment;

public class YZTabBottomInfo<Color> {
    public enum TabType {
        BITMAP, ICON
    }
    /**
     * 代表每个 Tab 的 Fragment
     * */
    public Class<? extends Fragment> fragmentClass;
    public String name;
    /**
     * 默认图标
     * */
    public Bitmap defaultBitmap;
    /**
     * 选中图标
     * */
    public Bitmap selectedBitmap;
    public String iconFont;

    /**
     * Tips：在java代码中直接设置 iconfont 字符串无效，需要定义在 string.xml 中
     * */
    public String defaultIconName;
    public String selectedIconName;
    public Color defaultColor;
    public Color tintColor;
    public TabType tabType;

    public YZTabBottomInfo(String name, Bitmap defaultBitmap, Bitmap selectedBitmap) {
        this.name = name;
        this.defaultBitmap = defaultBitmap;
        this.selectedBitmap = selectedBitmap;
        this.tabType = TabType.BITMAP;
    }

    public YZTabBottomInfo(String name, String iconFont, String defaultIconName,String selectedIconName, Color defaultColor, Color tintColor) {
        this.name = name;
        this.iconFont = iconFont;
        this.defaultIconName = defaultIconName;
        this.selectedIconName = selectedIconName;
        this.defaultColor = defaultColor;
        this.tintColor = tintColor;
        this.tabType = TabType.ICON;
    }
}
