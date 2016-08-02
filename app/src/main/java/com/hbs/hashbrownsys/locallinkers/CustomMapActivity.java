package com.hbs.hashbrownsys.locallinkers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SearchView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.hbs.hashbrownsys.locallinkers.Utility.MySupportMapFragment;
import com.hbs.hashbrownsys.locallinkers.adapter.MarkerInfoAdapter;
import com.hbs.hashbrownsys.locallinkers.http.CommonPostRequestThread;
import com.hbs.hashbrownsys.locallinkers.http.IHttpExceptionListener;
import com.hbs.hashbrownsys.locallinkers.http.IHttpResponseListener;
import com.hbs.hashbrownsys.locallinkers.model.MapModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hbslenovo-3 on 7/18/2016.
 */

public class CustomMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap myMap;
    double Latitude, Longitude;
    MapFragment mapFragment;
    SharedPreferences prefs;

    private ArrayList<LatLng> arrayPoints = null;
    PolylineOptions polylineOptions;

    ImageView filter_image, location_image, search_icon;
    SearchView search;


    MySupportMapFragment customMapFragment;
    Projection projection;
    boolean conditiondrager = false;

    ImageView back_image, Clear_Polygon;
    Polygon polygon;
    //  ProgressDialog progressDialog;
    Activity mContext;
    MapModel model;
    ArrayList<MapModel> arrayList = new ArrayList<MapModel>();
    //   ArrayList<Coupon_list_model> arrayList = new ArrayList<Coupon_list_model>();
    //   Coupon_list_model model;
    int type = 1;

    double LatitudeDouble = 0.0;
    double LongitudeDouble = 0.0;
    ImageView Draw;
    boolean Is_MAP_Moveable = false;
    FrameLayout fram_map;
    double latitude, longitude;
    List<LatLng> val;
    List<String> polydata = new ArrayList<String>();
    PolygonOptions rectOptions;
    LatLng center;
    double lat, changeLat, centerlat;
    double longi, changeLong, centerlongi;

    private HashMap<Marker, MapModel> eventMarkerMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_maps);
        mContext = CustomMapActivity.this;
        eventMarkerMap = new HashMap<Marker, MapModel>();

        Intent in = getIntent();

        String pathType = in.getStringExtra("pathFrom");
        if (pathType.equalsIgnoreCase("coupon")) {
            type = 2;
        } else if (pathType.equalsIgnoreCase("listing")) {
            type = 1;
        }
        search = (SearchView) Home.topToolBar.findViewById(R.id.search);
        search.setVisibility(View.GONE);
        filter_image = (ImageView) Home.topToolBar.findViewById(R.id.filter_image);
        filter_image.setVisibility(View.GONE);
        search_icon = (ImageView) Home.topToolBar.findViewById(R.id.search_icon);
        search_icon.setVisibility(View.GONE);
        location_image = (ImageView) Home.topToolBar.findViewById(R.id.location_image);
        location_image.setVisibility(View.GONE);

        Draw = (ImageView) findViewById(R.id.btn_draw_State);
        fram_map = (FrameLayout) findViewById(R.id.fram_map);

        Clear_Polygon = (ImageView) findViewById(R.id.clear_polygon);

        back_image = (ImageView) findViewById(R.id.back_image);

        back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        Clear_Polygon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myMap.clear();
            }
        });


        prefs = this.getSharedPreferences(Constants.LOCAL_LINKER_APP_PREFERENCES, Context.MODE_PRIVATE);

        String str_lat = prefs.getString(Constants.LATITUDE, "");
        String str_longi = prefs.getString(Constants.LONGITUDE, "");
        System.out.println("in home tab " + str_lat + " " + str_longi);


        Latitude = Double.parseDouble(str_lat);
        Longitude = Double.parseDouble(str_longi);

//        Latitude = Double.parseDouble("29.972254");
//        Longitude = Double.parseDouble("77.546299");

       /* map = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map);
        map.getMapAsync(this);//remember getMap() is deprecated!*/

        arrayPoints = new ArrayList<>();


        if (arrayPoints.size() > 0) {
            arrayPoints.clear();
        }
/*setting map*/
//        mapFragment = (MapFragment) getActivity().getFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);


        customMapFragment = ((MySupportMapFragment) this.getFragmentManager().findFragmentById(R.id.mapcustom));
        customMapFragment.getMapAsync(this);


        Draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Is_MAP_Moveable == false) {
                    Is_MAP_Moveable = true;
                    myMap.clear();

                    Draw.setImageDrawable(getResources().getDrawable(R.drawable.ic_draw_cancel));
                } else {
                    // myMap.clear();
                    Is_MAP_Moveable = false;

                    Draw.setImageDrawable(getResources().getDrawable(R.drawable.draw40));
                  /*  Thread thread = new Thread() {
                        @Override
                        public void run() {

                        }
                    };
                    thread.start();*/
                    Show_map_Marker();
                }
            }
        });


        fram_map.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float x = event.getX();
                float y = event.getY();

                int x_co = Math.round(x);
                int y_co = Math.round(y);

                Projection projection = myMap.getProjection();
                Point x_y_points = new Point(x_co, y_co);

                LatLng latLng = myMap.getProjection().fromScreenLocation(x_y_points);
                latitude = latLng.latitude;

                longitude = latLng.longitude;

                int eventaction = event.getAction();


                switch (eventaction) {
                    case MotionEvent.ACTION_DOWN:
                        // finger touches the screen
                        if (Is_MAP_Moveable == true) {
                            myMap.clear();
                            polydata = new ArrayList<String>();
                            val = new ArrayList<LatLng>();
                            val.add(new LatLng(latitude, longitude));
                        }

                        break;

                    case MotionEvent.ACTION_MOVE:
                        // finger moves on the screen
                        if (Is_MAP_Moveable == true) {
                            val.add(new LatLng(latitude, longitude));
                            polydata.add(longitude + " " + latitude);
                            Log.d("polydata", "" + polydata);
                            Log.i("MOVE METHOD", "true");
                            Log.i("logintude", "" + longitude + " " + latitude);
                            Draw_Line();
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                        // finger leaves the screen
                        if (Is_MAP_Moveable == true) {
                            Log.i("MOVE METHOD", "false");
                            Draw_Map();

                        }

                        break;
                }

                return Is_MAP_Moveable == true;
            }
        });


    }

    public void Draw_Line() {

        PolylineOptions line = new PolylineOptions();
        //map.addPolyline(new PolylineOptions();
        line.addAll(val);
        line.width(3);
        line.color(Color.parseColor("#2faadd"));

        myMap.addPolyline(line);
    }

    public void Draw_Map() {

        myMap.clear();
        rectOptions = new PolygonOptions();
        rectOptions.addAll(val);
        rectOptions.strokeColor(Color.parseColor("#2faadd"));
        rectOptions.strokeWidth(5);
        rectOptions.fillColor(Color.parseColor("#ffffffff"));
        myMap.addPolygon(rectOptions);
        Log.d("all lat long", "" + val);
//        Is_MAP_Moveable = false;
        Is_MAP_Moveable = true;
        getResultForPolygon();

    }

    public void getResultForPolygon() {
        // CustomMapActivity.this.setProgressBarIndeterminateVisibility(true);
        //  new Polytask().execute();
        JSONObject json = prepareJsonObjectForPolygon();
        String url = Constants.URL + Constants.MAP_MARKER_POLYGON;
        System.out.println("map url : " + url);
        //   progressDialog = ProgressDialog.show(mContext, "", "Checking. Please wait...", false);
        CommonPostRequestThread requestThread = new CommonPostRequestThread(url, json.toString(), responseListenerPolygon, exceptionListenerPolygon);
        requestThread.start();

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;

//       LatLng sydney = new LatLng(Latitude, Longitude);
        LatLng sydney = new LatLng(Latitude, Longitude);

        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.new_location_pin);

        Marker added = myMap.addMarker(new MarkerOptions()
                .position(sydney).icon(icon));




/*
        Marker melbourne = myMap.addMarker(new MarkerOptions()
                .position(sydney)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));*/


        //googleMap.setMyLocationEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));
   /*     if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }*/


        if (arrayList.size() == 0) {
            Log.d("map marker", ",........... map marker.............,");
            Show_map_Marker();
        } else {

        }


        myMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public synchronized void onCameraChange(CameraPosition cameraPosition) {
                center = myMap.getCameraPosition().target;
                Log.d("Position of camera", "" + center.latitude + "," + center.longitude);
                centerlat = center.latitude;
                centerlongi = center.longitude;

                JSONObject json = prepareOnScrollMapJsonObject(String.valueOf(centerlat), String.valueOf(centerlongi));
                String url = Constants.URL + Constants.MAP_MARKER;
                System.out.println("map url : " + url);
                //       progressDialog = ProgressDialog.show(mContext, "", "Checking. Please wait...", false);
                CommonPostRequestThread requestThread = new CommonPostRequestThread(url, json.toString(), responseListener, exceptionListener);
                requestThread.start();

            }
        });


        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.d("marker clicked", "marker clicked");
                MapModel eventInfo = eventMarkerMap.get(marker);
                if (eventInfo != null) {
                    Log.d("category id", eventInfo.getCategoryId() + "," + eventInfo.getLatitude() + "," + eventInfo.getLongitude());

                    double nlat = Double.parseDouble(eventInfo.getLatitude());
                    double nlong = Double.parseDouble(eventInfo.getLongitude());
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(nlat, nlong))
                            .zoom(15)
                            .build();

                    myMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    myMap.setInfoWindowAdapter(new MarkerInfoAdapter(CustomMapActivity.this, getLayoutInflater(), eventInfo));
                    myMap.setOnInfoWindowClickListener(new MarkerInfoAdapter(CustomMapActivity.this, getLayoutInflater(), eventInfo));
                    marker.showInfoWindow();
                }
                return true;
            }

        });

   /*     googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Toast.makeText(CustomMapActivity.this, "current.", Toast.LENGTH_LONG).show();
            }
        });*/


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //myMap.clear();
      /*  Fragment fragment = (getFragmentManager().findFragmentById(R.id.mapcustom));
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.remove(fragment);
        ft.commit();*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /*first time on map get nearby marker*/
    public void Show_map_Marker() {
        JSONObject json = prepareJsonObject();
        String url = Constants.URL + Constants.MAP_MARKER;
        System.out.println("map url : " + url);
        //  progressDialog = ProgressDialog.show(mContext, "", "Checking. Please wait...", false);
        CommonPostRequestThread requestThread = new CommonPostRequestThread(url, json.toString(), responseListener, exceptionListener);
        requestThread.start();
    }


    /*on scroll the map api hit*/
    public JSONObject prepareOnScrollMapJsonObject(String centerlatitude, String centerLongitude) {
        prefs = mContext.getSharedPreferences(Constants.LOCAL_LINKER_APP_PREFERENCES, Context.MODE_PRIVATE);


        int city_id = prefs.getInt("city_id", 0);
        // String userid = prefs.getString(Constants.USER_ID, null);
        JSONObject innerJsonObject = new JSONObject();
        try {


            // innerJsonObject.put("user_id", userid);
            innerJsonObject.put("distance", 20);
            innerJsonObject.put("cityID", city_id);
            innerJsonObject.put("latitude", centerlatitude);
            innerJsonObject.put("longitude", centerLongitude);
//            innerJsonObject.put("latitude", "29.972254");
//            innerJsonObject.put("longitude", "77.546299");
            innerJsonObject.put("type", type);
            System.out.println("map JSON : " + innerJsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return innerJsonObject;
    }


    /* jsonObject first time*/
    public JSONObject prepareJsonObject() {
        prefs = mContext.getSharedPreferences(Constants.LOCAL_LINKER_APP_PREFERENCES, Context.MODE_PRIVATE);

        String Latitudestr = prefs.getString(Constants.LATITUDE, "");
        String Longitudestr = prefs.getString(Constants.LONGITUDE, "");
        int city_id = prefs.getInt("city_id", 0);
        // String userid = prefs.getString(Constants.USER_ID, null);
        JSONObject innerJsonObject = new JSONObject();
        try {


            // innerJsonObject.put("user_id", userid);
            innerJsonObject.put("distance", 20);
            innerJsonObject.put("cityID", city_id);
            innerJsonObject.put("latitude", Latitudestr);
            innerJsonObject.put("longitude", Longitudestr);
//            innerJsonObject.put("latitude", "29.972254");
//            innerJsonObject.put("longitude", "77.546299");
            innerJsonObject.put("type", type);
            System.out.println("map JSON : " + innerJsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return innerJsonObject;
    }

    /*on draw polygon api hit*/
    public JSONObject prepareJsonObjectForPolygon() {
        prefs = mContext.getSharedPreferences(Constants.LOCAL_LINKER_APP_PREFERENCES, Context.MODE_PRIVATE);


        JSONObject innerJsonObject = new JSONObject();
        try {

            JSONArray ja = new JSONArray(polydata);

            innerJsonObject.put("lst_longLat", ja);
            innerJsonObject.put("type", type);
            System.out.println("map JSON polygon: " + innerJsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return innerJsonObject;
    }

    /*on exception listener for api normal response*/
    IHttpExceptionListener exceptionListener = new IHttpExceptionListener() {

        @Override
        public void handleException(String message) {
         /*   try {
                if (progressDialog != null && progressDialog.isShowing() == true)
                    progressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }*/
        }
    };


    /*on exception listener for polygon*/
    IHttpExceptionListener exceptionListenerPolygon = new IHttpExceptionListener() {

        @Override
        public void handleException(String message) {
       /*     try {
                if (progressDialog != null && progressDialog.isShowing() == true)
                    progressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }*/
        }
    };

    /*listener for first time api response*/
    IHttpResponseListener responseListener = new IHttpResponseListener() {

        @Override
        public void handleResponse(String response) {
            try {
                if (response != null) {
                    //  progressDialog.dismiss();
                    JSONObject obj = new JSONObject(response);
                    System.out.println("Json result coupon " + obj.toString());
                    String Result = obj.getString("Result");
                    System.out.println("Json result" + Result.toString());

                    if (Result.equals("0")) {
                        handler.sendEmptyMessage(0);


                    } else if (Result.equals("1")) {


                        JSONArray jsonArray = obj.getJSONArray("Lst_Coupons");

                        if (jsonArray.length() == 0) {


                            handler.sendEmptyMessage(2);

                            return;
                        }


                        Log.e("", "Lst_Coupons" + jsonArray.length());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            final MapModel modal = new MapModel();
                            JSONObject almonObject = jsonArray.getJSONObject(i);
                            modal.setActualPrice(almonObject.getString("ActualPrice"));
                            modal.setAddress(almonObject.getString("Address"));
                            modal.setBusinessName(almonObject.getString("BusinessName"));
                            modal.setCategoryId(almonObject.getString("CategoryId"));
                            modal.setCategoryName(almonObject.getString("CategoryName"));
                            modal.setCityId(almonObject.getString("CityId"));
                            modal.setCityName(almonObject.getString("CityName"));
                            modal.setCouponId(almonObject.getString("CouponId"));
                            modal.setCouponPrice(almonObject.getString("CouponPrice"));
                            modal.setCreatedBy(almonObject.getString("CreatedBy"));
                            modal.setCreatedDate(almonObject.getString("CreatedDate"));
                            modal.setIsApprovedByAdmin(almonObject.getString("IsApprovedByAdmin"));
                            modal.setIsDeleted(almonObject.getString("IsDeleted"));
                            modal.setLatitude(almonObject.getString("Latitude"));
                            modal.setLongitude(almonObject.getString("Longitude"));
                            modal.setOfferDetails(almonObject.getString("OfferDetails"));
                            modal.setPayToMerchant(almonObject.getString("PayToMerchant"));
                            modal.setPhoneNumber(almonObject.getString("PhoneNumber"));
                            modal.setSalePrice(almonObject.getString("SalePrice"));
                            modal.setSelectedPosition(almonObject.getString("SelectedPosition"));
                            modal.setSubCategoryId(almonObject.getString("SubCategoryId"));
                            modal.setSubCategoryName(almonObject.getString("SubCategoryName"));
                            modal.setTermsAndCondition(almonObject.getString("TermsAndCondition"));
                            modal.setTitle(almonObject.getString("Title"));
                            modal.setUpdatedDate(almonObject.getString("UpdatedDate"));
                            modal.setImage(almonObject.getString("Image"));
                            modal.setIsAsPerBill(almonObject.getString("IsAsPerBill"));
                            modal.setDistance(almonObject.getInt("Distance"));
                            mContext.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    arrayList.add(modal);
                                }
                            });
                        }

                        handler.sendEmptyMessage(1);
                    } else if (Result.equals("2")) {
                        handler.sendEmptyMessage(2);

                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    /*listener for polygon response api*/
    IHttpResponseListener responseListenerPolygon = new IHttpResponseListener() {

        @Override
        public void handleResponse(String response) {
            try {
                if (response != null) {
                    //  progressDialog.dismiss();
                    JSONObject obj = new JSONObject(response);
                    System.out.println("Json result coupon " + obj.toString());
                    String Result = obj.getString("Result");
                    System.out.println("Json result" + Result.toString());

                    if (Result.equals("0")) {
                        handler.sendEmptyMessage(0);


                    } else if (Result.equals("1")) {


                        JSONArray jsonArray = obj.getJSONArray("Lst_Coupons");

                        if (jsonArray.length() == 0) {


                            handler.sendEmptyMessage(2);

                            return;
                        }




                        Log.e("", "Lst_Coupons" + jsonArray.length());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            final MapModel modal = new MapModel();
                            JSONObject almonObject = jsonArray.getJSONObject(i);
                            modal.setActualPrice(almonObject.getString("ActualPrice"));
                            modal.setAddress(almonObject.getString("Address"));
                            modal.setBusinessName(almonObject.getString("BusinessName"));
                            modal.setCategoryId(almonObject.getString("CategoryId"));
                            modal.setCategoryName(almonObject.getString("CategoryName"));
                            modal.setCityId(almonObject.getString("CityId"));
                            modal.setCityName(almonObject.getString("CityName"));
                            modal.setCouponId(almonObject.getString("CouponId"));
                            modal.setCouponPrice(almonObject.getString("CouponPrice"));
                            modal.setCreatedBy(almonObject.getString("CreatedBy"));
                            modal.setCreatedDate(almonObject.getString("CreatedDate"));
                            modal.setIsApprovedByAdmin(almonObject.getString("IsApprovedByAdmin"));
                            modal.setIsDeleted(almonObject.getString("IsDeleted"));
                            modal.setLatitude(almonObject.getString("Latitude"));
                            modal.setLongitude(almonObject.getString("Longitude"));
                            modal.setOfferDetails(almonObject.getString("OfferDetails"));
                            modal.setPayToMerchant(almonObject.getString("PayToMerchant"));
                            modal.setPhoneNumber(almonObject.getString("PhoneNumber"));
                            modal.setSalePrice(almonObject.getString("SalePrice"));
                            modal.setSelectedPosition(almonObject.getString("SelectedPosition"));
                            modal.setSubCategoryId(almonObject.getString("SubCategoryId"));
                            modal.setSubCategoryName(almonObject.getString("SubCategoryName"));
                            modal.setTermsAndCondition(almonObject.getString("TermsAndCondition"));
                            modal.setTitle(almonObject.getString("Title"));
                            modal.setUpdatedDate(almonObject.getString("UpdatedDate"));
                            modal.setImage(almonObject.getString("Image"));
                            modal.setIsAsPerBill(almonObject.getString("IsAsPerBill"));
                            modal.setDistance(almonObject.getInt("Distance"));
                            mContext.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    arrayList.add(modal);
                                }
                            });
                        }

                        handler.sendEmptyMessage(1);
                    } else if (Result.equals("2")) {
                        handler.sendEmptyMessage(2);

                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    /*Handler handle api response*/
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            switch (msg.what) {


                case 0:
                    // Registration not Successful
                /*    try {
                        if (progressDialog != null && progressDialog.isShowing() == true)
                            progressDialog.dismiss();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/

                    break;


                case 2:
                    // Registration not Successful
                    try {
                      /*  if (progressDialog != null && progressDialog.isShowing() == true)
                            progressDialog.dismiss();*/
                        //  Toast.makeText(CustomMapActivity.this, "No Result Found.", Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;


                case 1:
                    // myMap.clear();
                    try {
                      /*  if (progressDialog != null && progressDialog.isShowing() == true)
                            progressDialog.dismiss();*/

                        CustomMapActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //adapter.notifyDataSetChanged();

                               /* for (int count = 0; count < arrayList.size(); count++) {
                                    // String type = arrayList.get(count).getType();
                                    final String name = arrayList.get(count).getBusinessName();
                                    String desc = arrayList.get(count).getAddress();
                                    String Latitude = arrayList.get(count).getLatitude();
                                    String Longitude = arrayList.get(count).getLongitude();
                                    String Title = arrayList.get(count).getTitle();
                                    //   String Description = arrayList.get(count).getDescription();
                                    //  Description = Html.fromHtml(Description).toString();
                                    double Distance = arrayList.get(count).getDistance();
                                    String distanceInString = String.valueOf(Distance);
                                    LatitudeDouble = Double.parseDouble(Latitude);
                                    LongitudeDouble = Double.parseDouble(Longitude);

                                    //    if (type.equalsIgnoreCase("Coupon")) {
                                    BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.beauty_parlour);

                                    LatLng customposition = new LatLng(LatitudeDouble, LongitudeDouble);
                                    MarkerOptions markerOptions = new MarkerOptions().position(customposition)
                                            .title(Title)
                                            //  .snippet(Description)
                                            .icon(icon);

                                    Marker mMarker = myMap.addMarker(markerOptions);

                                    eventMarkerMap.put(mMarker, connectEventsFirstTimeModal);


                                }*/
                                Is_MAP_Moveable = false;

                                Draw.setImageDrawable(getResources().getDrawable(R.drawable.draw40));

                                if (arrayList.size() > 0) {
                                    setUpEventMarkersOnMap(arrayList);
                                }
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;


                default:
                    break;
            }
        }
    };


    private void setUpEventMarkersOnMap(ArrayList<MapModel> connectEventsFirstTimeModalArrayList) {

        for (int i = 0; i < connectEventsFirstTimeModalArrayList.size(); i++) {
            Log.e("------------" + connectEventsFirstTimeModalArrayList.size(), "---i" + i);
            try {

                MapModel connectEventsFirstTimeModal = connectEventsFirstTimeModalArrayList.get(i);


                double latitude = Double.parseDouble(connectEventsFirstTimeModalArrayList.get(i).getLatitude());
                double longitude = Double.parseDouble(connectEventsFirstTimeModalArrayList.get(i).getLongitude());
                LatLng markerLatLng = new LatLng(latitude, longitude);

                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(getImageName(connectEventsFirstTimeModalArrayList.get(i).getCategoryId()));

                Marker added = myMap.addMarker(new MarkerOptions()
                        .position(markerLatLng).icon(icon));

                eventMarkerMap.put(added, connectEventsFirstTimeModal);


            } catch (Exception e) {
                e.printStackTrace();
            }

        }


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


    /*get center latlong points*/
    private LatLng computeCentroid(ArrayList<LatLng> points) {
        double latitude = 0;
        double longitude = 0;
        int n = points.size();

        for (LatLng point : points) {
            latitude += point.latitude;
            longitude += point.longitude;
        }

        return new LatLng(latitude / n, longitude / n);
    }

    /*detect point in polygon*/
    private boolean isPointInPolygon(LatLng tap, ArrayList<LatLng> vertices) {
        int intersectCount = 0;
        for (int j = 0; j < vertices.size() - 1; j++) {
            if (rayCastIntersect(tap, vertices.get(j), vertices.get(j + 1))) {
                intersectCount++;
            }
        }

        return ((intersectCount % 2) == 1); // odd = inside, even = outside;
    }

    /*method get point is in polygon or not*/
    private boolean rayCastIntersect(LatLng tap, LatLng vertA, LatLng vertB) {

        double aY = vertA.latitude;
        double bY = vertB.latitude;
        double aX = vertA.longitude;
        double bX = vertB.longitude;
        double pY = tap.latitude;
        double pX = tap.longitude;

        if ((aY > pY && bY > pY) || (aY < pY && bY < pY)
                || (aX < pX && bX < pX)) {
            return false; // a and b can't both be above or below pt.y, and a or
            // b must be east of pt.x
        }

        double m = (aY - bY) / (aX - bX); // Rise over run
        double bee = (-aX) * m + aY; // y = mx + b
        double x = (pY - bee) / m; // algebra is neat!

        return x > pX;
    }


}
