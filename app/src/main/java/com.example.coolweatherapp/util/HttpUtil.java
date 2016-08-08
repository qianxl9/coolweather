package com.example.coolweatherapp.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jh on 2016/8/6.
 */
public class HttpUtil {
    public static void sendHttpRequest(final String address,final HttpCallbackListener listener){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpURLConnection connection = null;
                    try {
                        URL url = new URL(address);
                        connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        connection.setRequestProperty("apikey","8e30a9ec5d09e4c6b4f1e5f05f69304f");

                        if(connection.getResponseCode() == 200){
                            InputStream inputStream = connection.getInputStream();
                            StringBuilder builder = new StringBuilder();
                            String lin;
                            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                                while (true){
                                        try {
                                            if((lin = reader.readLine()) !=null){
                                                builder.append(lin);
                                            }else
                                                break;
                                        }catch (Exception e){
                                            break;
                                        }
                                }
                            if(listener !=null){
                                listener.onFinish(builder.toString());
                            }
                        }
                    }catch (Exception e){
                        if(listener !=null)
                            listener.onError(e);
                    }finally {
                        if (connection != null)
                            connection.disconnect();
                    }
                }
            }).start();
    }
}
