package com.destro13.sunrisesunsetapp.mvp.presenter;

import android.content.Context;

import com.destro13.sunrisesunsetapp.api.GoogleApiController;
import com.destro13.sunrisesunsetapp.api.SunriseSunsetApiController;
import com.destro13.sunrisesunsetapp.mvp.model.City;
import com.destro13.sunrisesunsetapp.mvp.model.googleplaces.CityReport;
import com.destro13.sunrisesunsetapp.mvp.model.sunrisesunset.SunriseSunsetReport;
import com.destro13.sunrisesunsetapp.mvp.view.FindCityView;
import com.destro13.sunrisesunsetapp.util.ConnectionChecker;
import com.destro13.sunrisesunsetapp.util.Constants;
import com.destro13.sunrisesunsetapp.util.DateUtil;

import java.util.LinkedList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

public class FindCityPresenterImpl implements FindCityPresenter {
    private FindCityView findCityView;
    private Context context;
    private Subscription subscription = Subscriptions.empty();
    private CityReport cityReport;
    private SunriseSunsetReport sunriseSunsetReport;
    private LinkedList<City> cities;

    public FindCityPresenterImpl(Context context) {
        this.context = context;
        this.findCityView = (FindCityView) context;
        cities = new LinkedList<>();
    }

    @Override
    public void getCityCoordinates(String city) {
        if (ConnectionChecker.isConnected(context)) {

            if (!subscription.isUnsubscribed()) {
                subscription.unsubscribe();
            }

            findCityView.showLoading();
            subscription = new GoogleApiController()
                    .getApi()
                    .getCity(city, Constants.GOOGLE_PLACES_API_KEY)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<CityReport>() {
                        @Override
                        public void onCompleted() {
                            getSunriseSunset(cityReport.getResults().get(0)
                                    .getGeometry().getLocation().getLat(),
                                    cityReport.getResults().get(0)
                                            .getGeometry().getLocation().getLng());
                            //findCityView.closeLoading();
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            findCityView.closeLoading();
                            findCityView.showError("Server error");
                        }

                        @Override
                        public void onNext(CityReport report) {
                            cityReport = report;
                        }
                    });
        } else
            findCityView.showError("Network error");
    }

    private void getSunriseSunset(final double latitude, final double longitude){
        new SunriseSunsetApiController()
                .getApi()
                .getSunriseSunset(latitude, longitude, 0, "today")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SunriseSunsetReport>() {
                    @Override
                    public void onCompleted() {
                        cities.addFirst(new City(cityReport.getResults().get(0).getName(),
                                latitude,
                                longitude,
                                DateUtil.utcToLocal(sunriseSunsetReport.getResults().getSunrise()),
                                DateUtil.utcToLocal(sunriseSunsetReport.getResults().getSunset())));
                        findCityView.setCities(cities);
                        findCityView.closeLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        findCityView.closeLoading();
                        findCityView.showError("Server error");
                    }

                    @Override
                    public void onNext(SunriseSunsetReport report) {
                        sunriseSunsetReport = report;
                    }
                });
    }

    @Override
    public void unSubscribe() {
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
