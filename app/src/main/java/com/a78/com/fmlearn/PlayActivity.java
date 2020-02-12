package com.a78.com.fmlearn;

import android.animation.ValueAnimator;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import com.a78.com.fmlearn.adapters.PlayViewPagerAdapter;
import com.a78.com.fmlearn.base.BaseActivity;
import com.a78.com.fmlearn.interfaces.IPlayCallBack;
import com.a78.com.fmlearn.presenters.PlayPresenter;
import com.a78.com.fmlearn.views.PlayPopView;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayActivity extends BaseActivity implements IPlayCallBack, PlayPopView.PlayPopViewClickListener, PlayPopView.PlayPopViewActionListener {

    private ImageView startOrStopButton;
    private PlayPresenter playPresenter;
    private SimpleDateFormat minutesDateFormate = new SimpleDateFormat("mm:ss");
    private SimpleDateFormat hoursDateFormate = new SimpleDateFormat("hh:mm:ss");
    private TextView totalTimeText;
    private TextView currentTimeText;
    private SeekBar playSeekBar;
    private boolean isSeekBarTouch = false;
    private int currentProgress;
    private ImageView nextImageButton;
    private ImageView preImageButton;
    private TextView titleText;
    private static final String TAG = "PlayActivity";
    private ViewPager contentViewPager;
    private PlayViewPagerAdapter playViewPagerAdapter;
    private ImageView swithchButton;
    private XmPlayListControl.PlayMode currentPlayMode = XmPlayListControl.PlayMode.PLAY_MODEL_LIST;
    private static Map<XmPlayListControl.PlayMode, XmPlayListControl.PlayMode>  modePlayModeMap = new HashMap<>();

    static {
        modePlayModeMap.put(XmPlayListControl.PlayMode.PLAY_MODEL_LIST, XmPlayListControl.PlayMode.PLAY_MODEL_LIST_LOOP);
        modePlayModeMap.put(XmPlayListControl.PlayMode.PLAY_MODEL_LIST_LOOP, XmPlayListControl.PlayMode.PLAY_MODEL_RANDOM);
        modePlayModeMap.put(XmPlayListControl.PlayMode.PLAY_MODEL_RANDOM, XmPlayListControl.PlayMode.PLAY_MODEL_SINGLE_LOOP);
        modePlayModeMap.put(XmPlayListControl.PlayMode.PLAY_MODEL_SINGLE_LOOP, XmPlayListControl.PlayMode.PLAY_MODEL_LIST);

    }

    private ImageView selectListButton;
    private PlayPopView playPopView;
    private ValueAnimator enterValueAnimator;
    private ValueAnimator exitValueAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        playPresenter = PlayPresenter.getPlayPresenter();
        playPresenter.registerViewCallBack(this);
        initView();
        initEvent();
        initBgAnimation();

    }

    private void initBgAnimation() {
        enterValueAnimator = ValueAnimator.ofFloat(1.0f, 0.6f);
        enterValueAnimator.setDuration(500);
        enterValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                updateBackAlpha((Float) valueAnimator.getAnimatedValue());
            }
        });

        exitValueAnimator = ValueAnimator.ofFloat(0.6f, 1.0f);
        exitValueAnimator.setDuration(500);
        exitValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                updateBackAlpha((Float) valueAnimator.getAnimatedValue());
            }
        });


    }

    @Override
    protected void onDestroy() {
        playPresenter.registerViewCallBack(this);
        super.onDestroy();

    }

    private void initEvent() {
        startOrStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (playPresenter.isPlaying()){
                    playPresenter.pause();
                }else {
                    playPresenter.play();
                }
            }
        });

        playSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b){
                    currentProgress = i;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isSeekBarTouch = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                playPresenter.seekTo(currentProgress);
                isSeekBarTouch = false;
            }
        });

        nextImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playPresenter.playNext();
            }
        });

        preImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playPresenter.playPre();
            }
        });

        swithchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playPresenter.switchPlayModel(modePlayModeMap.get(currentPlayMode));
            }
        });

        selectListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playPopView.showAtLocation(view, Gravity.BOTTOM,0,0);
//                updateBackAlpha(0.6f);
                enterValueAnimator.start();
            }
        });

        contentViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                playPresenter.playByIndex(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        playPopView.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
//                updateBackAlpha(1.0f);
                exitValueAnimator.start();
            }
        });

        playPopView.setPlayPopViewClick(this);
        playPopView.setPlayPopViewActionListener(this);
    }

    private void updateBackAlpha(float v) {
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.alpha = v;
        window.setAttributes(layoutParams);
    }

    private void initView() {
        startOrStopButton = findViewById(R.id.play_or_pause_btn);
        totalTimeText = findViewById(R.id.play_total_time_position);
        currentTimeText = findViewById(R.id.play_current_time_postion_text);
        playSeekBar = findViewById(R.id.paly_seekbar);
        nextImageButton = findViewById(R.id.play_next);
        preImageButton = findViewById(R.id.play_pre);
        titleText = findViewById(R.id.play_title_text);
        contentViewPager = findViewById(R.id.play_content_viewpager);
        if (swithchButton == null) {
            swithchButton = findViewById(R.id.player_mode_switch_btn);
        }
        playViewPagerAdapter = new PlayViewPagerAdapter();
        contentViewPager.setAdapter(playViewPagerAdapter);
        selectListButton = findViewById(R.id.player_list);
        playPopView = new PlayPopView();



        playPresenter.getPlayList();

        playPresenter.play();
        onPlayStart();
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
            startOrStopButton.setImageResource(R.mipmap.video_play);
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
        playViewPagerAdapter.setDate(list);
        if (playPopView != null){
            playPopView.setPopPlayAdapterDate(list);
        }
    }

    @Override
    public void onPlayModeChange(XmPlayListControl.PlayMode playMode) {

        if (playPopView != null){
            playPopView.upDatePlayMode(playMode);
        }

        currentPlayMode = playMode;
        int resid = R.mipmap.play_list;
        switch (playMode){
            case PLAY_MODEL_LIST:
                resid = R.mipmap.play_list;
                break;
            case PLAY_MODEL_SINGLE_LOOP:
                resid = R.mipmap.play_simgel_loop;
                break;
            case PLAY_MODEL_RANDOM:
                resid = R.mipmap.play_random;
                break;
            case PLAY_MODEL_LIST_LOOP:
                resid = R.mipmap.play_loop;
                break;
        }
        if (swithchButton == null){
            swithchButton = findViewById(R.id.player_mode_switch_btn);
        }
        swithchButton.setImageResource(resid);
    }

    @Override
    public void onProgressChange(int current, int total) {
        String totalTime;
        String currentTime;
        if (total > 1000 * 60 * 60){
            totalTime = hoursDateFormate.format(total);
            currentTime = hoursDateFormate.format(current);
        }else {
            totalTime = minutesDateFormate.format(total);
            currentTime = minutesDateFormate.format(current);
        }
        if (totalTimeText != null) {
            totalTimeText.setText(totalTime);
        }
        if (currentTimeText != null){
            currentTimeText.setText(currentTime);
        }
        if (!isSeekBarTouch) {
            if (playSeekBar != null) {
                playSeekBar.setMax(total);
                playSeekBar.setProgress(current);
            }
        }
    }

    @Override
    public void onAdLoading() {

    }

    @Override
    public void onAdFinished() {

    }

    @Override
    public void onTrackUpdate(Track track, int playIndex) {
        if (titleText != null) {
            titleText.setText(track.getTrackTitle());
        }
            contentViewPager.setCurrentItem(playIndex,true);
        if (playPopView != null) {
            playPopView.setPopPlayAdapterCurrentPosition(playIndex);
        }
//        Log.d(TAG, "onTrackUpdate: viewpager" + contentViewPager.getCurrentItem());
    }

    @Override
    public void updateListOrder(Boolean isReverse) {
        playPopView.updatePlayOrder(isReverse);
    }

    @Override
    public void playPopViewItemClick(int position) {
        if (playPresenter != null){
            playPresenter.playByIndex(position);
        }
    }


    @Override
    public void onPlayPopViewSwitchClick() {
        if (playPresenter != null) {
            playPresenter.switchPlayModel(modePlayModeMap.get(currentPlayMode));
        }
    }



    @Override
    public void onPlayPopViewOrderClick() {
        if (playPresenter != null) {
            playPresenter.reversePlayList();
        }

    }
}
