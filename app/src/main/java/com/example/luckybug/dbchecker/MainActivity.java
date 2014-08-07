package com.example.luckybug.dbchecker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.luckybug.dbchecker.R;

public class MainActivity extends Activity {

    public void onNewList(View view) {
        Toast.makeText(this, "New List!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, PlacesActivity.class);
        startActivity(intent);
    }
    public void onListsHistory(View view) {
        Toast.makeText(this, "Lists history!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, HistoryList.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
