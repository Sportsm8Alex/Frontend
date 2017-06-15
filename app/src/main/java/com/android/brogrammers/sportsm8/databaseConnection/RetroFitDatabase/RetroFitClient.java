package com.android.brogrammers.sportsm8.databaseConnection.RetroFitDatabase;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.brogrammers.sportsm8.databaseConnection.RetroFitDatabase.DatabaseClasses.UserInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Korbi on 03.06.2017.
 */

public class RetroFitClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient(String baseURL) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static void storeObjectList(ArrayList<?> arrayList, String filename, Context context) {
        Gson gson = new Gson();
        SharedPreferences sharedPrefs = context.getSharedPreferences(filename, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(filename + "JSON", gson.toJson(arrayList));
        editor.apply();
    }

    public static ArrayList<?> retrieveObjectList(String filename, Context context,Type listType) {
        Gson gson = new Gson();
        SharedPreferences sharedPrefs = context.getSharedPreferences(filename, context.MODE_PRIVATE);
        String JsonString = sharedPrefs.getString(filename+"JSON", "");
        return gson.fromJson(JsonString,listType);
    }
}
