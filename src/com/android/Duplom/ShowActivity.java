package com.android.Duplom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
        setContentView(R.layout.show);

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
                        Intent intent = new Intent(ShowActivity.this, CopyDbActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }



            }
        });

    }
}
