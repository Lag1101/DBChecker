package com.example.luckybug.dbchecker;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
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

    static final int MENU_TAKE_PHOTO = 0;
    static final int CAMERA_PIC_REQUEST = 1;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        // create an array of Strings, that will be put to our ListActivity
        list = (ArrayList<GoodModel>)getIntent().getSerializableExtra("goodsList");//new ArrayList<GoodModel>();
        adapter = new InteractiveArrayAdapter(this,
                list);
        setListAdapter(adapter);

        ListView list = getListView();
        registerForContextMenu(list);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        // TODO Auto-generated method stub
        int position = ((AdapterView.AdapterContextMenuInfo)menuInfo).position;
        Toast.makeText(this, "Longclicked on position " + position, Toast.LENGTH_SHORT).show();
        switch (v.getId()) {
            default:
                menu.add(0, MENU_TAKE_PHOTO, 0, "Take photo");
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            // пункты меню для tvColor
            case MENU_TAKE_PHOTO: {
                Toast.makeText(this, "Smile!", Toast.LENGTH_SHORT).show();

                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);

                break;
            }
        }
        return super.onContextItemSelected(item);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_PIC_REQUEST) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            Toast.makeText(this, "Took", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Model item = (Model) getListAdapter().getItem(position);

        Toast.makeText(this, item.getName() , Toast.LENGTH_LONG).show();
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

                    list.add(new GoodModel(value, GoodsActivity.this));
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