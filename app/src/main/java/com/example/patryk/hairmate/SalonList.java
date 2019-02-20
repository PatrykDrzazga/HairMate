package com.example.patryk.hairmate;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;


public class SalonList extends AppCompatActivity {

    ListView salonList;
    TextView infotext;
    Intent i;
    public static String str_where;
    public String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salon_list);
        /////
        infotext = (TextView) findViewById(R.id.list_salon_header);
        salonList = (ListView)findViewById(R.id.list_salon_listview);

        Intent i = getIntent();
        str_where = '"'+i.getStringExtra("city")+'"';

        String query = null;
        try {
            query = URLEncoder.encode(str_where, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        url = query;

        AsynDataClass jsonAsync = new AsynDataClass();
        jsonAsync.execute("");

        infotext.setText(getString(R.string.list_salon_header_text) + " " + i.getStringExtra("city"));

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SalonList.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private class AsynDataClass extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient(new BasicHttpParams());
            HttpPost httpPost = new HttpPost("http://webserwis.exone-web.pl/get_salon_city.php?city=" + url);
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
            final List<Salon> parsedObject = returnParsedJsonObject(result);
            SalonListAdapter jsonCustomAdapter = new SalonListAdapter(SalonList.this, parsedObject);
            salonList.setAdapter(jsonCustomAdapter);

            salonList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Salon salon = parsedObject.get(position);
                    Intent intent = new Intent(SalonList.this, SalonView.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("data", parsedObject.get(position));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

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

    private List<Salon> returnParsedJsonObject(String result){
        List<Salon> jsonObject = new ArrayList<Salon>();
        JSONObject resultObject = null;
        JSONArray jsonArray = null;
        Salon newItemObject = null;

        try {
            resultObject = new JSONObject(result);
            jsonArray = resultObject.optJSONArray("products");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonChildNode = null;
                try {
                    jsonChildNode = jsonArray.getJSONObject(i);
                    String id_salon = jsonChildNode.getString("id_salon");
                    String photo = jsonChildNode.getString("photo");
                    String name = jsonChildNode.getString("name");
                    String city = jsonChildNode.getString("city");
                    String address = jsonChildNode.getString("address");
                    String phone_number = jsonChildNode.getString("phone_number");
                    String email = jsonChildNode.getString("email");
                    String describtion = jsonChildNode.getString("describtion");

                    newItemObject = new Salon(id_salon, photo, name, city, address, email, phone_number, describtion);
                    jsonObject.add(newItemObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (NullPointerException e){
            AlertDialog alertDialog = new AlertDialog.Builder(SalonList.this).create();
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


