package com.destro13.sunrisesunsetapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.destro13.sunrisesunsetapp.mvp.model.sunrisesunset.SunriseSunsetReport;
import com.destro13.sunrisesunsetapp.mvp.presenter.LocationPresenter;
import com.destro13.sunrisesunsetapp.mvp.presenter.LocationPresenterImpl;
import com.destro13.sunrisesunsetapp.mvp.view.FindCityActivity;
import com.destro13.sunrisesunsetapp.mvp.view.LocationView;
import com.destro13.sunrisesunsetapp.util.DateUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LocationActivity extends AppCompatActivity implements LocationView {
    @BindView(R.id.longitude_textView)
    TextView longitudeTextView;
    @BindView(R.id.latitude_textView)
    TextView latitudeTextView;
    @BindView(R.id.sunrise_textView)
    TextView sunriseTextView;
    @BindView(R.id.sunset_textView)
    TextView sunsetTextView;
    @BindView(R.id.another_place_button)
    Button anotherPlaceButton;

    MaterialDialog loadingMaterialDialog;

    private static final String TAG = LocationActivity.class.getSimpleName();

    //private LocationManager locationManager;
    private LocationPresenter locationPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        locationPresenter = new LocationPresenterImpl(this);
        //locationManager = new LocationManager(this);
      //  locationManager.init();

    }

    public void updateCoordinates(double longitude, double latitude){
        latitudeTextView.setText(Double.toString(latitude));
        longitudeTextView.setText(Double.toString(longitude));
        locationPresenter.showSunriseSunset(latitude,longitude);
    }

    @OnClick(R.id.another_place_button)
    public void goToFindCityActivity(){
        startActivity(new Intent(this, FindCityActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        locationPresenter.onActivityResult(requestCode,resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
        locationPresenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationPresenter.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (locationPresenter != null) {
            locationPresenter.unSubscribe();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        locationPresenter.onRequestPermissionResult(requestCode, permissions,grantResults);
    }

    @Override
    public void showLoading() {
        loadingMaterialDialog = new MaterialDialog.Builder(this)
                .title("Loading")
                .content("Please,wait")
                .progress(true, 0)
                .show();
    }

    @Override
    public void closeLoading() {
        loadingMaterialDialog.dismiss();
    }

    @Override
    public void showError(String s) {
        new MaterialDialog.Builder(this)
                .title("Error")
                .content(s)
                .positiveText("Oк")
                .show();
    }

    @Override
    public void setData(SunriseSunsetReport sunriseSunsetReport) {
        sunriseTextView.setText(DateUtil.utcToLocal(sunriseSunsetReport.getResults().getSunrise()));
        sunsetTextView.setText(DateUtil.utcToLocal(sunriseSunsetReport.getResults().getSunset()));
    }
}