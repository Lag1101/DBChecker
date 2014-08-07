package com.example.luckybug.dbchecker;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.provider.Contacts;
import android.util.JsonWriter;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by vasiliy.lomanov on 05.08.2014.
 */
public class PlacesActivity extends ListActivity implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener{


    static final float eps = 150;

    LocationClient mLocationClient;

    ArrayAdapter<Model> adapter;
    PlacesList placesList = new PlacesList();

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // create an array of Strings, that will be put to our ListActivity
        loadData();

        adapter = new InteractivePlaceArrayAdapter(this,
                placesList.list);
        setListAdapter(adapter);

        ListView list = getListView();
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                Toast.makeText(PlacesActivity.this,
                        "Item in position " + position + " long clicked",
                        Toast.LENGTH_LONG).show();
                // Возвращает "истину", чтобы завершить событие клика, чтобы
                // onListItemClick больше не вызывался
                return true;
            }
        });

        mLocationClient = new LocationClient(this, this, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Connect the client.
        mLocationClient.connect();
    }
    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        mLocationClient.disconnect();
        saveData();
        super.onStop();
    }
    @Override
    public void onConnected(Bundle dataBundle) {
        // Display the connection status
        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();

    }
    @Override
    public void onDisconnected() {
        // Display the connection status
        Toast.makeText(this, "Disconnected. Please re-connect.",
                Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                int itemNumber = data.getIntExtra("itemNumber", 0);
                Model item = (Model) getListAdapter().getItem(itemNumber);
                item.setList((ArrayList<GoodModel>) data.getSerializableExtra("goodsList"));
            }
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Model item = (Model) getListAdapter().getItem(position);

        Location currentLocation = mLocationClient.getLastLocation();
        float distance = currentLocation.distanceTo(item.getLocation());

        Toast.makeText(this, "Distance to " + item.getName() + " is " + Float.toString(distance), Toast.LENGTH_LONG).show();

        if( distance < eps ) {
            Intent intent = new Intent(getBaseContext(), GoodsActivity.class);
            intent.putExtra("goodsList", item.getList());
            intent.putExtra("itemNumber", position);
            startActivityForResult(intent, 0);
            //startActivity(intent);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.places_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id) {
            case R.id.places_settings:
            {
                AlertDialog.Builder alert = new AlertDialog.Builder(this);

                alert.setTitle("New place");
                alert.setMessage("Type name of new place");
                // Добавим поле ввода
                final EditText input = new EditText(this);
                alert.setView(input);

                alert.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String value = input.getText().toString();
                        // Получили значение введенных данных!

                        Location currentLocation = mLocationClient.getLastLocation();
                        placesList.list.add(new Model(value, currentLocation));
                        adapter.notifyDataSetChanged();
                    }
                });

                alert.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Если отменили.
                    }
                });

                alert.show();


                return true;
            }
            case R.id.send_to_server:
            {
                final List<NameValuePair> places = new ArrayList<NameValuePair>();

                for( Model place : placesList.list) {
                    List<NameValuePair> goods = new ArrayList<NameValuePair>();
                    for( GoodModel good : place.getList()) {
                        goods.add(new BasicNameValuePair(good.getDescription(), getResources().getStringArray(R.array.good_check)[good.isSelected()]));
                    }
                    places.add(new BasicNameValuePair(place.getName(), goods.toString()));
                }
                Thread thread = new Thread(new Runnable(){
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
            case R.id.clear:
            {
                placesList.list.clear();
                adapter.notifyDataSetChanged();
            }
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    void loadData() {
        try{
            SharedPreferences settings = getSharedPreferences("list.json", 0);

            Gson gson = new Gson();

            String listStr = settings.getString("list", "");

            if( listStr.isEmpty() )
            {
                throw new Exception("There isn't any data");
            }

            placesList = gson.fromJson(listStr, placesList.getClass());

            Toast.makeText(this, "Successfully loaded", Toast.LENGTH_LONG).show();
        }catch (Exception e) {
            placesList = new PlacesList();
            Toast.makeText(this, "Error while reading " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        //placesList = new PlacesList();
    }
    void saveData() {
        try{
            Gson gson = new Gson();

            SharedPreferences settings = getSharedPreferences("list.json", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("list",  gson.toJson(placesList));

            // Commit the edits!
            editor.apply();
            Toast.makeText(this, "Successfully saved", Toast.LENGTH_LONG).show();

        }catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}