package com.example.gmodsv1;

import android.os.AsyncTask;
import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.Method;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DatabaseUtils {
//    static OkHttpClient client = new OkHttpClient();
//    public static JSONArray search(String queryStr, Integer numResults) throws IOException, JSONException {
//        String url = "https://5c4c-89-187-162-120.ngrok.io/search?";
//        String fetchUrl = url + "querystr=" + queryStr + "&results=" + numResults;
//        Response response = null;
//
//        Request request = new Request.Builder()
//                .url(url)
//                .build();
//
//        response = client.newCall(request).execute();
//
//        assert response.body() != null;
//        return new JSONArray(response.body().string());
//    }
//    class HttpRequest extends AsyncTask<Pair<Request, Method>, Void, Pair<Response, Method>> {
//
//        @Override
//        protected Response doInBackground(Pair<Request, Method>... requests) {
//            Response response = null;
//            try {
//                response = client.newCall(requests[0].first).execute();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return response;
//        }
//
//        @Override
//        protected void onPostExecute(Pair<Response, Method> response) {
//            super.onPostExecute(response);
//
//            Object[] parameters = new Object[1];
//            parameters[0] = response;
//            method.invoke(object, parameters);
//        }
//    }
}
