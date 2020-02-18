package com.a78.com.fmlearn.interfaces;

import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;

/**
 * Created by home on 2020/2/17.
 */

public interface IHistoryCallBack {

    void onHistoriesLoad(List<Track> tracks);
}
