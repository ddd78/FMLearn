package com.a78.com.fmlearn.interfaces;

import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.List;

/**
 * Created by home on 2020/2/16.
 */

public interface ISubCallBack {

    void onAddResult(boolean result);

    void onDeleteResult(boolean result);

    void onSubscriptionLoad(List<Album> albums);

    void onSunFull();
}
