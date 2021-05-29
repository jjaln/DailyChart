package com.jjaln.dailychart.notification;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class ServiceThread extends Thread {
    Handler handler;
    boolean isRun = true;
    public int sleep_time = 10000;

    public ServiceThread(Handler handler) {
        this.handler = handler;
    }

    public void stopForever() {
        synchronized (this) {
            this.isRun = false;
        }
    }

    public void run() {
        //반복적으로 수행할 작업을 한다.


//        while (isRun) {
        handler.sendEmptyMessage(0);//쓰레드에 있는 핸들러에게 메세지를 보냄
//            try {
//                Thread.sleep(sleep_time); //10초씩 쉰다. 10800000 = 3시간
//            } catch (Exception e) {
//            }
//        }
    }
}


