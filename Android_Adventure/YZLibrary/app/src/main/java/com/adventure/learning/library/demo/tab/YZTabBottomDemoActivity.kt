package com.adventure.learning.library.demo.tab

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.adventure.learning.library.R
import com.adventure.learning.yzui.ui.tab.bottom.YZTabBottomInfo
import com.adventure.learning.yzui.ui.tab.bottom.YZTabBottomLayout

class YZTabBottomDemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yztab_bottom_demo);
        initTabBottomLayout();
    }

    private fun initTabBottomLayout() {
        val tabBottomLayout: YZTabBottomLayout = findViewById<YZTabBottomLayout>(R.id.yz_tablayout);
        tabBottomLayout.tabAlpha = 0.85f;
        val bottomInfoList: MutableList<YZTabBottomInfo<*>> = ArrayList();
        val homeInfo = YZTabBottomInfo(
            "首页",
            "fonts/iconfont.ttf",
            getString(R.string.if_home),
            null,
            "#656667",
            "#d44949"
        )
        val favoriteInfo = YZTabBottomInfo(
            "收藏",
            "fonts/iconfont.ttf",
            getString(R.string.if_favorite),
            null,
            "#656667",
            "#d44949"
        )
        val categoryInfo = YZTabBottomInfo(
            "分类",
            "fonts/iconfont.ttf",
            getString(R.string.if_category),
            null,
            "#656667",
            "#d44949"
        )
        val recommendInfo = YZTabBottomInfo(
            "推荐",
            "fonts/iconfont.ttf",
            getString(R.string.if_recommend),
            null,
            "#656667",
            "#d44949"
        )
        val profileInfo = YZTabBottomInfo(
            "我的",
            "fonts/iconfont.ttf",
            getString(R.string.if_profile),
            null,
            "#656667",
            "#d44949"
        )
        bottomInfoList.add(homeInfo);
        bottomInfoList.add(favoriteInfo);
        bottomInfoList.add(categoryInfo);
        bottomInfoList.add(recommendInfo);
        bottomInfoList.add(profileInfo);
        tabBottomLayout.inflateInfo(bottomInfoList);
        tabBottomLayout.addTabSelectedChangeListener{ _, _, nextInfo ->
            Toast.makeText(this, nextInfo.name, Toast.LENGTH_SHORT).show();
        }
        tabBottomLayout.defaultSelected(homeInfo);
    }
}