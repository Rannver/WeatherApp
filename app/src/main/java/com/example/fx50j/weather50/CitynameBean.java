package com.example.fx50j.weather50;

/**
 * Created by FX50J on 2016/6/19.
 */
public class CitynameBean {

    private String Province;
    private String City;
    private String Country;   //district
    private String area_id;   //城市编码

    public String GetProvince() {
        return Province;
    }

    public  String GetCity(){
        return City;
    }

    public String GetCountry(){
        return Country;
    }

    public String GetId(){
        return  area_id;
    }

    public void SetId(String id){
        area_id = id;
    }
    public void SetProvince(String province){
        Province = province;
    }

    public void SetCity(String city){
        City = city;
    }

    public void SetCountry(String country){
        Country = country;
    }
}