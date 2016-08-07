package com.example.coolweatherapp.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.ListMenuItemView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coolweatherapp.model.City;
import com.example.coolweatherapp.model.CoolWeatherDB;
import com.example.coolweatherapp.model.Province;
import com.example.coolweatherapp.util.HttpCallbackListener;
import com.example.coolweatherapp.util.HttpUtil;
import com.example.coolweatherapp.util.Utility;
import com.example.jh.coolweather.R;

import org.json.JSONException;
import org.xmlpull.v1.XmlPullParserException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jh on 2016/8/6.
 */
public class ChooseAreaActivity extends AppCompatActivity {

    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;

    private ProgressDialog progressDialog;
    private TextView titleText;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private CoolWeatherDB coolWeatherDB;
    private List<String> dataList = new ArrayList<String>();

    private List<City> cityList;
    private List<Province> provinceList;

    private City selectedCity;
    private Province selectedProvince;
    private int currentLevel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_area);
        listView = (ListView) findViewById(R.id.list_view);
        titleText = (TextView) findViewById(R.id.title_text);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(adapter);
        coolWeatherDB = CoolWeatherDB.getInstance(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(currentLevel == LEVEL_PROVINCE){
                    selectedProvince = provinceList.get(i);
                    queryCities();
                }else if(currentLevel == LEVEL_CITY){
                    selectedCity = cityList.get(i);
                    queryCounties();
                }
            }
        });
        queryProvinces();
    }

    private void queryProvinces() {
        provinceList = coolWeatherDB.loadProvince();
        if(provinceList.size()>0){
            dataList.clear();
            for (Province province:
                 provinceList) {
                dataList.add(province.getProvince_cn());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText("中国");
            currentLevel = LEVEL_PROVINCE;
        }else {
            queryFromServer(null,"province");
        }
    }

    private void queryCounties() {
        cityList = coolWeatherDB.loadCities(selectedCity.getDistrict_cn(),false);
        if(currentLevel == LEVEL_CITY){
            if(cityList.size() > 0 ){
                dataList.clear();
                for (City city:
                        cityList) {
                    dataList.add(city.getName_cn());
                }
                adapter.notifyDataSetChanged();
                listView.setSelection(0);
                titleText.setText(selectedCity.getDistrict_cn());
                currentLevel = LEVEL_CITY;
            }else {
                queryFromServer(selectedCity.getDistrict_cn(),"county");
            }
        }
    }

    private void queryCities() {
        cityList = coolWeatherDB.loadCities(selectedProvince.getProvince_cn(),true);
        if(currentLevel == LEVEL_PROVINCE){
            if(cityList.size() > 0 ){
                dataList.clear();
               cityList = filter(cityList);
                for (City city:
                     cityList) {
                    dataList.add(city.getDistrict_cn());
                }
                adapter.notifyDataSetChanged();
                listView.setSelection(0);
                titleText.setText(selectedProvince.getProvince_cn());
                currentLevel = LEVEL_CITY;
            }else {
                queryFromServer(selectedProvince.getProvince_cn(),"city");
            }
        }
    }

    private List<City> filter(List<City> cityList) {
        List<City> cityx = new ArrayList<City>();
        for (int i = 0 ; i<cityList.size() ; i++){

            if( i==0 ){
                cityx.add(cityList.get(i));
            }else {
                String node = cityList.get(i).getDistrict_cn();
                String node1 = cityList.get(i-1).getDistrict_cn();
                if(!node1.equals(node))
                cityx.add(cityList.get(i));
            }
        }
        cityList.clear();
        return cityx;
    }

    private void queryFromServer(final String addre,final String type) {
        String addres;
        if(!TextUtils.isEmpty(addre)){
            addres = "http://apis.baidu.com/apistore/weatherservice/citylist?cityname="+addre;
        }else {
            addres = "http://flash.weather.com.cn/wmaps/xml/china.xml";
        }
        showProgressDialog();
        HttpUtil.sendHttpRequest(addres, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) throws XmlPullParserException, JSONException {
                boolean result=false;

                if("province".equals(type)){
                    result = Utility.handleProvincesResponse(coolWeatherDB,response);
                }else if("city".equals(type) || ("county".equals(type)) ){
                    result = Utility.handleCitiesResponse(coolWeatherDB,response);
                }

                if(result){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type)){
                                queryProvinces();
                            }else if("city".equals(type)){
                                queryCities();
                            }else if ("county".equals(type)){
                                queryCounties();
                            }
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            Toast.makeText(ChooseAreaActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                        }
                    });
            }
        });
    }

    private void closeProgressDialog() {
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }

    private void showProgressDialog() {
        if(progressDialog ==null){
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在加载中，请稍候");
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }
}
