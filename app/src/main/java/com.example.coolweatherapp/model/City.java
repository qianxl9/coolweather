package com.example.coolweatherapp.model;

/**
 * Created by jh on 2016/8/6.
 */
public class City {


    /**
     * province_cn : 云南
     * district_cn : 西双版纳
     * name_cn : 勐海
     * name_en : menghai
     * area_id : 101291603
     */

    private String province_cn;
    private String district_cn;
    private String name_cn;

    public String getProvince_cn() {
        return province_cn;
    }

    public void setProvince_cn(String province_cn) {
        this.province_cn = province_cn;
    }

    public String getDistrict_cn() {
        return district_cn;
    }

    public void setDistrict_cn(String district_cn) {
        this.district_cn = district_cn;
    }

    public String getName_cn() {
        return name_cn;
    }

    public void setName_cn(String name_cn) {
        this.name_cn = name_cn;
    }
}
