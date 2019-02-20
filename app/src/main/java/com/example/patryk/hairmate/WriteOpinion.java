package com.example.patryk.hairmate;


import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class WriteOpinion extends AsyncTask<String, String, String> {

        JSONParser jsonParser = new JSONParser();
        private static final String TAG_SUCCESS = "success";
        private static String url_create_product = "http://webserwis.exone-web.pl/create_opinion.php";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {
            String id_salon = InformationTab.id;
            String title = OpinionsTab.title_opinion.getText().toString();
            String content = OpinionsTab.content_opinion.getText().toString();
            String author = OpinionsTab.author_opinion.getText().toString();


            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id_salon", id_salon));
            params.add(new BasicNameValuePair("title", title));
            params.add(new BasicNameValuePair("content", content));
            params.add(new BasicNameValuePair("author", author));


            JSONObject json = jsonParser.makeHttpRequest(url_create_product,
                    "POST", params);


            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    System.out.println("Dodano opinie");

                } else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {

        }

    }


