package com.example.coolweatherapp.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.example.coolweatherapp.model.City;
import com.example.coolweatherapp.model.CoolWeatherDB;
import com.example.coolweatherapp.model.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.Locale;
import java.util.logging.SimpleFormatter;

/**
 * Created by jh on 2016/8/6.
 */
public class Utility {

    /*
    * 解析和处理服务器返回的省级数据；
    * */
    public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB,String response) throws XmlPullParserException {
       try{
           if (!TextUtils.isEmpty(response)){
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(response));
            int eventype = xmlPullParser.getEventType();
            boolean judgment = false;
            while (eventype != XmlPullParser.END_DOCUMENT){
                switch (eventype){
                    case XmlPullParser.START_TAG:{
                        String nodeName = xmlPullParser.getName();
                        Province province = new Province();
                        if("city".endsWith(nodeName)){
                            String save = xmlPullParser.getAttributeValue(0);
                            province.setProvince_cn(save);
                            coolWeatherDB.saveProvince(province);
                        }
                        judgment = true;
                        break;
                    }
                }
                eventype = xmlPullParser.next();
            }
            if(judgment)
                return true;
            else
                return false;
        }
       }catch (Exception e){
           e.printStackTrace();
       }
        return false;
    }


    /*
    * 解析和处理省市级数据
    * */
    public static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB,String response) throws JSONException {

        if(!TextUtils.isEmpty(response)){
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("retData");
                for (int i= 0 ; i<jsonArray.length(); i++){
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    City city= new City();
                    city.setProvince_cn(jsonObject1.getString("province_cn"));
                    city.setName_cn(jsonObject1.getString("name_cn"));
                    city.setDistrict_cn(jsonObject1.getString("district_cn"));
                    coolWeatherDB.saveCity(city);
                }
                return true;
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return false;
    }

    /*
    * 解析服务器返回的JSON 数据，并将解析出的数据存储到本地。
    * */

    public static void handleWeatherResponse(Context context,String response) throws JSONException {
        JSONObject object = new JSONObject(response);
        JSONObject jsonObject = object.getJSONObject("retData");
        String city = jsonObject.getString("city");
        String date = jsonObject.getString("date");
        String time = jsonObject.getString("time");
        String weather = jsonObject.getString("weather");
        String l_tmp = jsonObject.getString("l_tmp");//最低气温
        String h_tmp = jsonObject.getString("h_tmp");//最高气温
        saveWeatherInfo(context,city,date,time,weather,l_tmp,h_tmp);
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static void saveWeatherInfo(Context context, String city, String date, String time, String weather, String l_tmp, String h_tmp) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("city_selected",true);
        editor.putString("city",city);
        editor.putString("date",date); //日期
        editor.putString("time",time); //时间
        editor.putString("weather",weather);
        editor.putString("l_tmp",l_tmp);
        editor.putString("h_tmp",h_tmp);
        editor.commit();
    }
}
