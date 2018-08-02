package com.notification.notyos10.weather;

import android.location.Location;
import android.util.Log;


import com.notification.notyos10.weather.objects.CurrentWeather;
import com.notification.notyos10.weather.objects.FutureWeather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



public class ForecastUtil {
    private static final String TAG = ForecastUtil.class.getSimpleName();
    private static final String FORECASTIO_API_KEY = "3aa9bf008420ee1373c08b3273c027d6";// ToDo enter API key
    private static final String FORECASTIO_URL = "https://api.forecast.io/forecast/";
    private List<FutureWeather> mTotalWeather;
    private CurrentWeather mCurrentWeather;

    public ForecastUtil() {
        mCurrentWeather = new CurrentWeather();

    }

    public String getForecastURL(Location location) {
        return FORECASTIO_URL + FORECASTIO_API_KEY + "/" + location.getLatitude() + "," + location.getLongitude();
    }

    public String getForecastURL(double latitude, double longitude) {
        return FORECASTIO_URL + FORECASTIO_API_KEY + "/" + latitude + "," + longitude;
    }


    public List<FutureWeather> getTotalWeather() {
        return mTotalWeather;
    }

    public void setCurrentWeatherData(String jsonData) throws JSONException {
        CurrentWeather currentWeather = new CurrentWeather();
        JSONObject weather = new JSONObject(jsonData);
        JSONObject currently = weather.getJSONObject("currently");
        JSONObject daily = weather.getJSONObject("daily");
        JSONArray data = daily.getJSONArray("data");
        JSONObject day = data.getJSONObject(0);


        currentWeather.setHumidity((int) Math.round(currently.getDouble("humidity") * 100) + "%");
        currentWeather.setTime(currently.getLong("time"));
        currentWeather.setIcon(currently.getString("icon"));
        currentWeather.setPrecip((int) Math.round(currently.getDouble("precipProbability") * 100) + "%");
        currentWeather.setSummary(day.getString("summary"));
        currentWeather.setTemperature(currently.getDouble("temperature"));
        currentWeather.setTimezone(weather.getString("timezone"));

        mCurrentWeather = currentWeather;
    }

    public void setFutureWeatherData(String jsonData) throws JSONException {

        JSONObject weather = new JSONObject(jsonData);
        JSONObject daily = weather.getJSONObject("daily");
        JSONArray data = daily.getJSONArray("data");
        JSONObject day;


        mTotalWeather = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            FutureWeather futureWeather = new FutureWeather();
            futureWeather.setTimezone(weather.getString("timezone"));
            day = data.getJSONObject(i);
            futureWeather.setHumidity(day.getDouble("humidity"));
            futureWeather.setTime(day.getLong("time"));
            futureWeather.setIcon(day.getString("icon"));
            futureWeather.setPrecipProbability("" + Math.round((day.getInt("precipProbability") * 100)));
            futureWeather.setSummary(day.getString("summary"));
            futureWeather.setHighTemperature(day.getDouble("temperatureMax"));
            futureWeather.setLowTemperature(day.getDouble("temperatureMin"));
            mTotalWeather.add(futureWeather);
        }
    }

    public CurrentWeather getCurrentWeather() {
        return mCurrentWeather;
    }


}
