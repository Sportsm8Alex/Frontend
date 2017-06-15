package com.android.brogrammers.sportsm8.databaseConnection.RetroFitDatabase;

import com.android.brogrammers.sportsm8.databaseConnection.RetroFitDatabase.DatabaseClasses.Group;
import com.android.brogrammers.sportsm8.databaseConnection.RetroFitDatabase.DatabaseClasses.Meeting;
import com.android.brogrammers.sportsm8.databaseConnection.RetroFitDatabase.DatabaseClasses.Success;
import com.android.brogrammers.sportsm8.databaseConnection.RetroFitDatabase.DatabaseClasses.UserInfo;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
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
    Call<List<Meeting>> getMeetings(@Field("function") String function, @Field("email") String email);

    @FormUrlEncoded
    @POST("IndexMeetings.php")
    Call<Void> confirmMeeting(@Field("function") String function, @Field("meetingID") int meetingID, @Field("email") String email);

    @FormUrlEncoded
    @POST("IndexMeetings.php")
    Call<ResponseBody> confirmMeeting2(@Field("function") String function, @Field("meetingID") int meetingID, @Field("email") String email);

    @FormUrlEncoded
    @POST("IndexMeetings.php")
    Call<Void> setOtherTime(@Field("function") String function, @Field("start") int beginDatabase, @Field("duration") int duration, @Field("meetingID") int meetingID, @Field("email") String email);

    @FormUrlEncoded
    @POST("IndexMeetings.php")
    Call<Void> declineMeeting(@Field("function") String function, @Field("meetingID") int meetingID, @Field("email") String email);

    @FormUrlEncoded
    @POST("IndexMeetings.php")
    Call<ResponseBody> addMembersToMeeting2(@Field("function") String function, @Field("MeetingID") int meetingID, @FieldMap Map<String, String> members);

    @FormUrlEncoded
    @POST("IndexMeetings.php")
    Call<Void> createMeeting(@Field("function") String function, @Field("startTime") String startTime, @Field("endTime") String endTime, @Field("minPar") int mimMemberCount, @Field("member") String email, @Field("activity") String Activity, @Field("sportID") int sportID, @Field("dynamic") int dynamic, @FieldMap Map<String, String> members, @Field("longitude") double
            longitude, @Field("latitude") double latitude);

    @FormUrlEncoded
    @POST("IndexMeetings.php")
    Call<List<UserInfo>> getMemberList(@Field("function") String function, @Field("MeetingID") int meetingID);


    @FormUrlEncoded
    @POST("IndexMeetings.php")
    Call<ResponseBody> isMeetingConfirmed(@Field("function") String function, @Field("MeetingID") int meetingID);

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
    Call<List<UserInfo>> getGroupMembers(@Field("function") String function, @Field("GroupID") String GroupID);

    @FormUrlEncoded
    @POST("IndexGroups.php")
    Call<Void> addMembersToGroup(@Field("function") String function, @Field("GroupID") String GroupID, @FieldMap Map<String, String> members);

}
