package com.destro13.sunrisesunsetapp.mvp.presenter;

import android.content.Intent;

public interface LocationPresenter extends BasePresenter {
    void showSunriseSunset(double latitude, double longitude);

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void onResume();

    void onPause();

    void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults);
}
