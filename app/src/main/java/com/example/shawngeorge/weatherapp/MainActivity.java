package com.example.shawngeorge.weatherapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import data.WeatherPreferences;
import utilities.NetworkUtils;
import utilities.OpenWeatherJsonUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView weatherDisplay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /** References TextView from XML*/
        weatherDisplay = (TextView) findViewById(R.id.tv_weather_data);

        loadWeatherData();
    }

    private void loadWeatherData()
    {
        String location;
        location = WeatherPreferences.getPreferredWeatherLocation(this);
        new FetchWeatherTask().execute(location);
    }

    private class FetchWeatherTask extends AsyncTask<String, Void, String[]> {
        @Override
        protected String[] doInBackground(String... params) {
            if(params.length == 0)
            {
                return null;
            }
            String location = params[0];
            URL weatherURL = NetworkUtils.buildURL(location);

            try
            {
                String jsonWeatherResponse = NetworkUtils.getResponseFromHttpUrl(weatherURL);
                String[] simpleWeatherData = OpenWeatherJsonUtils.getSimpleWeatherStringsFromJson(MainActivity.this, jsonWeatherResponse);

                return simpleWeatherData;
            } catch(Exception e)
            {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] weatherData)
        {
            if(weatherData != null)
            {
                for(String data:weatherData)
                {
                    weatherDisplay.append(data + "\n\n\n");
                }
            }
        }



    }
}
