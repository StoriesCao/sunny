package com.stories.sunny;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by Charlottecao on 10/14/17.
 */

public class WelcomeActivity extends AppCompatActivity {

    private static final int GO_MAIN = 1;

    private static final int TIME = 2000;

    private ImageView mImageView;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == GO_MAIN) {
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                finish();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        setContentView(R.layout.activity_welcome);

        mImageView = (ImageView) findViewById(R.id.welcome_image_view);
        Glide.with(WelcomeActivity.this).load(R.drawable.ic_goodmorning).into(mImageView);

        mHandler.sendEmptyMessageDelayed(GO_MAIN, TIME);
    }


}
