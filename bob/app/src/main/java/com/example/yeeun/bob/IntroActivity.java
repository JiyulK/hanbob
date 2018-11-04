package com.example.yeeun.bob;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class IntroActivity extends AppCompatActivity {

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_intro);
        handler = new Handler();
        //3초 동안 인트로 화면
        handler.postDelayed(rIntent, 3000);
    }
    Runnable rIntent = new Runnable() {
        @Override
        public void run() {
            Intent Main = new Intent(IntroActivity.this, MenuActivity.class);
            startActivity(Main);

            //fade in -> fade out
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handler.removeCallbacks(rIntent);
    }
}
