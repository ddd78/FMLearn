package com.a78.com.fmlearn;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.a78.com.fmlearn.adapters.IndicatorAdapter;
import com.a78.com.fmlearn.adapters.MainViewPagerAdapter;
import com.a78.com.fmlearn.base.BaseActivity;
import com.a78.com.fmlearn.data.XmDBHelper;
import com.a78.com.fmlearn.interfaces.IPlayCallBack;
import com.a78.com.fmlearn.presenters.PlayPresenter;
import com.a78.com.fmlearn.presenters.RecommendationPresenter;
import com.a78.com.fmlearn.utils.LogUtil;
import com.squareup.picasso.Picasso;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.category.Category;
import com.ximalaya.ting.android.opensdk.model.category.CategoryList;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity implements IPlayCallBack {

    private static final String TAG = "MainActivity";
    private IndicatorAdapter indicatorAdapter;
    private ViewPager mainViewPager;
    private PlayPresenter playPresenter;
    private ImageView controlTitleImage;
    private ImageView startOrStopButton;
    private TextView titleText;
    private TextView autorText;
    private RelativeLayout controlView;
    private RelativeLayout searchRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playPresenter = PlayPresenter.getPlayPresenter();
        playPresenter.registerViewCallBack(this);
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

        startOrStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (playPresenter != null) {
                    if (playPresenter.getIsPlaylistSet()){
                        if (playPresenter.isPlaying()){
                            playPresenter.pause();
                        }else {
                            playPresenter.play();
                        }
                    }else {
                         playFirstRecommodation();
                    }

                }
            }
        });

        controlView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (playPresenter.getIsPlaylistSet()) {
                    startActivity(new Intent(MainActivity.this, PlayActivity.class));
                }else {
                    playFirstRecommodation();
                }
            }
        });
        searchRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                startActivity(intent);
            }
        });
    }

    private void playFirstRecommodation() {
        List<Album> albumList = RecommendationPresenter.getRecommendationInstance().getAlbumList();
        if (albumList != null){
            playPresenter.getPlayListByAlbumId(albumList.get(0).getId());
        }
    }

    private void initView() {
        controlTitleImage = findViewById(R.id.activity_main_control_content_image);
        startOrStopButton = findViewById(R.id.activity_main_control_control_image);
        titleText = findViewById(R.id.activity_main_control_title_text);
        titleText.setSelected(true);
        autorText = findViewById(R.id.activity_main_control_autor_text);

        controlView = findViewById(R.id.activity_main_control_ralative);


        searchRelativeLayout = findViewById(R.id.main_search_relative);

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

    @Override
    public void onPlayStart() {
        if (startOrStopButton != null) {
            startOrStopButton.setImageResource(R.mipmap.pause);
        }
    }

    @Override
    public void onPlayPause() {
        if (startOrStopButton != null) {
            startOrStopButton.setImageResource(R.mipmap.play);
        }
    }

    @Override
    public void onPlayStop() {

    }

    @Override
    public void onPlayError() {

    }

    @Override
    public void nextPlay(Track track) {

    }

    @Override
    public void onPrePlay(Track track) {

    }

    @Override
    public void onListLoad(List<Track> list) {

    }

    @Override
    public void onPlayModeChange(XmPlayListControl.PlayMode playMode) {

    }

    @Override
    public void onProgressChange(int currentProgress, int total) {

    }

    @Override
    public void onAdLoading() {

    }

    @Override
    public void onAdFinished() {

    }

    @Override
    public void onTrackUpdate(Track track, int playIndex) {
        if (track != null) {
            if (!TextUtils.isEmpty(track.getTrackTitle())) {
                titleText.setText(track.getTrackTitle());
            }
            if (!TextUtils.isEmpty(track.getAnnouncer().getNickname())) {
                autorText.setText(track.getAnnouncer().getNickname());
            }
        }
        if (track.getCoverUrlLarge() != null) {
            Picasso.with(this).load(track.getCoverUrlLarge()).into(controlTitleImage);
        }
    }

    @Override
    public void updateListOrder(Boolean isReverse) {

    }
}
