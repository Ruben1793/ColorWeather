package com.aldominium.colorweather.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aldominium.colorweather.Minute;
import com.aldominium.colorweather.R;

import java.util.ArrayList;

public class MinutelyWeatherAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Minute> minutes;

    public MinutelyWeatherAdapter(Context context,ArrayList<Minute> minutes ){
        this.context = context;
        this.minutes = minutes;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.minutely_list_item, parent, false);
        MinuteVieHolder minuteVieHolder = new MinuteVieHolder(view);
        return minuteVieHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((MinuteVieHolder)holder).onBind(position);
    }

    @Override
    public int getItemCount() {
        if (minutes == null){
            return 0;
        }
        return minutes.size();
    }

    class MinuteVieHolder extends RecyclerView.ViewHolder{

        TextView titleTextView;
        TextView rainProbabilityTextView;

        public MinuteVieHolder(View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.minutelyTitleTextView);
            rainProbabilityTextView = itemView.findViewById(R.id.minutelyRainProbabilityTextView);
        }

        public void onBind(int position){
            Minute minute = minutes.get(position);
            titleTextView.setText(minute.getTitle());
            rainProbabilityTextView.setText(minute.getRainProbability());
        }
    }
}
