package com.example.jinglion.firsttest;

import android.app.Service;
import android.content.ComponentName;
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
import android.widget.TextView;

import com.example.jinglion.firsttest.Service.TimeService;
import com.example.jinglion.firsttest.Service.TimeService.MyBinder;

public class MainActivity extends AppCompatActivity {
    private TextView mCurTime;
    private Intent timeSerIntent;     //跳转至服务意图
    private TimeService timeService;  //系统后台服务
    private String currTime;  //系统当前时间
    private boolean isOpenAPP = false;//判断APP是否正在打开
    private SharedPreferences sp;

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

        timeSerIntent = new Intent(MainActivity.this, TimeService.class);
        bindService(timeSerIntent, mTimeConn, Service.BIND_AUTO_CREATE);//绑定服务

        isOpenAPP = true;//设置为真，表示APP已经被打开




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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
