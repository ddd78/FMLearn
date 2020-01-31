package com.a78.com.fmlearn.presenters;

import android.support.annotation.Nullable;

import com.a78.com.fmlearn.interfaces.IRecommendationCallBack;
import com.a78.com.fmlearn.interfaces.IRecommendationPresenter;
import com.a78.com.fmlearn.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by home on 2020/1/31.
 */

public class RecommendationPresenter implements IRecommendationPresenter {

    private static final String TAG = "RecommendationPresenter";

    private List<IRecommendationCallBack> recommendationCallBackList = new ArrayList<>();

    private RecommendationPresenter() {}

    private static RecommendationPresenter recommendationInstance = null;

    public static RecommendationPresenter getRecommendationInstance(){
        if (recommendationInstance == null) {
            synchronized (RecommendationPresenter.class){
                if (recommendationInstance == null) {
                    recommendationInstance = new RecommendationPresenter();
                }
            }
        }
        return recommendationInstance;
    }


    @Override
    public void getRecommendationList() {
        getRecommendationData();
    }

    @Override
    public void pullRefrest() {

    }

    @Override
    public void loadMore() {

    }

    @Override
    public void registerViewCallBack(IRecommendationCallBack callBack) {
        if (callBack != null && !recommendationCallBackList.contains(callBack)) {
            recommendationCallBackList.add(callBack);
        }
    }

    @Override
    public void unRegisterViewCallBack(IRecommendationCallBack callBack) {
        if (recommendationCallBackList.contains(callBack)) {
            recommendationCallBackList.remove(callBack);
        }
    }

    public void getRecommendationData() {
        Map<String, String> map = new HashMap<String, String>();
        map.put(DTransferConstants.LIKE_COUNT, "50");
        CommonRequest.getGuessLikeAlbum(map, new IDataCallBack<GussLikeAlbumList>() {
            @Override
            public void onSuccess(@Nullable GussLikeAlbumList gussLikeAlbumList) {
                List<Album> albumList = gussLikeAlbumList.getAlbumList();
                handleRecommodationResult(albumList);
            }

            @Override
            public void onError(int i, String s) {
                LogUtil.d(TAG,"error");
            }
        });
    }

    private void handleRecommodationResult(List<Album> albumList) {
        if (recommendationCallBackList != null){
            for (IRecommendationCallBack callBack : recommendationCallBackList) {
                callBack.onRecommendationLoad(albumList);
            }
        }
    }

}
