package com.a78.com.fmlearn.interfaces;

import com.a78.com.fmlearn.base.IBasePresenter;
import com.ximalaya.ting.android.opensdk.model.album.Album;

/**
 * Created by home on 2020/2/16.
 */

public interface ISubPresenter extends IBasePresenter<ISubCallBack> {

    void addSubscription(Album album);

    void deleteSubscription(Album album);

    void getSubscription();

    boolean isSub(Album album);
}
