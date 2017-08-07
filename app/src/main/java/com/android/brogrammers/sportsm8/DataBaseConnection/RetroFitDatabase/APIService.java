package com.android.brogrammers.sportsm8.DataBaseConnection.RetroFitDatabase;

import com.android.brogrammers.sportsm8.DataBaseConnection.RetroFitDatabase.DatabaseClasses.Group;
import com.android.brogrammers.sportsm8.DataBaseConnection.RetroFitDatabase.DatabaseClasses.Match;
import com.android.brogrammers.sportsm8.DataBaseConnection.RetroFitDatabase.DatabaseClasses.Meeting;
import com.android.brogrammers.sportsm8.DataBaseConnection.RetroFitDatabase.DatabaseClasses.Sport;
import com.android.brogrammers.sportsm8.DataBaseConnection.RetroFitDatabase.DatabaseClasses.Team;
import com.android.brogrammers.sportsm8.DataBaseConnection.RetroFitDatabase.DatabaseClasses.UserInfo;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Korbi on 03.06.2017.
 */

public interface APIService {
    //MEETINGS
    @GET("Meetings/Meetings")
    Single<List<Meeting>> getMeetings(@Query("email") String email);

    @GET("Meetings/MemberList")
    Single<List<UserInfo>> getMemberList(@Query("MeetingID") int meetingID);

    @FormUrlEncoded
    @POST("Meetings/newMeeting")
    Completable createMeeting(@Field("startTime") String startTime, @Field("endTime") String endTime, @Field("minPar") int mimMemberCount, @Field("member") String email, @Field("activity") String Activity, @Field("sportID") int sportID, @Field("dynamic") int dynamic, @FieldMap Map<String, String> members, @Field("longitude") double
            longitude, @Field("latitude") double latitude);

    @FormUrlEncoded
    @POST("Meetings/confirmMeeting")
    Completable confirmMeeting(@Field("meetingID") int meetingID, @Field("email") String email);

    @GET("Meetings/MeetingMemberTimes")
    Single<List<UserInfo>> getMeetingMemberTimes(@Query("meetingID") int meetingID);

    @FormUrlEncoded
    @POST("Meetings/setOtherTime")
    Completable setOtherTime(@Field("mystartTime") String startTime, @Field("myendTime") String endTime, @Field("meetingID") int meetingID, @Field("email") String email);

    @FormUrlEncoded
    @POST("Meetings/declineMeeting")
    Completable declineMeeting(@Field("meetingID") int meetingID, @Field("email") String email);

    @FormUrlEncoded
    @POST("Meetings/addMembersToMeeting")
    Completable addMembersToMeeting(@Field("MeetingID") int meetingID, @FieldMap Map<String, String> members);

    @GET("Meetings/isMeetingConfirmed")
    Single<JsonObject> isMeetingConfirmed(@Query("MeetingID") int meetingID);

    //FRIENDSHIPS
    @FormUrlEncoded
    @POST("IndexFriendship.php")
    Call<List<UserInfo>> getFriends(@Field("function") String function, @Field("email") String email);


    @FormUrlEncoded
    @POST("IndexFriendship.php")
    Call<List<UserInfo>> searchFriends(@Field("function") String function, @Field("email") String email, @Field("friendname") String searchText);

    //GROUPS
    @FormUrlEncoded
    @POST("IndexGroups.php")
    Call<List<Group>> getGroups(@Field("function") String function, @Field("email") String email);

    @FormUrlEncoded
    @POST("IndexGroups.php")
    Single<List<UserInfo>> getGroupMembers(@Field("function") String function, @Field("GroupID") int GroupID);

    @FormUrlEncoded
    @POST("IndexGroups.php")
    Completable addMembersToGroup(@Field("function") String function, @Field("GroupID") int groupID, @FieldMap Map<String, String> members);


    @FormUrlEncoded
    @POST("IndexGroups.php")
    Call<Void> createGroup(@Field("function") String function, @Field("groupName") String groupName, @FieldMap Map<String, String> members);

    @FormUrlEncoded
    @POST("IndexGroups.php")
    Completable leaveGroup(@Field("function") String function, @Field("GroupID") int groupID, @Field("email") String email);

    ///////TEAMS
    @FormUrlEncoded
    @POST("IndexTeams.php")
    Single<List<Team>> getTeams(@Field("function") String function, @Field("email") String email);

    @FormUrlEncoded
    @POST("IndexTeams.php")
    Call<List<UserInfo>> getTeamMembers(@Field("function") String function, @Field("teamID") int teamID);

    @FormUrlEncoded
    @POST("IndexTeams.php")
    Call<Void> createTeam(@Field("function") String function, @Field("teamName") String groupName, @Field("longitude") double longitude, @Field("latitude") double latitude, @Field("sportID") int sportID, @FieldMap Map<String, String> members);

    ///////////Sports
    @FormUrlEncoded
    @POST("IndexSports.php")
    Call<List<Sport>> getSports(@Field("function") String function);


    //////////////Matches
    @FormUrlEncoded
    @POST("IndexMatches.php")
    Call<List<Match>> getFriendsMatches(@Field("function") String function, @Field("email") String email);





    @FormUrlEncoded
    @POST("IndexMeetings.php")
    Single<List<Meeting>> getMeetingsPOST(@Field("function") String function, @Field("email") String email);

    @FormUrlEncoded
    @POST("IndexMeetings.php")
    Call<ResponseBody> confirmMeeting2(@Field("function") String function, @Field("meetingID") int meetingID, @Field("email") String email);

}
