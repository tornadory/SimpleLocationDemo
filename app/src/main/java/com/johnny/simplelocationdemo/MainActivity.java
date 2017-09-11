package com.johnny.simplelocationdemo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.json.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends AppCompatActivity {
    private ProgressDialog progress;
    TextView locInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locInfo = (TextView)findViewById(R.id.locInfo);

        startService(new Intent(getBaseContext(), SimpleLocationService.class));
    }

//    public void getLocation(View View){
//        Log.d("SimpleLocationService", "getLocation function");
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
//        Log.d("SimpleLocationService", locStr.toString());
//        locInfo.setText(locStr.toString());
//
//    }
//
//    public void setLocation(View View){
//        Log.d("simpleserver", "call setLocation method");
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
                jsonData.put("lan", "999");
                jsonData.put("lon", "888");

                URL url = new URL("https://simple-location-demo.herokuapp.com/location");


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
                        locInfo.setText(output);
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

                URL url = new URL("https://simple-location-demo.herokuapp.com/location");

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
                        locInfo.setText(output);
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
}
