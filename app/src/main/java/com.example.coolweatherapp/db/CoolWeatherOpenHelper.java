package com.example.coolweatherapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.hardware.SensorManager;

/**
 * Created by jh on 2016/8/6.
 */
public class CoolWeatherOpenHelper extends SQLiteOpenHelper {

    public static final String CREATE_PROVINCE = "create table Province(" +
            "id integer primary key autoincrement," +
            "province_name text,)";

    public static final String CREATE_CITY = "create table City(" +
            "id integer primary key autoincrement," +
            "city_name text," +
            "province_id integer)";

    public static final String CREATE_COUNTY = "create table County(" +
            "id integer primary key autoincurement," +
            "county_name text" +
            "city_id integer)";

    public CoolWeatherOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_CITY);
        sqLiteDatabase.execSQL(CREATE_COUNTY);
        sqLiteDatabase.execSQL(CREATE_PROVINCE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
