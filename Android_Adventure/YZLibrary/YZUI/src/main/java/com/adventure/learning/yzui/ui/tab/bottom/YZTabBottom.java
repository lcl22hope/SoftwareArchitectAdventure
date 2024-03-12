package com.adventure.learning.yzui.ui.tab.bottom;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.adventure.learning.yzlibrary.log.Logger;
import com.adventure.learning.yzui.R;
import com.adventure.learning.yzui.ui.tab.common.YZTab;

public class YZTabBottom extends RelativeLayout implements YZTab<YZTabBottomInfo<?>> {

    public YZTabBottomInfo<?> getTabInfo() {
        return mTabInfo;
    }

    private YZTabBottomInfo<?> mTabInfo;

    public ImageView getTabImageView() {
        return tabImageView;
    }

    public TextView getTabIconView() {
        return tabIconView;
    }

    public TextView getTabNameView() {
        return tabNameView;
    }

    private ImageView tabImageView;
    private TextView tabIconView;
    private TextView tabNameView;

    public YZTabBottom(Context context) {
        this(context, null);
    }

    public YZTabBottom(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YZTabBottom(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
        init();
    }

    public YZTabBottom(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.yz_tab_bottom, this);
        tabImageView = findViewById(R.id.iv_image);
        tabIconView = findViewById(R.id.tv_icon);
        tabNameView = findViewById(R.id.tv_name);
        
    }

    /**
     * @param data
     */
    @Override
    public void setTabInfo(@NonNull YZTabBottomInfo<?> data) {
        this.mTabInfo = data;
        inflateInfo(false, true);
    }

    private void inflateInfo(boolean selected, boolean init) {
        if (mTabInfo.tabType == YZTabBottomInfo.TabType.ICON) {
            if (init) {
                // iconfont 模式下图片需要隐藏
                tabImageView.setVisibility(GONE);
                tabIconView.setVisibility(VISIBLE);
                Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), mTabInfo.iconFont);
                tabIconView.setTypeface(typeface);
                if (!TextUtils.isEmpty(mTabInfo.name)) {
                    tabNameView.setText(mTabInfo.name);
                }
            }
            if (selected) {
                tabIconView.setText(TextUtils.isEmpty(mTabInfo.selectedIconName) ? mTabInfo.defaultIconName : mTabInfo.selectedIconName);
                tabIconView.setTextColor(getTextColor(mTabInfo.tintColor));
                tabNameView.setTextColor(getTextColor(mTabInfo.tintColor));
            } else {
                tabIconView.setText(mTabInfo.defaultIconName);
                tabIconView.setTextColor(getTextColor(mTabInfo.defaultColor));
                tabNameView.setTextColor(getTextColor(mTabInfo.defaultColor));
            }
        } else if (mTabInfo.tabType == YZTabBottomInfo.TabType.BITMAP) {
            if (init) {
                tabImageView.setVisibility(VISIBLE);
                tabIconView.setVisibility(GONE);
                if (!TextUtils.isEmpty(mTabInfo.name)) {
                    tabNameView.setText(mTabInfo.name);
                }
            }
            if (selected) {
                tabImageView.setImageBitmap(mTabInfo.selectedBitmap);
            } else {
                tabImageView.setImageBitmap(mTabInfo.defaultBitmap);
            }
        }
    }

    private int getTextColor(Object color) {
        if (color instanceof String) {
            return (int) Color.parseColor((String) color);
        } else {
            return (int) color;
        }
    }

    /**
     * 改变某个 tab 的高度
     *
     * @param height
     */
    @Override
    public void resetHeight(int height) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.height = height;
        // 重置高度
        setLayoutParams(layoutParams);
        getTabImageView().setVisibility(GONE);
    }

    /**
     * @param index
     * @param prevInfo
     * @param nextInfo
     */
    @Override
    public void onTabSelectedChange(int index, @NonNull YZTabBottomInfo<?> prevInfo, @NonNull YZTabBottomInfo<?> nextInfo) {
        // case1: 首次进入
        // case2: 重复选择自己
        if (prevInfo != mTabInfo && nextInfo != mTabInfo || prevInfo == nextInfo) {
            Logger.et("YZTabBottom", "onTabSelectedChange: 重复选择自己");
            return;
        }
        // case2: 切换
        if (prevInfo == mTabInfo) {
            inflateInfo(false, false);
        } else {
            inflateInfo(true, false);
        }
    }
}
