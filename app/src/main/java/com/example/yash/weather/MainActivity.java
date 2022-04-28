package com.example.yash.weather;

import android.content.Context;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    EditText cityName;
    TextView display;
    public void execute(View view)
    {   try {
        downloadTask task = new downloadTask();
        String encoded = URLEncoder.encode(cityName.getText().toString(), "UTF-8");
        task.execute("https://openweathermap.org/data/2.5/weather?q=" + encoded +"&appid=439d4b804bc8187953eb36d2a8c26a02");
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(cityName.getWindowToken(), 0);
    }catch(Exception e)
    {
        e.printStackTrace();
        Toast.makeText(MainActivity.this, "Can not find Weather!", Toast.LENGTH_SHORT).show();
    }
    }
    public class downloadTask extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection connection = null;
            try
            {
              url = new URL(urls[0]);
              connection = (HttpURLConnection) url.openConnection();
                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while(data != -1)
                {
                    char current = (char) data;
                    result += current;
                    data = reader.read();

                }
                return result;
            }catch(Exception e)
            {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Can not find Weather!", Toast.LENGTH_SHORT).show();
                return null;
            }
        }
        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String weather = jsonObject.getString("weather");
                Log.i("weather", weather);
                JSONArray arr = new JSONArray(weather);
                for(int i = 0; i < arr.length();i++)
                {
                    JSONObject jsonPart = arr.getJSONObject(i);
                    String main = jsonPart.getString("main");
                    String description = jsonPart.getString("description");
                    if(!main.equals("") && !description.equals("")) {
                        display.setText(main + ": " + description + "\r\n");
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Can not find Weather!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityName = findViewById(R.id.cityName);
        display = findViewById(R.id.displayWeather);



    }
}
