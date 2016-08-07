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
    public static final String DB_NAME = "cool_weather.db";

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
            values.put("province_cn",city.getProvince_cn());
            values.put("name_cn",city.getName_cn());
            database.insert("City",null,values);
        }
    }

    public List<City> loadCities(String provinceCn,boolean categroy){
        List<City> list = new ArrayList<City>();
        Cursor cursor = null;
        if (categroy){
            cursor = database.query("City",null,"province_cn = ?",new String[]{provinceCn},null,null,null);
        }else if(!categroy) {
            cursor = database.query("City",null,"city_name = ?",new String[]{provinceCn},null,null,null);
        }

        while (cursor.moveToNext()){
            City city = new City();
            city.setName_cn(cursor.getString(cursor.getColumnIndex("name_cn")));
            city.setDistrict_cn(cursor.getString(cursor.getColumnIndex("city_name")));
            city.setProvince_cn(provinceCn);
            list.add(city);
        }
        if(cursor !=null)
            cursor.close();
        return list;
    }
}


