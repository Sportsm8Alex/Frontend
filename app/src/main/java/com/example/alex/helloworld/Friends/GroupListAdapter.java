package com.example.alex.helloworld.Friends;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.example.alex.helloworld.GroupDetailView;
import com.example.alex.helloworld.Information;
import com.example.alex.helloworld.MeetingDetailView;
import com.example.alex.helloworld.R;
import com.example.alex.helloworld.databaseConnection.Database;

import java.util.ArrayList;


public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.MyViewHolder> {

    Context context;
    ArrayList<Information> data;
    ArrayList<Information> backup;
    LayoutInflater inflater;
    String email;
    Boolean selectionMode;
    GroupsListFragment groupsListFragment;
    int count = 0;

    public GroupListAdapter(Context context, ArrayList<Information> data, GroupsListFragment groupsListFragment, Boolean selectionMode) {
        this.context = context;
        this.data = data;
        backup = data;
        this.groupsListFragment = groupsListFragment;
        this.selectionMode = selectionMode;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_group_view, parent, false);
        return new MyViewHolder(view, context, data);
    }

    private final static int FADE_DURATION = 300;

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.groupname.setText(data.get(position).GroupName);
        setScaleAnimation(holder.itemView);
    }

    public int search(String search) {
        int posi = 0;
        for (int i = 0; i < getItemCount(); i++) {
            if (data.get(i).getUsername().toLowerCase().startsWith(search.toLowerCase())) {
                posi = i;
            }
        }

        return posi;
    }

    private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);

    }


    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView groupname;
        TextView email;
        ArrayList<Information> informations = new ArrayList<>();
        Context contxt;


        public MyViewHolder(final View itemView, Context ctx, ArrayList<Information> info) {
            super(itemView);
            this.informations = info;
            this.contxt = ctx;
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
            itemView.setSelected(false);
            groupname = (TextView) itemView.findViewById(R.id.group_name);


        }


        @Override
        public void onClick(View view) {


            if (selectionMode) {
                groupsListFragment.toggle(getAdapterPosition());
                if (!view.isSelected()) {
                    view.setBackgroundColor(ContextCompat.getColor(context, R.color.lightblue));
                    view.setSelected(true);
                } else if (view.isSelected()) {
                    view.setBackgroundColor(ContextCompat.getColor(context, R.color.cardview_light_background));
                    view.setSelected(false);

                }
            }else{
                Intent intent = new Intent(context,GroupDetailView.class);
                Bundle b = new Bundle();
                b.putString("GroupID",data.get(getAdapterPosition()).GroupID);
                b.putString("GroupName",data.get(getAdapterPosition()).GroupName);
                intent.putExtras(b);
                context.startActivity(intent);
            }

        }

        @Override
        public boolean onLongClick(View view) {
            if (!selectionMode) {
                selectionMode = true;
                groupsListFragment.toggle(getAdapterPosition());
                if (!view.isSelected()) {
                    view.setBackgroundColor(ContextCompat.getColor(context, R.color.lightblue));
                    view.setSelected(true);
                } else if (view.isSelected()) {
                    view.setBackgroundColor(ContextCompat.getColor(context, R.color.cardview_light_background));
                    view.setSelected(false);

                }
                groupsListFragment.activateSelectionMode(true, count);
                return true;
            }

            return false;
        }
    }


}


