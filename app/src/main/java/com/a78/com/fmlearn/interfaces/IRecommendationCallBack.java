package com.a78.com.fmlearn.interfaces;

import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.List;

/**
 * Created by home on 2020/1/31.
 */

public interface IRecommendationCallBack {

    void onRecommendationLoad(List<Album> result);

    void onLoadMore(List<Album> result);

    void onRefreshMore(List<Album> result);
}
