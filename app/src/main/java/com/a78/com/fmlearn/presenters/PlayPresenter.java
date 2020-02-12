package com.a78.com.fmlearn.presenters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.a78.com.fmlearn.base.BaseApplication;
import com.a78.com.fmlearn.interfaces.IPlayCallBack;
import com.a78.com.fmlearn.interfaces.IPlayPresenter;
import com.ximalaya.ting.android.opensdk.model.PlayableModel;
import com.ximalaya.ting.android.opensdk.model.advertis.Advertis;
import com.ximalaya.ting.android.opensdk.model.advertis.AdvertisList;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.advertis.IXmAdsStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by home on 2020/2/7.
 */

public class PlayPresenter implements IPlayPresenter, IXmAdsStatusListener, IXmPlayerStatusListener {

    private static final String TAG = "PlayPresenter";

    private List<IPlayCallBack> callBacks = new ArrayList<>();
    private final XmPlayerManager xmPlayerManager;
    private boolean isPlaying = false;
    private Track mTrack;
    private int mPlayIndex = 0;
    private final SharedPreferences playModelSharePreference;
    private Boolean isOrder = true;


    private PlayPresenter(){
        xmPlayerManager = XmPlayerManager.getInstance(BaseApplication.getsContext());
        xmPlayerManager.addAdsStatusListener(this);
        xmPlayerManager.addPlayerStatusListener(this);

        playModelSharePreference = BaseApplication.getsContext().getSharedPreferences("PlayModel", Context.MODE_PRIVATE);
    }

    private static PlayPresenter playPresenter = null;

    public static PlayPresenter getPlayPresenter(){
        if (playPresenter == null){
            synchronized (PlayPresenter.class){
                if (playPresenter == null){
                    playPresenter = new PlayPresenter();
                }
            }
        }
        return playPresenter;
    }

    private boolean isPlaylistSet = false;

    public void setPlayList(List<Track> list,int playIndex){
        if (xmPlayerManager !=null){
            xmPlayerManager.setPlayList(list,playIndex);
            isPlaylistSet = true;
            mTrack = list.get(playIndex);
            mPlayIndex = playIndex;
        }else{
            Log.d(TAG, "setPlayList: error");
        }
    }

    @Override
    public void registerViewCallBack(IPlayCallBack iPlayCallBack) {

        XmPlayListControl.PlayMode playModel = XmPlayListControl.PlayMode.valueOf(playModelSharePreference.getString("play_model", XmPlayListControl.PlayMode.PLAY_MODEL_LIST.name()));
        iPlayCallBack.onPlayModeChange(playModel);
        if (!callBacks.contains(iPlayCallBack)){
            callBacks.add(iPlayCallBack);
        }
    }

    @Override
    public void unregisterViewCallback(IPlayCallBack iPlayCallBack) {
        callBacks.remove(iPlayCallBack);
    }


    @Override
    public void play() {
        if (isPlaylistSet){
            xmPlayerManager.play();
            isPlaying = true;
            playSuccess();
        }
    }

    private void playSuccess() {
        for (IPlayCallBack callBack : callBacks) {
            callBack.onPlayStart();
//            callBack.updateTitle(playTitle);
//            callBack.onTrackUpdate(mTrack, mPlayIndex);
//            Log.d(TAG, "playSuccess:11111 " + mPlayIndex);
            setPlayTitle();
        }
    }

    private void setPlayTitle(){
        for (IPlayCallBack callBack : callBacks) {
            callBack.onTrackUpdate(mTrack, mPlayIndex);
        }
    }

    @Override
    public void pause() {
        if (isPlaylistSet){
            xmPlayerManager.pause();
            isPlaying = false;
            pauseSuccess();
        }
    }

    private void pauseSuccess() {
        for (IPlayCallBack callBack : callBacks) {
            callBack.onPlayPause();
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public void playPre() {
        if (xmPlayerManager != null){
            xmPlayerManager.playPre();
        }
    }

    @Override
    public void playNext() {
        if (xmPlayerManager != null){
            xmPlayerManager.playNext();
        }
    }

    @Override
    public void switchPlayModel(XmPlayListControl.PlayMode playMode) {
        xmPlayerManager.setPlayMode(playMode);

        SharedPreferences.Editor edit = playModelSharePreference.edit();
        edit.putString("play_model", playMode.name());
        edit.commit();

        for (IPlayCallBack callBack : callBacks) {
            callBack.onPlayModeChange(playMode);
        }

    }

    @Override
    public void getPlayList() {
        if (xmPlayerManager != null) {
            List<Track> playList = xmPlayerManager.getPlayList();
            for (IPlayCallBack callBack : callBacks) {
                callBack.onListLoad(playList);
            }
        }

    }

    @Override
    public void playByIndex(int index) {
        if (xmPlayerManager != null){
            xmPlayerManager.play(index);
        }
    }

    @Override
    public void seekTo(int progress) {
        xmPlayerManager.seekTo(progress);
    }

    @Override
    public boolean isPlaying() {
        return isPlaying;
    }

    @Override
    public void reversePlayList() {
        List<Track> tracks = xmPlayerManager.getPlayList();
        Collections.reverse(tracks);
        mPlayIndex = tracks.size() - 1 - mPlayIndex;
        mTrack = (Track) xmPlayerManager.getCurrSound();
        isOrder = !isOrder;
        for (IPlayCallBack callBack : callBacks) {
            callBack.onListLoad(tracks);
            callBack.onTrackUpdate(mTrack,mPlayIndex);
            callBack.updateListOrder(isOrder);
        }
    }

    @Override
    public void playByAlbumId(long id) {

    }


    //=============广告============
    @Override
    public void onStartGetAdsInfo() {
        Log.d(TAG, "onStartGetAdsInfo: ");
    }

    @Override
    public void onGetAdsInfo(AdvertisList advertisList) {
        Log.d(TAG, "onGetAdsInfo: ");
    }

    @Override
    public void onAdsStartBuffering() {
        Log.d(TAG, "onAdsStartBuffering: ");
    }

    @Override
    public void onAdsStopBuffering() {
        Log.d(TAG, "onAdsStopBuffering: ");
    }

    @Override
    public void onStartPlayAds(Advertis advertis, int i) {
        Log.d(TAG, "onStartPlayAds: ");
    }

    @Override
    public void onCompletePlayAds() {
        Log.d(TAG, "onCompletePlayAds: ");
    }

    @Override
    public void onError(int i, int i1) {
        Log.d(TAG, "onError: ");
    }

    //================广告============

    //================播放============
    @Override
    public void onPlayStart() {
        Log.d(TAG, "onPlayStart: ");
    }

    @Override
    public void onPlayPause() {
        Log.d(TAG, "onPlayPause: ");
    }

    @Override
    public void onPlayStop() {
        Log.d(TAG, "onPlayStop: ");
    }

    @Override
    public void onSoundPlayComplete() {
        Log.d(TAG, "onSoundPlayComplete: ");
    }

    @Override
    public void onSoundPrepared() {
        Log.d(TAG, "onSoundPrepared: ");
        XmPlayListControl.PlayMode playModel = XmPlayListControl.PlayMode.valueOf(playModelSharePreference.getString("play_model", XmPlayListControl.PlayMode.PLAY_MODEL_LIST.name()));
        xmPlayerManager.setPlayMode(playModel);
    }

    @Override
    public void onSoundSwitch(PlayableModel preModel, PlayableModel curPlay) {
        if (xmPlayerManager != null) {
            mPlayIndex = xmPlayerManager.getCurrentIndex();
        }
        if (curPlay instanceof Track){
            mTrack = (Track) curPlay;
            setPlayTitle();
        }
    }

    @Override
    public void onBufferingStart() {
        Log.d(TAG, "onBufferingStart: ");
    }

    @Override
    public void onBufferingStop() {
        Log.d(TAG, "onBufferingStop: ");
    }

    @Override
    public void onBufferProgress(int i) {
        Log.d(TAG, "onBufferProgress: ");
    }

    @Override
    public void onPlayProgress(int i, int i1) {
        for (IPlayCallBack callBack : callBacks) {
            callBack.onProgressChange(i,i1);
        }
    }

    @Override
    public boolean onError(XmPlayerException e) {
        return false;
    }
    //================播放============
}
