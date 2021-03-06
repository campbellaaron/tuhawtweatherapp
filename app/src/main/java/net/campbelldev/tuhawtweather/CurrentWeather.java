package net.campbelldev.tuhawtweather;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by aaroncampbell on 6/9/17.
 */

public class CurrentWeather {
    private String mIcon;
    private long mTime;
    private double mTemp;
    private double mWindSpeed;
    private double mPrecipChance;
    private double mFeelsLikeTemp;
    private String mSummary;
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

    public int getIconID(){
        //clear-day, clear-night, rain, snow, sleet, wind, fog, cloudy, partly-cloudy-day, or partly-cloudy-night
        int iconID = R.drawable.clear_day;
        if (mIcon.equals("clear-day")) {
            iconID = R.drawable.clear_day;
        } else if (mIcon.equals("clear-night")) {
            iconID = R.drawable.clear_night;
        } else if (mIcon.equals("rain")) {
            iconID = R.drawable.rain;
        } else if (mIcon.equals("snow")) {
            iconID = R.drawable.snow;
        } else if (mIcon.equals("sleet")) {
            iconID = R.drawable.sleet;
        } else if (mIcon.equals("wind")) {
            iconID = R.drawable.wind;
        } else if (mIcon.equals("fog")) {
            iconID = R.drawable.fog;
        } else if (mIcon.equals("cloudy")) {
            iconID = R.drawable.cloudy;
        } else if (mIcon.equals("partly-cloudy-day")) {
            iconID = R.drawable.partly_cloudy;
        } else if (mIcon.equals("partly-cloudy-night")) {
            iconID = R.drawable.cloudy_night;
        }
        return iconID;
    }

    public long getTime() {
        return mTime;
    }

    public String getFormattedTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
        formatter.setTimeZone(TimeZone.getTimeZone(getTimezone()));
        Date dateTime = new Date(getTime()*1000);
        String timeString = formatter.format(dateTime);

        return timeString;
    }

    public String getFormattedDate() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM dd, yyyy");
        dateFormatter.setTimeZone(TimeZone.getTimeZone(getTimezone()));
        Date dateTime = new Date(getTime()*1000);
        String dateString = dateFormatter.format(dateTime);

        return dateString;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public int getTemp() {
        return (int)Math.round(mTemp);
    }

    public int getFeelsLikeTemp() {
        return (int) Math.round(mFeelsLikeTemp);
    }

    public void setFeelsLikeTemp(double feelsLikeTemp) {
        mFeelsLikeTemp = feelsLikeTemp;
    }

    public int getWindSpeed() {
        return (int) Math.round(mWindSpeed);
    }

    public void setWindSpeed(double windSpeed) {
        mWindSpeed = windSpeed;
    }

    public void setTemp(double temp) {
        mTemp = temp;
    }

    public int getPrecipChance() {
        double precipPercentage = mPrecipChance * 100;
        return (int)Math.round(precipPercentage);
    }

    public void setPrecipChance(double precipChance) {
        mPrecipChance = precipChance;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }
}
