package com.example.prantice;

import android.content.Context;
import android.widget.ListView;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ParsingJson {
    private Context context;

    public ParsingJson(Context context) {
        this.context = context;
    }
    @NonNull
    public ArrayList<Weathers> onParsingJson(String json) throws JSONException {
        ArrayList<Weathers> weathers = new ArrayList<>();
        JSONArray array = new JSONArray(json);
        for (int i = 0; i < array.length(); i++) {
            Weathers weather = new Weathers();
            JSONObject object = array.getJSONObject(i);
            String country = object.getString((String) object.names().get(0));
            String flag = object.getString((String) object.names().get(1));
            String weath = object.getString((String) object.names().get(2));
            String temperature = object.getString((String) object.names().get(3));
            weather.setCountry(country);
            weather.setFlag(flag);
            weather.setWeather(weath);
            weather.setTemperature(temperature);
            weathers.add(weather);
        }
        return weathers;
    }
}
