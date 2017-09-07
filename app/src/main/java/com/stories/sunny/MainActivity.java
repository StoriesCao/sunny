package com.stories.sunny;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* ****** */
        Button cityManagerButton = (Button) findViewById(R.id.place_manager);
        cityManagerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CityManagerActivity.class);
                startActivity(intent);
            }
        });

        TextView AQI = (TextView) findViewById(R.id.forecast_now_air_quality);
        AQI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AQIActivity.class);
                startActivity(intent);
            }
        });

    }
}
