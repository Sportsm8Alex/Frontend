package com.example.alex.helloworld.Friends;

/**
 * Created by Korbi on 10.12.2016.
 */

public interface ClickListener {
    void onItemClicked(int position,Boolean fromGroup);
    boolean onItemLongClicked(int position,Boolean fromGroup);
}
