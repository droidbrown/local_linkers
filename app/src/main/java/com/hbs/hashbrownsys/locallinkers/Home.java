package com.hbs.hashbrownsys.locallinkers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
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

import com.hbs.hashbrownsys.locallinkers.adapter.CustomAdapter;
import com.hbs.hashbrownsys.locallinkers.fragment.Change_City;
import com.hbs.hashbrownsys.locallinkers.fragment.HomeFragment;
import com.hbs.hashbrownsys.locallinkers.fragment.My_Order_Fragment;
import com.hbs.hashbrownsys.locallinkers.fragment.ProfileFragment;
import com.hbs.hashbrownsys.locallinkers.model.ItemObject;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Home extends AppCompatActivity implements LocationListener {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        topToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(topToolBar);
        topToolBar.setNavigationIcon(R.drawable.menu);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Font = Typeface.createFromAsset(getAssets(), "fonts/MyriadPro-Regular.otf");
        DrawerLinear = (RelativeLayout) findViewById(R.id.drawerPane);
        TextView local_linker = (TextView) findViewById(R.id.local_linker);
        local_linker.setTypeface(Font);
        TextView toolbarTitle = (TextView) topToolBar.findViewById(R.id.toolbar_app_name);
        toolbarTitle.setTypeface(Font);  //
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        prefs = getSharedPreferences(Constants.LOCAL_LINKER_APP_PREFERENCES, Context.MODE_PRIVATE);
        UserName = prefs.getString(Constants.USER_NAME, null);
        Log.e("", "UserName" + UserName);

        Image = prefs.getString(Constants.IMAGE, null);
        Log.e("", "Image" + Image);

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

        if (UserName != null) {
            profile_name.setText(UserName);
        } else {
            profile_name.setText("Guest");
        }

        if (Image != null && !Image.trim().equals("") && !Image.trim().equals("null")) {
            try {
                UrlImageViewHelper.setUrlDrawable(imageView1, "http://locallinkers.com/UserImages/" + Image + "?width=120&mode=crop");
            } catch (Exception e) {
                Log.e("ERROR ", e.toString());
            }
        } else {
            imageView1.setImageResource(R.drawable.no_preview);
        }


        selectItemFragment(fragment_position);

        listViewItems = new ArrayList<ItemObject>();
        listViewItems.add(new ItemObject("Home", R.drawable.ic_home));
        listViewItems.add(new ItemObject("My Orders", R.drawable.ic_myorder));
        listViewItems.add(new ItemObject(City_Name, R.drawable.ic_curentlocation));
        listViewItems.add(new ItemObject("Logout", R.drawable.ic_logout));


        adapter = new CustomAdapter(this, listViewItems);
        mDrawerList.setAdapter(adapter);

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


        imageView1.setOnClickListener(new View.OnClickListener() {
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
            profile_name.setText(UserName);
        } else {
            profile_name.setText("Guest");
        }

        if (Image != null && !Image.trim().equals("") && !Image.trim().equals("null")) {
            try {
                ImageLoader imageLoader = ImageLoader.getInstance();
                DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .cacheInMemory(true)
                        .build();

                imageLoader.displayImage("http://locallinkers.com/UserImages/" + Image + "?width=120&mode=crop",
                        imageView1, options);
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
        adapter.notifyDataSetChanged();
        mDrawerList.setAdapter(new CustomAdapter(this, listViewItems));
        return super.onPrepareOptionsMenu(menu);

    }

    private void retrivesharedPreferences() {
        String photo = prefs.getString("PRODUCT_PHOTO", "photo");
        assert photo != null;
        if (!photo.equals("photo")) {
            ImageLoader imageLoader = ImageLoader.getInstance();
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .build();

            imageLoader.displayImage("http://locallinkers.azurewebsites.net/admin/categoryimages/" + photo,
                    imageView1, options);
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
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Fragment fragment = new ProfileFragment();
//        fragment.onActivityResult(requestCode, resultCode, data);
//    }
}
