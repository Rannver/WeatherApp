package com.example.fx50j.weather50;

public class WeatherBean {

    private String temp;
    private String weather;
    private String temp_min;
    private String temp_max;
    private String wd;
    private String ws;
    private String sunrise;
    private String sunset;
    private String longitude;
    private String latitude;
    private String time;
    private String date;

    public void SetTemp(String t){
        temp=t;
    }

    public void SetWeather(String w){
        weather=w;
    }

    public void SetTempOfmin(String t){
        temp_min=t;
    }

    public void SetTempOfMax(String t){
        temp_max=t;
    }

    public void SetWD(String w){
        wd=w;
    }

    public void SetWS(String w){
        ws=w;
    }

    public void SetSunrise(String s){
        sunrise=s;
    }

    public void SetSunset(String s){
        sunset=s;
    }

    public void SetLongitude(String l){
        longitude=l;
    }

    public void SetLatitude(String l){
        latitude=l;
    }

    public void SetTime(String t){
        time=t;
    }

    public void SetDate(String d){
        date=d;
    }

    public String GetTemp(){
        return temp;
    }

    public String GetWeather(){
        return weather;
    }

    public String GetTempOfMin(){
        return temp_min;
    }

    public String GetTempOfMax(){
        return temp_max;
    }

    public String GetWD(){
        return wd;
    }

    public String GetWS(){
        return ws;
    }

    public String GetSunrise(){
        return sunrise;
    }

    public String GetSunset(){
        return sunset;
    }

    public String GetLongitude(){
        return longitude;
    }

    public String GetLatitude(){
        return latitude;
    }

    public String GetTime(){
        return time;
    }

    public String GetDate(){
        return date;
    }
}
