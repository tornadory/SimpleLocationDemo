package com.johnny.simplelocationdemo;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleLocationService extends Service implements LocationListener {
    public SimpleLocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onLocationChanged(Location location) {
        //Getting current date and time
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
//        String currentDateandTime = sdf.format(new Date());

        //send the newst location info to the server
        Log.d("SimpleLocationService", "new location " + location.toString());
        //String jsonLocation =

//        TextView[] lPos=Pos;
//        lPos[0].setText(Double.toString(location.getLatitude()));
//        lPos[1].setText(Double.toString(location.getLongitude()));
//        lPos[2].setText(Double.toString(location.getAltitude()));
//
//        int nsat=location.getExtras().getInt("satellites", -1);
//        lPos[3].setText(Integer.toString(nsat));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        //send the newst location info to the server
    }

    @Override
    public void onProviderEnabled(String s) {
        //send the newst location info to the server
    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
