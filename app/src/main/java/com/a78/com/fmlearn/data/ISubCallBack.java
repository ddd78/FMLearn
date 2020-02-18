package com.a78.com.fmlearn.data;

import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.List;

/**
 * Created by home on 2020/2/16.
 */

public interface ISubCallBack {

    void onAddResult(boolean isSuccess);

    void onDeleteResult(boolean isSuccess);

    void onSubListLoad(List<Album> resultList);
}
