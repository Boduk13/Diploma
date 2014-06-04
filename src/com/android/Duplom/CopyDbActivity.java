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
import android.preference.PreferenceManager;
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
import com.android.Duplom.finish_decrypt.AESHelper;
import com.android.Duplom.preferences.AppPreferences;

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
    SharedPreferences mySharedPreferences;


    Context context;


    public String MY_PREF;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.main);

        context=getApplicationContext();
        MY_PREF = context.getPackageName()+"_preferences";



        //Init varibl
        myDbHelper = new DatabaseHelper(CopyDbActivity.this);
        input = (EditText) findViewById(R.id.editText);
        listView = (ListView) findViewById(R.id.listView);
        from = new String[]{myDbHelper.status_mes};
        to = new int[]{R.id.tvText};


        //Open Data base
        openDB();



        //validation input text
        input.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {



            }
        });



        // создааем адаптер и настраиваем список
        updateList();
        //processing list
        listProcessing();

        mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);




    }

    public void is_Valid_Person_Name(EditText edt) throws NumberFormatException {
        if (edt.getText().toString().equals("")) {


        } else

           if (!edt.getText().toString().matches("[a-zA-Z0-9 ]+")) {
                edt.setError("Accept Alphabets and Numbers Only!");
                searchText = null;
            }

            else if (c.getCount() == 0) {
                edt.setError("Your code is not found in database. Please update program or database!");


        }

    }

//add menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        menu.add (Menu.FIRST, 1, 1, R.string.setting).setIcon(R.drawable.ic_menu_preferences);
        menu.add (Menu.FIRST, 2, 2, R.string.about).setIcon(R.drawable.ic_menu_start_conversation);
        menu.add (Menu.FIRST, 3, 3, R.string.exit).setIcon(R.drawable.ic_menu_logout);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub


        switch (item.getItemId()) {
            case 1:

                Intent intent = new Intent(CopyDbActivity.this, AppPreferences.class);
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
                is_Valid_Person_Name(input);

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
                try {
                    if(checkLicency()){
                        Log.d(TAG,"Licency Ok!");
                        startActivity(intent);
                    } else {
                        Log.d(TAG,"No licence");
                        Intent getLicency = new Intent(CopyDbActivity.this, GetLicencyActivity.class);
                        startActivity(getLicency);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //....
                //---


            }
        });

    }

    public boolean checkLicency() throws Exception {

        String normalText = getDivaceIMEI();
        String normalTextEnc = loadLicency(mySharedPreferences, getApplicationContext().getString(R.string.licency_kod));
        String seedValue ="bodik";
        Log.d("aes", "key+ " + seedValue);
        String normalTextDec = " ";
        try {


            normalTextEnc = normalTextEnc.replaceAll(" ","");
            Log.d(TAG, normalTextEnc);

            normalTextDec = AESHelper.decrypt(seedValue,normalTextEnc);
            Log.d(TAG, normalTextDec);
          //  setContentView(txe);
        } catch (Exception e) {
            Log.d(TAG,"error decrypt");

            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
       if(normalTextDec.equals(normalText))
        {
        return true;
        }

        else
        {

            return false;
        }
    }



    private String getDivaceIMEI(){


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

    protected void onResume() {

        super.onResume();
    }

}



