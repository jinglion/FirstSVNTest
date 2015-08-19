package com.example.jinglion.firsttest.Broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.jinglion.firsttest.MainActivity;

/**
 * Created by jinglion on 2015-8-18.
 */
public class BootBroadcastReceiver extends BroadcastReceiver{
    private static final String ACTION = "android.intent.action.BOOT_COMPLETED";
    private SharedPreferences sp;
    @Override
    public void onReceive(Context context, Intent intent) {
        sp = context.getSharedPreferences("config.txt", Context.MODE_PRIVATE);
        if (sp.getBoolean("isAutoOpenAPP", false)){ //设置是否自动开启APP
            if (intent.getAction().equals(ACTION)) {
                Intent mainBootActivity = new Intent(context, MainActivity.class);
                mainBootActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(mainBootActivity);
            }
        }
    }

}
