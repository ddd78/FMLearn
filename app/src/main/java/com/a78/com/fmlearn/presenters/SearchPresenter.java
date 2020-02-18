package com.a78.com.fmlearn.presenters;

import android.support.annotation.Nullable;
import android.util.Log;

import com.a78.com.fmlearn.data.XmApi;
import com.a78.com.fmlearn.interfaces.ISearchCallBack;
import com.a78.com.fmlearn.interfaces.ISearchPresenter;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.SearchAlbumList;
import com.ximalaya.ting.android.opensdk.model.word.HotWord;
import com.ximalaya.ting.android.opensdk.model.word.HotWordList;
import com.ximalaya.ting.android.opensdk.model.word.QueryResult;
import com.ximalaya.ting.android.opensdk.model.word.SuggestWords;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by home on 2020/2/14.
 */

public class SearchPresenter implements ISearchPresenter {

    private static final String TAG = "SearchPresenter";

    private List<ISearchCallBack> callBacks = new ArrayList<>();

    private static SearchPresenter mSearchPresenter = null;

    private static final int DEFAULT_PAGE = 1;

    private int currentPage = DEFAULT_PAGE;
    private String currentKeyWord = null;
    private int currentResultSize;
    private List<Album> currentAlbums;

    private SearchPresenter() {
    }

    public static SearchPresenter getInstance(){
        if (mSearchPresenter == null){
            synchronized (SearchPresenter.class){
                if (mSearchPresenter == null) {
                    mSearchPresenter = new SearchPresenter();
                }
            }
        }
        return mSearchPresenter;
    }

    @Override
    public void registerViewCallBack(ISearchCallBack iSearchCallBack) {
        if (iSearchCallBack != null && !callBacks.contains(iSearchCallBack)){
            callBacks.add(iSearchCallBack);
        }
    }

    @Override
    public void unregisterViewCallback(ISearchCallBack iSearchCallBack) {
        if (callBacks.contains(iSearchCallBack)){
            callBacks.remove(iSearchCallBack);
        }
    }

    @Override
    public void doSearch(String keyword) {
        currentKeyWord = keyword;
        search();

    }

    private void search() {
        XmApi.getInstance().searchByKeyword(currentKeyWord, currentPage, new IDataCallBack<SearchAlbumList>() {
            @Override
            public void onSuccess(@Nullable SearchAlbumList searchAlbumList) {
                currentAlbums = searchAlbumList.getAlbums();
                currentResultSize = currentAlbums.size();
                if (currentAlbums != null){
                    for (ISearchCallBack callBack : callBacks) {
                        callBack.onSearchResultLoaded(currentAlbums);
                    }
                }
            }

            @Override
            public void onError(int i, String s) {
                getDateError(i,s);
            }
        });
    }

    @Override
    public void reSearch() {
        search();
    }

    @Override
    public void loadMore() {
        currentPage++;
        XmApi.getInstance().searchByKeyword(currentKeyWord, currentPage, new IDataCallBack<SearchAlbumList>() {
            @Override
            public void onSuccess(@Nullable SearchAlbumList searchAlbumList) {
                if (searchAlbumList.getAlbums().size() == 0){
                    for (ISearchCallBack callBack : callBacks) {
                        callBack.onLoadMoreResult(currentAlbums,false);
                    }
                }else {
                    currentAlbums.addAll(searchAlbumList.getAlbums());
                    for (ISearchCallBack callBack : callBacks) {
                        callBack.onLoadMoreResult(currentAlbums,true);
                    }
                }
            }

            @Override
            public void onError(int i, String s) {
                currentPage--;
                getDateError(i, s);
            }
        });
//        for (ISearchCallBack callBack : callBacks) {
//            callBack.onLoadMoreResult();
//        }

    }

    @Override
    public void getHotWord() {
        XmApi.getInstance().getHotWords(new IDataCallBack<HotWordList>() {
            @Override
            public void onSuccess(@Nullable HotWordList hotWordList) {
                if (hotWordList != null){
                    List<HotWord> wordList = hotWordList.getHotWordList();
                    Log.d(TAG, "onSuccess: " + wordList.size());
                    for (ISearchCallBack callBack : callBacks) {
                        callBack.onHotWordLoaded(wordList);
                    }

                }
            }

            @Override
            public void onError(int i, String s) {
                getDateError(i,s);
            }
        });
    }

    @Override
    public void getRecommendWord(String keyword) {
        XmApi.getInstance().getSuggestWord(keyword, new IDataCallBack<SuggestWords>() {
            @Override
            public void onSuccess(@Nullable SuggestWords suggestWords) {
                if (suggestWords != null){
                    List<QueryResult> queryResults = suggestWords.getKeyWordList();
                    for (ISearchCallBack callBack : callBacks) {
                        callBack.onRecommendWordLoaded(queryResults);
                    }
                }
            }

            @Override
            public void onError(int i, String s) {
                getDateError(i,s);
            }
        });
    }

    private void getDateError(int i, String s) {
        for (ISearchCallBack callBack : callBacks) {
            callBack.onError(i, s);
        }
    }
}
