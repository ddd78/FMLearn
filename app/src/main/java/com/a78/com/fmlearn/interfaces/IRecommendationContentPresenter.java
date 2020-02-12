package com.a78.com.fmlearn.interfaces;

import com.a78.com.fmlearn.base.IBasePresenter;

/**
 * Created by home on 2020/2/3.
 */

public interface IRecommendationContentPresenter extends IBasePresenter<IRecommendationContentCallBack> {

    void pullToMore();

    void loadMore();

    void getRecommendationContent(int albumId, int pageIndex);

//    void registerViewCallBack(IRecommendationContentCallBack iRecommendationContentCallBack);
//
//    void unregisterViewCallback(IRecommendationContentCallBack iRecommendationContentCallBack);


}
