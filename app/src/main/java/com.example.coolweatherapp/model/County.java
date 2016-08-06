package com.example.coolweatherapp.model;

/**
 * Created by jh on 2016/8/6.
 */
public class County {

    /**
     * id : 1
     * name_cn : 华宁
     * cityId : 54
     */

    private int id;
    private String name_cn;
    private int cityId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName_cn() {
        return name_cn;
    }

    public void setName_cn(String name_cn) {
        this.name_cn = name_cn;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
