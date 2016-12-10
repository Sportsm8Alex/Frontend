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


public class GroupListAdapter extends SelectableAdapter<GroupListAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Information> data;
    private LayoutInflater inflater;
    private Boolean selectionMode;
    private GroupsListFragment groupsListFragment;
    private int count = 0;
    private final static int FADE_DURATION = 300;

    private ClickListener clickListener;

    public GroupListAdapter(Context context, ClickListener clickListener, ArrayList<Information> data) {
        this.context = context;
        this.clickListener = clickListener;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_group_view, parent, false);
        return new MyViewHolder(view, context, clickListener);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.groupname.setText(data.get(position).GroupName);
        holder.itemView.setBackgroundColor(isSelected(position) ? ContextCompat.getColor(context, R.color.lightblue) : ContextCompat.getColor(context, R.color.cardview_light_background));
        //(setScaleAnimation(holder.itemView);
    }

    private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);

    }

    public void setSelectionMode(Boolean bool) {
        selectionMode = bool;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setClickListener(Friends clickListener) {
        this.clickListener = clickListener;
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView groupname, number;
        Context contxt;

        ClickListener listener;

        MyViewHolder(final View itemView, Context ctx, ClickListener listener) {
            super(itemView);
            this.contxt = ctx;
            this.groupname = (TextView) itemView.findViewById(R.id.group_name);
            this.number = (TextView) itemView.findViewById(R.id.number_group);
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
            itemView.setSelected(false);
            this.listener = listener;
        }


        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.group_card:
                    if (!selectionMode) {
                        Intent intent = new Intent(context, GroupDetailView.class);
                        Bundle b = new Bundle();
                        b.putString("GroupID", data.get(getAdapterPosition()).GroupID);
                        b.putString("GroupName", data.get(getAdapterPosition()).GroupName);
                        intent.putExtras(b);
                        context.startActivity(intent);
                    } else {
                        if (listener != null) {
                            listener.onItemClicked(getAdapterPosition(), true);
                        }
                    }
                    break;
            }
        }

        @Override
        public boolean onLongClick(View view) {
            if (listener != null) {
                return listener.onItemLongClicked(getAdapterPosition(), true);
            }
            return false;
        }
    }


}


