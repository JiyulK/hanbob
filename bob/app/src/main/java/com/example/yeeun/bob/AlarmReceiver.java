package com.example.yeeun.bob;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by YeEun on 2018-05-30.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent mServiceintent = new Intent(context, AlarmSoundService.class);
        context.startService(mServiceintent);
    }
}