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
import android.widget.Button;
import android.widget.EditText;
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

public class OpinionsTab extends Fragment{


    private static final String TAG = "OpinionsTab";
    TextView error_empty;
    public static EditText title_opinion, content_opinion, author_opinion;
    Button add_opinion;
    ListView opinionList;
    public String query;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab3_fragment,container,false);

        opinionList = (ListView) view.findViewById(R.id.opinion_listview);
        error_empty = (TextView) view.findViewById(R.id.error_empty);


        query = null;
        try {
            query = URLEncoder.encode('"'+ InformationTab.id+'"', "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        AsynDataClass jsonAsync = new AsynDataClass();
        jsonAsync.execute("");


        /////////DODAWANIE OPINII/////
        title_opinion = (EditText) view.findViewById(R.id.title_opinion);
        content_opinion = (EditText) view.findViewById(R.id.edit_text_opinion);
        author_opinion = (EditText) view.findViewById(R.id.opinion_author);
        add_opinion = (Button) view.findViewById(R.id.add_opinion_button);


        add_opinion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new WriteOpinion().execute();
                error_empty.setVisibility(View.GONE);
                AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                alertDialog.setTitle("Informacja");
                alertDialog.setMessage("Dziękujemy za dodanie opinii!");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Zamknij", new DialogInterface.OnClickListener() {
                    @Override
                        public void onClick(DialogInterface dialog, int which) {
                            title_opinion.setText("");
                            content_opinion.setText("");
                            author_opinion.setText("");
                            AsynDataClass jsonAsync = new AsynDataClass();
                            jsonAsync.execute("");
                        }
                    });
                    alertDialog.show();
                }

        });



        return view;
    }


    private class AsynDataClass extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient(new BasicHttpParams());
            HttpPost httpPost = new HttpPost("http://webserwis.exone-web.pl/get_opinions.php?id_salon=" + query);
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
            final List<Opinion> parsedObject = returnParsedJsonObject(result);
            OpinionAdapter jsonCustomAdapter = new OpinionAdapter(getContext(), parsedObject);
            opinionList.setAdapter(jsonCustomAdapter);
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

    private List<Opinion> returnParsedJsonObject(String result){
        List<Opinion> jsonObject = new ArrayList<Opinion>();
        JSONObject resultObject = null;
        JSONArray jsonArray = null;
        Opinion newItemObject = null;

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
                    String id = jsonChildNode.getString("id_opinia");
                    String title = jsonChildNode.getString("title");
                    String content = jsonChildNode.getString("content");
                    String author = jsonChildNode.getString("author");
                    String date = jsonChildNode.getString("data");
                    newItemObject = new Opinion(id, title, content, author, date);

                    jsonObject.add(newItemObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (NullPointerException e){
            error_empty.setVisibility(View.VISIBLE);
            error_empty.setText("Brak opinii o tym salonie!");
        }

        return jsonObject;

    }
}
