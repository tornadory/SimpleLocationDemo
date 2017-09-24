package com.johnny.simplelocationdemo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListViewCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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
import java.util.ArrayList;


/**
 * Created by admin on 9/22/2017.
 */

public class SimpleLocationDemoActivity extends AppCompatActivity {
    private ListView userListView;
    private ArrayAdapter<String> listAdapter;

    //private String[] users;
    private ArrayList<String> users = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        userListView = (ListView)findViewById(R.id.simpleListView);

        users.add("t2");
        users.add("t3");

        users.clear();

        getUsers();

//        listAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, android.R.id.text1, users);
//        userListView.setAdapter(listAdapter);

        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int itemPostion = i;
                String itemValue = (String)userListView.getItemAtPosition(itemPostion);
                Intent mapIntent = new Intent(SimpleLocationDemoActivity.this, MapActivity.class);
                Bundle b = new Bundle();
                b.putString("username", itemValue);
                mapIntent.putExtras(b);
                startActivity(mapIntent);
            }
        });

    }

    public void getUsers(){
        new SimpleLocationDemoActivity.GetClass(this).execute();
    }

    private class GetClass extends AsyncTask<String, Void, Void> {
        private ProgressDialog progress = new ProgressDialog(SimpleLocationDemoActivity.this);
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

                URL url = new URL("https://simple-location-demo.herokuapp.com/users");

                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");

                int responseCode = connection.getResponseCode();

                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Response Code : " + responseCode);

                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator")  + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator")  + "Type " + "GET");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();

                while((line = br.readLine()) != null ) {
                    responseOutput.append(line);
                }
                br.close();

                System.out.println("output===============" + responseOutput.toString());
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
                for(int i=0; i < jArray.length(); i++) {

                    JSONObject jObject = jArray.getJSONObject(i);

                    String username = jObject.getString("username");
                    String email = jObject.getString("email");
                    String deviceid = jObject.getString("deviceID");

                    users.add(username);

                    System.out.println("SimpleLocationDemo " + "UserName:" + username + email + deviceid);

                } // End Loop
                this.progress.dismiss();

                System.out.println("users " + users.toString());



                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listAdapter = new ArrayAdapter<String>(SimpleLocationDemoActivity.this,android.R.layout.simple_list_item_1, android.R.id.text1, users);

                        userListView.setAdapter(listAdapter);
                    }
                });

            } catch (JSONException e) {
                Log.e("JSONException", "Error: " + e.toString());
            } // catch (JSONException e)
        }
    }
}
