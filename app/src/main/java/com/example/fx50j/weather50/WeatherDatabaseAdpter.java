package com.example.fx50j.weather50;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 所有天气数据的数据库
 */
public class WeatherDatabaseAdpter extends SQLiteOpenHelper{

    //今日天气数据表
    public static final String CREATE_TODAY = "create table today_weather(" +
            "id integer primary key autoincrement," +
            "cityname text," +   //城市名称
            "cityid text," +   //城市id
            "today_temp text," +  //今日温度
            "today_type text," +  //今日天气类型
            "today_tempmin text," +  //今日最低温度
            "today_tempmax text," +  //今日最高温度
            "today_wd text," +  //今日风向
            "today_ws text," +  //今日风力
            "today_sunrise text," +  //今日日出时间
            "today_sunset text," +   //今日日落时间
            "today_longitude text," +  //城市经度
            "today_latitude text," +  //城市纬度
            "today_time text," +  //今日更新时间
            "today_date text)" ;//今日日期

    //未来天气数据表
    public static final String CREATE_FUTURE = "create table future_weather(" +
            "id integer primary key autoincrement," +
            "cityid text," +
            "future_date text," +  //未来天气日期
            "future_week text," +  //未来星期
            "future_wind text," +  //未来风向
            "future_tempmax text," +  //未来最高温度
            "future_tempmin text," +  //未来最低温度
            "future_type text)";  //未来天气类型

    //城市信息表
    public static final String CREATE_CITY = "create table city(" +
            "id integer primary key autoincrement," +
            "cityname text," +
            "cityid text)";

    //已选城市数目
    public static final String CREATE_COUNT = "create table count(" +
            "id integer primary key autoincrement," +
            "count integer)";

    private Context mcontext;

    public WeatherDatabaseAdpter(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mcontext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TODAY);
        db.execSQL(CREATE_FUTURE);
        db.execSQL(CREATE_CITY);
        db.execSQL(CREATE_COUNT);
        Log.d("状态","数据库创建成功");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop table if exists today_weather");
        db.execSQL("drop table if exists future_weather");
        db.execSQL("drop table if exists city");
        db.execSQL("drop table if exists count");

    }
}
