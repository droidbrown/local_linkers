package com.hbs.hashbrownsys.locallinkers;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class Map extends AppCompatActivity
{
    ImageView back_image;
  //  private static final LatLng DAVAO = new LatLng(7.0722, 125.6131);
    private GoogleMap mMap;
    // latitude and longitude
    double latitude;
    double longitude;
    String Latitude,Longitude,address,Title;
    TextView txt_header_name;
    Typeface Font;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        back_image = (ImageView) findViewById(R.id.back_image);
        txt_header_name=(TextView)findViewById(R.id.txt_header_name);
        Font = Typeface.createFromAsset(getAssets(), "fonts/MyriadPro-Regular.otf");

        Latitude = getIntent().getExtras().getString("Latitude");
        Longitude = getIntent().getExtras().getString("Longitude");
        address = getIntent().getExtras().getString("address");
        Title=getIntent().getExtras().getString("title");
        txt_header_name.setText(Title);
        txt_header_name.setTypeface(Font);

        Log.e("latlong",""+Latitude);
        Log.e("latlong",""+Longitude);

        if(Longitude == null && Longitude == null)
        {
            latitude = 7.0722;
            longitude = 125.6131;
        }
        else
        {
            latitude = Double.parseDouble(Latitude);
            longitude = Double.parseDouble(Longitude);
        }


        back_image.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
        });

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mMap = supportMapFragment.getMap();
        // create marker
        MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title(address);
        // adding marker
        mMap.addMarker(marker);
        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latitude, longitude)).zoom(12).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }

}
