package com.example.alex.helloworld.Friends;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alex.helloworld.Information;
import com.example.alex.helloworld.R;

import java.util.ArrayList;
import java.util.List;


public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.MyViewHolder> {


    Context context;
    ArrayList<Information> data;
    LayoutInflater inflater;
    String email;

    public FriendsListAdapter(Context context, ArrayList<Information> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.friends_row_item, parent, false);

        return new MyViewHolder(view, context, data);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.username.setText(data.get(position).username);
        holder.email.setText(data.get(position).email);
        final Information infoData = data.get(position);
    }


    @Override
    public int getItemCount() {
        return data.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView username;
        TextView email;
        ArrayList<Information> informations = new ArrayList<>();
        ArrayList<Information> selected = new ArrayList<>();
        Context contxt;


        public MyViewHolder(final View itemView, Context ctx, ArrayList<Information> info) {
            super(itemView);
            this.informations = info;
            this.contxt = ctx;
            itemView.setOnClickListener(this);
            itemView.setSelected(false);
            username = (TextView) itemView.findViewById(R.id.username_text);
            email = (TextView) itemView.findViewById(R.id.user_email);

        }


        @Override
        public void onClick(View view) {

            if(!view.isSelected()) {
                view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
                view.setSelected(true);
            }else if(view.isSelected()){
                view.setBackgroundColor(ContextCompat.getColor(context,R.color.cardview_light_background));
                view.setSelected(false);
            }
        }
    }
}


