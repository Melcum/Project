package com.example.projectsudoku;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;


public class MyReceiver extends BroadcastReceiver {

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    MediaPlayer mp = null;

    String msg, phoneNo = "";
    @Override
    public void onReceive(final Context context, final Intent intent) {

        if (intent.getAction() == SMS_RECEIVED){

            mp = MediaPlayer.create(context, R.raw.intro);
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mp.stop();
                    mp.release();
                }
            });

            Bundle dataBundle = intent.getExtras();
            if (dataBundle != null){
                Object[] mypdu = (Object[])dataBundle.get("pdus");
                final SmsMessage[] message = new SmsMessage[mypdu.length];
                for (int i = 0; i<mypdu.length; i++){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        String format = dataBundle.getString("format");
                        message[i] = SmsMessage.createFromPdu((byte[])mypdu[i], format);
                    }
                    else{
                        message[i] = SmsMessage.createFromPdu((byte[])mypdu[i]);
                    }
                    msg = message[i].getMessageBody();
                    phoneNo = message[i].getOriginatingAddress();
                }

                Toast.makeText(context, "Message: "+msg+"\nNumber: "+phoneNo, Toast.LENGTH_LONG).show();
                SmsManager smgr = SmsManager.getDefault();
                smgr.sendTextMessage(phoneNo, null, "Im busy right now", null, null);
            }
        }
    }

    
}
