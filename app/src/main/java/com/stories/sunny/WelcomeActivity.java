package com.stories.sunny;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.stories.sunny.util.HttpUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

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
        setContentView(R.layout.activity_welcome);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        mImageView = (ImageView) findViewById(R.id.welcome_image_view);
        SharedPreferences preferences = getSharedPreferences("BingPic", MODE_PRIVATE);
        String realAddress = preferences.getString("real_bing_pic_address", "");
        if (realAddress.isEmpty()) {
            requestHomePic();
        } else {
            Glide.with(WelcomeActivity.this).load(realAddress).into(mImageView);
        }

        mHandler.sendEmptyMessageDelayed(GO_MAIN, TIME);
    }

    /**
     * 第一次没有缓存的时候调用
     */
    public void requestHomePic() {
        String picUrl = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(picUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String realBingPicAddress = response.body().string();
                SharedPreferences.Editor editor = getSharedPreferences("BingPic", MODE_PRIVATE).edit();
                editor.putString("real_bing_pic_address", realBingPicAddress);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WelcomeActivity.this).load(realBingPicAddress).into(mImageView);
                    }
                });
            }
        });
    }



}
