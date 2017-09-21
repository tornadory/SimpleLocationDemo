package com.johnny.simplelocationdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SimpleLocationServiceReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        System.out.println(SimpleLocationServiceReceiver.class.getSimpleName() + "onRecevie..." + intent.getAction());
//        if(intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)){
//            Intent serviceIntent = new Intent(context, SimpleLocationService.class);
//            context.startService(serviceIntent);
//        }
        Intent serviceIntent = new Intent(context, SimpleLocationService.class);
        context.startService(serviceIntent);
    }
}
