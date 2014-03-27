package com.android.Duplom;



import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.*;

import static com.android.Duplom.R.layout;

public class CopyDbActivity extends Activity {

	Cursor c=null;
    DatabaseHelper myDbHelper;
    String TAG = "Database";
    EditText input;
    Editable searchText;
    String[] from;
    int[] to;
    SimpleCursorAdapter adapter;
    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.main);

       //Init varibl
        myDbHelper = new DatabaseHelper(CopyDbActivity.this);
        input = (EditText) findViewById(R.id.editText);
        listView = (ListView) findViewById(R.id.listView);
        from = new String[] { myDbHelper.status_mes};
        to = new int[] { R.id.tvText };


        //Open Data base
        openDB();


        //clear input after click
        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input.setText("");
            }
        });

        // создааем адаптер и настраиваем список
        c = myDbHelper.fetchRecordsByQuery();
        adapter = new SimpleCursorAdapter(CopyDbActivity.this, R.layout.item, c, from, to, 0);
        listView.setAdapter(adapter);

        //on cheng text
        input.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //update list when text chenging
                updateList();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //selected item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ErrorMassege errorMassege = new ErrorMassege(c.getString(1),c.getString(2),c.getString(3),c.getString(4));

                Intent intent = new Intent(CopyDbActivity.this, ShowActivity.class);

                intent.putExtra(String.valueOf(R.string.status_mes), errorMassege.getStatusMessage());
                intent.putExtra(String.valueOf(R.string.description), errorMassege.getDescription());
                intent.putExtra(String.valueOf(R.string.net_action), errorMassege.getNetworkAction());
                intent.putExtra(String.valueOf(R.string.on_site_action), errorMassege.getOnSiteAction());

                startActivity(intent);


            }
        });



               
        
    }
    public void updateList(){
        searchText = input.getText();

        c = myDbHelper.fetchRecordsByQuery(searchText);


        // создааем адаптер и настраиваем список
        adapter = new SimpleCursorAdapter(CopyDbActivity.this,
                R.layout.item, c, from, to, 0);
        listView.setAdapter(adapter);
    }


    private void openDB(){
        try {
            myDbHelper.createDataBase();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");
        }
        try {
            myDbHelper.openDataBase();

        }catch(SQLException sqle){
            throw sqle;
        }
        Toast.makeText(CopyDbActivity.this, "Database is Ok!", Toast.LENGTH_SHORT).show();
    }
}



