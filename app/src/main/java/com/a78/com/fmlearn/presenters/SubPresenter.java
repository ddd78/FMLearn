package com.a78.com.fmlearn.presenters;

import com.a78.com.fmlearn.base.BaseApplication;
import com.a78.com.fmlearn.data.SubDao;
import com.a78.com.fmlearn.interfaces.ISubCallBack;
import com.a78.com.fmlearn.interfaces.ISubPresenter;
import com.a78.com.fmlearn.utils.Constants;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by home on 2020/2/16.
 */

public class SubPresenter implements ISubPresenter, com.a78.com.fmlearn.data.ISubCallBack {

    private static SubPresenter mSubPresenter = null;
    private List<ISubCallBack> callBacks = new ArrayList<>();
    private final SubDao dao;

    private Map<Long, Album> mDate = new HashMap<>();

    private SubPresenter(){
        dao = SubDao.getInstance();
        dao.setCallBack(this);

    }

    public static SubPresenter getInstance(){
        if (mSubPresenter == null){
            synchronized (SubPresenter.class){
                if (mSubPresenter == null) {
                    mSubPresenter = new SubPresenter();
                }
            }
        }
        return mSubPresenter;
    }

    private void  listSubscription(){
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) {
                if (dao != null) {
                    dao.listAlbum();
                }
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    @Override
    public void registerViewCallBack(ISubCallBack iSubCallBack) {
        listSubscription();
        if (!callBacks.contains(iSubCallBack)){
            callBacks.add(iSubCallBack);
        }
    }

    @Override
    public void unregisterViewCallback(ISubCallBack iSubCallBack) {
        if (callBacks.contains(iSubCallBack)) {
            callBacks.remove(iSubCallBack);
        }
    }

    @Override
    public void addSubscription(final Album album) {

        if (mDate.size() >= Constants.SUB_MAX_NUM){
            for (ISubCallBack callBack : callBacks) {
                callBack.onSunFull();
            }
        }else {
            Observable.create(new ObservableOnSubscribe<Object>() {
                @Override
                public void subscribe(ObservableEmitter<Object> emitter) {
                    if (dao != null) {
                        dao.addAlbum(album);
                    }
                }
            }).subscribeOn(Schedulers.io()).subscribe();
        }

    }

    @Override
    public void deleteSubscription(final Album album) {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) {
                if (dao != null) {
                    dao.deleteAlbum(album);
                }
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    @Override
    public void getSubscription() {
        listSubscription();
    }

    @Override
    public boolean isSub(Album album) {
        Album reuslt = mDate.get(album.getId());
        return reuslt != null;
    }

    @Override
    public void onAddResult(final boolean isSuccess) {
//        if (mDate.size() >= Constants.SUB_MAX_NUM){
//            BaseApplication.getHandler().post(new Runnable() {
//                @Override
//                public void run() {
//                    for (ISubCallBack callBack : callBacks) {
//                        callBack.onSunFull();
//                    }
//                }
//            });
//
//        }else {
            BaseApplication.getHandler().post(new Runnable() {
                @Override
                public void run() {
                    for (ISubCallBack callBack : callBacks) {
                        callBack.onAddResult(isSuccess);
                    }
                }
            });
//        }
    }

    @Override
    public void onDeleteResult(final boolean isSuccess) {
        BaseApplication.getHandler().post(new Runnable() {
            @Override
            public void run() {
                for (ISubCallBack callBack : callBacks) {
                    callBack.onDeleteResult(isSuccess);
                }
            }
        });
    }

    @Override
    public void onSubListLoad(final List<Album> resultList) {
        mDate.clear();
        for (Album album : resultList) {
            mDate.put(album.getId(),album);
        }
        BaseApplication.getHandler().post(new Runnable() {
            @Override
            public void run() {
                for (ISubCallBack callBack : callBacks) {
                    callBack.onSubscriptionLoad(resultList);
                }
            }
        });
    }
}
