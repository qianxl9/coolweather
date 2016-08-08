package com.example.coolweatherapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.coolweatherapp.util.HttpCallbackListener;
import com.example.coolweatherapp.util.HttpUtil;
import com.example.coolweatherapp.util.Utility;
import com.example.jh.coolweather.R;

import org.json.JSONException;
import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;

/**
 * Created by jh on 2016/8/7.
 */
public class WeatherActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView cityNameText;
    private TextView publishText;
    private LinearLayout weatherLayout;
    private TextView dataTimeText;
    private TextView weatherContentText;
    private TextView temperatureLeft;
    private TextView temperatureRight;
    private Button homeButton;
    private Button refreshButton;
    private String cityName;
    private String provinceName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_layout);
        cityNameText = (TextView) findViewById(R.id.city_name);
        publishText = (TextView) findViewById(R.id.publish_text);
        weatherLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
        dataTimeText = (TextView) findViewById(R.id.data_time_text);
        weatherContentText = (TextView) findViewById(R.id.weather_content);
        temperatureLeft = (TextView)findViewById(R.id.temperature_left);
        temperatureRight = (TextView) findViewById(R.id.temperature_right);
        homeButton = (Button) findViewById(R.id.home_button);
        refreshButton = (Button) findViewById(R.id.refresh_button);
        homeButton.setOnClickListener(this);
        refreshButton.setOnClickListener(this);

       String nameCn = getIntent().getStringExtra("nameCn");
        cityName = getIntent().getStringExtra("cityName");
        provinceName = getIntent().getStringExtra("province");
        editorShared(cityName,provinceName);
        if(!TextUtils.isEmpty(nameCn)){
            publishText.setText("同步中。。。");
            weatherLayout.setVisibility(View.INVISIBLE);
            cityNameText.setVisibility(View.INVISIBLE);
            queryWeather(nameCn);
        }else {
            showWeather();
        }
    }

    private void showWeather() {
        weatherLayout.setVisibility(View.VISIBLE);
        cityNameText.setVisibility(View.VISIBLE);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        cityNameText.setText(preferences.getString("city",""));
        publishText.setText("今天"+preferences.getString("time","")+"发布");
        dataTimeText.setText(preferences.getString("date",""));
        weatherContentText.setText(preferences.getString("weather",""));
        temperatureLeft.setText(preferences.getString("l_tmp","")+"℃");
        temperatureRight.setText(preferences.getString("h_tmp","")+"℃");
    }

    private void editorShared(String i,String x){
        if(!(TextUtils.isEmpty(i)||TextUtils.isEmpty(x))){
            SharedPreferences.Editor preferences = getSharedPreferences("c_p_Nmae",MODE_PRIVATE).edit();
            preferences.putString("cityName",i);
            preferences.putString("provinceName",x);
            preferences.commit();
        }

    }

    private String[] extractValue(){
        String[] value = new String[2];
        SharedPreferences sharedPreferences = getSharedPreferences("c_p_Nmae",MODE_PRIVATE);
        value[0] = sharedPreferences.getString("cityName","");
        value[1] = sharedPreferences.getString("provinceName","");
        return value;
    }

    private void queryWeather(String nameCn) {
            String adds = "http://apis.baidu.com/apistore/weatherservice/cityname?"+"cityname="+nameCn;
            queryFromServer(adds);
    }

    private void queryFromServer(String adds) {
        HttpUtil.sendHttpRequest(adds, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) throws XmlPullParserException, JSONException {
                Utility.handleWeatherResponse(WeatherActivity.this,response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showWeather();
                    }
                });
            }
            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        publishText.setText("同步失败");
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.home_button:
                String[] value = extractValue();
                Intent intent = new Intent(WeatherActivity.this,ChooseAreaActivity.class);
                intent.putExtra("from_weather_activity",true);
                intent.putExtra("cityName",value[0]);
                intent.putExtra("province",value[1]);
                startActivity(intent);
                finish();
                break;
            case R.id.refresh_button:
                publishText.setText("同步中.....");
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                String nameCn = preferences.getString("city","");
                queryWeather(nameCn);
                break;
        }
    }
}
