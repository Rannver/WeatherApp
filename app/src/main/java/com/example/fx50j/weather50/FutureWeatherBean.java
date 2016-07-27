package com.example.fx50j.weather50;

/**
 * Created by FX50J on 2016/7/17.
 */
public class FutureWeatherBean {

    private String date;
    private String week;
    private String wind;
    private String hightemp;
    private String lowtemp;
    private String type;

    public void SetDate(String d){
        date = d;
    }

    public void SetWeek(String w){
        week = w;
    }

    public void SetWind(String w){
        wind = w;
    }

    public void SetHightemp(String h){
        hightemp = h;
    }

    public void SetLowtemp(String l){
        lowtemp = l;
    }

    public void SetType(String t){
        type = t;
    }

    public String GetDate(){
        return date;
    }

    public String GetWind(){
        return wind;
    }

    public String GetWeek(){
        return week;
    }

    public String GetHightemp(){
        return hightemp;
    }

    public String GetLowtemp(){
        return lowtemp;
    }

    public String GetType(){
        return type;
    }
}
