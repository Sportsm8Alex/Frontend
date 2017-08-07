package com.android.brogrammers.sportsm8.SocialTab.Teams.TeamsFragment;

import android.support.annotation.NonNull;

import com.android.brogrammers.sportsm8.DataBaseConnection.RetroFitDatabase.APIService;
import com.android.brogrammers.sportsm8.DataBaseConnection.RetroFitDatabase.APIUtils;
import com.android.brogrammers.sportsm8.DataBaseConnection.RetroFitDatabase.DatabaseClasses.Team;
import com.android.brogrammers.sportsm8.DataBaseConnection.Repositories.impl.DatabaseTeamsRepository;
import com.android.brogrammers.sportsm8.UserClasses.LoginScreen;
import com.google.android.gms.common.api.Api;

import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Korbi on 16.06.2017.
 */

class TeamsFragmentPresenter {


    private final TeamsFragmentView view;
    private DatabaseTeamsRepository teamsRepository;
    private final Scheduler mainScheduler;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    TeamsFragmentPresenter(TeamsFragmentView teamsFragmentView, DatabaseTeamsRepository databaseTeamsRepository, Scheduler mainScheduler) {
        this.view = teamsFragmentView;
        this.teamsRepository = databaseTeamsRepository;
        this.mainScheduler = mainScheduler;
    }

    void loadTeams() {
        compositeDisposable.add(teamsRepository.getTeams()
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



    public void unsubscribe(){
        compositeDisposable.clear();
    }

}

