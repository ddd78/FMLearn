package com.a78.com.fmlearn.base;

/**
 * Created by home on 2020/2/7.
 */

public interface IBasePresenter<T> {

    void registerViewCallBack(T t);

    void unregisterViewCallback(T t);
}
