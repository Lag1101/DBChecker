package com.example.luckybug.dbchecker;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by vasiliy.lomanov on 05.08.2014.
 */
public class PlacesActivity extends ListActivity implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener{


    static final float eps = 1500;

    LocationClient mLocationClient;

    ArrayAdapter<Model> adapter;
    PlacesList placesList = new PlacesList();

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        adapter = new InteractivePlaceArrayAdapter(this, placesList.list, R.layout.placeitem);
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
                ArrayList<GoodModel> list =  (ArrayList<GoodModel>) data.getSerializableExtra("goodsList");
                saveData(item, list);
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
        } else {
            // 1. Instantiate an AlertDialog.Builder with its constructor
            new AlertDialog.Builder(this)
                .setMessage("Вы слишком далеко")
                .setTitle("Предупреждение")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface i, int which) {

                    }
                })
                .show();
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
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    void saveData(Model item, ArrayList<GoodModel> list) {
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
            //save
            {
                String settingName = item.getName() +" : " + DateFormat.getDateTimeInstance().format(new Date());

                listOfLists.list.add( settingName );

                editor.putString("listOfLists.json",  gson.toJson(listOfLists));

                Model newItem = new Model(settingName, item.getLocation());
                newItem.list = list;

                editor.putString(settingName, gson.toJson(newItem));

                // Commit the edits!
                editor.apply();
                Toast.makeText(this, "Successfully saved", Toast.LENGTH_LONG).show();
            }



        }catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}