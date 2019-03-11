package com.arcadio.arcmonitize;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import arcmonitize.Settings;
import arcmonitize.ads.AdControllPanel;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final AdControllPanel adControllPanel = new AdControllPanel(this);
        adControllPanel.setAdsInterval(1);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                adControllPanel.showOnAdLoadIfReward(null, true);
            }
        });
    }

}
