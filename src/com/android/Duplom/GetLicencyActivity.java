package com.android.Duplom;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    Context context;

    public String MY_PREF;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.getlicensy);

        context = getApplicationContext();

        MY_PREF = context.getPackageName()+"_preferences";

        TextView licency = (TextView) findViewById(R.id.licency_view);
        licency.setText("You dont have licency!");

        final Button getLicency = (Button) findViewById(R.id.getlicensy);
        Button tryLicency = (Button) findViewById(R.id.tryLicency);

        tryLicency.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {


                                              ///check email and pasword
                                              if (loadLicency(mySharedPreferences, getApplicationContext().getString(R.string.user_email)).equals("") &&
                                                      loadLicency(mySharedPreferences, getApplicationContext().getString(R.string.user_pass)).equals("")) {

                                                  try {

                                                      // get prompts.xml view
                                                      LayoutInflater layoutInflater = LayoutInflater.from(GetLicencyActivity.this);

                                                      View promptView = layoutInflater.inflate(R.layout.email_data_toast, null);

                                                      AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GetLicencyActivity.this);

                                                      // set prompts.xml to be the layout file of the alertdialog builder
                                                      alertDialogBuilder.setView(promptView);

                                                      final EditText input_emai = (EditText) promptView.findViewById(R.id.userInput);
                                                      final EditText input_pass = (EditText) promptView.findViewById(R.id.userInput2);

                                                      // setup a dialog window
                                                      alertDialogBuilder
                                                              .setCancelable(false)
                                                              .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                  public void onClick(DialogInterface dialog, int id) {
                                                                      // get user input and set it to result

                                                                      savePreferences(mySharedPreferences, getApplicationContext().getString(R.string.user_email), input_emai.getText().toString());
                                                                      savePreferences(mySharedPreferences, getApplicationContext().getString(R.string.user_pass), input_pass.getText().toString());

                                                                      serchLicency();
                                                                  }
                                                              })
                                                              .setNegativeButton("Cancel",
                                                                      new DialogInterface.OnClickListener() {
                                                                          public void onClick(DialogInterface dialog, int id) {
                                                                              dialog.cancel();
                                                                          }
                                                                      }
                                                              );

                                                      // create an alert dialog
                                                      AlertDialog alertD = alertDialogBuilder.create();

                                                      alertD.show();

                                                  } catch (Exception e) {
                                                      Log.d(TAG, "Error dialog");
                                                      e.printStackTrace();
                                                  }
// ==-
                                              } else {
                                                  //red messege
                                                  serchLicency();
                                              }
                                          };
                                      });



        getLicency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String address, subject, emailtext;
                String TAG = "GMailReader";
                // Наши поля и кнопка
                address = getApplicationContext().getString(R.string.adminEmail);
                subject = getApplicationContext().getString(R.string.subject);
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

    protected void savePreferences(SharedPreferences mySharedPreferences, String key,String kod)
    {


// получить доступ к объекту Editor, чтобы изменить общие настройки.
        mySharedPreferences = getSharedPreferences(MY_PREF,MODE_WORLD_WRITEABLE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
// задать новые базовые типы в объекте общих настроек.

        editor.putString(key, kod);

        editor.commit();
        loadLicency(mySharedPreferences, key);

    }

    public String loadLicency(SharedPreferences mySharedPreferences, String key)
    {

        int mode = Activity.MODE_PRIVATE;

        mySharedPreferences = getSharedPreferences(MY_PREF, mode);

        String stringPreference = mySharedPreferences.getString(key, "");
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

    public void clearLicency(SharedPreferences mySharedPreferences){

        mySharedPreferences = getSharedPreferences(MY_PREF,MODE_WORLD_WRITEABLE);
        SharedPreferences.Editor editor = mySharedPreferences.edit().clear();
        editor.commit();
        Log.d(TAG, "licency cleared!");
    }

    private void serchLicency(){
        ///read messeges from gmail
        //try licency
        Runnable r=new Runnable() {
            @Override
            public void run() {

                final String user_email = loadLicency(mySharedPreferences, getApplicationContext().getString(R.string.user_email));
                final String user_pass = loadLicency(mySharedPreferences, getApplicationContext().getString(R.string.user_pass));

                Properties props = new Properties();
                props.setProperty("mail.store.protocol", "imaps");

                Session session = Session.getDefaultInstance(props, null);
                IMAPStore imapStore = null;

                try {
                    imapStore = (IMAPStore) session.getStore("imaps");
                    imapStore.connect("imap.gmail.com", user_email, user_pass);
                    final IMAPFolder folder = (IMAPFolder) imapStore.getFolder("Inbox");
                    folder.open(IMAPFolder.READ_ONLY);
                    Message m[]=folder.getMessages();


                    Log.d(TAG, "Read messeges!");

                    Multipart mp;
                    BodyPart bp;
                    for(Message n:m){


                        Log.d(TAG,"Subject="+n.getSubject());
                        if (n.getSubject().equals(getApplicationContext().getString(R.string.subject))){
                            Log.d(TAG,"licency find!");
                            try {

                                mp = (Multipart) n.getContent();
                                bp = mp.getBodyPart(0);

                                String text = (String) bp.getContent();

                                String imei = getDivaceIMEI();

                                String tmp = text.substring(0,imei.length());
                                Log.d(TAG,"Imei= "+imei+" lenghts="+imei.length());
                                Log.d(TAG,"Text= "+tmp+" lenghts="+tmp.length());

                                savePreferences(mySharedPreferences, getApplicationContext().getString(R.string.licency_kod), tmp);


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






}