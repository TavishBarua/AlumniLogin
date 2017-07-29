package com.tavish.alumnilogin;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.preference.PreferenceManager;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;



public class AppConfig extends Application {

    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor sharedPreferencesEditor;
    private static Context mContext;

    @Override
    public void onCreate()
    {
        super.onCreate();

        try {

            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            sharedPreferencesEditor = sharedPreferences.edit();
            setContext(getApplicationContext());

            //MultiDex.install(this);

           TwitterAuthConfig authConfig = new TwitterAuthConfig(getResources().getString(R.string.twitter_consumer_key), getResources().getString(R.string.twitter_secret_key));
          Fabric.with(this, new Twitter(authConfig));

            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyDeath()
                    .build());

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Context getContext() {
        return mContext;
    }

    public static void setContext(Context mctx) {
        mContext = mctx;
    }


    public static SharedPreferences.Editor getApplicationPreferenceEditor()
    {
        return sharedPreferencesEditor;
    }

    public static SharedPreferences getApplicationPreference()
    {
        return sharedPreferences;
    }

}
