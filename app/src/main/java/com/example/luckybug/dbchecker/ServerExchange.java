package com.example.luckybug.dbchecker;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LuckyBug on 04.08.2014.
 */
public class ServerExchange extends Thread {
    ServerExchange() {
        super(new Runnable(){
            @Override
            public void run() {
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost("http://192.168.1.103:3000/");

                    try {
                        // Добавим данные (пара - "название - значение")
                        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                        nameValuePairs.add(new BasicNameValuePair("login", "andro"));
                        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                        // Выполним запрос
                        HttpResponse response = httpclient.execute(httppost);

                    } catch (Exception e) {
                        //txtView.setText(e.getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
