package com.a78.com.fmlearn.presenters;

import android.util.Log;

import com.a78.com.fmlearn.base.BaseApplication;
import com.a78.com.fmlearn.data.HistoryDao;
import com.a78.com.fmlearn.interfaces.IHistoryCallBack;
import com.a78.com.fmlearn.interfaces.IHistoryPresenter;
import com.a78.com.fmlearn.utils.Constants;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by home on 2020/2/17.
 */

public class HistoryPresenter implements IHistoryPresenter, com.a78.com.fmlearn.data.IHistoryCallBack {

    private static final String TAG = "HistoryPresenter";

    private static HistoryPresenter presenter = null;

    private List<IHistoryCallBack> callBacks = new ArrayList<>();
    private final HistoryDao historyDao;
    private List<Track> mCurrentTracks = new ArrayList<>();
    private Track mCurrentTrack = null;
    private boolean isDodeleteOutSize = false;

    private HistoryPresenter() {
        historyDao = HistoryDao.getInstance();
        historyDao.setCallBack(this);
    }

    public static HistoryPresenter getInstance(){
        if (presenter == null) {
            synchronized (HistoryPresenter.class){
                if (presenter == null) {
                    presenter = new HistoryPresenter();
                }
            }
        }
        return presenter;
    }

    @Override
    public void registerViewCallBack(IHistoryCallBack iHistoryCallBack) {
        if (!callBacks.contains(iHistoryCallBack)) {
            callBacks.add(iHistoryCallBack);
        }
    }

    @Override
    public void unregisterViewCallback(IHistoryCallBack iHistoryCallBack) {
        if (callBacks.contains(iHistoryCallBack)) {
            callBacks.remove(iHistoryCallBack);
        }
    }

    @Override
    public void lishHistory() {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                if (historyDao != null) {
                    historyDao.listHistory();
                }
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    @Override
    public void addHistory(final Track track) {
        Log.d(TAG, "addHistory:+++++++++++++++++ ");

        if (mCurrentTracks != null && mCurrentTracks.size() >= Constants.MAX_HISTORY_COUNT){
            this.mCurrentTrack = track;
            isDodeleteOutSize = true;
            deleteHistory(mCurrentTracks.get(mCurrentTracks.size()-1));
        }else {
            Observable.create(new ObservableOnSubscribe<Object>() {
                @Override
                public void subscribe(ObservableEmitter<Object> e) throws Exception {
                    if (historyDao != null) {
                        historyDao.addHistory(track);
                    }
                }
            }).subscribeOn(Schedulers.io()).subscribe();
        }
    }

    @Override
    public void deleteHistory(final Track track) {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                if (historyDao != null) {
                    historyDao.deleteHistory(track);
                }
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    @Override
    public void clearHistories() {

        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                if (historyDao != null) {
                    historyDao.clearHistory();
                }
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    @Override
    public void onHistoryAdd(boolean isSuccess) {
        lishHistory();
    }

    @Override
    public void onHistoryDelete(boolean isSuccess) {
        if (isDodeleteOutSize && mCurrentTrack != null){
            isDodeleteOutSize = true;
            addHistory(mCurrentTrack);
        }else {
            lishHistory();
        }
    }

    @Override
    public void onHistoryLoad(List<Track> tracks) {

        Log.d(TAG, "onHistoryLoad: ++++++++++++++++"+tracks.size());
        this.mCurrentTracks.clear();
        this.mCurrentTracks.addAll(tracks);
//        this.mCurrentTracks = tracks;
        BaseApplication.getHandler().post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "onHistoryLoad: +" + mCurrentTracks.size());
                for (IHistoryCallBack callBack : callBacks) {
                    callBack.onHistoriesLoad(mCurrentTracks);
                }
            }
        });
    }

    @Override
    public void onHistoryClear(boolean isSuccess) {
        lishHistory();
    }
}
