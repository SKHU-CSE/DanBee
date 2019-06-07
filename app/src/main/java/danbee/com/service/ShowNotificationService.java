package danbee.com.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.kakao.usermgmt.response.model.User;

import java.util.Timer;
import java.util.TimerTask;

import danbee.com.AppHelper;
import danbee.com.MainActivity;
import danbee.com.R;
import danbee.com.UserInfo;
import danbee.com.kickdata.BatteryResult;

import static danbee.com.App.CHANNEL_ID;

public class ShowNotificationService extends Service {
    Timer timer = new Timer();
    int battery;
    int kickid;
    Notification notification;
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        battery = MainActivity.battery;
        kickid = UserInfo.info.getKickid();
                //intent.getIntExtra("battery", 0);

        //notification 클릭시 이동할 Activity
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        // RemoteViews remoteView = new RemoteViews(getPackageName(), R.layout.service_statebar);
        notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("단비")
                .setContentText(battery+"%")
                .setSmallIcon(R.drawable.danbeelogo)
                .setContentIntent(pendingIntent)
                .setOnlyAlertOnce(true)
                .build();

        startForeground(1, notification);
        //10초마다 반복통신
        timer.schedule(requestTimer,0, 10000);


        //startForeground 안할시 몇분후 서비스종료됨

        return START_NOT_STICKY;

    }

    TimerTask requestTimer = new TimerTask() {
        @Override
        public void run() {
            batteryRequest(kickid);

            //notification 클릭시 이동할 Activity
            Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);
            // RemoteViews remoteView = new RemoteViews(getPackageName(), R.layout.service_statebar);
            notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                    .setContentTitle("단비")
                    .setContentText(battery+"%")
                    .setSmallIcon(R.drawable.danbeelogo)
                    .setContentIntent(pendingIntent)
                    .setOnlyAlertOnce(true)
                    .build();

            startForeground(1, notification);

            Log.d("test","loop:"+battery);
        }
    };
    //킥보드 배터리 값가져오기
    //http://3.17.25.223/api/kick/battery/get/{kickid}
    void batteryRequest(int kickid){
        String url = "http://3.17.25.223/api/kick/battery/get/"+kickid;
        Log.d("test", "serviceurl:"+url);
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() { //응답 받음
                    @Override
                    public void onResponse(String response) {
                        Log.d("test", "bettery: "+response);
                        batteryProcessResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("test", "bettery: "+error);
                    }
                }
        );
        //자동캐싱잇는경우 이전결과 그대로보여짐
        request.setShouldCache(false);  //새로요청해서 결과보여줌
        AppHelper.requestQueue.add(request);
    }

    //json 파싱
    public void batteryProcessResponse(String response){

        Gson gson = new Gson();
        BatteryResult batteryResult = gson.fromJson(response, BatteryResult.class);
        if(batteryResult.result == 777){
            battery = batteryResult.battery;
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if(AppHelper.requestQueue == null)
            AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());

    }
}
