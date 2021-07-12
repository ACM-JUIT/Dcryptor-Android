package com.anshagrawal.dcmlkit.UtilsService;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesClass {
    private static final String USER_PREFERENCE = "user_cypher";
    private android.content.SharedPreferences appShared;
    private android.content.SharedPreferences.Editor prefsEditor;

    public SharedPreferencesClass(Context context){
        appShared = context.getSharedPreferences(USER_PREFERENCE, Activity.MODE_PRIVATE);
        this.prefsEditor = appShared.edit();
    }

    //for integer
    public int getValueInteger(String key){
        return appShared.getInt(key, 0);
    }

    public void setValueInteger(String key, int value){
        prefsEditor.putInt(key, value).commit();
    }

    //for string

    public String getValueString(String key){
        return appShared.getString(key, "");
    }

        public void setValueString(String key, String value){
        prefsEditor.putString(key, value).commit();
    }

    //for boolean

    public boolean getValueBoolean(String key){
        return appShared.getBoolean(key, false);
    }

    public void setValueBoolean(String key, boolean value){
        prefsEditor.putBoolean(key, value).commit();
    }

    public void clearData(){
        prefsEditor.clear().commit();
    }
}
