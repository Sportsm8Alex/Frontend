package com.android.brogrammers.sportsm8.databaseConnection.RetroFitDatabase;

import com.android.brogrammers.sportsm8.databaseConnection.RetroFitDatabase.DatabaseClasses.Group;
import com.android.brogrammers.sportsm8.databaseConnection.RetroFitDatabase.DatabaseClasses.Meeting;
import com.android.brogrammers.sportsm8.databaseConnection.RetroFitDatabase.DatabaseClasses.UserInfo;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Korbi on 03.06.2017.
 */

public interface APIService {
    //MEETINGS
    @FormUrlEncoded
    @POST("IndexMeetings.php")
    Call<ArrayList<Meeting>> getMeetings(@Field("function") String function, @Field("email") String email);

    @FormUrlEncoded
    @POST("IndexMeetings.php")
    Call<Void> confirmMeeting(@Field("function") String function, @Field("meetingID") int meetingID, @Field("email") String email);

    @FormUrlEncoded
    @POST("IndexMeetings.php")
    Call<Void> setOtherTime(@Field("function") String function, @Field("start") int beginDatabase, @Field("duration") int duration, @Field("meetingID") int meetingID, @Field("email") String email);

    @FormUrlEncoded
    @POST("IndexMeetings.php")
    Call<Void> declineMeeting(@Field("function") String function, @Field("meetingID") int meetingID, @Field("email") String email);

    @FormUrlEncoded
    @POST("IndexMeetings.php")
    Call<Void> createMeeting(@Field("function") String function, @Field("startTime") String startTime, @Field("endTime") String endTime, @Field("minPar") int mimMemberCount, @Field("member") String email, @Field("activity") String Activity, @Field("sportID") int sportID, @Field("dynamic") int dynamic, @FieldMap Map<String,String> members);


    //FRIENDSHIPS
    @FormUrlEncoded
    @POST("IndexFriendship.php")
    Call<ArrayList<UserInfo>> getFriends(@Field("function") String function, @Field("email") String email);


    @FormUrlEncoded
    @POST("IndexFriendship.php")
    Call<ArrayList<UserInfo>> searchFriends(@Field("function") String function, @Field("email") String email, @Field("friendname") String searchText);

    //GROUPS
    @FormUrlEncoded
    @POST("IndexGroups.php")
    Call<ArrayList<Group>> getGroups(@Field("function") String function, @Field("email") String email);

    @FormUrlEncoded
    @POST("IndexGroups.php")
    Call<ArrayList<UserInfo>> getGroupMembers(@Field("function") String function, @Field("GroupID") String GroupID);


    //Not Tested
    @FormUrlEncoded
    @POST("IndexGroups.php")
    Call<Void> addMembersToGroup(@Field("function") String function, @Field("GroupID") String GroupID,@FieldMap Map<String,String> members);




}
