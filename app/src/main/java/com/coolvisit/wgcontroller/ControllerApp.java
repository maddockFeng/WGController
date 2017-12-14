package com.coolvisit.wgcontroller;

import android.app.Application;


public class ControllerApp extends Application {


    public static ControllerApp application = null;

    public static ControllerApp getInstance() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;

    }

    @Override
    public void onTerminate() {
        super.onTerminate();

    }

}
