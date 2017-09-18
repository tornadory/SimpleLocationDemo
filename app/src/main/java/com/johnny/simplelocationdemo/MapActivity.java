package com.johnny.simplelocationdemo;

import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MapActivity extends AppCompatActivity implements LocationSource, View.OnClickListener, AMapLocationListener {

    private MapView mapView;
    private AMap aMap;

    private AMapLocationClient mLocationClient = null;
    private AMapLocationClientOption mLocationOption = null;
    private OnLocationChangedListener mListener = null;

    private boolean isFirstLoc = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_2);

        mapView = (MapView)findViewById(R.id.idMap);
        mapView.onCreate(savedInstanceState);
        aMap = mapView.getMap();

        UiSettings settings = aMap.getUiSettings();
        aMap.setLocationSource(this);
        settings.setMyLocationButtonEnabled(true);
        aMap.setMyLocationEnabled(true);

        initLoc();
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
    public void onClick(View view) {

    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
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

                if(isFirstLoc){
                    aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
                    mListener.onLocationChanged(aMapLocation);
                    aMap.addMarker(getMarkerOptions(aMapLocation));
                    StringBuffer buffer = new StringBuffer();
                    buffer.append(aMapLocation.getCountry() + "" + aMapLocation.getProvince()
                            + "" + aMapLocation.getCity() + "" + aMapLocation.getDistrict()
                            + "" + aMapLocation.getStreet()
                            +"" + aMapLocation.getStreetNum());
                    Toast.makeText(getApplicationContext(), buffer.toString(),Toast.LENGTH_LONG).show();
                    isFirstLoc = false;
                }
            }else {
                Log.e("AmapError", "location Error, ErrorCode:" + aMapLocation.getErrorCode());
                Toast.makeText(getApplicationContext(), "Location Failed ", Toast.LENGTH_LONG).show();
            }
        }
    }

    private MarkerOptions getMarkerOptions(AMapLocation amapLocation) {
        //设置图钉选项
        MarkerOptions options = new MarkerOptions();
        //图标
        //options.icon(BitmapDescriptorFactory.fromResource(R.mipmap.fire));
        //位置
        options.position(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude()));
        StringBuffer buffer = new StringBuffer();
        buffer.append(amapLocation.getCountry() + "" + amapLocation.getProvince() + "" + amapLocation.getCity() +  "" + amapLocation.getDistrict() + "" + amapLocation.getStreet() + "" + amapLocation.getStreetNum());
        //标题
        options.title(buffer.toString());
        //子标题
        options.snippet("这里好火");
        //设置多少帧刷新一次图片资源
        options.period(60);

        return options;

    }


    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {
        mListener = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}
