package com.android.Duplom.mailreader;

import android.service.textservice.SpellCheckerService;
import android.util.Log;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import java.util.Properties;

/**
 * Created by Богдан on 29.04.2014.
 */
public class GMailReader extends javax.mail.Authenticator {
    private static final String TAG = "GMailReader";

    private String mailhost = "imap.gmail.com";
    private Session session;
    private Store store;

    public GMailReader(String user, String password) {

        Log.e(TAG, "Start reader!");

        Properties props = System.getProperties();
        if (props == null) {
            Log.e(TAG, "Properties are null !!");
        } else {
            props.setProperty("mail.store.protocol", "imaps");

            Log.d(TAG, "Transport: " + props.getProperty("mail.transport.protocol"));
            Log.d(TAG, "Store: " + props.getProperty("mail.store.protocol"));
            Log.d(TAG, "Host: " + props.getProperty("mail.imap.host"));
            Log.d(TAG, "Authentication: " + props.getProperty("mail.imap.auth"));
            Log.d(TAG, "Port: " + props.getProperty("mail.imap.port"));
        }
        try {
            session = Session.getDefaultInstance(props, null);
            store = session.getStore("imaps");
            store.connect(mailhost, user, password);
            Log.i(TAG, "Store: " + store.toString());
        } catch (NoSuchProviderException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}