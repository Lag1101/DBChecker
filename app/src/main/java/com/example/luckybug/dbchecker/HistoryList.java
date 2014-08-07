package com.example.luckybug.dbchecker;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.luckybug.dbchecker.R;
import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class HistoryList extends ListActivity {

    PlacesList placesList = new PlacesList();
    ArrayAdapter<Model> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        placesList.list = new ArrayList<Model>();

        loadData();

        adapter = new InteractivePlaceArrayAdapter(this,
                placesList.list, R.layout.placeitem);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Model item = (Model) getListAdapter().getItem(position);

        Intent intent = new Intent(getBaseContext(), GoodsActivity.class);
        intent.putExtra("goodsList", item.getList());
        intent.putExtra("itemNumber", position);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.history_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
            return true;
            case R.id.send_to_server: {
                final List<NameValuePair> places = new ArrayList<NameValuePair>();

                for (Model place : placesList.list) {
                    List<NameValuePair> goods = new ArrayList<NameValuePair>();
                    for (GoodModel good : place.getList()) {
                        goods.add(new BasicNameValuePair(good.getDescription(), good.getCheck()));
                    }
                    places.add(new BasicNameValuePair(place.getName(), goods.toString()));
                }
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            HttpClient httpclient = new DefaultHttpClient();
                            HttpPost httppost = new HttpPost("http://192.168.1.101:3000/");

                            try {
                                // Добавим данные (пара - "название - значение")
                                httppost.setEntity(new UrlEncodedFormEntity(places, "UTF-8"));
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
                thread.start();

                return true;
            }
            default:return super.onOptionsItemSelected(item);
        }
    }

    void loadData() {
        try{

            Gson gson = new Gson();
            StringList listOfLists = null;
            SharedPreferences settings = getSharedPreferences("settings", 0);
            SharedPreferences.Editor editor = settings.edit();
            //load
            try
            {
                String listStr = settings.getString("listOfLists.json", "");
                if( listStr.isEmpty() )
                {
                    throw new Exception("There isn't any data");
                }
                listOfLists = gson.fromJson(listStr, StringList.class);
            } catch (Exception e) {
                listOfLists = new StringList();
            }

            for(String listName : listOfLists.list) {
                String listStr = settings.getString(listName, "");

                if (listStr.isEmpty()) {
                    throw new Exception("There isn't any data");
                }
                Model newItem = gson.fromJson(listStr, Model.class);
                placesList.list.add(newItem);
            }

            Toast.makeText(this, "Successfully loaded", Toast.LENGTH_LONG).show();
        }catch (Exception e) {
            placesList = new PlacesList();
            Toast.makeText(this, "Error while reading " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        //placesList = new PlacesList();
    }
}
