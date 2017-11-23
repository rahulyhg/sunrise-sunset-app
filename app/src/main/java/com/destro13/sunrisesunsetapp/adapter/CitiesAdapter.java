package com.destro13.sunrisesunsetapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.destro13.sunrisesunsetapp.R;
import com.destro13.sunrisesunsetapp.mvp.model.City;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.CityHolder> {
    private Context context;
    List<City> cities = Collections.emptyList();


    public CitiesAdapter(Context context) {
        this.context = context;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
        notifyDataSetChanged();
    }


    @Override
    public CityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.city_item, parent, false);
        return new CityHolder(view);
    }

    @Override
    public void onBindViewHolder(CityHolder holder, int position) {
        final City city = cities.get(position);
        holder.city.setText(city.getName());
        holder.longitude.setText(Double.toString(city.getLongitude()));
        holder.latitude.setText(Double.toString(city.getLatitude()));
        holder.sunrise.setText(city.getSunrise());
        holder.sunset.setText(city.getSunset());
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    class CityHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.city_textView)
        TextView city;
        @BindView(R.id.latitude_textView)
        TextView latitude;
        @BindView(R.id.longitude_textView)
        TextView longitude;
        @BindView(R.id.sunrise_textView)
        TextView sunrise;
        @BindView(R.id.sunset_textView)
        TextView sunset;

        public CityHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
