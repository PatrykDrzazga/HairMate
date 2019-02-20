package com.example.patryk.hairmate;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class ServiceTab extends Fragment{
    private static final String TAG = "ServiceTab";

    private TextView text;
    public String query;
    ListView serviceList;
    TextView price_text;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab2_fragment,container,false);

        serviceList = (ListView) view.findViewById(R.id.price_list_view);
        price_text = (TextView) view.findViewById(R.id.price_text);



        query = null;
        try {
            query = URLEncoder.encode('"'+ InformationTab.id+'"', "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        AsynDataClass jsonAsync = new AsynDataClass();
        jsonAsync.execute("");


        return view;
    }

    private class AsynDataClass extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient(new BasicHttpParams());
            HttpPost httpPost = new HttpPost("http://webserwis.exone-web.pl/get_prices.php?id_salon=" + query);
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
            final List<Service> parsedObject = returnParsedJsonObject(result);
            ServiceAdapter jsonCustomAdapter = new ServiceAdapter(getContext(), parsedObject);
            serviceList.setAdapter(jsonCustomAdapter);
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

    private List<Service> returnParsedJsonObject(String result){
        List<Service> jsonObject = new ArrayList<Service>();
        JSONObject resultObject = null;
        JSONArray jsonArray = null;
        Service newItemObject = null;

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
                    String id = jsonChildNode.getString("id_uslugi");
                    String name = jsonChildNode.getString("name");
                    String price = jsonChildNode.getString("price");

                    newItemObject = new Service(id,name,price);

                    jsonObject.add(newItemObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (NullPointerException e){
            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
            alertDialog.setTitle("Błąd!");
            alertDialog.setMessage("Brak połączenia z internetem!");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Zamknij", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getActivity().finish();
                }
            });
            alertDialog.show();
        }

        return jsonObject;

    }
}
