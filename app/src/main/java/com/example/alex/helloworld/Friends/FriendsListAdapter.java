package com.example.alex.helloworld.Friends;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.alex.helloworld.Information;
import com.example.alex.helloworld.R;
import com.example.alex.helloworld.databaseConnection.Database;
import com.example.alex.helloworld.databaseConnection.UIthread;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;


public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.MyViewHolder> {


    private final static int FADE_DURATION = 500;
    private Context context;
    private ArrayList<Information> data, backup;
    private LayoutInflater inflater;
    private FriendsListFragment friendsListFragment;
    private OnlyFriendsView searchNewFriends;
    private Boolean addToMeetingMode, addFriendMode, creatGroupMode = false;
    private int count = 0;


    public FriendsListAdapter(Context context, ArrayList<Information> data, FriendsListFragment friendsListFragment, OnlyFriendsView searchNewFriends, Boolean addToMeetingMode, Boolean newFriendsMode) {
        this.context = context;
        this.data = data;
        this.backup = data;
        this.friendsListFragment = friendsListFragment;
        this.searchNewFriends = searchNewFriends;
        this.addToMeetingMode = addToMeetingMode;
        this.addFriendMode = newFriendsMode;
        inflater = LayoutInflater.from(context);

    }

    public void setDataBackup() {
        data = backup;
        notifyDataSetChanged();
    }

    public void resetData(ArrayList<Information> data) {
        this.data = data;
        addToMeetingMode = false;
        count = 0;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_friends_view, parent, false);
        return new MyViewHolder(view, context);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        if (data.get(position).selected) {
            holder.relativeLayoutCardview.setBackgroundColor(ContextCompat.getColor(context, R.color.lightblue));
            holder.itemView.setSelected(true);
        } else if (!data.get(position).selected) {
            holder.relativeLayoutCardview.setBackgroundColor(ContextCompat.getColor(context, R.color.cardview_light_background));
            holder.itemView.setSelected(false);
        }
        if (Integer.valueOf(data.get(position).confirmed) == 0 && !addFriendMode) {
            holder.friendsrequest.setVisibility(View.VISIBLE);
        } else {
            holder.friendsrequest.setVisibility(View.GONE);
        }


        //Loads profile Picture with Ion Library in an AsyncTask
        Picasso.with(context)
                .load("http://sportsm8.bplaced.net" + data.get(position).PPpath)
                .placeholder(R.drawable.dickbutt)
                .error(R.drawable.dickbutt)
                // .memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                .into(holder.profileP);
       /*  Ion.with(context)
                .load("http://sportsm8.bplaced.net" + data.get(position).PPpath)
                .noCache()
                .withBitmap()
                .placeholder(R.drawable.dickbutt)
                .error(R.drawable.dickbutt)
                .intoImageView(holder.profileP);


        Ion.with(holder.profileP)
                .placeholder(R.drawable.dickbutt)
                .error(R.drawable.dickbutt)
                .load("http://sportsm8.bplaced.net" + data.get(position).PPpath);*/
        holder.username.setText(data.get(position).username);
        holder.email.setText(data.get(position).email);
        setScaleAnimation(holder.itemView);

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


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, UIthread {
        TextView username;
        TextView email;
        ImageView profileP;
        CardView cardView;
        Context contxt;
        ImageButton accept, decline;
        LinearLayout friendsrequest;
        RelativeLayout relativeLayoutCardview;


        public MyViewHolder(final View itemView, Context ctx) {
            super(itemView);
            this.contxt = ctx;
            friendsrequest = (LinearLayout) itemView.findViewById(R.id.linearL_friendsrequest);
            accept = (ImageButton) itemView.findViewById(R.id.accept_friendship);
            decline = (ImageButton) itemView.findViewById(R.id.decline_friendship);
            cardView = (CardView) itemView.findViewById(R.id.cardview_friends);
            profileP = (ImageView) itemView.findViewById(R.id.profile_picture3);
            username = (TextView) itemView.findViewById(R.id.username_text);
            email = (TextView) itemView.findViewById(R.id.user_email);
            relativeLayoutCardview = (RelativeLayout) itemView.findViewById(R.id.rL_cardview);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            itemView.setSelected(false);
            accept.setOnClickListener(this);
            decline.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            Database db = new Database(this, context);
            SharedPreferences sharedPrefs = contxt.getSharedPreferences("loginInformation", Context.MODE_PRIVATE);
            String emailString = sharedPrefs.getString("email", "");
            switch (view.getId()) {
                case R.id.cardview_friends:
                    if (!addFriendMode) {
                        if ((addToMeetingMode || creatGroupMode) && Integer.valueOf(data.get(getAdapterPosition()).confirmed) == 1) {
                            if (!view.isSelected()) {
                                count++;
                                relativeLayoutCardview.setBackgroundColor(ContextCompat.getColor(context, R.color.lightblue));
                                view.setSelected(true);
                            } else if (view.isSelected()) {
                                relativeLayoutCardview.setBackgroundColor(ContextCompat.getColor(context, R.color.cardview_light_background));
                                view.setSelected(false);
                                count--;

                            }
                            if (searchNewFriends == null) {
                                friendsListFragment.toggle(getAdapterPosition());
                                friendsListFragment.updateCount(count);
                                if(count==0){
                                    creatGroupMode=false;
                                    friendsListFragment.activateGroupSelectionMode(false,count);
                                }
                            } else {
                                searchNewFriends.toggle(getAdapterPosition());
                            }


                        }
                    } else {
                        String friendemail = searchNewFriends.getEmail(getAdapterPosition());
                        String[] params = {"IndexFriendship.php", "function", "setFriend", "email", emailString, "friendemail", friendemail};
                        db.execute(params);
                        searchNewFriends.finish();
                    }
                    break;
                case R.id.accept_friendship:
                    String[] params = {"IndexFriendship.php", "function", "confirmFriend", "email", emailString, "friendemail", data.get(getAdapterPosition()).email};
                    db.execute(params);
                    friendsrequest.setVisibility(View.GONE);
                    break;
                case R.id.decline_friendship:
                    String[] params2 = {"IndexFriendship.php", "function", "deleteFriend", "email", emailString, "friendemail", data.get(getAdapterPosition()).email};
                    db.execute(params2);
                    data.remove(getAdapterPosition());
                    notifyDataSetChanged();
                    break;
            }
        }


        @Override
        public boolean onLongClick(View view) {

            if (!addFriendMode && !addToMeetingMode && Integer.valueOf(data.get(getAdapterPosition()).confirmed) == 1) { //No LongClick Action while adding new Friends or CreatingGroups
                creatGroupMode = true;
                friendsListFragment.toggle(getAdapterPosition());
                if (!view.isSelected()) {
                    count++;
                    relativeLayoutCardview.setBackgroundColor(ContextCompat.getColor(context, R.color.lightblue));
                    view.setSelected(true);
                } else if (view.isSelected()) {
                    relativeLayoutCardview.setBackgroundColor(ContextCompat.getColor(context, R.color.cardview_light_background));
                    view.setSelected(false);
                    count--;
                }
                friendsListFragment.activateGroupSelectionMode(true, count);
                return true;
            }
            return false;
        }

        @Override
        public void updateUI() {

        }

        @Override
        public void updateUI(String answer) {

        }
    }


}


