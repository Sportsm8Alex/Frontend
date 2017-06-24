package com.android.brogrammers.sportsm8.repositories;

import com.android.brogrammers.sportsm8.databaseConnection.RetroFitDatabase.DatabaseClasses.Team;
import com.android.brogrammers.sportsm8.databaseConnection.RetroFitDatabase.DatabaseClasses.UserInfo;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Single;

/**
 * Created by Korbi on 16.06.2017.
 */

public interface TeamsRepository {
    Single<List<Team>> getTeams();
    Single<List<UserInfo>> getTeamMembers(final Team team);
}
