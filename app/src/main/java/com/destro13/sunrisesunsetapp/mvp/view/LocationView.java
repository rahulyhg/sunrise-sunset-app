package com.destro13.sunrisesunsetapp.mvp.view;

import com.destro13.sunrisesunsetapp.mvp.model.SunriseSunsetReport;

public interface LocationView {
    void showLoading();
    void closeLoading();
    void showError(String s);
    void setData(SunriseSunsetReport sunriseSunsetReport);
}
