package com.android.Duplom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

import javax.mail.*;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by Мар'янчик on 07.05.2014.
 */
public class GetLicencyActivity extends Activity  {
    SharedPreferences mySharedPreferences;
    String TAG = "Try licensy";

    public static String MY_PREF = "licency";
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.getlicensy);

        TextView licency = (TextView) findViewById(R.id.licency_view);
        licency.setText("You dont have licency!");

        final Button getLicency = (Button) findViewById(R.id.getlicensy);
        Button tryLicency = (Button) findViewById(R.id.tryLicency);

        tryLicency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ///read messeges from gmail
                //try licency
                Runnable r=new Runnable() {
                    @Override
                    public void run() {
                        Properties props = new Properties();
                        props.setProperty("mail.store.protocol", "imaps");

                        Session session = Session.getDefaultInstance(props, null);
                        IMAPStore imapStore = null;

                        try {
                            imapStore = (IMAPStore) session.getStore("imaps");
                            imapStore.connect("imap.gmail.com", "hbodya13@gmail.com", "BodikTurbo30043991");
                            final IMAPFolder folder = (IMAPFolder) imapStore.getFolder("Inbox");
                            folder.open(IMAPFolder.READ_ONLY);
                            Message m[]=folder.getMessages();


                            Log.d(TAG, "Read messeges!");

                            Multipart mp;
                            BodyPart bp;
                            for(Message n:m){


                                Log.d(TAG,"Subject="+n.getSubject());
                                if (n.getSubject().equals("licency")){
                                    Log.d(TAG,"licency find!");
                                    try {

                                        mp = (Multipart) n.getContent();
                                        bp = mp.getBodyPart(0);

                                        String text = (String) bp.getContent();
                                        String tmp = text.substring(0,16);
                                        String imei = getDivaceIMEI();

                                        Log.d(TAG,"Imei= "+imei+" lenghts="+imei.length());
                                        Log.d(TAG,"Text= "+tmp+" lenghts="+tmp.length());

                                        savePreferences(mySharedPreferences, tmp);


                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                }
                            }
                            Log.d(TAG,"Break");
                        } catch (MessagingException e) {
                            e.printStackTrace();
                        }

                    }
                };
                Thread t=new Thread(r);
                t.start();

            }
        });

        getLicency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String address, subject, emailtext;
                String TAG = "GMailReader";
                // Наши поля и кнопка
                address = "maryan4uk95@gmail.com";
                subject = "licency";
                emailtext = getDivaceIMEI();

                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

                emailIntent.setType("plain/text");
                // Кому
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                        new String[] { address });
                // Зачем
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                        subject);
                // О чём
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                        emailtext);
                   /* // С чем
                    emailIntent.putExtra(
                            android.content.Intent.EXTRA_STREAM,
                            Uri.parse("file://"
                                    + Environment.getExternalStorageDirectory()
                                    + "/Клипы/SOTY_ATHD.mp4"));

                    emailIntent.setType("text/video");*/
                // Поехали!
                GetLicencyActivity.this.startActivity(Intent.createChooser(emailIntent,
                        "Отправка письма..."));
            }
        });
    }

    protected void savePreferences(SharedPreferences mySharedPreferences,String kod)
    {
// получить доступ к объекту Editor, чтобы изменить общие настройки.
        mySharedPreferences = getSharedPreferences(MY_PREF,MODE_WORLD_WRITEABLE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
// задать новые базовые типы в объекте общих настроек.

        editor.putString("licency_kod", kod);

        editor.commit();
        loadLicency(mySharedPreferences);

    }

    public String loadLicency(SharedPreferences mySharedPreferences)
    {

        int mode = Activity.MODE_PRIVATE;

        mySharedPreferences = getSharedPreferences(MY_PREF, mode);

        String stringPreference = mySharedPreferences.getString("licency_kod", "");
        Log.d(TAG, "Shared pref= " + stringPreference);
        return stringPreference;

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
            identifier = Settings.Secure.getString(GetLicencyActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);

        Log.d(TAG, "Divace licency= " + identifier);
        return identifier;
    }




}