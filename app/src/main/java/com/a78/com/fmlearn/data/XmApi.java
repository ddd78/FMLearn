package com.a78.com.fmlearn.data;

import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;
import com.ximalaya.ting.android.opensdk.model.album.SearchAlbumList;
import com.ximalaya.ting.android.opensdk.model.track.TrackList;
import com.ximalaya.ting.android.opensdk.model.word.HotWordList;
import com.ximalaya.ting.android.opensdk.model.word.SuggestWords;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by home on 2020/2/13.
 */

public class XmApi {

    private XmApi() {

    }

    private static XmApi xmApi = null;

    public static XmApi getInstance(){
        if (xmApi == null){
            synchronized (XmApi.class){
                if (xmApi == null){
                    xmApi = new XmApi();
                }
            }
        }
        return xmApi;
    }

    public void getRecommoadtionList(IDataCallBack<GussLikeAlbumList> callBack){
        Map<String, String> map = new HashMap<String, String>();
        map.put(DTransferConstants.LIKE_COUNT, "50");
        CommonRequest.getGuessLikeAlbum(map,callBack);
    }

    public void getTracks(IDataCallBack<TrackList> callBack,int albumId, int pageIndex){
        Map<String, String> map = new HashMap<>();
        map.put(DTransferConstants.SORT, "asc");
        map.put(DTransferConstants.ALBUM_ID, albumId + "");
        map.put(DTransferConstants.PAGE, pageIndex + "");
        map.put(DTransferConstants.PAGE_SIZE, 20 + "");
        CommonRequest.getTracks(map, callBack);
    }

    /**
     * 根据关键字，进行搜索。
     *
     * @param keyword
     */
    public void searchByKeyword(String keyword, int page, IDataCallBack<SearchAlbumList> callback) {
        Map<String, String> map = new HashMap<>();
        map.put(DTransferConstants.SEARCH_KEY, keyword);
        map.put(DTransferConstants.PAGE, page + "");
        map.put(DTransferConstants.PAGE_SIZE, 10 + "");
        CommonRequest.getSearchedAlbums(map, callback);
    }


    /**
     * 获取推荐的热词
     *
     * @param callback
     */
    public void getHotWords(IDataCallBack<HotWordList> callback) {
        Map<String, String> map = new HashMap<>();
        map.put(DTransferConstants.TOP, String.valueOf(20));
        CommonRequest.getHotWords(map, callback);
    }


    /**
     * 根据关键字获取联想词
     *
     * @param keyword  关键字
     * @param callback 回调
     */
    public void getSuggestWord(String keyword, IDataCallBack<SuggestWords> callback) {
        Map<String, String> map = new HashMap<>();
        map.put(DTransferConstants.SEARCH_KEY, keyword);
        CommonRequest.getSuggestWord(map, callback);
    }

}
