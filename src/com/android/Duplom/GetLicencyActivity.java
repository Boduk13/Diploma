package com.android.Duplom;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
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
    Handler h;
    EditText input_emai = null;
    String emailtosave;
    String prefList;

    public String MY_PREF;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getApplicationContext();
        MY_PREF = context.getPackageName()+"_preferences";
        mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        prefList = loadLicency(mySharedPreferences, "PREF_LIST");


        if (prefList.equals("2") )
        {
            setTheme(R.style.Theme_Green);
        }
        else {
            setTheme(R.style.Theme_Blue);
        }
        setContentView(R.layout.getlicensy);





        final TextView licency = (TextView) findViewById(R.id.licency_view);
        licency.setText("You dont have licency!");

        final Button getLicency = (Button) findViewById(R.id.getlicensy);
        Button tryLicency = (Button) findViewById(R.id.tryLicency);

        h = new Handler() {
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case 1:
                       licency.setTextColor(Color.RED);
                       licency.setText("Please wait! Serching license...");
                        break;
                    case 2:
                        licency.setTextColor(Color.GREEN);
                        licency.setText("License Find! Press BACK!");
                        break;
                    case 3:

                        break;
                }
            }

            ;
        };
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

                                                     input_emai = (EditText) promptView.findViewById(R.id.userInput);
                                                      final EditText input_pass = (EditText) promptView.findViewById(R.id.userInput2);


                                                      input_emai.addTextChangedListener(new TextWatcher() {
                                                                                            @Override
                                                                                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


                                                                                            }

                                                                                            @Override
                                                                                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                                                                                            }

                                                                                            @Override
                                                                                            public void afterTextChanged(Editable s) {
                                                                                            /////valiadate email
                                                                                                Is_Valid_Email(input_emai);
                                                                                            }
                                                                                        });

                                                          // setup a dialog window
                                                      alertDialogBuilder
                                                              .setCancelable(false)
                                                              .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                  public void onClick(DialogInterface dialog, int id) {
                                                                      // get user input and set it to result

                                                                      savePreferences(mySharedPreferences, getApplicationContext().getString(R.string.user_email), emailtosave);
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

                GetLicencyActivity.this.startActivity(Intent.createChooser(emailIntent,
                        "Отправка письма..."));
            }
        });
    }
    public void Is_Valid_Email(EditText edt) {
        if (edt.getText().toString() == null) {
            edt.setError("Invalid Email Address");
            emailtosave = null;
        } else if (isEmailValid(edt.getText().toString()) == false) {
            edt.setError("Invalid Email Address");
            emailtosave = null;
        } else {
            emailtosave = edt.getText().toString();
        }
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    } // end of email matcher

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
        Log.d(TAG, "Shared pref=" + stringPreference);
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
                    h.sendEmptyMessage(1);
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

                                String imei = getDivaceIMEI();
                                String text = (String) bp.getContent();
                                char[] imei_from_email = text.toCharArray();
                                System.out.println(text+ "lenghth =" + text.length());
                              String replese = text.replaceAll("[\n\r]", "");

                                //replese = text.replaceAll(" ","");

                                System.out.println(replese+ "lenghth =" + replese.length());

                                ///





                                            Log.d(TAG,"Imei= "+imei+" lenghts="+imei.length());
                                    Log.d(TAG, "Text= " + replese + " lenghts=" + replese.length());

                                    savePreferences(mySharedPreferences, getApplicationContext().getString(R.string.licency_kod), replese);
                                h.sendEmptyMessage(2);

                                ///



                                //go back


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