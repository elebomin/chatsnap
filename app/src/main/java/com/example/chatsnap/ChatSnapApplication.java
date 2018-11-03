package com.example.chatsnap;

import android.app.Application;

import com.facebook.stetho.Stetho;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChatSnapApplication extends Application {

    /** Default tag used for logging */
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatSnapApplication.class);

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) { LOGGER.debug("onCreate "); }
        if (BuildConfig.DEBUG) {
            // use Stetho to debug Room DB
            Stetho.initializeWithDefaults(this);
        }
    }
}
