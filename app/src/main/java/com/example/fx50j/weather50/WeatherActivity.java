package com.example.fx50j.weather50;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.apistore.sdk.ApiCallBack;
import com.baidu.apistore.sdk.ApiStoreSDK;
import com.baidu.apistore.sdk.network.Parameters;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FX50J on 2016/7/14.
 */
public class WeatherActivity extends AppCompatActivity {

    private TextView tv_cityname;
    private TextView tv_temp;
    private TextView tv_weather;
    private TextView tv_wind;
    private TextView tv_SunTime;
    private TextView tv_position;
    private TextView tv_f5;
    private ImageButton btu_city;
    private ImageButton  btu_refresh;
    private RecyclerView recyclerView;
    private WeatherDatabaseAdpter weatherDatabaseAdpter;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_layout);

        recyclerView = (RecyclerView) findViewById(R.id.R_List);
        tv_cityname= (TextView) findViewById(R.id.tv_cityname);
        tv_temp = (TextView) findViewById(R.id.tv_temperature);
        tv_weather= (TextView) findViewById(R.id.tv_weather);
        tv_wind = (TextView) findViewById(R.id.tv_wind);
        tv_SunTime= (TextView) findViewById(R.id.tv_SunTime);
        tv_position= (TextView) findViewById(R.id.tv_position);
        tv_f5 = (TextView) findViewById(R.id.tv_f5);
        btu_city= (ImageButton) findViewById(R.id.btu_city);
        //城市管理按钮的页面跳转(跳转至已选城市界面
        btu_refresh = (ImageButton) findViewById(R.id.btu_refresh);
        //刷新按钮
        weatherDatabaseAdpter =new WeatherDatabaseAdpter(this,"WeatherData.db",null,4);

        InitView();

        //点击按钮跳转至城市管理界面
        btu_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeatherActivity.this, ChooseCityActivity.class);
                startActivity(intent);
            }
        });




        //获取上一个页面传来的数据
        String str="";
        Intent intent = getIntent();
        str = intent.getStringExtra("Cityname");
        String citycode="";
        citycode = intent.getStringExtra("Cityid");

        if (str==null&&citycode==null){
            //读取默认城市的天气数据
            weatherDatabaseAdpter =new WeatherDatabaseAdpter(this,"WeatherData.db",null,4);
            SQLiteDatabase db = weatherDatabaseAdpter.getWritableDatabase();
            Cursor cursor = db.query("city", null, null, null, null, null, null);
            if (cursor.moveToFirst()){
                String city_code = cursor.getString(cursor.getColumnIndex("cityid"));
                String city_name = cursor.getString(cursor.getColumnIndex("cityname"));
                tv_cityname.setText(city_name);
                LoadWeatherData_today(city_code);
                LoadWeatherData_Future(city_code);
            }
        }else{
            //显示现在的天气
            ShowCityNowWeather(str, citycode);
            ShowCityFutureWeather(str, citycode);
        }

        //点击按钮刷新当前城市天气状况
        btu_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str="";
                Intent intent = getIntent();
                str = intent.getStringExtra("Cityname");
                String citycode = intent.getStringExtra("Cityid");
                if (str==null&&citycode==null){
                    //读取默认城市的天气数据
                    SQLiteDatabase db = weatherDatabaseAdpter.getWritableDatabase();
                    Cursor cursor = db.query("city", null, null, null, null, null, null);
                    if (cursor.moveToFirst()){
                        String city_code = cursor.getString(cursor.getColumnIndex("cityid"));
                        String city_name = cursor.getString(cursor.getColumnIndex("cityname"));
                        tv_cityname.setText(city_name);
                        ShowCityNowWeather(city_name,city_code);
                        ShowCityFutureWeather(city_name,city_code);
                    }
                }else{
                    //显示现在的天气
                    ShowCityNowWeather(str, citycode);
                    ShowCityFutureWeather(str, citycode);
                }
            }
        });


    }

    //显示现在的天气的函数
    public void ShowCityNowWeather(final String city_name, final String city_id){

        tv_cityname.setText(city_name);

        Parameters parameters = new Parameters();
        parameters.put("cityname", city_name);

        ApiStoreSDK.execute("http://apis.baidu.com/apistore/weatherservice/cityname", ApiStoreSDK.GET, parameters, new ApiCallBack() {

            @Override
            public void onSuccess(int i, String s) {
                super.onSuccess(i, s);
                Log.d("State", "请求成功");
                Log.d("String", s);

                //联网请求成功,先删除原来的数据
                DeleteWeatherData_today(city_id);

                WeatherBean weatherBean = null;
                JSONObject jsonObject;

                try {
                    jsonObject = new JSONObject(s);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("retData");
                    weatherBean = new WeatherBean();
                    weatherBean.SetTemp(jsonObject1.optString("temp"));
                    weatherBean.SetWeather(jsonObject1.optString("weather"));
                    weatherBean.SetTempOfmin(jsonObject1.optString("l_tmp"));
                    weatherBean.SetTempOfMax(jsonObject1.optString("h_tmp"));
                    weatherBean.SetWD(jsonObject1.optString("WD"));
                    weatherBean.SetWS(jsonObject1.optString("WS"));
                    weatherBean.SetSunrise(jsonObject1.optString("sunrise"));
                    weatherBean.SetSunset(jsonObject1.optString("sunset"));
                    weatherBean.SetLongitude(jsonObject1.optString("longitude"));
                    weatherBean.SetLatitude(jsonObject1.optString("latitude"));
                    weatherBean.SetTime(jsonObject1.optString("time"));
                    weatherBean.SetDate(jsonObject1.optString("date"));

                    tv_temp.setText(jsonObject1.optString("temp"));
                    tv_weather.setText(jsonObject1.optString("weather") + "  " + jsonObject1.optString("l_tmp") + "℃~" + jsonObject1.optString("h_tmp") + "℃");
                    tv_wind.setText(jsonObject1.optString("WD") + "  " + jsonObject1.optString("WS"));
                    tv_SunTime.setText("日出：" + jsonObject1.optString("sunrise") + "  " + "日落：" + jsonObject1.optString("sunset"));
                    tv_position.setText("坐标：" + jsonObject1.optString("longitude") + "," + jsonObject1.optString("latitude"));
                    tv_f5.setText(jsonObject1.optString("date") + " " + jsonObject1.optString("time"));

                    SaveWeatherData_Today(
                            city_name,city_id,
                            weatherBean.GetTemp(),weatherBean.GetWeather(),weatherBean.GetTempOfMin(),weatherBean.GetTempOfMax(),weatherBean.GetWD(),weatherBean.GetWS(),weatherBean.GetSunrise(),
                            weatherBean.GetSunset(),weatherBean.GetLongitude(),weatherBean.GetLatitude(),weatherBean.GetTime(),weatherBean.GetDate());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(int i, String s, Exception e) {
                super.onError(i, s, e);
                Log.d("State", "请求失败");
                Toast toast = Toast.makeText(WeatherActivity.this,"联网失败，请重试",Toast.LENGTH_LONG);
                toast.show();
                //联网失败时显示在数据库里存储的信息
                LoadWeatherData_today(city_id);
            }
        });
    }


    //显示城市未来天气的函数
    public void ShowCityFutureWeather (final String str, final String citycode){

        Parameters parameters = new Parameters();
        parameters.put("cityname",str);
        parameters.put("cityid", citycode);
        ApiStoreSDK.execute("http://apis.baidu.com/apistore/weatherservice/recentweathers", ApiStoreSDK.GET, parameters, new ApiCallBack() {

            @Override
            public void onSuccess(int i, String s) {
                Log.d("状态", "请求成功");
                Log.d("数据", s);

                //删除原有未来天气数据
                DeleteWeatherData_Future(citycode);

                FutureWeatherBean futureWeatherBean = null;
                JSONObject jsonObject, jsonObject_future,jsonObject_history;
                final List<FutureWeatherBean> futureWeatherBeanList = new ArrayList<FutureWeatherBean>();

                try {
                    jsonObject = new JSONObject(s);
                    jsonObject_future = jsonObject.getJSONObject("retData");
                    jsonObject_history = jsonObject.getJSONObject("retData");
                    Log.d("状态", "retData获取");
                    JSONArray jsonArray = jsonObject_future.getJSONArray("forecast");
                    Log.d("长度", String.valueOf(jsonArray.length()));
                    JSONArray jsonArray_history = jsonObject_future.getJSONArray("history");
                    for (int a = 0; a < jsonArray.length(); a++) {
                        jsonObject_future = jsonArray.getJSONObject(a);
                        futureWeatherBean = new FutureWeatherBean();
                        futureWeatherBean.SetDate(jsonObject_future.optString("date"));
                        if (a==0){
                            futureWeatherBean.SetWeek("明天");
                        }else{
                            futureWeatherBean.SetWeek(jsonObject_future.optString("week"));
                        }
                        futureWeatherBean.SetWind(jsonObject_future.optString("fengli"));
                        futureWeatherBean.SetHightemp(jsonObject_future.optString("hightemp"));
                        futureWeatherBean.SetLowtemp(jsonObject_future.optString("lowtemp"));
                        futureWeatherBean.SetType(jsonObject_future.optString("type"));
                        futureWeatherBeanList.add(futureWeatherBean);

                        //将获得数据存入数据库
                        SaveWeatherData_Future(citycode,
                                futureWeatherBean.GetDate(), futureWeatherBean.GetWeek(), futureWeatherBean.GetWind(), futureWeatherBean.GetHightemp(), futureWeatherBean.GetLowtemp(), futureWeatherBean.GetType());
                        // 数据源的获取

                    }

                    for(int a=0;a<jsonArray_history.length()-3;a++){
                        jsonObject_future = jsonArray_history.getJSONObject(a);
                        futureWeatherBean = new FutureWeatherBean();
                        futureWeatherBean.SetDate(jsonObject_future.optString("date"));
                        if (a==0){
                            futureWeatherBean.SetWeek("昨天");
                        }else{
                            futureWeatherBean.SetWeek(jsonObject_future.optString("week"));
                        }
                        futureWeatherBean.SetWind(jsonObject_future.optString("fengli"));
                        futureWeatherBean.SetHightemp(jsonObject_future.optString("hightemp"));
                        futureWeatherBean.SetLowtemp(jsonObject_future.optString("lowtemp"));
                        futureWeatherBean.SetType(jsonObject_future.optString("type"));
                        futureWeatherBeanList.add(futureWeatherBean);

                        //存入数据库
                        SaveWeatherData_Future(citycode,
                                futureWeatherBean.GetDate(),futureWeatherBean.GetWeek(),futureWeatherBean.GetWind(),futureWeatherBean.GetHightemp(),futureWeatherBean.GetLowtemp(),futureWeatherBean.GetType());
                    }

                    /*适配器适配
                       1.布局管理器！(创建布局管理器，决定是水平排列还是垂直排列
                       2.setAdpter！
                    */
                    FutureWeatherAdpter futureWeatherAdpter = new FutureWeatherAdpter(futureWeatherBeanList);
                    recyclerView.setAdapter(futureWeatherAdpter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                super.onSuccess(i, s);
            }

            @Override
            public void onError(int i, String s, Exception e) {
                Log.d("状态", "请求失败");
                super.onError(i, s, e);
                LoadWeatherData_Future(citycode);
            }
        });


    }

    //RecyclerView的布局管理器
    public void InitView(){
        //创建RecyclerView的布局管理器
        LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(linearLayoutManager.HORIZONTAL);//设置为水平方向排列
        recyclerView.setLayoutManager(linearLayoutManager);
    }
    //将今日天气存入数据库

    public void SaveWeatherData_Today(
            String cityname,String cityid,
            String today_temp,String today_type,String today_lowtemp,String today_hightemp,String today_wd,String today_ws,String today_sunrise,String today_sunset,
            String today_longitude,String today_latitude,String today_time,String today_date){

        weatherDatabaseAdpter =new WeatherDatabaseAdpter(this,"WeatherData.db",null,4);
        SQLiteDatabase db = weatherDatabaseAdpter.getWritableDatabase();
        ContentValues values = new ContentValues();
//        Cursor cursor = db.query("weather",null,null,null,null,null,null);

        //将所选城市的今日天气信息存入数据库
        values.put("cityname",cityname);
        values.put("cityid",cityid);
        values.put("today_temp",today_temp);
        values.put("today_type",today_type);
        values.put("today_tempmin",today_lowtemp);
        values.put("today_tempmax",today_hightemp);
        values.put("today_wd",today_wd);
        values.put("today_ws",today_ws);
        values.put("today_sunrise",today_sunrise);
        values.put("today_sunset",today_sunset);
        values.put("today_longitude",today_longitude);
        values.put("today_latitude",today_latitude);
        values.put("today_time",today_time);
        values.put("today_date",today_date);
        db.insert("today_weather", null, values);
        values.clear();
        Log.d("状态","今日天气已存入数据库");
    }

    public void SaveWeatherData_Future(
            String cityid,
            String future_date,String future_week,String future_wind,String future_hightemp,String future_lowtemp,String future_type){
        weatherDatabaseAdpter =new WeatherDatabaseAdpter(this,"WeatherData.db",null,4);
        SQLiteDatabase db = weatherDatabaseAdpter.getWritableDatabase();
        ContentValues values = new ContentValues();
//        Cursor cursor = db.query("weather",null,null,null,null,null,null);

        //将所选城市的未来天气信息存入数据库
        values.put("cityid",cityid);
        values.put("future_date",future_date);
        values.put("future_week",future_week);
        values.put("future_wind",future_wind);
        values.put("future_tempmax",future_hightemp);
        values.put("future_tempmin", future_lowtemp);
        values.put("future_type", future_type);
        db.insert("future_weather", null, values);
        values.clear();
        Log.d("状态","未来天气数据已存入数据库");
    }

    //删除此前存储过的城市天气信息
    public void DeleteWeatherData_today(String cityid){
        weatherDatabaseAdpter =new WeatherDatabaseAdpter(this,"WeatherData.db",null,4);
        SQLiteDatabase db = weatherDatabaseAdpter.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("cityid",cityid);
        db.delete("today_weather", "cityid = ?", new String[]{cityid});
        values.clear();

        Log.d("状态", "原有今日天气数据已成功清除");
    }

    public void DeleteWeatherData_Future(String cityid){
        weatherDatabaseAdpter =new WeatherDatabaseAdpter(this,"WeatherData.db",null,4);
        SQLiteDatabase db = weatherDatabaseAdpter.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("cityid",cityid);
        db.delete("future_weather", "cityid = ?", new String[]{cityid});
        values.clear();

        Log.d("状态", "原有未来天气数据已成功清除");
    }

    //从数据库读取今日天气信息
    public void LoadWeatherData_today(String city_id){
        weatherDatabaseAdpter =new WeatherDatabaseAdpter(this,"WeatherData.db",null,4);
        SQLiteDatabase db = weatherDatabaseAdpter.getWritableDatabase();
        Cursor cursor = db.query("today_weather",null,"cityid = ?",new String[]{city_id},null,null,null);

        if (cursor.moveToFirst()){
            String today_temp = cursor.getString(cursor.getColumnIndex("today_temp"));
            String today_type = cursor.getString(cursor.getColumnIndex("today_type"));
            String today_tempmin = cursor.getString(cursor.getColumnIndex("today_tempmin"));
            String today_tempmax = cursor.getString(cursor.getColumnIndex("today_tempmax"));
            String today_wd = cursor.getString(cursor.getColumnIndex("today_wd"));
            String today_ws = cursor.getString(cursor.getColumnIndex("today_ws"));
            String today_sunrise = cursor.getString(cursor.getColumnIndex("today_sunrise"));
            String today_sunset = cursor.getString(cursor.getColumnIndex("today_sunset"));
            String today_longitude = cursor.getString(cursor.getColumnIndex("today_longitude"));
            String today_latitude = cursor.getString(cursor.getColumnIndex("today_latitude"));
            String today_time = cursor.getString(cursor.getColumnIndex("today_time"));
            String today_date = cursor.getString(cursor.getColumnIndex("today_date"));

            tv_temp.setText(today_temp);
            tv_weather.setText(today_type + "  " + today_tempmin + "℃~" + today_tempmax + "℃");
            tv_wind.setText(today_wd + "  " + today_ws);
            tv_SunTime.setText("日出：" + today_sunrise + "  " + "日落：" + today_sunset);
            tv_position.setText("坐标：" + today_longitude + "," + today_latitude);
            tv_f5.setText(today_date + " " + today_time);
        }
    }

    //从数据库读取未来天气信息
    public void LoadWeatherData_Future(String city_id){
        weatherDatabaseAdpter =new WeatherDatabaseAdpter(this,"WeatherData.db",null,4);
        SQLiteDatabase db = weatherDatabaseAdpter.getWritableDatabase();
        Cursor cursor = db.query("future_weather", null, "cityid = ?", new String[]{city_id}, null, null, null);
        final List<FutureWeatherBean> futureWeatherBeanList = new ArrayList<FutureWeatherBean>();

        if (cursor.moveToFirst()){
            do {
                FutureWeatherBean futureWeatherBean = new FutureWeatherBean();
                String future_date = cursor.getString(cursor.getColumnIndex("future_date"));
                String future_week = cursor.getString(cursor.getColumnIndex("future_week"));
                String future_wind = cursor.getString(cursor.getColumnIndex("future_wind"));
                String future_tempmax = cursor.getString(cursor.getColumnIndex("future_tempmax"));
                String future_tempmin = cursor.getString(cursor.getColumnIndex("future_tempmin"));
                String future_type = cursor.getString(cursor.getColumnIndex("future_type"));
                futureWeatherBean.SetDate(future_date);
                futureWeatherBean.SetWeek(future_week);
                futureWeatherBean.SetWind(future_wind);
                futureWeatherBean.SetHightemp(future_tempmax);
                futureWeatherBean.SetLowtemp(future_tempmin);
                futureWeatherBean.SetType(future_type);
                futureWeatherBeanList.add(futureWeatherBean);

                FutureWeatherAdpter futureWeatherAdpter = new FutureWeatherAdpter(futureWeatherBeanList);
                recyclerView.setAdapter(futureWeatherAdpter);
            }while (cursor.moveToNext());
        }
    }
}
