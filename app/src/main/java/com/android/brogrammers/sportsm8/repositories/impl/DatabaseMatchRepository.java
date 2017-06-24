package com.android.brogrammers.sportsm8.repositories.impl;

import com.android.brogrammers.sportsm8.UserClasses.LoginScreen;
import com.android.brogrammers.sportsm8.databaseConnection.RetroFitDatabase.APIService;
import com.android.brogrammers.sportsm8.databaseConnection.RetroFitDatabase.APIUtils;
import com.android.brogrammers.sportsm8.databaseConnection.RetroFitDatabase.DatabaseClasses.Match;
import com.android.brogrammers.sportsm8.repositories.MatchRepository;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Single;

/**
 * Created by Korbi on 22.06.2017.
 */

public class DatabaseMatchRepository implements MatchRepository {

    private APIService apiService;

    public DatabaseMatchRepository() {
        apiService = APIUtils.getAPIService();
    }

    @Override
    public Single<List<Match>> getMatches() {
        return Single.fromCallable(new Callable<List<Match>>() {
            @Override
            public List<Match> call() throws Exception {
                return apiService.getFriendsMatches("getFriendsMatches", LoginScreen.getRealEmail()).execute().body();
            }
        });
    }
}
