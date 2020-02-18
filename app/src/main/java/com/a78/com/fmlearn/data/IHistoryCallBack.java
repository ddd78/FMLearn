package com.a78.com.fmlearn.data;

import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;

/**
 * Created by home on 2020/2/17.
 */

public interface IHistoryCallBack {

    void onHistoryAdd(boolean isSuccess);

    void onHistoryDelete(boolean isSuccess);

    void onHistoryLoad(List<Track> tracks);

    void onHistoryClear(boolean isSuccess);
}
