package com.android.brogrammers.sportsm8.SocialViews.Teams.TeamsFragment;

import com.android.brogrammers.sportsm8.databaseConnection.RetroFitDatabase.DatabaseClasses.Team;

import java.util.List;

/**
 * Created by Korbi on 16.06.2017.
 */

public interface TeamsFragmentView {

    void displayTeams(List<Team> teams);
}
