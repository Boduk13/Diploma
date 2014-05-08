package com.android.Duplom.mail;

/**
 * Created by Богдан on 23.04.2014.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.android.Duplom.R;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

import javax.mail.*;
import java.io.IOException;
import java.util.Properties;


public class SimpleEMail extends Activity {
        Button send, read;
        String address, subject, emailtext;
    private static final String TAG = "GMailReader";

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.mailreadactyvity);

            // Наши поля и кнопка
            send = (Button) findViewById(R.id.button);
            read = (Button) findViewById(R.id.read);
            address = "Bodya-H2009@i.ua";
            subject = "subject";
            emailtext = "text";

            send.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

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
                    SimpleEMail.this.startActivity(Intent.createChooser(emailIntent,
                            "Отправка письма..."));
                }
            });

            read.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
///read messeges from gmail
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
                                folder.open(IMAPFolder.READ_WRITE);
                                Message m[]=folder.getMessages();
                                Log.d(TAG,"Read messeges!");
                                Multipart mp;
                                BodyPart bp;
                                for(Message n:m){


                                  if (n.getSubject().equals("?????")){
                                      try {
                                          mp = (Multipart) n.getContent();
                                          bp = mp.getBodyPart(0);

                                          String subject, text;
                                          subject = n.getSubject();
                                          text = bp.getContent().toString();

                                          Log.d(TAG,"Subject= "+subject);
                                          Log.d(TAG,"Text= "+text);
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
        }
    }
