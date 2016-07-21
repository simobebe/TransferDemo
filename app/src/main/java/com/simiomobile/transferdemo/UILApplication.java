package com.simiomobile.transferdemo;

import android.app.Application;

import com.karumi.dexter.Dexter;

/**
 * Created by Aor__Feyverly on 4/7/2559.
 */

public class UILApplication extends Application  {

    @Override
    public void onCreate() {
        super.onCreate();
        Dexter.initialize(getApplicationContext());
    }
}
