package com.example.fx50j.weather50;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FX50J on 2016/7/17.
 */
public class ChooseCityActivity extends AppCompatActivity {

    private Button btu_Add;
    private Button btu_Delete;
    private ListView lv_city;
    private WeatherDatabaseAdpter weatherDatabaseAdpter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choosecity_list);

        final List<CitynameBean> citynameBeanList = new ArrayList<CitynameBean>();
        weatherDatabaseAdpter = new WeatherDatabaseAdpter(this,"WeatherData.db",null,4);
        final SQLiteDatabase db = weatherDatabaseAdpter.getWritableDatabase();
        final Cursor cursor = db.query("city", null, null, null, null, null, null);

        btu_Add = (Button) findViewById(R.id.btu_AddCity);
        btu_Delete= (Button) findViewById(R.id.btu_DeleteCity);
        lv_city= (ListView) findViewById(R.id.lv_city);

        //跳转至查询城市界面
        btu_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseCityActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        //点击删除已选城市
        btu_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btu_Delete.setAlpha(0.5f);
                lv_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        SQLiteDatabase db_delete = weatherDatabaseAdpter.getWritableDatabase();
                        String cityid = citynameBeanList.get(position).GetId().toString();
                        db_delete.delete("city", "cityid = ?", new String[]{cityid});
                        db_delete.delete("today_weather", "cityid = ?", new String[]{cityid});
                        db_delete.delete("future_weather", "cityid = ?", new String[]{cityid});

                        Cursor cursor_count = db_delete.query("count", null, null, null, null, null, null);
                        Cursor cursor_city = db_delete.query("city", null, null, null, null, null, null);

                        ContentValues values = new ContentValues();
                        int count = 0;
                        if (cursor_count.moveToFirst()) {
                            do {
                                count = cursor_count.getInt(cursor_count.getColumnIndex("count"));
//                                Log.d("状态", "ChooseActivity Load Data:" + count);
                            } while (cursor_count.moveToNext());
                        }
                        cursor_count.close();
                        count -= 1;
                        if(count>=0){
//                            Log.d("状态", "ChooseActivity Change Data:" + count);
                            values.put("count", count);
                            db.update("count",values,null,null);
                            values.clear();
                        }else{
                            values.put("count",0);
                            db.update("count",values,null,null);
                            values.clear();
                        }

                        //重新显示List
                        if (cursor_city.moveToFirst()) {
                            final List<CitynameBean> citynameBeanList_refresh = new ArrayList<CitynameBean>();
                            do {
                                String city_name = cursor_city.getString(cursor.getColumnIndex("cityname"));
                                String city_id = cursor_city.getString(cursor.getColumnIndex("cityid"));
                                CitynameBean citynameBean = new CitynameBean();
                                citynameBean.SetCountry(city_name);
                                citynameBean.SetId(city_id);
                                citynameBeanList_refresh.add(citynameBean);
                                CitynameAdpter adpter = new CitynameAdpter(ChooseCityActivity.this, citynameBeanList_refresh);
                                lv_city.setAdapter(adpter);
                            } while (cursor_city.moveToNext());
                        }
                        cursor_city.close();
                    }
                });
            }
        });

        //从数据库获取城市名称显示在已选城市的List上

        if(cursor.moveToFirst()){
            do{
                String cityname = cursor.getString(cursor.getColumnIndex("cityname"));
                String cityid = cursor.getString(cursor.getColumnIndex("cityid"));
                CitynameBean citynameBean = new CitynameBean();
                citynameBean.SetCountry(cityname);
                citynameBean.SetId(cityid);
                citynameBeanList.add(citynameBean);

                CitynameAdpter adpter = new CitynameAdpter(ChooseCityActivity.this, citynameBeanList);
                lv_city.setAdapter(adpter);
            } while (cursor.moveToNext());
        }
        cursor.close();

        //点击item跳转界面
        lv_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String city = citynameBeanList.get(position).GetCountry().toString();
                String cityid = citynameBeanList.get(position).GetId().toString();

                Intent intent = new Intent(ChooseCityActivity.this, WeatherActivity.class);
                intent.putExtra("Cityname", city);
                intent.putExtra("Cityid", cityid);
                startActivity(intent);
            }
        });
    }

}
