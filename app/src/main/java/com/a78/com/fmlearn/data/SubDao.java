package com.a78.com.fmlearn.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.a78.com.fmlearn.base.BaseActivity;
import com.a78.com.fmlearn.base.BaseApplication;
import com.a78.com.fmlearn.utils.Constants;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.Announcer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by home on 2020/2/16.
 */

public class SubDao implements ISubDao {

    private static final String TAG = "SubDao";

    private static final SubDao ourInstance = new SubDao();
    private final XmDBHelper dbHelper;
    private ISubCallBack callBack = null;

    public static SubDao getInstance() {
        return ourInstance;
    }

    private SubDao() {

        dbHelper = new XmDBHelper(BaseApplication.getsContext());
    }

    @Override
    public void setCallBack(ISubCallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public void addAlbum(Album album) {
        SQLiteDatabase writableDatabase = null;
        boolean isAddSuccess = false;
        try{
             writableDatabase = dbHelper.getWritableDatabase();
             writableDatabase.beginTransaction();
             ContentValues values = new ContentValues();
             values.put(Constants.SUB_COVER_URL,album.getCoverUrlLarge());
             values.put(Constants.SUB_TITLE,album.getAlbumTitle());
             values.put(Constants.SUB_DESCRIPTION,album.getAlbumIntro());
             values.put(Constants.SUB_TRACKS_COUNT,album.getIncludeTrackCount());
             values.put(Constants.SUB_PLAY_COUNT,album.getPlayCount());
             values.put(Constants.SUB_AUTHOR_NAME,album.getAnnouncer().getNickname());
             values.put(Constants.SUB_ALBUM_ID,album.getId());
             writableDatabase.insert(Constants.SUB_TB_NAME , null , values);
             writableDatabase.setTransactionSuccessful();
             isAddSuccess = true;
        }catch (Exception e){
            e.printStackTrace();
            isAddSuccess = false;
//            if (callBack != null) {
//                callBack.onAddResult(false);
//            }
        }finally {
//
//            if (callBack != null) {
//                callBack.onAddResult(true);
//            }
            if (writableDatabase != null) {
                writableDatabase.endTransaction();
                writableDatabase.close();
            }
            if (callBack != null) {
                callBack.onAddResult(isAddSuccess);
            }
        }
    }

    @Override
    public void deleteAlbum(Album album) {
        SQLiteDatabase writableDatabase = null;
        boolean isDeleteSuccess = false;
        try{
            writableDatabase = dbHelper.getWritableDatabase();
            writableDatabase.beginTransaction();
            int delete = writableDatabase.delete(Constants.SUB_TB_NAME,Constants.SUB_ALBUM_ID + "=?", new String[]{album.getId()+""});
            writableDatabase.setTransactionSuccessful();
            isDeleteSuccess = true;
//            if (callBack != null) {
//                callBack.onDeleteResult(true);
//            }
        }catch (Exception e){
            e.printStackTrace();
//            if (callBack != null) {
//                callBack.onDeleteResult(false);
//            }
        }finally {
            if (writableDatabase != null) {
                writableDatabase.endTransaction();
                writableDatabase.close();
            }
            if (callBack != null) {
                callBack.onDeleteResult(isDeleteSuccess);
            }
        }
    }

    @Override
    public void listAlbum() {
        SQLiteDatabase db = null;
        List<Album> list = new ArrayList<>();
        try{
            db = dbHelper.getReadableDatabase();
            db.beginTransaction();
            Cursor cursor = db.query(Constants.SUB_TB_NAME , null , null , null , null , null , "_id desc" , null);
            while (cursor.moveToNext()){
                Album album = new Album();
                String coverUrl = cursor.getString(cursor.getColumnIndex(Constants.SUB_COVER_URL));
                album.setCoverUrlLarge(coverUrl);

                String title = cursor.getString(cursor.getColumnIndex(Constants.SUB_TITLE));
                album.setAlbumTitle(title);

                String description = cursor.getString(cursor.getColumnIndex(Constants.SUB_DESCRIPTION));
                album.setAlbumIntro(description);

                int tracksCount = cursor.getInt(cursor.getColumnIndex(Constants.SUB_TRACKS_COUNT));
                album.setIncludeTrackCount(tracksCount);

                int playCount = cursor.getInt(cursor.getColumnIndex(Constants.SUB_PLAY_COUNT));
                album.setPlayCount(playCount);

                int albumId = cursor.getInt(cursor.getColumnIndex(Constants.SUB_ALBUM_ID));
                album.setId(albumId);

                String autorName = cursor.getString(cursor.getColumnIndex(Constants.SUB_AUTHOR_NAME));
                album.setCoverUrlLarge(coverUrl);

                Announcer announcer = new Announcer();
                announcer.setNickname(autorName);

                album.setAnnouncer(announcer);
                list.add(album);
            }
            cursor.close();
            db.setTransactionSuccessful();
//            if (callBack != null) {
//                callBack.onSubListLoad(list);
//            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
            if (callBack != null) {
                callBack.onSubListLoad(list
                );
            }
        }
    }
}
