package com.a78.com.fmlearn.interfaces;

import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import java.util.List;

/**
 * Created by home on 2020/2/7.
 */

public interface IPlayCallBack {

    void onPlayStart();

    void onPlayPause();

    void onPlayStop();

    void onPlayError();

    void nextPlay(Track track);

    void onPrePlay(Track track);

    void onListLoad(List<Track> list);

    void onPlayModeChange(XmPlayListControl.PlayMode playMode);

    void onProgressChange(int currentProgress, int total);

    void onAdLoading();

    void onAdFinished();

    void onTrackUpdate(Track track, int playIndex);

    void updateListOrder(Boolean isReverse);

}
