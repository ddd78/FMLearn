package com.a78.com.fmlearn.interfaces;

import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;

/**
 * Created by home on 2020/2/3.
 */

public interface IRecommendationContentCallBack {

    void onContentListLoad(List<Track> tracks);

    void onContentLoad(Album album);

    void onContentLoadError();
}
