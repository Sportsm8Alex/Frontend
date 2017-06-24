package com.android.brogrammers.sportsm8.SocialViews.Teams.TeamsFragment;

import android.support.annotation.NonNull;

import com.android.brogrammers.sportsm8.databaseConnection.RetroFitDatabase.DatabaseClasses.Team;
import com.android.brogrammers.sportsm8.repositories.impl.DatabaseTeamsRepository;

import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Korbi on 16.06.2017.
 */

public class TeamsFragmentPresenter {


    private final TeamsFragmentView view;
    private DatabaseTeamsRepository teamsRepository;
    private final Scheduler mainScheduler;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public TeamsFragmentPresenter(TeamsFragmentView teamsFragmentView, DatabaseTeamsRepository databaseTeamsRepository, Scheduler mainScheduler) {
        this.view = teamsFragmentView;
        this.teamsRepository = databaseTeamsRepository;
        this.mainScheduler = mainScheduler;
    }

    public void loadTeams() {
        compositeDisposable.add(teamsRepository.getTeams()
                .subscribeOn(Schedulers.io())
                .observeOn(mainScheduler)
                .subscribeWith(new DisposableSingleObserver<List<Team>>() {
                    @Override
                    public void onSuccess(@NonNull List<Team> teams) {
                        view.displayTeams(teams);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                }));
    }


}

