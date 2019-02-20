package com.example.patryk.hairmate;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    AutoCompleteTextView city;
    Button search_button;
    public List<String> cities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AsynDataClass jsonAsync = new AsynDataClass();
        jsonAsync.execute("");


        city = (AutoCompleteTextView) findViewById(R.id.main_activity_city_textview);
        search_button = (Button) findViewById(R.id.main_activity_search_button);

        city.setThreshold(1);


        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(city.getText().toString().trim().length() > 0 && cities.contains((city.getText().toString()))){
                    Intent intent = new Intent(MainActivity.this, SalonList.class);
                    intent.putExtra("city", city.getText().toString());
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(MainActivity.this, R.string.error_city_empty, Toast.LENGTH_LONG).show();
                }
            }
        });






    }


    private class AsynDataClass extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient(new BasicHttpParams());
            HttpPost httpPost = new HttpPost("http://webserwis.exone-web.pl/get_cities.php");
            String jsonResult = "";

            try {
                HttpResponse response = httpClient.execute(httpPost);
                jsonResult = inputStreamToString(response.getEntity().getContent()).toString();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return jsonResult;
        }

        @Override

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            List<String> parsedObject = returnParsedJsonObject(result);
            cities = returnParsedJsonObject(result);
            ArrayAdapter<String> adapter = new ArrayAdapter<String> (MainActivity.this, android.R.layout.select_dialog_item, cities);
            city.setAdapter(adapter);
        }

        private StringBuilder inputStreamToString(InputStream is) {
            String rLine = "";
            StringBuilder answer = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            try {
                while ((rLine = br.readLine()) != null) {
                    answer.append(rLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return answer;
        }
    }

    private List<String> returnParsedJsonObject(String result){
        List<String> jsonObject = new ArrayList<String>();
        JSONObject resultObject = null;
        JSONArray jsonArray = null;

        try {
            resultObject = new JSONObject(result);
            jsonArray = resultObject.optJSONArray("cities");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonChildNode = null;
                try {
                    jsonChildNode = jsonArray.getJSONObject(i);
                    String miasto = jsonChildNode.getString("city");
                    jsonObject.add(miasto);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (NullPointerException e){
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("Błąd!");
            alertDialog.setMessage("Brak połączenia z internetem!");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Zamknij", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            alertDialog.show();
        }
        return jsonObject;

    }
}