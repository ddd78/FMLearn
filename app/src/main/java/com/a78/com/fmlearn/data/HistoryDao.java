package com.a78.com.fmlearn.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.a78.com.fmlearn.base.BaseApplication;
import com.a78.com.fmlearn.utils.Constants;
import com.ximalaya.ting.android.opensdk.model.album.Announcer;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by home on 2020/2/17.
 */

public class HistoryDao implements IHistoryDao{

    private static final String TAG = "HistoryDao";

    private static final HistoryDao ourInstance = new HistoryDao();
    private final XmDBHelper dbHelper;
    private IHistoryCallBack mCallBack = null;
    private Object mLock = new Object();

    public static HistoryDao getInstance() {
        return ourInstance;
    }

    private HistoryDao() {
        dbHelper = new XmDBHelper(BaseApplication.getsContext());
    }

    @Override
    public void setCallBack(IHistoryCallBack callBack) {
        this.mCallBack = callBack;
    }

    @Override
    public void addHistory(Track track) {
        synchronized (mLock) {
            SQLiteDatabase db = null;
            boolean isaddSuccess = false;
            try {
                db = dbHelper.getWritableDatabase();
                db.beginTransaction();
                db.delete(Constants.HISTORY_TB_NAME, Constants.HISTORY_TRACK_ID + "=?", new String[]{track.getDataId() + ""});
                ContentValues values = new ContentValues();
                values.put(Constants.HISTORY_TRACK_ID, track.getDataId());
                values.put(Constants.HISTORY_TITLE, track.getTrackTitle());
                values.put(Constants.HISTORY_PLAY_COUNT, track.getPlayCount());
                values.put(Constants.HISTORY_DURATION, track.getDuration());
                values.put(Constants.HISTORY_UPDATE_TIME, track.getUpdatedAt());
                values.put(Constants.HISTORY_COVER, track.getCoverUrlLarge());
                values.put(Constants.HISTORY_AUTHOR, track.getAnnouncer().getNickname());

                db.insert(Constants.HISTORY_TB_NAME, null, values);
                db.setTransactionSuccessful();
                isaddSuccess = true;
            } catch (Exception e) {
                isaddSuccess = false;
                e.printStackTrace();
            } finally {
                if (db != null) {
                    db.endTransaction();
                    db.close();
                }
                if (mCallBack != null) {
                    mCallBack.onHistoryAdd(isaddSuccess);
                }
            }
        }
    }

    @Override
    public void deleteHistory(Track track) {
        synchronized (mLock) {
            SQLiteDatabase db = null;
            boolean isDeleteSuccess = false;
            try {
                db = dbHelper.getWritableDatabase();
                db.beginTransaction();
                int delete = db.delete(Constants.HISTORY_TB_NAME, Constants.HISTORY_TRACK_ID + "=?", new String[]{track.getDataId() + ""});
                Log.d(TAG, "deleteHistory: " + delete);
                db.setTransactionSuccessful();
                isDeleteSuccess = true;
            } catch (Exception e) {
                isDeleteSuccess = false;
                e.printStackTrace();
            } finally {
                if (db != null) {
                    db.endTransaction();
                    db.close();
                }
                if (mCallBack != null) {
                    mCallBack.onHistoryDelete(isDeleteSuccess);
                }
            }
        }
    }

    @Override
    public void clearHistory() {
        synchronized (mLock) {
            SQLiteDatabase db = null;
            boolean isDeleteSuccess = false;
            try {
                db = dbHelper.getWritableDatabase();
                db.beginTransaction();
                db.delete(Constants.HISTORY_TB_NAME, null, null);
                db.setTransactionSuccessful();
                isDeleteSuccess = true;
            } catch (Exception e) {
                isDeleteSuccess = false;
                e.printStackTrace();
            } finally {

                if (db != null) {
                    db.endTransaction();
                    db.close();
                }
                if (mCallBack != null) {
                    mCallBack.onHistoryClear(isDeleteSuccess);
                }
            }
        }
    }

    @Override
    public void listHistory() {
        synchronized (mLock) {
            SQLiteDatabase db = null;
            List<Track> resultTrackList = new ArrayList<>();
            try {
                db = dbHelper.getReadableDatabase();
                db.beginTransaction();
                Cursor cursor = db.query(Constants.HISTORY_TB_NAME, null, null, null, null, null, "_id desc");
                while (cursor.moveToNext()) {
                    Track track = new Track();
                    int trackId = cursor.getInt(cursor.getColumnIndex(Constants.HISTORY_TRACK_ID));
                    track.setDataId(trackId);
                    String title = cursor.getString(cursor.getColumnIndex(Constants.HISTORY_TITLE));
                    track.setTrackTitle(title);
                    int playCount = cursor.getInt(cursor.getColumnIndex(Constants.HISTORY_PLAY_COUNT));
                    track.setPlayCount(playCount);
                    int duration = cursor.getInt(cursor.getColumnIndex(Constants.HISTORY_DURATION));
                    track.setDuration(duration);
                    long updateTime = cursor.getLong(cursor.getColumnIndex(Constants.HISTORY_UPDATE_TIME));
                    track.setUpdatedAt(updateTime);
                    String cover = cursor.getString(cursor.getColumnIndex(Constants.HISTORY_COVER));
                    track.setCoverUrlLarge(cover);
                    track.setCoverUrlSmall(cover);
                    track.setCoverUrlMiddle(cover);
                    String author = cursor.getString(cursor.getColumnIndex(Constants.HISTORY_AUTHOR));
                    Announcer announcer = new Announcer();
                    announcer.setNickname(author);
                    track.setAnnouncer(announcer);
                    resultTrackList.add(track);
                }
                db.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (db != null) {
                    db.endTransaction();
                    db.close();
                }
                if (mCallBack != null) {
                    mCallBack.onHistoryLoad(resultTrackList);
                }
            }
        }
    }
}
