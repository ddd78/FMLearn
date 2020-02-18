package com.a78.com.fmlearn.presenters;

import android.support.annotation.Nullable;

import com.a78.com.fmlearn.data.XmApi;
import com.a78.com.fmlearn.interfaces.IRecommendationContentCallBack;
import com.a78.com.fmlearn.interfaces.IRecommendationContentPresenter;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.model.track.TrackList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by home on 2020/2/3.
 */

public class RecommendationContentPresenter implements IRecommendationContentPresenter {

    private static final String TAG = "RecommendationContentPr";

    private List<IRecommendationContentCallBack> callBackList = new ArrayList<>();
    private List<Track> trackData = new ArrayList<>();

    private Album tagAlbun = null;
    private int mCurrentPageIndex = 0;
    private int mCurrentAlbumId = -1;

    public void setTagAlbun(Album tagAlbun) {
        this.tagAlbun = tagAlbun;
    }

    private RecommendationContentPresenter(){}

    private static RecommendationContentPresenter sInstance = null;

    public static RecommendationContentPresenter getInstance(){
        if (sInstance == null){
            synchronized (RecommendationContentPresenter.class){
                if (sInstance == null){
                    sInstance = new RecommendationContentPresenter();
                }
            }
        }
        return sInstance;
    }

    @Override
    public void pullToMore() {

    }

    @Override
    public void loadMore() {
        mCurrentPageIndex++;
        loadContentData(true);
    }

    @Override
    public void getRecommendationContent(int albumId, int pageIndex) {
        mCurrentAlbumId = albumId;
        mCurrentPageIndex = pageIndex;
        loadContentData(false);
    }

    private void loadContentData(final boolean isLoad){
        XmApi.getInstance().getTracks(new IDataCallBack<TrackList>() {
            @Override
            public void onSuccess(@Nullable TrackList trackList) {
                if (trackList.getTracks() != null){
                    List<Track> tracks = trackList.getTracks();
                    if (isLoad){
                        trackData.addAll(trackData.size(), tracks);
                        handlerGetData(tracks.size());
                    }else {
                        trackData.clear();
                        trackData.addAll(tracks);
                    }
                    getContentSuccess(trackData);
                }
            }

            @Override
            public void onError(int i, String s) {
                if (isLoad){
                    mCurrentPageIndex--;
                }
                getContentError();
            }
        },mCurrentAlbumId,mCurrentPageIndex);
//        Map<String, String> map = new HashMap<>();
//        map.put(DTransferConstants.SORT, "asc");
//        map.put(DTransferConstants.ALBUM_ID, mCurrentAlbumId + "");
//        map.put(DTransferConstants.PAGE, mCurrentPageIndex + "");
//        map.put(DTransferConstants.PAGE_SIZE, 20 + "");
//        CommonRequest.getTracks(map, new IDataCallBack<TrackList>() {
//            @Override
//            public void onSuccess(@Nullable TrackList trackList) {
//                if (trackList.getTracks() != null){
//                    List<Track> tracks = trackList.getTracks();
//                    if (isLoad){
//                        trackData.addAll(trackData.size(), tracks);
//                        handlerGetData(tracks.size());
//                    }else {
//                        trackData.clear();
//                        trackData.addAll(tracks);
//                    }
//                    getContentSuccess(trackData);
//                }
//            }
//
//            @Override
//            public void onError(int i, String s) {
//                if (isLoad){
//                    mCurrentPageIndex--;
//                }
//                getContentError();
//            }
//        });
    }

    private void handlerGetData(int size) {
        for (IRecommendationContentCallBack iRecommendationContentCallBack : callBackList) {
            iRecommendationContentCallBack.loadMoreFinish(size);
        }
    }

    private void getContentError() {
        for (IRecommendationContentCallBack iRecommendationContentCallBack : callBackList) {
            iRecommendationContentCallBack.onContentLoadError();
        }
    }

    private void getContentSuccess(List<Track> tracks) {
        for (IRecommendationContentCallBack iRecommendationContentCallBack : callBackList) {
            iRecommendationContentCallBack.onContentListLoad(tracks);
        }
    }

    @Override
    public void registerViewCallBack(IRecommendationContentCallBack iRecommendationContentCallBack) {
        if (!callBackList.contains(iRecommendationContentCallBack)) {
            callBackList.add(iRecommendationContentCallBack);
            if (tagAlbun != null){
                iRecommendationContentCallBack.onContentLoad(tagAlbun);
            }
        }
    }

    @Override
    public void unregisterViewCallback(IRecommendationContentCallBack iRecommendationContentCallBack) {
        if (callBackList.contains(iRecommendationContentCallBack)){
            callBackList.remove(iRecommendationContentCallBack);
        }
    }

}
