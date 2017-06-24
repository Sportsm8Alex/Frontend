package com.android.brogrammers.sportsm8.SocialViews.Teams.TeamsFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.brogrammers.sportsm8.R;
import com.android.brogrammers.sportsm8.SocialViews.FragmentSocial;
import com.android.brogrammers.sportsm8.SocialViews.Teams.TeamsFragment.Adapter.TeamsListAdapter;
import com.android.brogrammers.sportsm8.databaseConnection.RetroFitDatabase.DatabaseClasses.Team;
import com.android.brogrammers.sportsm8.repositories.impl.DatabaseTeamsRepository;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class TeamsFragmentActivity extends Fragment implements TeamsFragmentView {

    private FragmentSocial fragmentSocial;
    private RecyclerView recyclerView;
    TeamsFragmentPresenter presenter;
    TeamsListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teams, container, false);
        fragmentSocial = (FragmentSocial) getParentFragment();
        recyclerView = (RecyclerView) view.findViewById(R.id.teams_recycler_view);
        presenter = new TeamsFragmentPresenter(this, new DatabaseTeamsRepository(), AndroidSchedulers.mainThread());
        presenter.loadTeams();
        return view;
    }


    @Override
    public void displayTeams(List<Team> teams) {
        adapter = new TeamsListAdapter(getContext(), null, teams);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.notifyDataSetChanged();
    }
}
