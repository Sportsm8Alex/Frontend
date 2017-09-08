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

    @GET("Meetings/MeetingMemberTimes")
    Single<List<UserInfo>> getMeetingMemberTimes(@Query("meetingID") int meetingID);

    @GET("Meetings/isMeetingConfirmed")
    Single<JsonObject> isMeetingConfirmed(@Query("MeetingID") int meetingID);

    @FormUrlEncoded
    @POST("Meetings/newMeeting")
    Completable createMeeting(@Field("startTime") String startTime, @Field("endTime") String endTime, @Field("minPar") int mimMemberCount, @Field("member") String email, @Field("activity") String Activity, @Field("sportID") int sportID, @Field("dynamic") int dynamic, @FieldMap Map<String, String> members, @Field("longitude") double
            longitude, @Field("latitude") double latitude);

    @FormUrlEncoded
    @POST("Meetings/confirmMeeting")
    Completable confirmMeeting(@Field("meetingID") int meetingID, @Field("email") String email);

    @FormUrlEncoded
    @POST("Meetings/setOtherTime")
    Completable setOtherTime(@Field("mystartTime") String startTime, @Field("myendTime") String endTime, @Field("meetingID") int meetingID, @Field("email") String email);

    @FormUrlEncoded
    @POST("Meetings/declineMeeting")
    Completable declineMeeting(@Field("meetingID") int meetingID, @Field("email") String email);

    @FormUrlEncoded
    @POST("Meetings/addMembersToMeeting")
    Completable addMembersToMeeting(@Field("MeetingID") int meetingID, @FieldMap Map<String, String> members);

    //GROUPS
    @GET("Groups/Groups")
    Single<List<Group>> getGroups(@Query("email") String email);

    @GET("Groups/GroupMembers")
    Single<List<UserInfo>> getGroupMembers(@Query("GroupID") int GroupID);

    @FormUrlEncoded
    @POST("Groups/addMembersToGroup")
    Completable addMembersToGroup(@Field("GroupID") int groupID, @FieldMap Map<String, String> members);

    @FormUrlEncoded
    @POST("Groups/newGroup")
    Completable createGroup(@Field("groupName") String groupName, @FieldMap Map<String, String> members);

    @FormUrlEncoded
    @POST("Groups/leaveGroup")
    Completable leaveGroup(@Field("GroupID") int groupID, @Field("email") String email);


    //FRIENDSHIPS
    @GET("Friendships/Friends")
    Single<List<UserInfo>> getFriends(@Query("email") String email);

    @GET("Friendships/searchFriends")
    Single<List<UserInfo>> searchFriends( @Query("email") String email, @Query("friendname") String searchText);

    @FormUrlEncoded
    @POST("Friendships/confirmFriend")
    Completable confirmFriend(@Field("email") String email,@Field("friendemail") String friendemail);

    @FormUrlEncoded
    @POST("Friendships/setFriend")
    Completable setFriend(@Field("email") String email,@Field("friendemail") String friendemail);

    @FormUrlEncoded
    @POST("Friendships/removeFriend")
    Completable removeFriend(@Field("email") String email,@Field("friendemail") String friendemail);

    @FormUrlEncoded
    @POST("Friendships/confirmPending")
    Completable confirmPending(@Field("email") String email);


    ///////TEAMS
    @GET("Teams/Teams")
    Single<List<Team>> getTeams(@Query("email") String email);

    @GET("Teams/Members")
    Single<List<UserInfo>> getTeamMembers(@Query("teamID") int teamID);

    @FormUrlEncoded
    @POST("Teams/newTeam")
    Completable createTeam(@Field("teamName") String groupName, @Field("longitude") double longitude, @Field("latitude") double latitude, @Field("sportID") int sportID, @FieldMap Map<String, String> members);

    ///////////Sports
    @GET("Sports/sportList")
    Single<List<Sport>> getSports();


    //////////////Matches
    @GET("Matches/Matches")
    Single<List<Match>> getFriendsMatches(@Query("email") String email);

    //ACCOUNTS
    @FormUrlEncoded
    @POST("Accounts/createNewAccount")
    Completable createNewaccount(@Field("email") String email,@Field("username") String username);


    @FormUrlEncoded
    @POST("IndexMeetings.php")
    Call<ResponseBody> confirmMeeting2(@Field("function") String function, @Field("meetingID") int meetingID, @Field("email") String email);

}
