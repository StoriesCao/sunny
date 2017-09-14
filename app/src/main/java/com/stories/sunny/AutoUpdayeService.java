package com.stories.sunny;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.stories.sunny.db_model.CityStoraged;
import com.stories.sunny.gson_model.Weather;
import com.stories.sunny.util.HttpUtil;
import com.stories.sunny.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AutoUpdayeService extends Service {

    private List<CityStoraged> mCityStoragedList = DataSupport.findAll(CityStoraged.class);
    String[] mWeatherId = new String[100];

    private int k = 0;

    public AutoUpdayeService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateWeather();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int updateTimeInterval = 5 * 60 * 60 * 1000; //ms
        long triggerAtTime = SystemClock.elapsedRealtime() + updateTimeInterval;
        Intent i = new Intent(this, AutoUpdayeService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

    private void updateWeather() {
        for (int i = 0; i < mCityStoragedList.size(); i++) {
            mWeatherId[i] = mCityStoragedList.get(i).getWeatherId();
        }
        while (mWeatherId[k] != null) {
            String url = "https://free-api.heweather.com/v5/weather?city=" + mWeatherId[k] + "&key=f34a94fd34384c72be8d8c45112c7bb5";
            HttpUtil.sendOkHttpRequest(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseText = response.body().string();
                    Weather weather = Utility.parseWeatherJson(responseText);
                    if (weather != null && "ok".equals(weather.status)) {
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdayeService.this).edit();
                        editor.putString(weather.basic.cityName + "weatherJSON", responseText);
                        editor.apply();
                    }
                }
            });

            k++;
        }
    }
}
