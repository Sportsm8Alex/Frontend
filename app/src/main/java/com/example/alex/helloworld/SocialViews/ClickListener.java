package com.example.alex.helloworld.SocialViews;

/**
 * Created by Korbi on 10.12.2016.
 *
 * Interface for Implementing OnItemClick in RecyclerView
 */

public interface ClickListener {
    void onItemClicked(int position,Boolean fromGroup);
    boolean onItemLongClicked(int position,Boolean fromGroup);
}
