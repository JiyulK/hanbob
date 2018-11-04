package com.example.yeeun.bob;

import android.app.Application;

import com.bulong.rudeness.RudenessScreenHelper;

public class DisplayFunction extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        new RudenessScreenHelper(this,768).activate();
    }
}
