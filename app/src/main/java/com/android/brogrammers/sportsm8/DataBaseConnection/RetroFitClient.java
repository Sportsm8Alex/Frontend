package com.android.brogrammers.sportsm8.DataBaseConnection;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.joda.time.DateTime;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Korbi on 03.06.2017.
 */

public class RetroFitClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient(String baseURL) {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(loggingInterceptor);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(DateTime.class,new DateTimeConverter());
        Gson gson = gsonBuilder.create();


        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseURL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }
        return retrofit;
    }


    
    public static void storeObjectList(ArrayList<?> arrayList, String filename, Context context) {
        Gson gson = new Gson();
        SharedPreferences sharedPrefs = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(filename + "JSON", gson.toJson(arrayList));
        editor.apply();
    }

    public static List<?> retrieveObjectList(String filename, Context context, Type listType) {
        Gson gson = new Gson();
        SharedPreferences sharedPrefs = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        String JsonString = sharedPrefs.getString(filename+"JSON", "");
        return gson.fromJson(JsonString,listType);
    }

    private static class DateTimeConverter implements JsonDeserializer{
        @Override
        public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return null;
        }
    }


}
