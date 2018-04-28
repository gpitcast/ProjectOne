package com.feature.projectone.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/20.
 * 数据库操作类
 */

public class SearchHistoryDao {
    private Context context;
    private static final int DB_VERSION = 1;//版本号
    private static final String HISTORY_DB_NAME = "search_history_info.db";//数据库名称
    private static final String TABLE_NAME = "searchHistory";//表格名称
    private static final String KEY_NAME = "HistoryText";//表里面对应的字段
    private final SearchHistoryHelper historyHelper;

    public SearchHistoryDao(Context context) {
        this.context = context;
        historyHelper = new SearchHistoryHelper(context);
    }

    //添加数据
    public boolean add(String str) {
        SQLiteDatabase db = historyHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, str);
        try {
            db.insertOrThrow(TABLE_NAME, null, contentValues);
        } catch (SQLiteConstraintException e) {
            e.printStackTrace();
        }
//        db.execSQL("insert into " + TABLE_NAME + " (" + KEY_NAME + ")" + " values (" + str + ")"); sql语句方式添加数据
        db.close();
        return true;
    }

    //查询数据
    public List<String> select() {
        SQLiteDatabase db = historyHelper.getReadableDatabase();
        //去重复   倒序查询
        Cursor cursor = db.query(true, TABLE_NAME, new String[]{KEY_NAME}, null, null, null, null, "Id desc", null);//asc是表示升序，desc表示降序
        List<String> historyList = new ArrayList<>();
        if (cursor.getCount() > 0) {
            int count = 0;
            //每次查询最多最新的十条搜索数据
            while (cursor.moveToNext() && count < 10) {
                count++;
                String cursorString = cursor.getString(cursor.getColumnIndex(KEY_NAME));
                historyList.add(cursorString);
            }
        }
        db.close();
        cursor.close();
        return historyList;
    }

    //清空数据库的所有数据
    public void deleteAll() {
        SQLiteDatabase db = historyHelper.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }

    public class SearchHistoryHelper extends SQLiteOpenHelper {


        public SearchHistoryHelper(Context context) {
            super(context, HISTORY_DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            //创建表格
            String sql = "create table if not exists " + TABLE_NAME + " (Id integer primary key, HistoryText text)";
            sqLiteDatabase.execSQL(sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
            sqLiteDatabase.execSQL(sql);
            onCreate(sqLiteDatabase);
        }
    }
}
