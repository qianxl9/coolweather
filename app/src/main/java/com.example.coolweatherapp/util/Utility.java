package com.example.coolweatherapp.util;

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
                            province.setProvince_cn(xmlPullParser.getAttributeValue(0));
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
}
