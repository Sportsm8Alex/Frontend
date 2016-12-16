package com.example.alex.helloworld.SocialViews.groups;

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

import com.example.alex.helloworld.SocialViews.friends.OnlyFriendsView;
import com.example.alex.helloworld.databaseConnection.Information;
import com.example.alex.helloworld.R;
import com.example.alex.helloworld.databaseConnection.Database;
import com.example.alex.helloworld.databaseConnection.UIthread;

import org.json.JSONException;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Arrays;

public class GroupDetailView extends AppCompatActivity implements UIthread,SwipeRefreshLayout.OnRefreshListener {

    private ListView listView;
    private ArrayList<Information> members,Selection;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String GroupID, groupName;
    private TextView textView_name;

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
        textView_name =(TextView) findViewById(R.id.group_name_detailview);
        listView = (ListView) findViewById(R.id.listview_group_detail);
        swipeRefreshLayout =(SwipeRefreshLayout) findViewById(R.id.meeting_detail_swipeRefresh);

        textView_name.setText(groupName);
        swipeRefreshLayout.setOnRefreshListener(this);
        getMemberList();
    }

    public void onClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("search", false);
        Intent intent = new Intent(this, OnlyFriendsView.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, 1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                Selection = (ArrayList<Information>) bundle.getSerializable("partyList");

                ArrayList<String> paramsArrayList = new ArrayList<>(
                        Arrays.asList("IndexGroups.php", "function", "adMembersToGroup", "GroupID",GroupID));

                for (int i = 0; i < Selection.size(); i++) {
                    paramsArrayList.add("member" + i);
                    paramsArrayList.add(Selection.get(i).email);
                }
                String[] params = new String[paramsArrayList.size()];
                params = paramsArrayList.toArray(params);
                Database db = new Database(this,getBaseContext());
                db.execute(params);

            }
        }
        getMemberList();
    }

    private void getMemberList() {
        String[] params = {"IndexGroups.php", "function", "getGroupMembers", "GroupID", GroupID};
        Database db = new Database(this, getBaseContext());
        db.execute(params);
    }

    @Override
    public void updateUI() {

    }

    @Override
    public void updateUI(String answer) {
        SharedPreferences sharedPrefs = getSharedPreferences("IndexGroups", Context.MODE_PRIVATE);
        String meetingJson = sharedPrefs.getString("IndexGroupsgetGroupMembersJSON", "");
        try {
            members = Database.jsonToArrayList(meetingJson);
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
        ArrayList<String> emails = new ArrayList<>();
        for (int i = 0; i < members.size(); i++) {
            emails.add(members.get(i).email);
        }
        ListViewAdapter arrayAdapter = new ListViewAdapter(this,members);
        listView.setAdapter(arrayAdapter);
        TextView textView = (TextView) findViewById(R.id.group_size_detailview);
        textView.setText(members.size()+"");
        swipeRefreshLayout.setRefreshing(false);


    }

    @Override
    public void onRefresh() {
        getMemberList();
    }


    class ListViewAdapter extends BaseAdapter{

        ArrayList<Information> list;
        Context context;
        public ListViewAdapter(Context context,ArrayList<Information> listItem) {
            list=listItem;
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
            View row = inflater.inflate(android.R.layout.simple_list_item_1,viewGroup,false);
            TextView textView = (TextView) row.findViewById(android.R.id.text1);
            textView.setText(list.get(i).email);
            if(Integer.valueOf(list.get(i).confirmed) == 1){
                row.setBackgroundColor(ContextCompat.getColor(context, R.color.green));
            }
            return row;

        }
    }
}
