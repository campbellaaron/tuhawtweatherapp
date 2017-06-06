package net.campbelldev.tuhawtweather;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String API_KEY = "aefd56c69dcc8e1a7cb5a60336300ec0";
        double userLat = 37.8267;
        double userLong = -122.4233;

        String forecastLink = "https://api.darksky.net/forecast/" + API_KEY +
                "/" + userLat + "," + userLong;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(forecastLink).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    Log.v(TAG, response.body().string());
                    if (response.isSuccessful()) {
                    } else {
                        alertUserAboutNewError();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Exception caught: ", e);
                }
            }
        });
        Log.d(TAG, "Main UI code is running!");

    }

    private void alertUserAboutNewError() {
        
    }
}