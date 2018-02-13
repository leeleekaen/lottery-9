package com.jingtuo.android.lottery.model.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.jingtuo.android.lottery.model.entity.DcbInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 彩票数据库
 * <p>
 * Created by jingtuo on 2018/2/12.
 */

public class DBRepository {

    private static final String TAG = DBRepository.class.getSimpleName();

    private static final int VERSION = 1;

    private static final String TABLE_DCB = "dcb";
    private static final String COLUMN_YEAR = "year";
    private static final String COLUMN_SERIAL_NO = "serialNo";
    private static final String COLUMN_DCB_RED_BALL = "redBall";
    private static final String COLUMN_DCB_BLUE_BALL = "blueBall";
    private static final String COLUMN_INSERT_TIME = "insertTime";

    private static final String ORDER_BY_ASC = "ASC";
    private static final String ORDER_BY_DESC = "DESC";


    private DBRepository() {

    }

    public static DBRepository getInstance() {
        return SingletonHolder.INSTANCE;
    }


    private static class SingletonHolder {
        private static final DBRepository INSTANCE = new DBRepository();

        private SingletonHolder() {
        }
    }

    private class DB extends SQLiteOpenHelper {

        public DB(Context context, SQLiteDatabase.CursorFactory factory) {
            super(context, context.getPackageName(), factory, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //双色球
            db.execSQL("CREATE TABLE  IF NOT EXISTS " +
                    TABLE_DCB + " (" +
                    COLUMN_YEAR + " INTEGER, " +
                    COLUMN_SERIAL_NO + " INTEGER, " +
                    COLUMN_DCB_RED_BALL + " TEXT, " +
                    COLUMN_DCB_BLUE_BALL + " TEXT, " +
                    COLUMN_INSERT_TIME + " INTEGER, PRIMARY KEY(year, serialNo))");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //TODO 新版本暂无更新
        }
    }

    private SQLiteDatabase getReadableDatabase(Context context) {
        return new DB(context, null).getReadableDatabase();
    }

    private SQLiteDatabase getWritableDatabase(Context context) {
        return new DB(context, null).getWritableDatabase();
    }


    public void insertDcb(Context context, DcbInfo dcbInfo) {
        SQLiteDatabase database = getWritableDatabase(context);
        ContentValues values = new ContentValues();
        values.put(COLUMN_YEAR, dcbInfo.getYear());
        values.put(COLUMN_SERIAL_NO, dcbInfo.getSerialNo());
        values.put(COLUMN_DCB_RED_BALL, dcbInfo.getRedBall());
        values.put(COLUMN_DCB_BLUE_BALL, dcbInfo.getBlueBall());
        values.put(COLUMN_INSERT_TIME, System.currentTimeMillis());
        database.insert(TABLE_DCB, null, values);
    }

    /**
     * 查询指定年份的双色球
     *
     * @param context  上下文
     * @param year     年份
     * @param asc      是否升序
     * @param pageNo   页码
     * @param pageSize 每页数量
     * @return
     */
    public List<DcbInfo> queryDcb(Context context, int year, boolean asc, int pageNo, int pageSize) {
        List<DcbInfo> result = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase(context);
        try {
            Cursor cursor = database.query(TABLE_DCB, null, COLUMN_YEAR + "=?", new String[]{String.valueOf(year)}, null, null,
                    COLUMN_SERIAL_NO + " " + (asc ? ORDER_BY_ASC : ORDER_BY_DESC), formatToLimit(pageNo, pageSize));
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    result.add(getDcb(cursor));
                }
                cursor.close();
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return result;
    }

    /**
     * @param pageNo   页码
     * @param pageSize 每页数量
     * @return
     */
    private String formatToLimit(int pageNo, int pageSize) {
        return (pageNo - 1) * pageSize + ", " + pageSize;
    }

    /**
     * 获取双色球信息
     *
     * @param cursor
     * @return
     */
    private DcbInfo getDcb(Cursor cursor) {
        DcbInfo dcbInfo = new DcbInfo();
        dcbInfo.setYear(cursor.getInt(cursor.getColumnIndex(COLUMN_YEAR)));
        dcbInfo.setSerialNo(cursor.getInt(cursor.getColumnIndex(COLUMN_SERIAL_NO)));
        dcbInfo.setRedBall(cursor.getString(cursor.getColumnIndex(COLUMN_DCB_RED_BALL)));
        dcbInfo.setBlueBall(cursor.getString(cursor.getColumnIndex(COLUMN_DCB_BLUE_BALL)));
        dcbInfo.setInsertTime(cursor.getLong(cursor.getColumnIndex(COLUMN_INSERT_TIME)));
        return dcbInfo;
    }

    /**
     * 查询指定年份的双色球
     *
     * @param context  上下文
     * @param asc      是否升序
     * @param pageNo   页码
     * @param pageSize 每页数量
     * @return
     */
    public List<DcbInfo> queryDcb(Context context, boolean asc, int pageNo, int pageSize) {
        List<DcbInfo> result = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase(context);
        try {
            Cursor cursor = database.query(TABLE_DCB, null, null, null, null, null,
                    COLUMN_SERIAL_NO + " " + (asc ? ORDER_BY_ASC : ORDER_BY_DESC), formatToLimit(pageNo, pageSize));
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    result.add(getDcb(cursor));
                }
                cursor.close();
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return result;
    }


}
