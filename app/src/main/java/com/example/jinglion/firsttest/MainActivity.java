package com.example.jinglion.firsttest;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.jinglion.firsttest.Service.TimeService;
import com.example.jinglion.firsttest.Service.TimeService.MyBinder;

public class MainActivity extends AppCompatActivity {
    private TextView mCurTime;
    private Button mOpenOrNot;
    private Intent timeSerIntent;     //跳转至服务意图
    private TimeService timeService;  //系统后台服务
    private String currTime;  //系统当前时间
    private boolean isOpenAPP = false;//判断APP是否正在打开
    private boolean curSettingIsAutoOpen = false;  //判断当前设置
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private ServiceConnection mTimeConn = new ServiceConnection() {
        //当启动员和Service连接时调用
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            timeService = ((MyBinder)service).getService();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (isOpenAPP){
                        currTime = timeService.getSystemCurrentTime();
                        Message msg = Message.obtain();
                        msg.what = 0;
                        msg.obj = currTime;
                        myHander.sendMessage(msg);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }

        //当Service意外断开时调用
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private Handler myHander = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    mCurTime.setText((String)msg.obj);
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCurTime = (TextView) findViewById(R.id.id_cur_time);
        mOpenOrNot = (Button) findViewById(R.id.id_btn_open_or_not);

        isOpenAPP = true;

        sp = getSharedPreferences("config.txt", Context.MODE_PRIVATE);
        curSettingIsAutoOpen = sp.getBoolean("isAutoOpenAPP", false);
        editor = sp.edit();
        setAutoButtonText(curSettingIsAutoOpen);

        timeSerIntent = new Intent(MainActivity.this, TimeService.class);
        bindService(timeSerIntent, mTimeConn, Service.BIND_AUTO_CREATE);//绑定服务

        mOpenOrNot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putBoolean("isAutoOpenAPP", !curSettingIsAutoOpen);
                editor.commit();
                curSettingIsAutoOpen = sp.getBoolean("isAutoOpenAPP", false);
                setAutoButtonText(curSettingIsAutoOpen);
            }
        });

    }

    /**
     * 设置是否开机自启动的按钮
     * @param isCurSetting
     */
    private void setAutoButtonText(boolean isCurSetting) {
        if (!isCurSetting) {
            mOpenOrNot.setText("已关闭，点击开启");
        } else {
            mOpenOrNot.setText("已开启，点击关闭");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isOpenAPP = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mTimeConn);
    }

}
