package com.example.garvit.sunshine;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText= findViewById(R.id.editText);
        resultText=findViewById(R.id.resultTextView);
    }
    public void getWeather(View view)
    {
        String encodeCityName= null;
        try {
            encodeCityName = URLEncoder.encode(editText.getText().toString(),"UTF-8");
            DownloadTask task=new DownloadTask();
            task.execute("http://openweathermap.org/data/2.5/weather?q="+ encodeCityName +"&appid=b6907d289e10d714a6e88b30761fae22");
            InputMethodManager mgr= (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(editText.getWindowToken(),0);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Could not find weather :(", Toast.LENGTH_SHORT).show();
        }

    }




    public class DownloadTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection connection;
            try {
                url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1) {
                    char currrent = (char) data;
                    result += currrent;
                    data = reader.read();

                }
                return result;

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Could not find weather :(", Toast.LENGTH_SHORT).show();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String weatherinfo = jsonObject.getString("weather");
                Log.i("weather", weatherinfo);
                JSONArray array = new JSONArray(weatherinfo);
                String message="";
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonpart = array.getJSONObject(i);
                    String main = jsonpart.getString("main");
                    String description = jsonpart.getString("description");
                    Log.i("main", jsonpart.getString("main"));
                    Log.i("description", jsonpart.getString("description"));
                    if (!main.equals("") && !description.equals("")) {
                        message += main + ": " + description + "\r\n";
                    }
                }
                    if (!message.equals("")){
                        resultText.setText(message);
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Could not find weather :(", Toast.LENGTH_SHORT).show();
                    }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Could not find weather :(", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
