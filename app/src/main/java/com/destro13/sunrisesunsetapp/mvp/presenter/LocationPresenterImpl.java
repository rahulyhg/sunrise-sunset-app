package com.destro13.sunrisesunsetapp.mvp.presenter;

import android.content.Context;

import com.destro13.sunrisesunsetapp.api.ApiController;
import com.destro13.sunrisesunsetapp.mvp.model.SunriseSunsetReport;
import com.destro13.sunrisesunsetapp.mvp.view.LocationView;
import com.destro13.sunrisesunsetapp.util.ConnectionChecker;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

public class LocationPresenterImpl implements LocationPresenter {
    LocationView locationView;
    Context context;
    private SunriseSunsetReport sunriseSunsetReport;
    private Subscription subscription = Subscriptions.empty();

    public LocationPresenterImpl(Context context) {
        this.locationView = (LocationView) context;
        this.context = context;
    }

    @Override
    public void showSunriseSunset(double latitude, double longitude) {
        if (ConnectionChecker.isConnected(context)) {

            if (!subscription.isUnsubscribed()) {
                subscription.unsubscribe();
            }

            locationView.showLoading();
            subscription = ApiController
                    .getApi()
                    .getSunriseSunset(latitude, longitude, 0, "today")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<SunriseSunsetReport>() {
                        @Override
                        public void onCompleted() {
                            locationView.setData(sunriseSunsetReport);
                            locationView.closeLoading();
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            locationView.closeLoading();
                            locationView.showError("Server error");
                        }

                        @Override
                        public void onNext(SunriseSunsetReport report) {
                            sunriseSunsetReport = report;
                        }
                    });
        } else
            locationView.showError("Network error");
    }
}
