package com.destro13.sunrisesunsetapp.mvp.view;

import com.destro13.sunrisesunsetapp.mvp.model.City;

import java.util.LinkedList;

public interface FindCityView extends BaseView {
    void setCities(LinkedList<City> cities);
}
