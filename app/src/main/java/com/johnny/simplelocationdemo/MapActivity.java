package com.johnny.simplelocationdemo;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class MapActivity extends AppCompatActivity implements View.OnClickListener {

    private MapView mapView;
    private AMap aMap;

    private AMapLocationClient mLocationClient = null;
    private AMapLocationClientOption mLocationOption = null;
    //private OnLocationChangedListener mListener = null;

    Button btFirst;
    Button btLast;
    Button btPrev;
    Button btNext;

    private boolean isFirstLoc = true;

    private String username = "";

    private ArrayList<SimpleLocation> locations = new ArrayList<SimpleLocation>();

    private int index = 0;

    Marker localMarker = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_3);

        btFirst = (Button) findViewById(R.id.btFirst);
        btLast = (Button) findViewById(R.id.btLast);
        btPrev = (Button) findViewById(R.id.btPrev);
        btNext = (Button) findViewById(R.id.btNext);

        Bundle b = getIntent().getExtras();
        if(b != null){
            username = b.getString("username");
            //System.out.println("SimpleLocationDemo == " + "username is " + username);
        }

        getLocations();

        mapView = (MapView)findViewById(R.id.idMap3);
        mapView.onCreate(savedInstanceState);
        aMap = mapView.getMap();

        UiSettings settings = aMap.getUiSettings();
        //aMap.setLocationSource(this);
        settings.setMyLocationButtonEnabled(true);
        aMap.setMyLocationEnabled(true);

        //initLoc();

        btFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GotoFirst();
            }
        });

        btLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GotoLast();
            }
        });

        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GotoNext();
            }
        });

        btPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GotoPrev();
            }
        });
    }

//    private void initLoc() {
//        mLocationClient = new AMapLocationClient(getApplicationContext());
//        mLocationClient.setLocationListener(this);
//        mLocationOption = new AMapLocationClientOption();
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//        mLocationOption.setNeedAddress(true);
//        mLocationClient.setLocationOption(mLocationOption);
//        mLocationClient.startLocation();
//    }

    @Override
    public void onClick(View view) {

    }

//    @Override
//    public void onLocationChanged(AMapLocation aMapLocation) {
//        if(aMapLocation != null){
//            if(aMapLocation.getErrorCode() == 0){
////                aMapLocation.getLocationType();
////                aMapLocation.getLatitude();
////                aMapLocation.getLongitude();
////                aMapLocation.getAccuracy();
////                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////                Date date = new Date(aMapLocation.getTime());
////                df.format(date);
////                aMapLocation.getAddress();
////                aMapLocation.getCountry();
////                aMapLocation.getProvince();
////                aMapLocation.getCity();
////                aMapLocation.getDistrict();
////                aMapLocation.getStreet();
////                aMapLocation.getStreetNum();
////                aMapLocation.getCityCode();
////                aMapLocation.getAdCode();
//
//                if(isFirstLoc){
//                    aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
//                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
////                    mListener.onLocationChanged(aMapLocation);
//                    aMap.addMarker(getMarkerOptions(aMapLocation));
////                    StringBuffer buffer = new StringBuffer();
////                    buffer.append(aMapLocation.getCountry() + "" + aMapLocation.getProvince()
////                            + "" + aMapLocation.getCity() + "" + aMapLocation.getDistrict()
////                            + "" + aMapLocation.getStreet()
////                            +"" + aMapLocation.getStreetNum());
////                    Toast.makeText(getApplicationContext(), buffer.toString(),Toast.LENGTH_LONG).show();
//                    isFirstLoc = false;
//                }
//            }else {
//                Log.e("AmapError", "location Error, ErrorCode:" + aMapLocation.getErrorCode());
//                //Toast.makeText(getApplicationContext(), "Location Failed ", Toast.LENGTH_LONG).show();
//            }
//        }
//    }

    private MarkerOptions getSMarkerOptions(SimpleLocation location) {
        //设置图钉选项
        MarkerOptions options = new MarkerOptions();
        //图标
        //options.icon(BitmapDescriptorFactory.fromResource(R.mipmap.fire));
        //位置
        options.position(new LatLng(location.getLatitude(), location.getLongitude()));
        StringBuffer buffer = new StringBuffer();
        buffer.append(location.getAddress());
        //标题
        options.title(buffer.toString());
        //子标题
        options.snippet(location.getTime());
        //设置多少帧刷新一次图片资源
        options.period(60);

        return options;

    }

    private MarkerOptions getMarkerOptions(AMapLocation amapLocation) {
        //设置图钉选项
        MarkerOptions options = new MarkerOptions();
        //图标
        //options.icon(BitmapDescriptorFactory.fromResource(R.mipmap.fire));
        //位置
        options.position(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude()));
        StringBuffer buffer = new StringBuffer();
        buffer.append(amapLocation.getStreet() + "" + amapLocation.getStreetNum());
        //标题
        options.title(buffer.toString());
        //子标题
        options.snippet("YOU ARE HERE");
        //设置多少帧刷新一次图片资源
        options.period(60);

        return options;

    }

//
//    @Override
//    public void activate(OnLocationChangedListener onLocationChangedListener) {
//        mListener = onLocationChangedListener;
//    }
//
//    @Override
//    public void deactivate() {
//        mListener = null;
//    }

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




    public void getLocations(){
        new MapActivity.GetClass(this).execute();
    }

    private class GetClass extends AsyncTask<String, Void, Void> {
        private ProgressDialog progress = new ProgressDialog(MapActivity.this);
        private final Context context;
        InputStream inputStream = null;
        String result = "";

        public GetClass(Context c){
            this.context = c;
        }

        protected void onPreExecute(){
            //progress= new ProgressDialog(this.context);
            progress.setMessage("Loading");
            progress.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {

                URL url = new URL("https://simple-location-demo.herokuapp.com/locations/" + username);

                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");

                int responseCode = connection.getResponseCode();

                //System.out.println("\nSending 'POST' request to URL : " + url);
                //System.out.println("Response Code : " + responseCode);

                //final StringBuilder output = new StringBuilder("Request URL " + url);
                //output.append(System.getProperty("line.separator")  + "Response Code " + responseCode);
                //output.append(System.getProperty("line.separator")  + "Type " + "GET");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();

                while((line = br.readLine()) != null ) {
                    responseOutput.append(line);
                }
                br.close();

                //System.out.println("output===============" + responseOutput.toString());
                result = responseOutput.toString();
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            try {
                JSONArray jArray = new JSONArray(result);
                if(jArray.length() > 0)
                    for(int i=jArray.length() - 1; i >= 0; i--) {

                        JSONObject jObject = jArray.getJSONObject(i);

                        SimpleLocation location = new SimpleLocation();

                        location.setUsername(jObject.getString("username"));
                        location.setLatitude(Double.parseDouble(jObject.getString("latitude")));
                        location.setLongitude(Double.parseDouble(jObject.getString("longitude")));
                        location.setAddress(jObject.getString("address"));
                        location.setTime(jObject.getString("time"));

                        locations.add(location);

                    } // End Loop
                this.progress.dismiss();

                if(locations.size() > 1){
                    Collections.sort(locations, new Comparator<SimpleLocation>(){

                        @Override
                        public int compare(SimpleLocation l1, SimpleLocation l2) {
                            try{
                                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(l1.time).compareTo(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(l2.time));
                            }catch (ParseException e){
                                return 0;
                            }
                        }
                    });

//                    System.out.println("first data " + locations.get(0).time);
//                    System.out.println("last data " + locations.get(locations.size() -1 ).time);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(locations.size() > 0)
                            GotoLast();
                    }
                });


            } catch (JSONException e) {
                Log.e("JSONException", "Error: " + e.toString());
            } // catch (JSONException e)
        }
    }

    private void GotoFirst() {
        if(locations.size() > 1){
            index = 0;
            SimpleLocation location = locations.get(index);
            //System.out.println("SimpleLocation " + location.toString());
            aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
            aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
            localMarker = aMap.addMarker(getSMarkerOptions(location));
            localMarker.showInfoWindow();
        }
    }

    private void GotoLast(){
        if(locations.size() > 1){
            index = locations.size() - 1;
            SimpleLocation location = locations.get(index);
            //System.out.println("SimpleLocation " + location.toString());
            aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
            aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
            localMarker = aMap.addMarker(getSMarkerOptions(location));
            localMarker.showInfoWindow();
        }
    }

    private void GotoNext(){
        if(locations.size() > 1){
            if(index < locations.size() - 1){
                index++;
                SimpleLocation location = locations.get(index);
                //System.out.println("SimpleLocation " + location.toString());
                aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
                localMarker = aMap.addMarker(getSMarkerOptions(location));
                localMarker.showInfoWindow();
            }else {
                Toast.makeText(getApplicationContext(), "has been the last one", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void GotoPrev(){
        if(locations.size() > 1){
            if(index > 0){
                index--;
                SimpleLocation location = locations.get(index);
                //System.out.println("SimpleLocation " + location.toString());
                aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
                localMarker = aMap.addMarker(getSMarkerOptions(location));
                localMarker.showInfoWindow();
            }else{
                Toast.makeText(getApplicationContext(), "has been the first one", Toast.LENGTH_SHORT).show();
            }

        }
    }


}
