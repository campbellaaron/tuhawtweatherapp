package net.campbelldev.tuhawtweather;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private CurrentWeather mCurrentWeather;
    private DailyWeather mDailyWeather;

    @BindView(R.id.timeLabel) TextView mTimeLabel;
    @BindView(R.id.dateLabel) TextView mDateLabel;
    @BindView(R.id.temperatureLabel) TextView mTemperatureLabel;
    @BindView(R.id.minMaxTempLabel) TextView mMinMaxTempLabel;
    @BindView(R.id.minMaxTempValue) TextView mMinMaxTempValue;
    @BindView(R.id.windSpeedValue) TextView mWindSpeedValue;
    @BindView(R.id.precipValue) TextView mPrecipValue;
    @BindView(R.id.summaryLabel) TextView mSummaryLabel;
    @BindView(R.id.iconImageView) ImageView mIconImageView;
    @BindView(R.id.poweredBy) ImageView mPoweredBy;
    @BindView(R.id.refreshImageView) ImageView mRefreshImage;
    @BindView(R.id.progressBar) ProgressBar mProgressBar;
    @BindView(R.id.feelsLikeValue) TextView mFeelsLikeValue;
    private FusedLocationProviderClient mFusedLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mProgressBar.setVisibility(View.INVISIBLE);


        final double userLat = 38.4687;
        final double userLong = -82.5510;

        mRefreshImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getForecast(userLat, userLong);
            }
        });

        mPoweredBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://darksky.net/poweredby/")));
            }
        });

        getForecast(userLat, userLong);

    }

    private void getForecast(double userLat, double userLong) {
        final String API_KEY = "aefd56c69dcc8e1a7cb5a60336300ec0";

        String forecastLink = "https://api.darksky.net/forecast/" + API_KEY +
                "/" + userLat + "," + userLong;
        if (isNetworkUnavailable()) {
            toggleRefresh();

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(forecastLink).build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                    alertUserAboutNewError();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        if (response.isSuccessful()) {
                            mCurrentWeather = getCurrentDetails(jsonData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });
                        } else {
                            alertUserAboutNewError();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    } catch (JSONException jse) {
                        Log.e(TAG, "Exception caught: ", jse);
                    }
                }
            });
        } else {
            Toast.makeText(this, R.string.network_unavailable, Toast.LENGTH_LONG).show();
        }
    }

    private void toggleRefresh() {
        if (mProgressBar.getVisibility() == View.INVISIBLE) {
            mProgressBar.setVisibility(View.VISIBLE);
            mRefreshImage.setVisibility(View.INVISIBLE);
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
            mRefreshImage.setVisibility(View.VISIBLE);
        }
    }

    private void updateDisplay() {
        String temperature = mCurrentWeather.getTemp() + "";
        String feelsLike = mCurrentWeather.getFeelsLikeTemp() + "";
        String time = mCurrentWeather.getFormattedTime();
        String date = mCurrentWeather.getFormattedDate();
        String wind = mCurrentWeather.getWindSpeed() + " mph";
        String precip = mCurrentWeather.getPrecipChance() + "%";
        String summary = mCurrentWeather.getSummary();

        mTemperatureLabel.setText(temperature);
        mTimeLabel.setText(time);
        mDateLabel.setText(date);
        mWindSpeedValue.setText(wind);
        mFeelsLikeValue.setText(feelsLike);
        mPrecipValue.setText(precip);
        mSummaryLabel.setText(summary);
        Drawable drawable = getResources().getDrawable(mCurrentWeather.getIconID());
        mIconImageView.setImageDrawable(drawable);

    }

    private CurrentWeather getCurrentDetails(String jsonData) throws JSONException {
        CurrentWeather currentWeather = new CurrentWeather();
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");

        JSONObject currently = forecast.getJSONObject("currently"); //retrieve a JSON object from a JSON object
        JSONObject daily = forecast.getJSONObject("daily");
        JSONArray dailyData = daily.getJSONArray("data");
        Log.v(TAG, dailyData.toString());

        currentWeather.setTime(currently.getLong("time"));
        currentWeather.setIcon(currently.getString("icon"));
        currentWeather.setPrecipChance(currently.getDouble("precipProbability"));
        currentWeather.setSummary(currently.getString("summary"));
        currentWeather.setTemp(currently.getDouble("temperature"));
        currentWeather.setFeelsLikeTemp(currently.getDouble("apparentTemperature"));
        currentWeather.setWindSpeed(currently.getDouble("windSpeed"));
        currentWeather.setTimezone(timezone);

        Log.d(TAG, currentWeather.getFormattedTime());
        Log.d(TAG, currentWeather.getFormattedDate());

        return currentWeather;
    }

    private boolean isNetworkUnavailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (netInfo != null && netInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }

    private void alertUserAboutNewError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }
}