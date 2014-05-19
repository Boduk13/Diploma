package com.android.Duplom.preferences;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.provider.ContactsContract;
import android.widget.EditText;
import com.android.Duplom.R;

/**
 * Created by Bodik13 on 17.05.2014.
 */
public class AppPreferences extends PreferenceActivity {
    EditText email;
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencers);
    }


}