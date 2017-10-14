package com.stories.sunny;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Locale;

/**
 * Created by Charlottecao on 9/14/17.
 */

public class SettingActivity extends AppCompatActivity {

    private Toolbar mSettingToolbar;

    private Switch mSwitchLanguageSwitch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mSettingToolbar = (Toolbar) findViewById(R.id.setting_toolbar);
        setSupportActionBar(mSettingToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_button);
        }

        mSwitchLanguageSwitch = (Switch) findViewById(R.id.settings_switch_language);
        mSwitchLanguageSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Resources resources = getResources();
                    DisplayMetrics displayMetrics = resources.getDisplayMetrics();
                    Configuration config = resources.getConfiguration();
                    // 应用用户选择语言
                    config.setLocale(Locale.CHINESE);
                    createConfigurationContext(config);

                    BaseActivity.finishAllActivity();
                    Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                    //开始新的activity同时移除之前所有的activity
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
