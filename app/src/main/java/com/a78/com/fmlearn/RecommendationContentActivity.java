package com.a78.com.fmlearn;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.a78.com.fmlearn.adapters.RecommendationContentAdapter;
import com.a78.com.fmlearn.base.BaseActivity;
import com.a78.com.fmlearn.base.BaseApplication;
import com.a78.com.fmlearn.interfaces.IPlayCallBack;
import com.a78.com.fmlearn.interfaces.IRecommendationContentCallBack;
import com.a78.com.fmlearn.interfaces.ISubCallBack;
import com.a78.com.fmlearn.presenters.PlayPresenter;
import com.a78.com.fmlearn.presenters.RecommendationContentPresenter;
import com.a78.com.fmlearn.presenters.SubPresenter;
import com.a78.com.fmlearn.utils.Constants;
import com.a78.com.fmlearn.utils.ImageBlur;
import com.a78.com.fmlearn.views.UiLoad;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.bezierlayout.BezierLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import java.util.List;

public class RecommendationContentActivity extends BaseActivity implements IRecommendationContentCallBack, UiLoad.OnRetryClickListener, RecommendationContentAdapter.ContentItemClick, IPlayCallBack, ISubCallBack {

    private static final String TAG = "RecommendationContentAc";
    private RecommendationContentPresenter contentPresenter;
    private TextView titleText;
    private TextView autorText;
    private ImageView bigImage;
    private ImageView smallImage;
    private RecyclerView recommendationContentRecycleView;
    private RecommendationContentAdapter recommendationContentAdapter;
    private FrameLayout containerLayout;
    private UiLoad uiLoad;
    private long currentAlbumId = -1;
    private PlayPresenter playPresenter;
    private RelativeLayout startOrStopRelative;
    private ImageView startOrStopImage;
    private TextView startOrStopText;
    private TwinklingRefreshLayout refreshLayout;
    private boolean isLoadMore;
    private String trackTitle;
    private SubPresenter subPresenter;
    private TextView subButton;
    private Album mCUrrentAlbum;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendations_content);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        initView();
        initPresenter();
        initEvent();
    }


    private void initPresenter() {
        contentPresenter = RecommendationContentPresenter.getInstance();
        contentPresenter.registerViewCallBack(this);

        playPresenter = PlayPresenter.getPlayPresenter();
        playPresenter.registerViewCallBack(this);
        upDatePlayUi(playPresenter.isPlaying());

        subPresenter = SubPresenter.getInstance();
        subPresenter.registerViewCallBack(this);
    }

    private void upDatePlayUi(boolean playing) {
        if (playing){
            if (startOrStopText != null && startOrStopText != null){
                startOrStopImage.setImageResource(R.mipmap.pause);
                startOrStopText.setText("点击开始播放");
            }
        }else{
            if (startOrStopText != null && startOrStopText != null) {
                startOrStopImage.setImageResource(R.mipmap.play);
                startOrStopText.setText("继续播放");
            }
        }
    }

    private void initEvent() {

        subButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subPresenter != null) {
//                    upDateSubButton();
                    boolean isSub = subPresenter.isSub(mCUrrentAlbum);
                    if (isSub) {
                        subPresenter.deleteSubscription(mCUrrentAlbum);
                    }else {
                        subPresenter.addSubscription(mCUrrentAlbum);
                    }
                }
            }
        });

        startOrStopRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (playPresenter != null)
                    if (playPresenter.isPlaying()) {
                        playPresenter.pause();
                    } else {
                        playPresenter.play();
                    }
            }
        });
    }

    private View creatSuccessView(ViewGroup container) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_recom_content, container, false);
        refreshLayout = view.findViewById(R.id.recommendation_content_refresh_layout);
        BezierLayout bezierLayout = new BezierLayout(this);
        refreshLayout.setHeaderView(bezierLayout);
        refreshLayout.setMaxHeadHeight(140);
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                Toast.makeText(RecommendationContentActivity.this,"上拉刷新",Toast.LENGTH_SHORT).show();

                BaseApplication.getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RecommendationContentActivity.this,"刷新成功",Toast.LENGTH_SHORT).show();
                        refreshLayout.finishRefreshing();
                    }
                },2000);
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                contentPresenter.loadMore();
                isLoadMore = true;
            }
        });
        recommendationContentRecycleView = view.findViewById(R.id.recommendation_content_recyclelist);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recommendationContentRecycleView.setLayoutManager(linearLayoutManager);
        recommendationContentAdapter = new RecommendationContentAdapter();
        recommendationContentRecycleView.setAdapter(recommendationContentAdapter);
        recommendationContentAdapter.setContentItemClick(this);
        return view;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        contentPresenter.unregisterViewCallback(this);
        playPresenter.unregisterViewCallback(this);
        subPresenter.unregisterViewCallback(this);
    }

    private void initView() {
        containerLayout = findViewById(R.id.recommendation_content_container);
        titleText = findViewById(R.id.recommendation_content_title_text);
        autorText = findViewById(R.id.recommendation_content_autor_text);
        bigImage = findViewById(R.id.recommendation_content_big_image);
        smallImage = findViewById(R.id.recommendation_content_small_image);
        startOrStopImage = findViewById(R.id.recommendation_content_play_image);
        startOrStopText = findViewById(R.id.recommendation_content_play_text);
        startOrStopText.setSelected(true);

        subButton = findViewById(R.id.recommendation_content_sub_button);

        if (uiLoad == null) {
            uiLoad = new UiLoad(this) {
                @Override
                protected View getSuccessView(ViewGroup container) {
                    return creatSuccessView(container);
                }
            };
            uiLoad.setOnRetryClickListenser(this);
            containerLayout.removeAllViews();
            containerLayout.addView(uiLoad);
        }

        startOrStopRelative = findViewById(R.id.recommendation_content_play_state_relative);

    }

    private void upDateSubButton(){
        if (subPresenter != null) {
            boolean isSub = subPresenter.isSub(mCUrrentAlbum);
            if (isSub){
                subButton.setText("取消订阅");
            }else {
                subButton.setText("+ 订阅");
            }
        }
    }

    @Override
    public void onContentListLoad(List<Track> tracks) {

        if (isLoadMore){
            isLoadMore = false;
            refreshLayout.finishLoadmore();
        }

        recommendationContentAdapter.update(tracks);
        if (tracks == null || tracks.size() == 0){
            uiLoad.updateUi(UiLoad.UiLoadType.EMPTY);
        }else {
            uiLoad.updateUi(UiLoad.UiLoadType.SUCCESS);
        }

    }

    @Override
    public void onContentLoad(Album album) {
        if (album != null) {
            this.mCUrrentAlbum = album;
            currentAlbumId = album.getId();
            contentPresenter.getRecommendationContent((int) currentAlbumId,1);
            uiLoad.updateUi(UiLoad.UiLoadType.LOADING);
            titleText.setText(album.getAlbumTitle());
            autorText.setText(album.getAnnouncer().getNickname());
            Picasso.with(this).load(album.getCoverUrlLarge()).into(bigImage, new Callback() {
                @Override
                public void onSuccess() {
                    ImageBlur.makeBlur(bigImage,RecommendationContentActivity.this);
                }

                @Override
                public void onError() {
                }
            });
            Picasso.with(this).load(album.getCoverUrlLarge()).into(smallImage);
        }
    }


    @Override
    public void onContentLoadError() {
        uiLoad.updateUi(UiLoad.UiLoadType.ERROR);
    }

    @Override
    public void loadMoreFinish(int size) {
        if (size > 0){
            Toast.makeText(this, "已加载" + size + "条新内容！", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "暂无新内容！", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void reFreshFinsh(int size) {

    }

    @Override
    public void OnRetryClick() {
        contentPresenter.getRecommendationContent((int) currentAlbumId,1);
        uiLoad.updateUi(UiLoad.UiLoadType.LOADING);
    }

    @Override
    public void onContentItemClick(List<Track> trackList, int position) {
        PlayPresenter playPresenter = PlayPresenter.getPlayPresenter();
        playPresenter.setPlayList(trackList,position);

        Intent intent = new Intent(this,PlayActivity.class);
        startActivity(intent);
    }

    @Override
    public void onPlayStart() {
        if (startOrStopText != null && startOrStopText != null){
            startOrStopImage.setImageResource(R.mipmap.pause);
            if (!TextUtils.isEmpty(trackTitle)) {
                startOrStopText.setText(trackTitle);
            }else {
                startOrStopText.setText("停止播放");
            }
        }
    }

    @Override
    public void onPlayPause() {
        if (startOrStopText != null && startOrStopText != null) {
            startOrStopImage.setImageResource(R.mipmap.play);
            startOrStopText.setText("继续播放");
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
        if (track != null){
            trackTitle = track.getTrackTitle();
        }
    }

    @Override
    public void updateListOrder(Boolean isReverse) {

    }

    @Override
    public void onAddResult(boolean result) {
        if (result){
            Toast.makeText(this, "订阅成功！", Toast.LENGTH_SHORT).show();
            subPresenter.getSubscription();
        }else {
            Toast.makeText(this, "订阅失败！", Toast.LENGTH_SHORT).show();
            subPresenter.getSubscription();
        }
    }

    @Override
    public void onDeleteResult(boolean result) {
        if(result){
            Toast.makeText(this, "取消订阅成功！", Toast.LENGTH_SHORT).show();
            subPresenter.getSubscription();

        }else {
            Toast.makeText(this, "取消订阅失败！", Toast.LENGTH_SHORT).show();
            subPresenter.getSubscription();
        }
    }

    @Override
    public void onSubscriptionLoad(List<Album> albums) {
        upDateSubButton();
    }

    @Override
    public void onSunFull() {
        Toast.makeText(this, "最多订阅" + Constants.SUB_MAX_NUM + "个", Toast.LENGTH_SHORT).show();
    }
}
