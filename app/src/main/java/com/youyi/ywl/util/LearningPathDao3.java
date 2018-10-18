package com.youyi.ywl.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/9/17.
 * 精品微课 的学习轨迹数据库操作类
 */

public class LearningPathDao3 {
    private Context context;
    private int DB_VERSION = 1;//版本号
    private String DB_NAME;//数据库名称
    private String TABLE_NAME;//表格名称
    private String ID = "_id";//数据的唯一标识id
    //    private String URL = "url";//图片
//    private String TITLE = "title";//标题
    private String CONTENT = "content";//有关数据Map集合
    private String DATE = "date";//日期
    private String MILLIS = "millis";//时间毫秒值
    private LearningPathHelper learningPathHelper;
    private Gson gson;
    private long beforeTime = 300000;//间隔时间,取出指定时间内的数据

    public LearningPathDao3(Context context, String table_name, String db_name) {
        this.context = context;
        this.TABLE_NAME = table_name;
        this.DB_NAME = db_name;
        learningPathHelper = new LearningPathHelper(context);
        learningPathHelper.getWritableDatabase();//执行这一步才会创建数据库
    }

    //添加
    public boolean insert(HashMap<String, Object> map) {
        SQLiteDatabase rd = learningPathHelper.getReadableDatabase();
        SQLiteDatabase wd = learningPathHelper.getWritableDatabase();
        Cursor cursor = rd.query(TABLE_NAME, null, ID + "=?", new String[]{map.get("id") + ""}, null, null, null);
        if (cursor.moveToNext()) {
            //代表有这条数据,删除数据
            int delete = wd.delete(TABLE_NAME, ID + "=?", new String[]{map.get("id") + ""});
        }

        //获取日期
        Calendar instance = Calendar.getInstance();
        int year = instance.get(Calendar.YEAR);
        int month = instance.get(Calendar.MONTH);
        int day = instance.get(Calendar.DAY_OF_MONTH);
        String monthStr = "";
        String dayStr = "";
        if ((month + 1) < 10) {
            monthStr = "0" + (month + 1);
        } else {
            monthStr = (month + 1) + "";
        }

        if (day < 10) {
            dayStr = "0" + day;
        } else {
            dayStr = day + "";
        }

        //获取当前时间的毫秒值
        long currentTimeMillis = System.currentTimeMillis();

        if (gson == null) {
            gson = new Gson();
        }
        String mapStr = gson.toJson(map);

        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, map.get("id") + "");
        contentValues.put(CONTENT, mapStr);
        contentValues.put(DATE, year + "." + monthStr + "." + dayStr);
        contentValues.put(MILLIS, currentTimeMillis);

        wd.insert(TABLE_NAME, null, contentValues);
        rd.close();
        wd.close();
        return true;
    }

    public List<HashMap<String, Object>> select() {
        if (gson == null) {
            gson = new Gson();
        }

        SQLiteDatabase rd = learningPathHelper.getReadableDatabase();

        //获取当前时间的毫秒值,计算得到指定时间的毫秒值
        long currentTimeMillis = System.currentTimeMillis();
        long needTime = currentTimeMillis - beforeTime;

        Cursor cursor = rd.query(true, TABLE_NAME, new String[]{ID, CONTENT, DATE, MILLIS}, MILLIS + ">?",
                new String[]{needTime + ""}, null, null, null, null);
        ArrayList<HashMap<String, Object>> learningPathList = new ArrayList<>();
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex(ID));
            String content = cursor.getString(cursor.getColumnIndex(CONTENT));
            String date = cursor.getString(cursor.getColumnIndex(DATE));
            long millis = cursor.getLong(cursor.getColumnIndex(MILLIS));

            Type type = new TypeToken<HashMap<String, Object>>() {
            }.getType();
            HashMap<String, Object> map = (HashMap<String, Object>) gson.fromJson(content, type);
            map.put("date", date);
            map.put("millis", millis);
            learningPathList.add(map);
        }
        rd.close();
        return learningPathList;
    }

    public int deleteAll() {
        SQLiteDatabase wd = learningPathHelper.getWritableDatabase();
        int isDelete = wd.delete(TABLE_NAME, null, null);//d返回值是受影响的行数,0代表删除失败
        wd.close();
        return isDelete;
    }

    public class LearningPathHelper extends SQLiteOpenHelper {

        public LearningPathHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //创建表格
            String sql = "create table if not exists " + TABLE_NAME + " (" + "id integer primary key autoincrement," + ID + " text," + CONTENT + " text,"
                    + DATE + " text," + MILLIS + " integer)";
            db.execSQL(sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            String sql = "DROP TABLE IF EXISTS" + TABLE_NAME;
            db.execSQL(sql);
            onCreate(db);
        }
    }
}
