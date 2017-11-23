package com.destro13.sunrisesunsetapp.mvp.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.destro13.sunrisesunsetapp.R;
import com.destro13.sunrisesunsetapp.adapter.CitiesAdapter;
import com.destro13.sunrisesunsetapp.mvp.model.City;
import com.destro13.sunrisesunsetapp.mvp.presenter.FindCityPresenter;
import com.destro13.sunrisesunsetapp.mvp.presenter.FindCityPresenterImpl;

import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FindCityActivity extends AppCompatActivity implements FindCityView {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.city_searchView)
    SearchView searchView;

    MaterialDialog loadingMaterialDialog;

    private LinearLayoutManager linearLayoutManager;
    private CitiesAdapter citiesAdapter;
    private FindCityPresenter findCityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_city);

        ButterKnife.bind(this);
        initRecyclerView();

        findCityPresenter = new FindCityPresenterImpl(this);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                findCityPresenter.getCityCoordinates(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void initRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        citiesAdapter = new CitiesAdapter(FindCityActivity.this);
        recyclerView.setAdapter(citiesAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (findCityPresenter != null) {
            findCityPresenter.unSubscribe();
        }
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
    public void setCities(LinkedList<City> cities) {
        citiesAdapter.setCities(cities);
    }
}
