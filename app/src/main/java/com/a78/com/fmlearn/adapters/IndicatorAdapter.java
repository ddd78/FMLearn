package com.a78.com.fmlearn.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.a78.com.fmlearn.R;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

/**
 * Created by home on 2020/1/16.
 */

public class IndicatorAdapter extends CommonNavigatorAdapter{


    private final String[] mTitles;
    private OnIndicatorTapClickListenser OnClickLisenter;

    public IndicatorAdapter(Context context) {
        mTitles = context.getResources().getStringArray(R.array.iddicator_title);
    }

    @Override
    public int getCount() {
        if (mTitles != null){
            return mTitles.length;
        }
        return 0;
    }

    @Override
    public IPagerTitleView getTitleView(Context context, final int index) {
        SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
        simplePagerTitleView.setText(mTitles[index]);
        simplePagerTitleView.setNormalColor(context.getResources().getColor(R.color.Gainsboro));
        simplePagerTitleView.setSelectedColor(context.getResources().getColor(R.color.white));
        simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (OnClickLisenter != null){
                    OnClickLisenter.onTabClick(index);
                }
            }
        });
        return simplePagerTitleView;
    }

    @Override
    public IPagerIndicator getIndicator(Context context) {
        LinePagerIndicator indicator = new LinePagerIndicator(context);
        indicator.setColors(context.getResources().getColor(R.color.white));
        return indicator;
    }

    public void setOnIndicatorTapClickListenser(OnIndicatorTapClickListenser listenser){
        this.OnClickLisenter = listenser;
    }

    public interface OnIndicatorTapClickListenser{
        void onTabClick(int index);
    }
}
