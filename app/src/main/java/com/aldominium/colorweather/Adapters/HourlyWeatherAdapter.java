package com.aldominium.colorweather.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aldominium.colorweather.Models.Hour;
import com.aldominium.colorweather.R;

import java.util.ArrayList;

public class HourlyWeatherAdapter extends BaseAdapter {

    Context context;
    ArrayList<Hour> hours;

    public HourlyWeatherAdapter(Context context, ArrayList<Hour> hours){
        this.context = context;
        this.hours = hours;
    }

    @Override
    public int getCount() {
        if (hours==null)
            return 0;
        return hours.size();
    }

    @Override
    public Object getItem(int position) {
        return hours.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0; //We dont use this
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder;
        Hour hour = hours.get(position);

        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.hourly_list_item, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.title = view.findViewById(R.id.hourlyTitleTextView);
            viewHolder.description = view.findViewById(R.id.hourlyDescriptionTextView);
            view.setTag(viewHolder);
        }  else{
           viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.title.setText(hour.getTitle());
        viewHolder.description.setText(hour.getWeatherDescription());

        return view;
    }

    static  class ViewHolder{
        TextView title;
        TextView description;
    }
}
