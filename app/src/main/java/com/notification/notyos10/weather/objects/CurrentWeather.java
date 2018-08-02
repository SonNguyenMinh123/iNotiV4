package com.notification.notyos10.weather.objects;

import com.notification.notyos10.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class CurrentWeather {

    private String mIcon;
    private long mTime;
    private double mTemperature;
    private String mHumidity;
    private String mPrecip;
    private String mSummary;
    private String mTimezone;
    private String mIconUrl;


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

        try {
            switch (mIcon) {
                case "ic_clear-day":
                case "ic_clear":
                case "sunny":
                case "mostlysunny":
                    iconId = R.drawable.ic_sunnyt;
                    break;
                case "ic_clear-night":
                case "nt_clear":
                    iconId = R.drawable.ic_sunny_night;
                    break;
                case "rain":
                    iconId = R.drawable.ic_shower2;
                    break;
                case "chancerain":
                case "chanceflurries":
                case "drizzle":
                    iconId = R.drawable.ic_;
                    break;
                case "snow":
                case "flurries":
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
                case "hazy":
                    iconId = R.drawable.ic_fogt;
                    break;
                case "nt_fog":
                    iconId = R.drawable.ic_fog_night;
                    break;
                case "cloudy":
                    iconId = R.drawable.ic_cloudy3;
                    break;
                case "nt_cloudy":
                    iconId = R.drawable.ic_cloudy3_night;
                    break;
                case "mostlycloudy":
                    iconId = R.drawable.ic_cloudy4;
                    break;
                case "nt_mostlycloudy":
                    iconId = R.drawable.ic_cloudy4_night;
                    break;
                case "partly-cloudy-day":
                case "partlycloudy":
                    iconId = R.drawable.ic_cloudy1;
                    break;
                case "partly-cloudy-night":
                case "nt_partlycloudy":
                    iconId = R.drawable.ic_cloudy1_night;
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iconId;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public String getPrecip() {
        return mPrecip;
    }

    public void setPrecip(String precip) {
        mPrecip = precip;
    }

    public String getHumidity() {
        return mHumidity;
    }

    public void setHumidity(String humidity) {
        mHumidity = humidity;
    }

    public int getTemperature() {
        return (int) Math.round(mTemperature);
    }

    public void setTemperature(double temperature) {
        mTemperature = temperature;
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
}
