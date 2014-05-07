package com.android.Duplom.mailreader;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.android.Duplom.R;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import java.util.Properties;

public class HomeActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    private static final String TAG = "GMailReader";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainread);
        Button emailRead=(Button)findViewById(R.id.buttonread);
        emailRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                            for(Message n:m){
                                Log.d(TAG,"Subject= "+n.getSubject());
                                System.out.println(n.getSubject());
                            }
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