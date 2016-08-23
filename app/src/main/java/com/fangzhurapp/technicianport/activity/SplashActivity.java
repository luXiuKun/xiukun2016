package com.fangzhurapp.technicianport.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fangzhurapp.technicianport.R;
import com.fangzhurapp.technicianport.utils.SpUtil;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        init();
    }

    private void init() {

        Timer timer = new Timer(2000, 1000);
        timer.start();

    }


    class Timer extends CountDownTimer{



        public Timer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            if (SpUtil.getBoolean(SplashActivity.this,"firstlogin",true)){


                Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();
            }else {

                Intent intent = new Intent(SplashActivity.this, SelectIdent.class);
                startActivity(intent);
                SplashActivity.this.finish();
            }
        }
    }
}
