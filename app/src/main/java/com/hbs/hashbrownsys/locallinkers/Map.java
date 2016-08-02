package com.hbs.hashbrownsys.locallinkers;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class Map extends AppCompatActivity implements OnMapReadyCallback {
    ImageView back_image;
    //  private static final LatLng DAVAO = new LatLng(7.0722, 125.6131);
    private GoogleMap mMap;
    // latitude and longitude
    double latitude;
    double longitude;
    //  String Latitude,Longitude,address,Title;
    TextView txt_header_name;
    Typeface Font;
    String address, images_type, terms, updated_date, actual_price, sale_price, coupon_price, title, desc, CouponId, button_updated_text, Latitude, Longitude, payTomarchant, BusinessName;
    String IsAsPerBill;
    String catID, image;
    Activity activityctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        back_image = (ImageView) findViewById(R.id.back_image);
        txt_header_name = (TextView) findViewById(R.id.txt_header_name);
        Font = Typeface.createFromAsset(getAssets(), "fonts/MyriadPro-Regular.otf");

        images_type = getIntent().getExtras().getString("Type");
        address = getIntent().getExtras().getString("address");
        terms = getIntent().getExtras().getString("terms");
        updated_date = getIntent().getExtras().getString("updated_date");
        coupon_price = getIntent().getExtras().getString("coupon_price");
        title = getIntent().getExtras().getString("title");
        desc = getIntent().getExtras().getString("desc");
        sale_price = getIntent().getExtras().getString("sale_price");
        actual_price = getIntent().getExtras().getString("actual_price");
        CouponId = getIntent().getExtras().getString("CouponId");
        button_updated_text = getIntent().getExtras().getString("button_updated_text");
        Latitude = getIntent().getExtras().getString("Latitude");
        Longitude = getIntent().getExtras().getString("Longitude");
        payTomarchant = getIntent().getExtras().getString("paytomarchant");

        try {
            IsAsPerBill = getIntent().getExtras().getString("IsAsPerBill");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            catID = getIntent().getExtras().getString("catId");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            image = getIntent().getExtras().getString("image");
        } catch (Exception e) {
            e.printStackTrace();
        }


        BusinessName = getIntent().getExtras().getString("BusinessName");

        if (!BusinessName.equalsIgnoreCase("")) {
            txt_header_name.setText(BusinessName);
        } else {
            txt_header_name.setText(title);
        }


        txt_header_name.setTypeface(Font);

        Log.e("latlong", "" + Latitude);
        Log.e("latlong", "" + Longitude);

        if (Longitude == null && Longitude == null) {
            latitude = 29.972254;
            longitude = 77.546299;
        } else {
            latitude = Double.parseDouble(Latitude);
            longitude = Double.parseDouble(Longitude);
        }


        back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        // create marker
//        MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title(address);
//        // adding marker
//        googleMap.addMarker(marker);
//        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
//        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latitude, longitude)).zoom(12).build();
//        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


//       LatLng sydney = new LatLng(Latitude, Longitude);
        final LatLng sydney = new LatLng(latitude, longitude);
        //googleMap.setMyLocationEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));

        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(getImageName(catID));

        Marker added = googleMap.addMarker(new MarkerOptions()
                .position(sydney).icon(icon));


        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            // Use default InfoWindow frame
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            // Defines the contents of the InfoWindow
            @Override
            public View getInfoContents(Marker marker) {

                // Getting view from the layout file info_window_layout
                View v = getLayoutInflater().inflate(R.layout.custon_marker_view, null);

                if (marker != null) {
                    LinearLayout main_parent_window = (LinearLayout) v.findViewById(R.id.parent_window);
                    TextView textViewTitle = (TextView) v.findViewById(R.id.name_window);
                    TextView textViewDiscription = (TextView) v.findViewById(R.id.desc_window);
                    ImageView iconIMV = (ImageView) v.findViewById(R.id.imageWindow);

                    //   Log.d("info window", data.getCategoryId() + " , " + data.getAddress());
                    // icon.setImageResource(getImageName(data.getCategoryId()));

                    String Stringimage = "http://www.locallinkers.com/admin/couponimages/" + image + "?width=120&height=120&mode=crop";


                    System.out.println("Image to fetch " + Stringimage);


                    try {
                        Glide.with(Map.this).load(Stringimage).placeholder(R.drawable.placeholder).into(iconIMV);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    if (!BusinessName.equalsIgnoreCase("")) {
                        textViewTitle.setText(BusinessName);
                    } else {
                        textViewTitle.setText(title);
                    }

                    textViewDiscription.setText(title);


                }
                return (v);
            }
        });

        // Adding and showing marker while touching the GoogleMap
   /*     googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng arg0) {
                // Clears any existing markers from the GoogleMap
                mMap.clear();

                // Creating an instance of MarkerOptions to set position
                MarkerOptions markerOptions = new MarkerOptions();

                // Setting position on the MarkerOptions
                markerOptions.position(arg0);

                // Animating to the currently touched position
                mMap.animateCamera(CameraUpdateFactory.newLatLng(arg0));

                // Adding marker on the GoogleMap
                Marker marker = mMap.addMarker(markerOptions);

                // Showing InfoWindow on the GoogleMap
                marker.showInfoWindow();

            }
        });*/

        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                finish();
            }
        });


    }

    private int getImageName(String categoryId) {
        int id = Integer.parseInt(categoryId);

        if (id == 39) {
            return R.drawable.beauty_parlour;

        } else if (id == 50) {
            return R.drawable.education;
        } else if (id == 53) {
            return R.drawable.fitness;
        } else if (id == 64) {
            return R.drawable.music;
        } else if (id == 72) {
            return R.drawable.restaurant;
        } else if (id == 76) {
            return R.drawable.studios;
        } else if (id == 79) {
            return R.drawable.wedding;
        }

        return R.drawable.other;
    }

}
