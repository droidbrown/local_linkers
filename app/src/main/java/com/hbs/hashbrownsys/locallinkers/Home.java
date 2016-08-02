package com.hbs.hashbrownsys.locallinkers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.hbs.hashbrownsys.locallinkers.Utility.MarshMallowPermission;
import com.hbs.hashbrownsys.locallinkers.adapter.CustomAdapter;
import com.hbs.hashbrownsys.locallinkers.fragment.Change_City;
import com.hbs.hashbrownsys.locallinkers.fragment.HomeFragment;
import com.hbs.hashbrownsys.locallinkers.fragment.My_Order_Fragment;
import com.hbs.hashbrownsys.locallinkers.fragment.ProfileFragment;
import com.hbs.hashbrownsys.locallinkers.model.ItemObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Home extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CharSequence mTitle;
    private CharSequence mDrawerTitle;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private ActionBarDrawerToggle mDrawerToggle;
    public static Toolbar topToolBar;
    Typeface Font;
    final Context context = this;
    double longitude, latitude;
    private RelativeLayout DrawerLinear;
    public static int fragment_position = 1;
    CircleImageView imageView1;
    public boolean isgpsenabled = false;
    SharedPreferences prefs;
    String UserName, Image;
    ImageView filter_image, search_icon;
    SearchView search;
    ImageView location_image;
    public static String City_Name = "Change City";
    CustomAdapter adapter;
    boolean isLocationAllowed = false;
    List<ItemObject> listViewItems;
    TextView profile_name;
    Bitmap bitmap;

    private MarshMallowPermission marshMallowPermission;


    private static final String TAG = "app permission";

    Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    String lat, lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        topToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(topToolBar);
        topToolBar.setNavigationIcon(R.drawable.menu);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Font = Typeface.createFromAsset(this.getAssets(), "fonts/MyriadPro-Regular.otf");


        DrawerLinear = (RelativeLayout) findViewById(R.id.drawerPane);
        TextView local_linker = (TextView) findViewById(R.id.local_linker);
        local_linker.setTypeface(Font);
        TextView toolbarTitle = (TextView) topToolBar.findViewById(R.id.toolbar_app_name);
        toolbarTitle.setTypeface(Font);  //
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        prefs = getSharedPreferences(Constants.LOCAL_LINKER_APP_PREFERENCES, Context.MODE_PRIVATE);
        UserName = prefs.getString(Constants.USER_NAME, null);
        Log.e("", "UserName>>" + UserName);

        Image = prefs.getString(Constants.IMAGE, null);
        Log.e("", "Imag>>e" + Image);

        search = (SearchView) topToolBar.findViewById(R.id.search);
        search.setVisibility(View.GONE);
        search_icon = (ImageView) topToolBar.findViewById(R.id.search_icon);
        search_icon.setVisibility(View.GONE);
        filter_image = (ImageView) topToolBar.findViewById(R.id.filter_image);
        filter_image.setVisibility(View.GONE);
        location_image = (ImageView) topToolBar.findViewById(R.id.location_image);
        location_image.setVisibility(View.GONE);
        location_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        LayoutInflater inflater = getLayoutInflater();
        View listHeaderView = inflater.inflate(R.layout.drawer_header, null, false);
        mDrawerList.addHeaderView(listHeaderView);
        profile_name = (TextView) listHeaderView.findViewById(R.id.profile_name);
        profile_name.setTypeface(Font);  //
        imageView1 = (CircleImageView) listHeaderView.findViewById(R.id.imageView1);
/*update ame*/
        if (UserName != null) {
            profile_name.setText("Hi, " + UserName);
        } else {
            profile_name.setText("Hi, " + "Guest");
        }

        String url_image_profile = "http://www.locallinkers.com/UserImages/" + Image + "?width=120&mode=crop";
        System.out.println("in check outside: " + url_image_profile);
/*update user image*/
        if (Image != null && !Image.trim().equals("") && !Image.trim().equals("null")) {
            try {
                String url_image_profile1 = "http://www.locallinkers.com/UserImages/" + Image + "?width=120&mode=crop";
                System.out.println("in check : " + url_image_profile1);
                //  UrlImageViewHelper.setUrlDrawable(imageView1,url_image_profile );
                Glide.with(this).load(url_image_profile).placeholder(R.drawable.placeholder).into(imageView1);
            } catch (Exception e) {
                Log.e("ERROR ", e.toString());
            }
        } else {
            imageView1.setImageResource(R.drawable.no_preview);
        }


        listViewItems = new ArrayList<ItemObject>();
        listViewItems.add(new ItemObject("Home", R.drawable.ic_home));
        listViewItems.add(new ItemObject("My Orders", R.drawable.ic_myorder));
        listViewItems.add(new ItemObject(City_Name, R.drawable.ic_curentlocation));
        listViewItems.add(new ItemObject("Logout", R.drawable.ic_logout));

/*setting adapter on navigation drawer*/

        // Code here will run in UI thread
        adapter = new CustomAdapter(Home.this, listViewItems);
        mDrawerList.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        /*setting drawer*/
        mDrawerToggle = new ActionBarDrawerToggle(Home.this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.setDrawerIndicatorEnabled(true);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItemFragment(position);
            }
        });


        profile_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"working",Toast.LENGTH_SHORT).show();
                Fragment fragment = new ProfileFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_fragment_container, fragment);
                fragmentManager.beginTransaction().replace(R.id.main_fragment_container, fragment, "MY_CREATE_PRODUCTS");
                fragmentTransaction.commit();
                mDrawerLayout.closeDrawer(DrawerLinear);
            }
        });



        marshMallowPermission = new MarshMallowPermission(Home.this);
        LocationMethod();

        selectItemFragment(fragment_position);

    }


    void LocationMethod() {
        if (!marshMallowPermission.checkPermissionForLocation()) {
            marshMallowPermission.requestPermissionForLocation();
        } else {
            buildGoogleApiClient();

        }


    }


    private void selectItemFragment(int position) {
        Fragment fragment = null;
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (position) {
            case 1:
                fragment = new HomeFragment();
                fragmentManager.beginTransaction().replace(R.id.main_fragment_container, fragment);
                break;
            case 2:
                fragment = new My_Order_Fragment();
                fragmentManager.beginTransaction().replace(R.id.main_fragment_container, fragment);
                break;
            case 3:
                fragment = new Change_City();
                fragmentManager.beginTransaction().replace(R.id.main_fragment_container, fragment);
                break;

            case 4:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                // set title
                alertDialogBuilder.setTitle("Logout?");
                // set dialog message
                alertDialogBuilder
                        .setMessage("Do You want to Logout!")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                prefs.edit().putString(Constants.PHONE_NUMBER, "").commit();
                                prefs.edit().putInt(Constants.USER_ID, 0).commit();
                                prefs.edit().putInt("Category_id", 0).commit();
                                Intent explicitIntent = new Intent(Home.this, Login.class);
                                startActivity(explicitIntent);
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
                break;
            default:
                break;
        }
        if (fragment != null) {
            fragmentManager.beginTransaction().replace(R.id.main_fragment_container, fragment).commit();
            mDrawerList.setItemChecked(position, true);
            setTitle("");
            mDrawerLayout.closeDrawer(DrawerLinear);
            //mDrawerLayout.closeDrawer(mDrawerList);
        }
    }


    @Override
    public void setTitle(CharSequence title) {
        mTitle = "";
        getSupportActionBar().setTitle(mTitle);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        UserName = prefs.getString(Constants.USER_NAME, null);
        Log.e("", "UserName" + UserName);
        Image = prefs.getString(Constants.PRODUCT_PHOTO, null);

        Log.e("", "Image" + Image);
        if (UserName != null) {
            profile_name.setText("Hi, " + UserName);
        } else {
            profile_name.setText("Hi, " + "Guest");
        }
        if (Image != null && !Image.trim().equals("") && !Image.trim().equals("null")) {
            try {
             /*   ImageLoader imageLoader = ImageLoader.getInstance();
                DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .cacheInMemory(true)
                        .build();
                System.out.println("image profile custom " + Image);
                imageLoader.displayImage("http://www.locallinkers.com/UserImages/" + Image + "?width=120&mode=crop",
                        imageView1, options);*/

                Glide.with(this).load("http://www.locallinkers.com/UserImages/" + Image + "?width=120&mode=crop").placeholder(R.drawable.placeholder).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(imageView1);
            } catch (Exception e) {
                Log.e("ERROR ", e.toString());
            }
        } else {
            retrivesharedPreferences();
            //  imageView1.setImageResource(R.drawable.no_preview);
        }

        String citynmae = prefs.getString("city_name", null);
        Log.e("citynmae", ".............citynmae........." + citynmae);

        City_Name = "Change City " + "(" + citynmae + ")";
        listViewItems = new ArrayList<ItemObject>();
        listViewItems.add(new ItemObject("Home", R.drawable.ic_home));
        listViewItems.add(new ItemObject("My Orders", R.drawable.ic_myorder));
        listViewItems.add(new ItemObject(City_Name, R.drawable.ic_curentlocation));
        listViewItems.add(new ItemObject("Logout", R.drawable.ic_logout));

//        mDrawerList.setAdapter(new CustomAdapter(this, listViewItems));
//        adapter.notifyDataSetChanged();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                // Code here will run in UI thread
                adapter = new CustomAdapter(Home.this, listViewItems);
                mDrawerList.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

        return super.onPrepareOptionsMenu(menu);

    }

    private void retrivesharedPreferences() {
        String photo = prefs.getString("PRODUCT_PHOTO", "photo");
        System.out.println("photo : " + photo);
        assert photo != null;
        if (!photo.equals("photo")) {
      /*      ImageLoader imageLoader = ImageLoader.getInstance();
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .build();

            imageLoader.displayImage("http://www.locallinkers.com/admin/categoryimages/" + photo,
                    imageView1, options);*/
            Glide.with(this).load("http://www.locallinkers.com/admin/categoryimages/" + photo).placeholder(R.drawable.placeholder).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(imageView1);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
//        if (id == R.id.action_notification_value)
//        {
//            return true;
//        }

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {

//        int count = getFragmentManager().getBackStackEntryCount();
//
//        if (count == 0)
//        {
//            super.onBackPressed();
//        }
//        else
//        {
//            getFragmentManager().popBackStack();
//        }

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        Home.fragment_position = 1;
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
                //System.exit(1);
            }
        }, 2000);


    }


    @Override
    protected void onResume() {
        Log.d("hii", "............on resume working........... ");

//        FragmentManager fm = getSupportFragmentManager();
//        fm.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener()
//        {
//            @Override
//            public void onBackStackChanged()
//            {
//                if(getFragmentManager().getBackStackEntryCount() == 0)
//
//                    getFragmentManager().popBackStack();
//                   // finish();
//            }
//        });

        super.onResume();
        //  invalidateOptionsMenu();
    }


    @Override
    public void onLocationChanged(Location location) {
        longitude = location.getLongitude();
        latitude = location.getLatitude();
        prefs = getSharedPreferences(Constants.LOCAL_LINKER_APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.LATITUDE, "" + latitude);
        editor.putString(Constants.LONGITUDE, "" + longitude);
        editor.commit();
        Log.d("longitude,latitude", "" + latitude + "," + longitude);

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        callMethodToGetLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        buildGoogleApiClient();
    }

    synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        } else {
            buildGoogleApiClient();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient.isConnected() == true) {
            mGoogleApiClient.disconnect();
        }

    }


    private void callMethodToGetLocation() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(40000); // Update location after 40 second


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);


        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);


        if (mLastLocation != null) {
            lat = String.valueOf(mLastLocation.getLatitude());
            lon = String.valueOf(mLastLocation.getLongitude());
            prefs = getSharedPreferences(Constants.LOCAL_LINKER_APP_PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(Constants.LATITUDE, "" + lat);
            editor.putString(Constants.LONGITUDE, "" + lon);
            editor.commit();
            Log.d("lati,longi custom", "" + lat + "," + lon);
        }


    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Fragment fragment = new ProfileFragment();
//        fragment.onActivityResult(requestCode, resultCode, data);
//    }
}
