package com.a78.com.fmlearn.interfaces;

import com.a78.com.fmlearn.base.IBasePresenter;

/**
 * Created by home on 2020/2/14.
 */

public interface ISearchPresenter extends IBasePresenter<ISearchCallBack>{


    void doSearch(String keyword);

    /**
     * 重新搜索
     */
    void reSearch();

    /**
     * 加载更多的搜索结果
     */
    void loadMore();

    /**
     * 获取热词
     */
    void getHotWord();

    /**
     * 获取推荐的关键字（相关的关键字）
     *
     * @param keyword
     */
    void getRecommendWord(String keyword);





}
