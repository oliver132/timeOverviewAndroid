package com.timeoverview.oli.timeoverview;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

public class TimeOverview extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_overview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        final SQLiteDatabase mydatabase = openOrCreateDatabase("timeOverviewDB",MODE_PRIVATE,null);
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS items(Username VARCHAR,Password INTEGER);");
        updateItemList(mydatabase);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.additem);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.additemlayout);
                final Spinner spinner = (Spinner) findViewById(R.id.catSpinner);
                String[] items2 = { "Bouldern", "Beachen"};
                ArrayAdapter<CharSequence> adapter2 = new ArrayAdapter<CharSequence>(TimeOverview.this, android.R.layout.simple_spinner_item, items2);
                spinner.setAdapter(adapter2);
                final NumberPicker np = (NumberPicker) findViewById(R.id.numberPicker);
                np.setMaxValue(23);
                np.setMinValue(0);
                np.setWrapSelectorWheel(false);

                final NumberPicker np2 = (NumberPicker) findViewById(R.id.numberPicker2);
                np2.setMaxValue(59);
                np2.setMinValue(0);
                np2.setValue(30);
                np2.setWrapSelectorWheel(false);


                Button acceptAddItemButton = (Button) findViewById(R.id.addItemAcceptButton);
                acceptAddItemButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mydatabase.execSQL("INSERT INTO items VALUES('"+spinner.getSelectedItem()+"','"+(np.getValue()*60+np2.getValue())+"');");
                        updateItemList(mydatabase);
                        setContentView(R.layout.activity_time_overview);

                    }
                });
            }
        });
    }

    public void updateItemList(SQLiteDatabase mydatabase){

        setContentView(R.layout.activity_time_overview);
        Cursor resultSet = mydatabase.rawQuery("Select * from items",null);


        Cursor resultSet2 = mydatabase.rawQuery("SELECT Count(*) from items",null);
        resultSet2.moveToFirst();
        resultSet.moveToFirst();
        int max=resultSet2.getInt(0);
        if (max!=0) {
            String[] stringArray = new String[max - 1];
            int count = 0;
            while (resultSet.moveToNext()) {
                String username = resultSet.getString(0);
                Integer password = resultSet.getInt(1);
                stringArray[count] = username + " " + password;
                count += 1;
            }


            ArrayAdapter adapter = new ArrayAdapter<String>(TimeOverview.this, android.R.layout.simple_list_item_1, stringArray);
            ListView listView = (ListView) findViewById(R.id.listView);
            listView.setAdapter(adapter);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_time_overview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
