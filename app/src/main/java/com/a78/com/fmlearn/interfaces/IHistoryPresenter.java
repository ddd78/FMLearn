package com.a78.com.fmlearn.interfaces;

import com.a78.com.fmlearn.base.IBasePresenter;
import com.ximalaya.ting.android.opensdk.model.track.Track;

/**
 * Created by home on 2020/2/17.
 */

public interface IHistoryPresenter extends IBasePresenter<IHistoryCallBack>{

    void lishHistory();

    void addHistory(Track track);

    void deleteHistory(Track track);

    void clearHistories();
}
