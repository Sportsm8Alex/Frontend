package com.android.brogrammers.sportsm8.SocialViews.groups;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.brogrammers.sportsm8.SocialViews.friends.OnlyFriendsView;
import com.android.brogrammers.sportsm8.R;
import com.android.brogrammers.sportsm8.databaseConnection.Database;
import com.android.brogrammers.sportsm8.databaseConnection.RetroFitDatabase.APIService;
import com.android.brogrammers.sportsm8.databaseConnection.RetroFitDatabase.APIUtils;
import com.android.brogrammers.sportsm8.databaseConnection.RetroFitDatabase.DatabaseClasses.UserInfo;
import com.android.brogrammers.sportsm8.databaseConnection.RetroFitDatabase.RetroFitClient;
import com.android.brogrammers.sportsm8.databaseConnection.UIthread;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupDetailView extends AppCompatActivity implements UIthread, SwipeRefreshLayout.OnRefreshListener {

    private ListView listView;
    private List<UserInfo> members, Selection;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String GroupID, groupName;
    private TextView textView_name;
    private APIService apiService = APIUtils.getAPIService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Variables
        Bundle b = getIntent().getExtras();
        GroupID = b.getString("GroupID");
        groupName = b.getString("GroupName");
        //Views
        textView_name = (TextView) findViewById(R.id.group_name_detailview);
        listView = (ListView) findViewById(R.id.listview_group_detail);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.meeting_detail_swipeRefresh);

        textView_name.setText(groupName);
        swipeRefreshLayout.setOnRefreshListener(this);
        getMemberList();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_members_to_group:
                Bundle bundle = new Bundle();
                bundle.putBoolean("search", false);
                Intent intent = new Intent(this, OnlyFriendsView.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
                break;
            case R.id.leave_group_button:
                Database db = new Database(this, getBaseContext());
                SharedPreferences sharedPrefs = getBaseContext().getSharedPreferences("loginInformation", Context.MODE_PRIVATE);
                String email = sharedPrefs.getString("email", "");
                String[] params = {"IndexGroups.php", "function", "leaveGroup", "GroupID", GroupID, "email", email};
                db.execute(params);
                finish();
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                Selection = (ArrayList<UserInfo>) bundle.getSerializable("partyList");
                Map<String, String> membersMap = new HashMap<>();

                for (int i = 0; i < Selection.size(); i++) {
                    membersMap.put("member" + i, Selection.get(i).email);
                }
                apiService.addMembersToGroup("adMembersToGroup", GroupID, membersMap).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
            }
        }
        getMemberList();
    }

    private void getMemberList() {
        apiService.getGroupMembers("getGroupMembers", GroupID).enqueue(new Callback<List<UserInfo>>() {
            @Override
            public void onResponse(Call<List<UserInfo>> call, Response<List<UserInfo>> response) {
                RetroFitClient.storeObjectList(new ArrayList<Object>(response.body()), "groupMembers" + GroupID, getBaseContext());
                updateUI("");
            }

            @Override
            public void onFailure(Call<List<UserInfo>> call, Throwable t) {

            }
        });
    }

    @Override
    public void updateUI() {

    }

    @Override
    public void updateUI(String answer) {
        members = (ArrayList<UserInfo>) RetroFitClient.retrieveObjectList("groupMembers" + GroupID, getBaseContext(), new TypeToken<ArrayList<UserInfo>>() {
        }.getType());
        ListViewAdapter arrayAdapter = new ListViewAdapter(this, members);
        listView.setAdapter(arrayAdapter);
        TextView textView = (TextView) findViewById(R.id.group_size_detailview);
        textView.setText(members.size() + "");
        swipeRefreshLayout.setRefreshing(false);


    }

    @Override
    public void onRefresh() {
        getMemberList();
    }


    class ListViewAdapter extends BaseAdapter {

        List<UserInfo> list;
        Context context;

        public ListViewAdapter(Context context, List<UserInfo> listItem) {
            list = listItem;
            this.context = context;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(android.R.layout.simple_list_item_1, viewGroup, false);
            TextView textView = (TextView) row.findViewById(android.R.id.text1);
            textView.setText(list.get(i).email);
            if (Integer.valueOf(list.get(i).confirmed) == 1) {
                row.setBackgroundColor(ContextCompat.getColor(context, R.color.green));
            }
            return row;

        }
    }
}
