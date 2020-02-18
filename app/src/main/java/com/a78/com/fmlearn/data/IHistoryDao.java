package com.a78.com.fmlearn.data;

import com.ximalaya.ting.android.opensdk.model.track.Track;

/**
 * Created by home on 2020/2/17.
 */

public interface IHistoryDao {

    void setCallBack(IHistoryCallBack callBack);

    void addHistory(Track track);

    void deleteHistory(Track track);

    void clearHistory();

    void listHistory();
}
