package com.example.notificationnotifier;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.LinkedList;
import java.util.Queue;

@SuppressLint("OverrideAbstract")
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NotificationService extends NotificationListenerService {

    NotificationListenerService notificationListenerService;
    private String FACEBOOK_PACKAGE = "com.facebook.katana";
    private String WHATSAPP_PACKAGE = "com.whatsapp";
    private String MESSENGER_PACKAGE = "com.facebook.orca";
    private String TELEGRAM_PACKAGE = "org.telegram.messenger";
    private String GMAIL_PACKAGE = "com.google.android.gm";

    Queue<String> pkgQueue = new LinkedList<>();

    //AudioManager mobilemode = (AudioManager)getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
   // AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

    @Override
    public ComponentName startService(Intent service) {
        return super.startService(service);

    }

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {

        super.onNotificationPosted(sbn);
        String pkg = sbn.getPackageName();
        String msg = sbn.getNotification().toString();
        int notificationId = sbn.getId();

        pkgQueue.add(pkg);

        // ************* TO FETCH TITLE AND TEXT OF NOTIFICATION ******************

        String title = "",text = "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            title += sbn.getNotification().extras.getString("android.title");
            text += sbn.getNotification().extras.getString("android.text");
        }

        // *****************************************************************************************

        Thread t2;
        final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        final Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);


       while(!pkgQueue.isEmpty()){
           String pkgName = pkgQueue.peek();

           if(pkgName.equals(GMAIL_PACKAGE) || pkgName.equals(WHATSAPP_PACKAGE) || pkgName.equals(FACEBOOK_PACKAGE) || pkgName.equals(MESSENGER_PACKAGE) || pkgName.equals(TELEGRAM_PACKAGE)) {
               audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, 10, AudioManager.ADJUST_RAISE);
               r.play();


       }else{

               audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_MUTE,1);
            Log.d("REJECTED NOTIFICATION ",title+" "+text);
            NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
            nMgr.cancelAll();

        }
           pkgQueue.remove();
       }
    }


    @Override
    public boolean stopService(Intent name) {
       notificationListenerService.stopSelf();
        return super.stopService(name);
    }


}
