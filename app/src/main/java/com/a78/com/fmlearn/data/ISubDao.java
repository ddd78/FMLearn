package com.a78.com.fmlearn.data;

import com.ximalaya.ting.android.opensdk.model.album.Album;

/**
 * Created by home on 2020/2/16.
 */

public interface ISubDao {

    void setCallBack(ISubCallBack callBack);

    void addAlbum(Album album);

    void deleteAlbum(Album album);

    void listAlbum();

}
