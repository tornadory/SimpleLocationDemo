package com.johnny.simplelocationdemo;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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

import org.json.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends AppCompatActivity {
    private  String TAG = "SimpleLocationService";

    private ProgressDialog progress;

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;


    private  SimpleLocationService mSimpleLocationService;
    Intent mServiceIntent;

    String deviceid = "";
    String imei = "";
    String imsi = "";

    String username = "";
    String email = "";

    TextView tvUserName;
    TextView tvEmail;

    TelephonyManager tm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);


        tm = (TelephonyManager)getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        deviceid = tm.getDeviceId();
        imei = tm.getSimSerialNumber();
        imsi = tm.getSubscriberId();
        System.out.println("deviceID " + deviceid + "  imei " + imei + "  imsi " + imsi);
        //deviceID 861918032209661  PNumber   imei 89860315140214387784  imsi 460030364890485


        tvUserName = (TextView)findViewById(R.id.txUsername);
        tvEmail = (TextView)findViewById(R.id.txEmail);

        SharedPreferences sharedPre=getSharedPreferences("config", MODE_PRIVATE);
        username=sharedPre.getString("username", "");
        email=sharedPre.getString("email", "");
        tvUserName.setText(username);
        tvEmail.setText(email);

    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                System.out.println(TAG + "true");
                return true;
            }
        }
        System.out.println (TAG + "false");
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
//        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mapView.onDestroy();
    }

    /**
     * 初始化定位
     *
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    private void initLocation(){
        //初始化client
        locationClient = new AMapLocationClient(this.getApplicationContext());
        locationOption = getDefaultOption();
        //设置定位参数
        locationClient.setLocationOption(locationOption);
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
    }

    private void startLocation(){
        // 启动定位
        locationClient.startLocation();
    }

    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            if (null != location) {

                StringBuffer sb = new StringBuffer();
                //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                if(location.getErrorCode() == 0){
                    sb.append("定位成功" + "\n");
                    sb.append("定位类型: " + location.getLocationType() + "\n");
                    sb.append("经    度    : " + location.getLongitude() + "\n");
                    sb.append("纬    度    : " + location.getLatitude() + "\n");
                    sb.append("精    度    : " + location.getAccuracy() + "米" + "\n");
                    sb.append("提供者    : " + location.getProvider() + "\n");

                    sb.append("速    度    : " + location.getSpeed() + "米/秒" + "\n");
                    sb.append("角    度    : " + location.getBearing() + "\n");
                    // 获取当前提供定位服务的卫星个数
                    sb.append("星    数    : " + location.getSatellites() + "\n");
                    sb.append("国    家    : " + location.getCountry() + "\n");
                    sb.append("省            : " + location.getProvince() + "\n");
                    sb.append("市            : " + location.getCity() + "\n");
                    sb.append("城市编码 : " + location.getCityCode() + "\n");
                    sb.append("区            : " + location.getDistrict() + "\n");
                    sb.append("区域 码   : " + location.getAdCode() + "\n");
                    sb.append("地    址    : " + location.getAddress() + "\n"); //OK
                    sb.append("兴趣点    : " + location.getPoiName() + "\n");
                    //定位完成的时间
                    sb.append("定位时间: " + Utils.formatUTC(location.getTime(), "yyyy-MM-dd HH:mm:ss") + "\n");
                } else {
                    //定位失败
                    sb.append("定位失败" + "\n");
                    sb.append("错误码:" + location.getErrorCode() + "\n");
                    sb.append("错误信息:" + location.getErrorInfo() + "\n");
                    sb.append("错误描述:" + location.getLocationDetail() + "\n");
                }
                //定位之后的回调时间
                sb.append("回调时间: " + Utils.formatUTC(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss") + "\n");

                //解析定位结果，
                String result = sb.toString();
                System.out.println("result");
//                aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
//                aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
//                locationListener.onLocationChanged(location);
//                aMap.addMarker(getMarkerOptions(location));

            } else {
                System.out.println("Failed to locate");
            }
        }
    };

    //自定义一个图钉，并且设置图标，当我们点击图钉时，显示设置的信息
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

    private AMapLocationClientOption getDefaultOption(){
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }

    //    public void getLocation(View View){
//        Log.d(TAG, "getLocation function");
//        StringBuilder locStr = new StringBuilder();
//
//        URL url = null;
//        try {
//            url = new URL("https://simple-location-demo.herokuapp.com/location");
//            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
//
//            connection.setRequestMethod("GET");
//            connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
//            connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
//
//            int responseCode = connection.getResponseCode();
//
//            System.out.println("\nSending 'POST' request to URL : " + url);
//            System.out.println("Response Code : " + responseCode);
//
//            final StringBuilder output = new StringBuilder("Request URL " + url);
//
//            output.append(System.getProperty("line.separator")  + "Response Code " + responseCode);
//            output.append(System.getProperty("line.separator")  + "Type " + "GET");
//            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//            String line = "";
//            StringBuilder responseOutput = new StringBuilder();
//            System.out.println("output===============" + br);
//            while((line = br.readLine()) != null ) {
//                responseOutput.append(line);
//            }
//            br.close();
//
//            output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());
//            System.out.println("get output : " + output);
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (ProtocolException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        Log.d(TAG, locStr.toString());
//        locInfo.setText(locStr.toString());
//
//    }
//
//    public void setLocation(View View){
//        Log.d(TAG, "call setLocation method");
//        try{
//            JSONObject jsonData = new JSONObject();
//            jsonData.put("lan", "999");
//            jsonData.put("lon", "888");
//
//            URL url = new URL("https://simple-location-demo.herokuapp.com/location");
//            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
//            connection.setRequestMethod("POST");
//            connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
//            connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
//            connection.setRequestProperty("Content-Type","application/json");
//            connection.setDoOutput(true);
//            DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
//            dStream.write(jsonData.toString().getBytes("UTF-8"));
//            dStream.flush();
//            dStream.close();
//            int responseCode = connection.getResponseCode();
//
//            System.out.println("\nSending 'POST' request to URL : " + url);
//            System.out.println("Response Code : " + responseCode);
//
//            final StringBuilder output = new StringBuilder("Request URL " + url);
//            output.append(System.getProperty("line.separator")  + "Response Code " + responseCode);
//            output.append(System.getProperty("line.separator")  + "Type " + "POST");
//            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//            String line = "";
//            StringBuilder responseOutput = new StringBuilder();
//            System.out.println("output===============" + br);
//            while((line = br.readLine()) != null ) {
//                responseOutput.append(line);
//            }
//            br.close();
//
//            output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());
//            System.out.println("post output : " + output);
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (ProtocolException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public  void getLocation(View View){
        //startLocation();
        startActivity(new Intent(this, MapActivity.class));
    }

    public void sendPostRequest(View View) {
        new PostClass(this).execute();
    }

    public void sendGetRequest(View View) {
        new GetClass(this).execute();
    }


    private class PostClass extends AsyncTask<String, Void, Void> {

        private final Context context;

        public PostClass(Context c){
            this.context = c;
        }

        protected void onPreExecute(){
            progress= new ProgressDialog(this.context);
            progress.setMessage("Loading");
            progress.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {

                JSONObject jsonData = new JSONObject();
                jsonData.put("username", username);
                jsonData.put("email", email);
                jsonData.put("deviceID", deviceid);
                jsonData.put("imei", imei);
                jsonData.put("imsi", imsi);

                URL url = new URL("https://simple-location-demo.herokuapp.com/newuser");


                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                connection.setRequestProperty("Content-Type","application/json");
                connection.setDoOutput(true);
                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
                dStream.write(jsonData.toString().getBytes("UTF-8"));
                dStream.flush();
                dStream.close();
                int responseCode = connection.getResponseCode();

                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Response Code : " + responseCode);

                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator")  + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator")  + "Type " + "POST");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while((line = br.readLine()) != null ) {
                    responseOutput.append(line);
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());

                MainActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        progress.dismiss();
                    }
                });


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute() {
            progress.dismiss();
        }

    }

    private class GetClass extends AsyncTask<String, Void, Void> {

        private final Context context;

        public GetClass(Context c){
            this.context = c;
        }

        protected void onPreExecute(){
            progress= new ProgressDialog(this.context);
            progress.setMessage("Loading");
            progress.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {

                URL url = new URL("https://simple-location-demo.herokuapp.com/locations");

                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                String urlParameters = "fizz=buzz";
                connection.setRequestMethod("GET");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");

                int responseCode = connection.getResponseCode();

                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters);
                System.out.println("Response Code : " + responseCode);

                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator")  + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator")  + "Type " + "GET");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while((line = br.readLine()) != null ) {
                    responseOutput.append(line);
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());

                MainActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        progress.dismiss();

                    }
                });


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }
    }

    public void regUser(View view){
        if(tvUserName.getText().length() > 1 && tvEmail.getText().length()> 1){
            username = tvUserName.getText().toString();
            email = tvEmail.getText().toString();

            SharedPreferences sharedPre= getSharedPreferences("config", MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPre.edit();
            editor.putString("username", username);
            editor.putString("email", email);
            editor.commit();
            sendPostRequest(view);
        }
    }

    //hide this activity/quit this activity
    public void hideMe(View View){
        System.out.println(TAG + "hideMe called p");
        System.out.println(TAG + "try to start service");


        mSimpleLocationService = new SimpleLocationService(this);
        mServiceIntent = new Intent(this, mSimpleLocationService.getClass());

        if (!isMyServiceRunning(mSimpleLocationService.getClass())) {
            startService(mServiceIntent);
        }

        finish();
    }


}
