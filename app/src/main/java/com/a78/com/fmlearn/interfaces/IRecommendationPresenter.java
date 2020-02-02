package com.a78.com.fmlearn.interfaces;

/**
 * Created by home on 2020/1/31.
 */

public interface IRecommendationPresenter {

    void getRecommendationList();

    void registerViewCallBack(IRecommendationCallBack callBack);

    void unRegisterViewCallBack(IRecommendationCallBack callBack);
}
