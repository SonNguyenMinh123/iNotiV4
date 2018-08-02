package com.notification.notyos10.weather.objects;

import com.notification.notyos10.R;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class FutureWeather {

    private long mTime;
    private String mSummary;
    private String mIcon;
    private String mIconUrl;
    private String mPrecipProbability;
    private int mLowTemperature;
    private int mHighTemperature;
    private double mHumidity;
    private String mTimezone;


    public String getTimezone() {
        return mTimezone;
    }

    public void setTimezone(String timezone) {
        mTimezone = timezone;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public String getIconUrl() {
        return mIconUrl;
    }

    public void setIconUrl(String iconUrl) {
        mIconUrl = iconUrl;
    }

    public int getIconId() {
        // ic_clear-day, ic_clear-night, rain, snow, sleet, wind, fog, cloudy, partly-cloudy-day, or partly-cloudy-night
        int iconId = R.drawable.ic_dunno;
        switch (mIcon) {
            case "ic_clear-day":
            case "ic_clear":
            case "sunny":
            case "mostlysunny":
            case "ic_clear-night":
            case "nt_clear":
                iconId = R.drawable.ic_sunnyt;
                break;
            case "rain":
            case "flurries":
                iconId = R.drawable.ic_shower2;
                break;
            case "chancerain":
            case "chanceflurries":
                iconId = R.drawable.ic_;
                break;
            case "snow":
                iconId = R.drawable.ic_snow5;
                break;
            case "chancesnow":
                iconId = R.drawable.ic_snow1;
                break;
            case "sleet":
            case "chancesleet":
            case "nt_sleet":
                iconId = R.drawable.ic_sleett;
                break;
            case "wind":
                iconId = R.drawable.wind;
                break;
            case "fog":
            case "nt_fog":
            case "hazy":
                iconId = R.drawable.ic_fogt;
                break;
            case "cloudy":
            case "nt_cloudy":
                iconId = R.drawable.ic_cloudy3;
                break;
            case "mostlycloudy":
            case "nt_mostlycloudy":
                iconId = R.drawable.ic_cloudy4;
                break;
            case "partly-cloudy-day":
            case "partlycloudy":
            case "partly-cloudy-night":
            case "nt_partlycloudy":
                iconId = R.drawable.ic_cloudy1;
                break;
            case "hail":
                iconId = R.drawable.hail;
                break;
            case "thunderstorm":
            case "tstorms":
            case "chancetstorms":
                iconId = R.drawable.ic_tstorm3;
                break;
            case "tornado":
                iconId = R.drawable.wind;
                break;
        }
        return iconId;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public String getPrecipProbability() {
        return mPrecipProbability;
    }

    public void setPrecipProbability(String chanceRain) {
        mPrecipProbability = chanceRain;
    }

    public double getHumidity() {
        return mHumidity;
    }

    public void setHumidity(double humidity) {
        mHumidity = humidity;
    }

    public int getHighTemperature() {
        return mHighTemperature;
    }

    public void setHighTemperature(double temperature) {
        mHighTemperature = (int) Math.round(temperature);
    }

    public int getLowTemperature() {
        return mLowTemperature;
    }

    public void setLowTemperature(double temperature) {
        mLowTemperature = (int) Math.round(temperature);
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public String getFormattedTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE LLL d,y @ h:mm a");
        formatter.setTimeZone(TimeZone.getTimeZone(getTimezone()));
        Date dateTime = new Date(getTime() * 1000);
        return formatter.format(dateTime);
    }

    public String getDay() {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE");
        formatter.setTimeZone(TimeZone.getTimeZone(getTimezone()));
        Date dateTime = new Date(getTime() * 1000);
        return formatter.format(dateTime);
    }

    public String getHour() {
        SimpleDateFormat formatter = new SimpleDateFormat("H");
        formatter.setTimeZone(TimeZone.getTimeZone(getTimezone()));
        Date dateTime = new Date(getTime() * 1000);
        return formatter.format(dateTime);
    }
}
