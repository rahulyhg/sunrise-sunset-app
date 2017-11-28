package com.destro13.sunrisesunsetapp.mvp.presenter;

import android.content.Context;

import com.destro13.sunrisesunsetapp.api.GoogleApiController;
import com.destro13.sunrisesunsetapp.api.SunriseSunsetApiController;
import com.destro13.sunrisesunsetapp.mvp.model.City;
import com.destro13.sunrisesunsetapp.mvp.model.googleplaces.CityReport;
import com.destro13.sunrisesunsetapp.mvp.model.googleplaces.TimeZoneReport;
import com.destro13.sunrisesunsetapp.mvp.model.sunrisesunset.SunriseSunsetReport;
import com.destro13.sunrisesunsetapp.mvp.view.FindCityView;
import com.destro13.sunrisesunsetapp.util.ConnectionChecker;
import com.destro13.sunrisesunsetapp.util.Constants;
import com.destro13.sunrisesunsetapp.util.DateUtil;

import java.util.Calendar;
import java.util.LinkedList;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

import static com.destro13.sunrisesunsetapp.util.Constants.GOOGLE_PLACES_API_KEY;

public class FindCityPresenterImpl implements FindCityPresenter {
    private FindCityView findCityView;
    private Context context;
    private Subscription subscription = Subscriptions.empty();
    private CityReport cityReport;
    private SunriseSunsetReport sunriseSunsetReport;
    private TimeZoneReport timeZoneReport;
    private LinkedList<City> cities;

    public FindCityPresenterImpl(Context context) {
        this.context = context;
        this.findCityView = (FindCityView) context;
        cities = new LinkedList<>();
    }

    @Override
    public void getCityCoordinates(final String city) {
        if (ConnectionChecker.isConnected(context)) {

            if (!subscription.isUnsubscribed()) {
                subscription.unsubscribe();
            }

            findCityView.showLoading();
            subscription = new GoogleApiController()
                    .getApi()
                    .getCity(city, GOOGLE_PLACES_API_KEY)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<CityReport>() {
                        @Override
                        public void onCompleted() {
                            if(cityReport.getResults().size() != 0){
                                getTimeZone(cityReport.getResults().get(0)
                                                .getGeometry().getLocation().getLat(),
                                        cityReport.getResults().get(0)
                                                .getGeometry().getLocation().getLng());
                            }
                            else{
                                findCityView.showError("Wrong search request!");
                                findCityView.closeLoading();
                            }
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

    private void getSunriseSunset(final double latitude, final double longitude, final String timeZoneId){
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
                                DateUtil.utcToLocal(sunriseSunsetReport.getResults().getSunrise(), timeZoneId),
                                DateUtil.utcToLocal(sunriseSunsetReport.getResults().getSunset(), timeZoneId), timeZoneId));
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

    private void getTimeZone(final double latitude, final double longitude){
        Calendar calendar = Calendar.getInstance();
        long time = calendar.getTime().getTime() / 1000;
        new GoogleApiController()
                .getApi()
                .getTimeZone(String.valueOf(latitude) + " , " + String.valueOf(longitude),
                        time, Constants.GOOGLE_PLACES_API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TimeZoneReport>() {
                    @Override
                    public void onCompleted() {
                        getSunriseSunset(cityReport.getResults().get(0)
                                        .getGeometry().getLocation().getLat(),
                                cityReport.getResults().get(0)
                                        .getGeometry().getLocation().getLng(),
                                timeZoneReport.getTimeZoneId());
                        //findCityView.closeLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        findCityView.closeLoading();
                        findCityView.showError("Server error");
                    }

                    @Override
                    public void onNext(TimeZoneReport report) {
                        timeZoneReport = report;
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
