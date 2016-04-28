package com.hbs.hashbrownsys.locallinkers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

public class Terms_Conditions extends AppCompatActivity
{
    WebView webView;
    String terms;
    ImageView back_image;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms__conditions);

        terms = getIntent().getExtras().getString("terms");
        back_image = (ImageView) findViewById(R.id.back_image);
        webView = (WebView) findViewById(R.id.webView);
        webView.loadDataWithBaseURL(null, terms, "text/html", "utf-8", null);


        back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
