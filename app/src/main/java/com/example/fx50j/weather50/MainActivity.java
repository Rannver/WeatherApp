package com.example.fx50j.weather50;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.baidu.apistore.sdk.ApiCallBack;
import com.baidu.apistore.sdk.ApiStoreSDK;
import com.baidu.apistore.sdk.network.Parameters;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText cityname_in;
    private Button btu_check;
    private ListView list_city;
    private WeatherDatabaseAdpter weatherDatabaseAdpter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        cityname_in = (EditText) findViewById(R.id.ed_checkcity);
        btu_check = (Button) findViewById(R.id.btu_checkcity);
        list_city = (ListView) findViewById(R.id.list_city);

        weatherDatabaseAdpter = new WeatherDatabaseAdpter(this,"WeatherData.db",null,4);
        SQLiteDatabase db = weatherDatabaseAdpter.getWritableDatabase();
        ContentValues values_count = new ContentValues();
        Cursor cursor_count = db.query("count", null, null, null, null, null, null);
        int flag=0;

        values_count.put("count", 0);
        db.insert("count", null, values_count);
        values_count.clear();

//        if (cursor_count.moveToFirst()){
//            do{
//                Log.d("状态","flag="+cursor_count.getInt(cursor_count.getColumnIndex("count")));
//            }while(cursor_count.moveToNext());
//        }

        if (cursor_count.moveToFirst()){
            flag = cursor_count.getInt(cursor_count.getColumnIndex("count"));
//            Log.d("状态","初始化的flag:"+flag);
        }
        cursor_count.close();

        //将所有的数目全部更新成最新的那个
        values_count.put("count",flag);
        db.update("count",values_count,null,null);

        btu_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String cityname;
                cityname = cityname_in.getText().toString();
                Log.d("cityname", cityname);

                ShowCityList(cityname);
            }
        });
    }

    public void ShowCityList(String cityname){

        Parameters parameters = new Parameters();
        parameters.put("cityname",cityname);
        ApiStoreSDK.execute("http://apis.baidu.com/apistore/weatherservice/citylist", ApiStoreSDK.GET, parameters, new ApiCallBack() {
            @Override
            public void onSuccess(int i, String s) {
                Log.d("State:", "请求成功");
                Log.d("String:", s);

                JSONObject jsonObject;
                CitynameBean citynameBean;
                final List<CitynameBean> citynameBeanList = new ArrayList<CitynameBean>();

                try {
                    jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("retData");
                    for (int a = 0; a < jsonArray.length(); a++) {
                        jsonObject = jsonArray.getJSONObject(a);
                        citynameBean = new CitynameBean();
                        citynameBean.SetProvince(jsonObject.getString("province_cn"));
                        citynameBean.SetCity(jsonObject.getString("district_cn"));
                        citynameBean.SetCountry(jsonObject.getString("name_cn"));
                        citynameBean.SetId(jsonObject.getString("area_id"));
                        citynameBeanList.add(citynameBean);

                        CitynameAdpter adpter = new CitynameAdpter(MainActivity.this, citynameBeanList);
                        list_city.setAdapter(adpter);
                        //适配器适配

                        list_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                String city =  citynameBeanList.get(position).GetCountry().toString();
                                String cityid = citynameBeanList.get(position).GetId().toString();

                                SQLiteDatabase db = weatherDatabaseAdpter.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                Cursor cursor_count = db.query("count", null, null, null, null, null, null);

                                //查询数据库中是否有重复的城市 若重复不加入数据库
//                                Log.d("返回状态", String.valueOf(QuaryData(city, cityid)));
                                if (QuaryData(city, cityid)) {

                                    int flag=0;
                                    if (cursor_count.moveToLast()){
                                        flag = cursor_count.getInt(cursor_count.getColumnIndex("count"));
                                    }
                                    cursor_count.close();
                                    flag+=1;
                                    values.put("count", flag);
                                    db.update("count", values, null, null);
                                    values.clear();
                                    //更新已存储的城市的数目
                                    Log.d("状态", "已储存城市数目已更新");

                                    values.put("cityname", city);
                                    values.put("cityid", cityid);
                                    db.insert("city", null, values);
                                    values.clear();
                                    Log.d("状态", "不是曾经添加过的城市，已存入数据库");

                                    Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
                                    intent.putExtra("Cityname", city);
                                    intent.putExtra("Cityid", cityid);
                                    startActivity(intent);

                                } else{
                                    Log.d("状态", "已添加此城市，不存入数据库");
                                    Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
                                    intent.putExtra("Cityname", city);
                                    intent.putExtra("Cityid", cityid);
                                    startActivity(intent);
                                }

                            }
                        });

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                super.onSuccess(i, s);
            }

            @Override
            public void onError(int i, String s, Exception e) {
                Log.d("State", "请求失败");
                super.onError(i, s, e);
            }
        });
    }

    public boolean QuaryData(String city, String id){

        Log.d("状态","已执行查询函数");

        SQLiteDatabase db = weatherDatabaseAdpter.getWritableDatabase();
        Cursor cursor_city = db.query("city", null, null, null, null, null, null);
        Cursor cursor_count = db.query("count",null,null,null,null,null,null);
        int count = 0;
        int i = 0;

        if (cursor_count.moveToFirst()){
            count = cursor_count.getInt(cursor_count.getColumnIndex("count"));
            Log.d("状态","count="+count);
        }
        cursor_count.close();

        if(cursor_city.moveToFirst()){
            for (i=0;cursor_city.moveToPosition(i);i++){
                String cityname = cursor_city.getString(cursor_city.getColumnIndex("cityname"));
                String cityid = cursor_city.getString(cursor_city.getColumnIndex("cityid"));
                Log.d("数据库","城市名字："+cityname);
                Log.d("数据库", "城市id：" + cityid);
                if ((cityname.equals(city))&&(cityid.equals(id))){
//                    Log.d("i","break:"+i);
                    break;
                }
            }
        }
//        Log.d("i","for:"+i);
        cursor_city.close();
        if (count>i){
            return false;
        }else{
            return true;
        }
    }

}
