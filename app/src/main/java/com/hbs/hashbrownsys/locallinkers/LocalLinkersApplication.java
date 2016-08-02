package com.hbs.hashbrownsys.locallinkers;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;


import com.rollbar.android.Rollbar;

/**
 * Created by prateekarora on 28/04/16.
 */
public class LocalLinkersApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Rollbar.init(this, this.getResources().getString(R.string.rollbar_token), "production");

        // Create global configuration and initialize ImageLoader with this config
        //initImageLoader();
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

  /*  public void initImageLoader() {
        // UNIVERSAL IMAGE LOADER SETUP
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);
        // END - UNIVERSAL IMAGE LOADER SETUP
    }*/

}
