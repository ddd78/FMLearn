package com.a78.com.fmlearn.interfaces;

import com.a78.com.fmlearn.base.IBasePresenter;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

/**
 * Created by home on 2020/2/7.
 */

public interface IPlayPresenter extends IBasePresenter<IPlayCallBack> {

    void play();

    void pause();

    void stop();

    void playPre();

    void playNext();

    void switchPlayModel(XmPlayListControl.PlayMode playMode);

    void getPlayList();

    void playByIndex(int idex);

    void seekTo(int progress);

    boolean isPlaying();

    void reversePlayList();

    void playByAlbumId(long id);
}
