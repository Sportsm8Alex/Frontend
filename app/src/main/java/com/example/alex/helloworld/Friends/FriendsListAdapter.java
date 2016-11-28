package com.example.alex.helloworld.Friends;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alex.helloworld.Information;
import com.example.alex.helloworld.R;
import com.koushikdutta.ion.Ion;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.MyViewHolder> {



    private final static int FADE_DURATION = 300;
    private Context context;
    private ArrayList<Information> data;
    private LayoutInflater inflater;
    private FriendsTab friendsTab;


    public FriendsListAdapter(Context context, ArrayList<Information> data,FriendsTab friendsTab) {
        this.context = context;
        this.data = data;
        this.friendsTab = friendsTab;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_friends_view, parent, false);
        return new MyViewHolder(view, context);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

    //Loads profile Picture with Ion Library in an AsyncTask
        Ion.with(holder.profileP)
                .placeholder(R.drawable.dickbutt)
                .error(R.drawable.dickbutt)
                .load("http://sportsm8.bplaced.net" + data.get(position).PPpath);

        holder.username.setText(data.get(position).username);
        holder.email.setText(data.get(position).email);
        setScaleAnimation(holder.itemView);
    }

        //search is not working right now
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


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView username;
        TextView email;
        ImageView profileP;
        Context contxt;


        public MyViewHolder(final View itemView, Context ctx) {
            super(itemView);
            this.contxt = ctx;
            itemView.setOnClickListener(this);
            itemView.setSelected(false);
            profileP = (ImageView) itemView.findViewById(R.id.profile_picture);
            username = (TextView) itemView.findViewById(R.id.username_text);
            email = (TextView) itemView.findViewById(R.id.user_email);

        }


        @Override
        public void onClick(View view) {

            friendsTab.toggle(getAdapterPosition());
            if (!view.isSelected()) {
                view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
                view.setSelected(true);
            } else if (view.isSelected()) {
                view.setBackgroundColor(ContextCompat.getColor(context, R.color.cardview_light_background));
                view.setSelected(false);

            }
        }


    }


}


