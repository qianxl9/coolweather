package com.example.coolweatherapp.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.coolweatherapp.db.CoolWeatherOpenHelper;

import java.lang.ref.PhantomReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jh on 2016/8/6.
 */
public class CoolWeatherDB {

        /*
        * 数据库名
        * */
    public static final String DB_NAME = "cool_weather";

    /*
    * 数据库版本号
    * */
    public static final int VERSION = 1;
    private static CoolWeatherDB coolWeatherDB;
    private SQLiteDatabase database;

    private CoolWeatherDB(Context context){
        CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(context,DB_NAME,null,VERSION);
        database = dbHelper.getWritableDatabase();
    }

    public synchronized static CoolWeatherDB getInstance(Context context){
        if(coolWeatherDB ==null){
            coolWeatherDB = new CoolWeatherDB(context);
        }
        return coolWeatherDB;
    }

    public void saveProvince(Province province){
        if(province !=null){
            ContentValues values = new ContentValues();
            values.put("province_name",province.getProvince_cn());
            database.insert("Province",null,values);
        }
    }

    public List<Province> loadProvince(){

        List<Province> list = new ArrayList<Province>();
        Cursor cursor = database.query("Province" , null, null,null,null,null,null);
        while (cursor.moveToNext()){
            Province province =new Province();
            province.setId(cursor.getInt(cursor.getColumnIndex("id")));
            province.setProvince_cn(cursor.getString(cursor.getColumnIndex("province_name")));
            list.add(province);
        }
        if (cursor !=null)
            cursor.close();
        return list;
    }

    public void saveCity(City city){
        if(city !=null){
            ContentValues values = new ContentValues();
            values.put("city_name",city.getDistrict_cn());
            values.put("province_id",city.getProvinceid());
            database.insert("City",null,values);
        }
    }

    public List<City> loadCities(int provinceId){
        List<City> list = new ArrayList<City>();

        Cursor cursor = database.query("City",null,"province_id=?",new String[]{String.valueOf(provinceId)},null,null,null,null);
        while (cursor.moveToNext()){
            City city = new City();
            city.setId(cursor.getInt(cursor.getColumnIndex("id")));
            city.setDistrict_cn(cursor.getString(cursor.getColumnIndex("city_name")));
            city.setProvinceid(provinceId);
        }
        if(cursor !=null)
            cursor.close();
        return list;
    }

    public void saveCounty(County county){
        if (county != null){
            ContentValues values = new ContentValues();
            values.put("county_name",county.getName_cn());
            values.put("city_id",county.getCityId());
        }
    }

    public List<County> loadCounties(int cityId){
        List<County> list = new ArrayList<County>();
        Cursor cursor = database.query("County",null,"city_id = ?" , new String[]{String.valueOf(cityId)},null,null,null);
        while (cursor.moveToNext()){
            County county = new County();
            county.setCityId(cityId);
            county.setName_cn(cursor.getString(cursor.getColumnIndex("county_name")));
        }
        return list;
    }
}


