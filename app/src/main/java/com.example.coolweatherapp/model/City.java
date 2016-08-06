package com.example.coolweatherapp.model;

/**
 * Created by jh on 2016/8/6.
 */
public class City {

    /**
     * id : 2
     * district_cn : 大理
     * provinceid : 343
     */

    private int id;
    private String district_cn;
    private int provinceId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDistrict_cn() {
        return district_cn;
    }

    public void setDistrict_cn(String district_cn) {
        this.district_cn = district_cn;
    }

    public int getProvinceid() {
        return provinceId;
    }

    public void setProvinceid(int provinceid) {
        this.provinceId = provinceid;
    }
}
