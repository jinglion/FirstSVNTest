package com.example.jinglion.firsttest.Service;

import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jinglion on 2015-8-18.
 */
public class TimeService extends Service {
    private String currentTime;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("TimeService", "Bind-onCreate-TimeService");
    }

    public class MyBinder extends Binder {
        public TimeService getService(){
            return TimeService.this;
        }
    }

//    private final IBinder binder = new MyBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("TimeService", "Bind-onBind-TimeService");
        return new MyBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void unbindService(ServiceConnection conn) {
        Log.i("TimeService", "Bind-unbindService-TimeService");
        super.unbindService(conn);
    }

    @Override
    public void onDestroy() {
        Log.i("TimeService", "Bind-onDestroy-TimeService");
        super.onDestroy();
    }

    public String getSystemCurrentTime(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        currentTime = df.format(new Date());
        System.out.println(currentTime);// new Date()为获取当前系统时间
        return currentTime;
    }
}
