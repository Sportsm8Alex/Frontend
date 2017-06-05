package com.android.brogrammers.sportsm8.databaseConnection.RetroFitDatabase;

/**
 * Created by Korbi on 03.06.2017.
 */

public class APIUtils {

    private APIUtils(){}

    public static final String BASE_URL = "http://sportsm8.bplaced.net:80/php/dynamicphp/include/";

    public static APIService getAPIService(){
        return RetroFitClient.getClient(BASE_URL).create(APIService.class);
    }
}
