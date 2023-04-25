package com.example.prantice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class CustomAdapter extends ArrayAdapter<Weathers> {
    private Context context;
    private int type;
    private boolean tg;
    private ArrayList<Weathers> weathers;

    public CustomAdapter(@NonNull Context context, ArrayList<Weathers> weathers, int type, boolean tg) {
        super(context, R.layout.item, weathers);
        this.context = context;
        this.weathers = weathers;
        this.type = type;
        this.tg = tg;
    }
    public Weathers getItem(int position) {
        return weathers.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        Weathers weather = getItem(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.item, null);
        holder.imageView = convertView.findViewById(R.id.image);
        holder.count = convertView.findViewById(R.id.count);
        holder.temp = convertView.findViewById(R.id.temp);
        holder.imageView2 = convertView.findViewById(R.id.image2);
        ImageAsyncTask asyncTask = new ImageAsyncTask(context);
        holder.count.setText(weather.getCountry());
        holder.temp.setText(weather.getTemperature() + "'C");
        if(tg) {
            Glide.with(context)
                    .load(weather.getFlag())
                    .into(holder.imageView);
            Toast.makeText(context, "글라이드 사용중", Toast.LENGTH_SHORT).show();
        } else {
            if (type == 1) {
                try {
                    holder.imageView.setImageBitmap(asyncTask.execute(weather.getFlag()).get());
                } catch (ExecutionException | InterruptedException e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                ImageFastNetwork fastNetwork = new ImageFastNetwork(context);
                fastNetwork.request(weather.getFlag(), holder.imageView);
            }
        }
        if (weather.getWeather().equals("맑음")){
            holder.imageView2.setImageResource(R.drawable.nb01);
        } else if (weather.getWeather().equals("비")) {
            holder.imageView2.setImageResource(R.drawable.nb08);
        } else if (weather.getWeather().equals("흐림")) {
            holder.imageView2.setImageResource(R.drawable.nb04);
        } else if (weather.getWeather().equals("우박")) {
            holder.imageView2.setImageResource(R.drawable.nb07);
        } else {
            holder.imageView2.setImageResource(R.drawable.nb11);
        }
        return convertView;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView count;
        TextView temp;
        ImageView imageView2;
    }
}
