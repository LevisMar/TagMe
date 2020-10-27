package com.example.tagme;

import android.app.Application;
import android.content.Context;

/**
 * Oggetto applicazione personalizzato. Creato
 * per permettere di recuperare l'android
 * application context in qualsiasi classe Java
 * che ne necessita
 */
public class GlobalApplication extends Application
{
    // Variabile in cui verrà salvato il contesto dell'App
    private static Context appContext;

    @Override
    public void onCreate()
    {
        super.onCreate();
        appContext = getApplicationContext();
    }

    /**
     * Restituisce il contesto dell'App
     */
    public static Context getAppContext()
    {
        return appContext;
    }
}