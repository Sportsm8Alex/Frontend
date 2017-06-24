package com.android.brogrammers.sportsm8.SocialViews.Teams.TeamsDetailView;

import com.android.brogrammers.sportsm8.databaseConnection.RetroFitDatabase.DatabaseClasses.UserInfo;

import java.util.List;

/**
 * Created by Korbi on 20.06.2017.
 */

public interface TeamDetailView {
    void displayMembers(List<UserInfo> members);

}
