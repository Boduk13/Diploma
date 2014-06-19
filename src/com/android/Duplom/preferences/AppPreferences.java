package com.android.Duplom.preferences;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.EditText;
import com.android.Duplom.R;

/**
 * Created by Bodik13 on 17.05.2014.
 */
public class AppPreferences extends PreferenceActivity  {
    EditText email;
    SharedPreferences mySharedPreferences;
    String prefList;
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencers);
        PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    protected void onPause() {
        finish();
        super.onPause();
    }
}