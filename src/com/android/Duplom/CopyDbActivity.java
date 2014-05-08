package com.android.Duplom;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.*;
import com.android.Duplom.mail.SimpleEMail;

import java.io.IOException;

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
    public static String MY_PREF = "licency";
    SharedPreferences mySharedPreferences;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.main);


        //Init varibl
        myDbHelper = new DatabaseHelper(CopyDbActivity.this);
        input = (EditText) findViewById(R.id.editText);
        listView = (ListView) findViewById(R.id.listView);
        from = new String[]{myDbHelper.status_mes};
        to = new int[]{R.id.tvText};


        //Open Data base
        openDB();


        //clear input after click
        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //on click edit text
            }
        });

        // создааем адаптер и настраиваем список
        updateList();
        //processing list
        listProcessing();




    }

//add menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        menu.add (Menu.FIRST, 1, 1, R.string.setting).setIcon(R.drawable.ic_menu_preferences);
        menu.add (Menu.FIRST, 2, 2, R.string.about).setIcon(R.drawable.ic_menu_start_conversation);
        menu.add (Menu.FIRST, 3, 3, R.string.exit).setIcon(R.drawable.ic_menu_logout);
        menu.add (Menu.FIRST, 4, 4, "read maill");


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub


        switch (item.getItemId()) {
            case 1:
                //Intent intent = new Intent(CopyDbActivity.this, MailReaderActyvity.class);
                Intent intent = new Intent(CopyDbActivity.this, SimpleEMail.class);
                startActivity(intent);


                Toast.makeText(getApplicationContext(),"You selected Settings", Toast.LENGTH_LONG).show();
                return true;

            case 2:
                Toast.makeText(getApplicationContext(),
                        "You selected About", Toast.LENGTH_LONG).show();
                return true;

            case 3:
                Toast.makeText(getApplicationContext(),
                        "You selected Exit", Toast.LENGTH_LONG).show();
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(CopyDbActivity.this);
                alertDialog.setTitle("Exit?");

                alertDialog.setMessage("Are you sure, exit?");

                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        finish();

                    }
                });

                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                alertDialog.show();
                return true;


        }
        return super.onOptionsItemSelected(item);
    }




    public void listProcessing(){

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

        //selected item list
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ErrorMassege errorMassege = new ErrorMassege(c.getString(1), c.getString(2), c.getString(3), c.getString(4));

                Intent intent = new Intent(CopyDbActivity.this, ShowActivity.class);

                intent.putExtra(String.valueOf(R.string.status_mes), errorMassege.getStatusMessage());
                intent.putExtra(String.valueOf(R.string.description), errorMassege.getDescription());
                intent.putExtra(String.valueOf(R.string.net_action), errorMassege.getNetworkAction());
                intent.putExtra(String.valueOf(R.string.on_site_action), errorMassege.getOnSiteAction());







///check licensy
                if(checkLicency()){
                    Log.d(TAG,"Licency Ok!");
                    startActivity(intent);
                } else {
                    Log.d(TAG,"No licence");
                    Intent getLicency = new Intent(CopyDbActivity.this, GetLicencyActivity.class);
                    startActivity(getLicency);

                }

                //....
                //---


            }
        });

    }

    public boolean checkLicency(){

        String xml_licency = (String) loadLicency(mySharedPreferences, "licency_kod");
       savePreferences(mySharedPreferences,"imei", getDivaceIMEI());
        String device_licency = loadLicency(mySharedPreferences,"imei");


        //test
        //xml_licency = "16af5d471dbeb579";

        //две строки равны, если
       //if((xml_licency).compareToIgnoreCase(device_licency) == 0)
       //if(TextUtils.equals(xml_licency, device_licency))
       if(xml_licency.equals(device_licency))
        {
        return true;
        }

        else
        {
            clearLicency(mySharedPreferences);
            return false;
        }
    }



    private String getDivaceIMEI(){
        ///get imei
       /* TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
*/
        String identifier = null;
        TelephonyManager tm = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);

        if (tm != null)
            identifier = tm.getDeviceId();
        if (identifier == null || identifier .length() == 0)
            identifier = Settings.Secure.getString(CopyDbActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);

    return identifier;
    }
    public void updateList(){
        searchText = input.getText();

        c = myDbHelper.fetchRecordsByQuery(searchText);


        // создааем адаптер и настраиваем список
        adapter = new SimpleCursorAdapter(CopyDbActivity.this,
                R.layout.item, c, from, to, 0);
        listView.setAdapter(adapter);

        //set animation
        LayoutAnimationController controller = AnimationUtils
                .loadLayoutAnimation(this, R.anim.list_layout_controller);
        getListView().setLayoutAnimation(controller);
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


    public ListView getListView() {
        return listView;
    }

    protected void savePreferences(SharedPreferences mySharedPreferences,String key, String kod)
    {
// получить доступ к объекту Editor, чтобы изменить общие настройки.
        mySharedPreferences = getSharedPreferences(MY_PREF,MODE_WORLD_READABLE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
// задать новые базовые типы в объекте общих настроек.

        editor.putString(key, kod);
        editor.commit();

    }

    public void clearLicency(SharedPreferences mySharedPreferences){

        mySharedPreferences = getSharedPreferences(MY_PREF,MODE_WORLD_WRITEABLE);
        SharedPreferences.Editor editor = mySharedPreferences.edit().clear();
         editor.commit();
        Log.d(TAG, "licency cleared!");
    }


    public String loadLicency(SharedPreferences mySharedPreferences, String key)
    {


        mySharedPreferences = getSharedPreferences(MY_PREF, MODE_WORLD_READABLE);

        String stringPreference;
        stringPreference = mySharedPreferences.getString(key,"");

        Log.d(TAG, "Shared pref= " + stringPreference+" key="+key);

        return stringPreference;

    }

}



