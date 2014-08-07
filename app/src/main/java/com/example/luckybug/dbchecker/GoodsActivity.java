package com.example.luckybug.dbchecker;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

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
        if (requestCode == CAMERA_PIC_REQUEST && requestCode == RESULT_OK) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            Toast.makeText(this, "Took", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        final GoodModel item = (GoodModel) getListAdapter().getItem(position);

        new AlertDialog.Builder(this)
                .setTitle("Выберите действие")
                .setItems(R.array.good_check, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        item.setCheck(getResources().getStringArray(R.array.good_check)[which]);
                        adapter.notifyDataSetChanged();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .create()
                .show();

        Toast.makeText(this, item.getDescription() , Toast.LENGTH_LONG).show();
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                //.setTitle("")
                .setMessage("Сохранить изменения?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent();
                        intent.putExtra("itemNumber", getIntent().getIntExtra("itemNumber", 0));
                        intent.putExtra("goodsList", list);
                        setResult(RESULT_OK, intent);
                        GoodsActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent();
                        setResult(RESULT_CANCELED, intent);
                        GoodsActivity.super.onBackPressed();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .create()
                .show();
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