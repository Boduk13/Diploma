package com.android.Duplom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
//import com.android.Duplom.mail.MailSenderActivity;

/**
 * Created by bodik on 26.03.14.
 */
public class ShowActivity  extends Activity {
    String status_mes;
    String description;
    String net_action;
    String on_site_action;

    TextView rezultat;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        status_mes = getIntent().getStringExtra(String.valueOf(R.string.status_mes));
        description = getIntent().getStringExtra(String.valueOf(R.string.description));
        net_action = getIntent().getStringExtra(String.valueOf(R.string.net_action));
        on_site_action = getIntent().getStringExtra(String.valueOf(R.string.on_site_action));


        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_description);


        Button btn = (Button)findViewById(R.id.button_back);


        rezultat = (TextView) findViewById(R.id.rezultat);
        rezultat.setText(Html.fromHtml("<p><b>Status Message</b> : </p>" + status_mes
                +"<br> <p><b>Description</b> : </p>" + description
                +"<br> <p><b>Network Action</b> : </p>" + net_action
                +"<br> <p><b>On-site Action</b> : </p>" + on_site_action ));



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.button_back:


                        onBackPressed();
                        /*Intent intent = new Intent(ShowActivity.this, CopyDbActivity.class);
                        startActivity(intent);*/
                        break;
                    default:
                        break;
                }



            }
        });

    }
    //add menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        menu.add (Menu.FIRST, 1, 1, R.string.setting).setIcon(R.drawable.ic_menu_preferences);
        menu.add (Menu.FIRST, 2, 2, R.string.about).setIcon(R.drawable.ic_menu_start_conversation);



        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub


        switch (item.getItemId()) {
            case 1:
                Toast.makeText(getApplicationContext(),

                        "You selected Settings", Toast.LENGTH_LONG).show();
                return true;

            case 2:
                Toast.makeText(getApplicationContext(),
                        "You selected About", Toast.LENGTH_LONG).show();
                return true;


        }
        return super.onOptionsItemSelected(item);
    }

}
