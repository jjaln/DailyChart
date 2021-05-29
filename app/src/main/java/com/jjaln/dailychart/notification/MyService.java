package com.jjaln.dailychart.notification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.jjaln.dailychart.MainActivity;
import com.jjaln.dailychart.R;
import com.jjaln.dailychart.SplashActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyService extends Service {

    NotificationManager Notifi_M;
    ServiceThread thread;
    Notification Notifi ;

    private Thread mainThread;
    public static Intent serviceIntent = null;

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // 최초 생성되었을때 한번 실행
    @Override
    public void onCreate() {
        Log.d("Notification","create channel");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel =
                    new NotificationChannel(
                            "alarm_channel_id",
                            "알람",
                            NotificationManager.IMPORTANCE_DEFAULT
                    );
            notificationChannel.setDescription("알람");
            notificationManager.createNotificationChannel(notificationChannel);
        }
        super.onCreate();
    }
    // 서비스가 종료될 때 실행되는 함수
    @Override
    public void onDestroy() {
        super.onDestroy();
//        thread.stopForever();
//        thread = null;//쓰레기 값을 만들어서 빠르게 회수하라고 null을 넣어줌

        Log.d("Lifecycle","excute onDestroy");

        serviceIntent = null;
        setAlarmTimer();
        Thread.currentThread().interrupt();

        if (mainThread != null) {
            mainThread.interrupt();
            mainThread = null;
        }
    }
    // 백그라운드에서 실행되는 동작들이 들어가는 곳
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        return START_STICKY;


        serviceIntent = intent;
        Log.d("foreground","start service");

        mainThread = new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                SimpleDateFormat sdf = new SimpleDateFormat("aa hh:mm");
                boolean run = true;
                while (run) {
                    try {
                        Thread.sleep(1000 * 60 * 1); // 1 minute
                        Date date = new Date();
                        //showToast(getApplication(), sdf.format(date));
                        sendNotification(sdf.format(date));
                    } catch (Exception ex) {
//                        run = false;
//                        e.printStackTrace();
                    }
                }
            }
        });
        mainThread.start();

        return START_NOT_STICKY;
    }

    protected void setAlarmTimer() {
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.add(Calendar.SECOND, 1);
        Intent intent = new Intent(this, AlarmRecever.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0,intent,0);

        AlarmManager mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mAlarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), sender);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);

        Log.d("Notification", "fore ground noti send");
        String channelId = "fcm_default_channel";//getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.logo_main)//drawable.splash)
                        .setContentTitle("Daily Chart")
                        .setContentText("포어 그라운드 실행중")
                        .setAutoCancel(true)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,"Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

        // 알람
        Notifi_M = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        fluctateNoti();
//        myServiceHandler handler = new myServiceHandler();
//        thread = new ServiceThread(handler);
//        thread.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void fluctateNoti(){
        Intent intent = new Intent(MyService.this, SplashActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(MyService.this, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
        Log.d("Notification","noti send");

        Notifi = new Notification.Builder(getApplicationContext(),"alarm_channel_id")
                .setContentTitle("Content Title")
                .setContentText("Content Text")
                .setSmallIcon(R.drawable.logo_main)
                .setTicker("알림!!!")
                .setContentIntent(pendingIntent)
                .build();

        //소리추가
        Notifi.defaults = Notification.DEFAULT_SOUND;

        //알림 소리를 한번만 내도록
        Notifi.flags = Notification.FLAG_ONLY_ALERT_ONCE;

        //확인하면 자동으로 알림이 제거 되도록
        Notifi.flags = Notification.FLAG_AUTO_CANCEL;


        Notifi_M.notify( 777 , Notifi);


        //토스트 띄우기
        //Toast.makeText(MyService.this, "뜸?", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) { //핸들링 하는 부분
        Log.d("Error","onTaskRemoved - 강제 종료 " + rootIntent);
//        Intent intent = new Intent(getApplicationContext(), MyService.class);
//        PendingIntent pendingIntent = PendingIntent.getService(this, 1, intent, PendingIntent.FLAG_ONE_SHOT);
        stopSelf(); //서비스 종료
//        super.onTaskRemoved(rootIntent);
    }

//    class myServiceHandler extends Handler {
//        @RequiresApi(api = Build.VERSION_CODES.O)
//        @Override
//        public void handleMessage(android.os.Message msg) {
//            Intent intent = new Intent(MyService.this, SplashActivity.class);
//            PendingIntent pendingIntent = PendingIntent.getActivity(MyService.this, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
//            Log.d("Notification","noti send");
//
//            Notifi = new Notification.Builder(getApplicationContext(),"alarm_channel_id")
//                    .setContentTitle("Content Title")
//                    .setContentText("Content Text")
//                    .setSmallIcon(R.drawable.logo_main)
//                    .setTicker("알림!!!")
//                    .setContentIntent(pendingIntent)
//                    .build();
//
//            //소리추가
//            Notifi.defaults = Notification.DEFAULT_SOUND;
//
//            //알림 소리를 한번만 내도록
//            Notifi.flags = Notification.FLAG_ONLY_ALERT_ONCE;
//
//            //확인하면 자동으로 알림이 제거 되도록
//            Notifi.flags = Notification.FLAG_AUTO_CANCEL;
//
//
//            Notifi_M.notify( 777 , Notifi);
//
//
//            //토스트 띄우기
//            //Toast.makeText(MyService.this, "뜸?", Toast.LENGTH_LONG).show();
//        }
//    };

}