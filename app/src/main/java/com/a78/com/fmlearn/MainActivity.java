package com.a78.com.fmlearn;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.a78.com.fmlearn.adapters.IndicatorAdapter;
import com.a78.com.fmlearn.adapters.MainViewPagerAdapter;
import com.a78.com.fmlearn.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.category.Category;
import com.ximalaya.ting.android.opensdk.model.category.CategoryList;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends FragmentActivity {

    private static final String TAG = "MainActivity";
    private int num;
    private IndicatorAdapter indicatorAdapter;
    private ViewPager mainViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initEvent();
    }

    private void initEvent() {
        indicatorAdapter.setOnIndicatorTapClickListenser(new IndicatorAdapter.OnIndicatorTapClickListenser() {
            @Override
            public void onTabClick(int index) {
                mainViewPager.setCurrentItem(index);
            }
        });
    }

    private void initView() {
        MagicIndicator magicIndicator = findViewById(R.id.main_indicator);
        magicIndicator.setBackgroundColor(getResources().getColor(R.color.maincolor));

        indicatorAdapter = new IndicatorAdapter(this);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(indicatorAdapter);

        magicIndicator.setNavigator(commonNavigator);

        mainViewPager = findViewById(R.id.main_viewpager);
        MainViewPagerAdapter mainViewPagerAdapter = new MainViewPagerAdapter(this.getSupportFragmentManager());
        mainViewPager.setAdapter(mainViewPagerAdapter);


        ViewPagerHelper.bind(magicIndicator, mainViewPager);
    }
}
