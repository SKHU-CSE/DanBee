package danbee.com.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import danbee.com.MainActivity;
import danbee.com.R;

import static danbee.com.App.CHANNEL_ID;

public class ShowNotificationService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        int battery = MainActivity.battery;
                //intent.getIntExtra("battery", 0);

        //notification 클릭시 이동할 Activity
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        // RemoteViews remoteView = new RemoteViews(getPackageName(), R.layout.service_statebar);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("단비")
                .setContentText(battery+"%")
                .setSmallIcon(R.drawable.danbeelogo)
                .setContentIntent(pendingIntent)
                .setOnlyAlertOnce(true)
                .build();



        startForeground(1, notification);

        //startForeground 안할시 몇분후 서비스종료됨

        return START_NOT_STICKY;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
