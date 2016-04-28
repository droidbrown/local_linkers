package com.hbs.hashbrownsys.locallinkers;

import android.app.Application;

import com.rollbar.android.Rollbar;

/**
 * Created by prateekarora on 28/04/16.
 */
public class LocalLinkersApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Rollbar.init(this, this.getResources().getString(R.string.rollbar_token), "production");
    }

}
