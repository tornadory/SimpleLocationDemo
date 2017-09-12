package com.johnny.simplelocationdemo;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Debug;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleLocationService extends Service implements LocationSource, AMapLocationListener {
    private AMapLocationClient mLocationClient = null;
    private AMapLocationClientOption mLocationOption = null;
    private OnLocationChangedListener mListener = null;

    public SimpleLocationService(Context applicationContext) {
        super();
        System.out.println("SimpleLocationService " + " start simplelocaitonservice constructor");
    }

    public SimpleLocationService(){

    }

    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("SimpleLocationService " + " start onBind");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        System.out.println("SimpleLocationService" + " start simplelocaitonservice ");
        initLoc();
        return START_STICKY;
    }

    private void initLoc() {
        mLocationClient = new AMapLocationClient(getApplicationContext());
        mLocationClient.setLocationListener(this);
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setNeedAddress(true);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.startLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        System.out.println("simple location service " + "onLocationChanged - AMapLocation" + aMapLocation.toString());
        if(aMapLocation != null){
            if(aMapLocation.getErrorCode() == 0){
                aMapLocation.getLocationType();
                aMapLocation.getLatitude();
                aMapLocation.getLongitude();
                aMapLocation.getAccuracy();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(aMapLocation.getTime());
                df.format(date);
                aMapLocation.getAddress();
                aMapLocation.getCountry();
                aMapLocation.getProvince();
                aMapLocation.getCity();
                aMapLocation.getDistrict();
                aMapLocation.getStreet();
                aMapLocation.getStreetNum();
                aMapLocation.getCityCode();
                aMapLocation.getAdCode();

                mListener.onLocationChanged(aMapLocation);
                StringBuffer buffer = new StringBuffer();
                buffer.append(aMapLocation.getLatitude() + "" + aMapLocation.getLongitude()
                        + "" +aMapLocation.getCountry() + "" + aMapLocation.getProvince()
                        + "" + aMapLocation.getCity() + "" + aMapLocation.getDistrict()
                        + "" + aMapLocation.getStreet()
                        +"" + aMapLocation.getStreetNum());
                Toast.makeText(getApplicationContext(), buffer.toString(),Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(getApplicationContext(), "Location Failed ", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {

    }
}
