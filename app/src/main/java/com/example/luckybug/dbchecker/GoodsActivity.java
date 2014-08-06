package com.example.luckybug.dbchecker;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LuckyBug on 06.08.2014.
 */
public class GoodsActivity extends ListActivity {
    ArrayAdapter<GoodModel> adapter;
    ArrayList<GoodModel> list;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        // create an array of Strings, that will be put to our ListActivity
        list = (ArrayList<GoodModel>)getIntent().getSerializableExtra("goodsList");//new ArrayList<GoodModel>();
        adapter = new InteractiveArrayAdapter(this,
                list);
        setListAdapter(adapter);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("itemNumber", getIntent().getIntExtra("itemNumber", 0));
        intent.putExtra("goodsList", list);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.goods_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.good_settings) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);

            alert.setTitle("New good");
            alert.setMessage("Type description of new good");
            // Добавим поле ввода
            final EditText input = new EditText(this);
            alert.setView(input);

            alert.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String value = input.getText().toString();
                    // Получили значение введенных данных!

                    list.add(new GoodModel(value));
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
        return super.onOptionsItemSelected(item);
    }
}