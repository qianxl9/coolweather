package com.example.coolweatherapp.util;

import org.json.JSONException;
import org.xmlpull.v1.XmlPullParserException;

/**
 * Created by jh on 2016/8/6.
 */
public interface HttpCallbackListener {
    void onFinish(String response) throws XmlPullParserException, JSONException;

    void onError(Exception e);
}
