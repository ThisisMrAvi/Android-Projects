package com.mravi.earthquakedata;

import android.util.Log;

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
import java.nio.charset.Charset;
import java.util.ArrayList;

class QueryData {
    private static final String LOG_TAG = QueryData.class.getSimpleName();

    private QueryData() {

    }

    private static URL createURL(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem in Url", e);
            return null;
        }
        return url;
    }

    static ArrayList<Earthquake> extractEarthquakes(String inputUrl) {
        ArrayList<Earthquake> earthquakes = new ArrayList<>();

        try {
            String baseJson = makeHttpRequest(createURL(inputUrl));
            JSONObject baseJsonResponse = new JSONObject(baseJson);
            JSONArray earthquakeArray = baseJsonResponse.getJSONArray("features");
            for (int i = 0; i < earthquakeArray.length(); i++) {
                JSONObject currentEarthquake = earthquakeArray.getJSONObject(i);
                JSONObject properties = currentEarthquake.getJSONObject("properties");
                double magnitude = properties.getDouble("mag");
                String location = properties.getString("place");
                String date = properties.getString("time");
                String url = properties.getString("url");
                Earthquake earthquake = new Earthquake(magnitude, location, date, url);
                earthquakes.add(earthquake);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error in earthquake JSON data parsing", e);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error in earthquake JSON data parsing", e);
        }

        return earthquakes;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error occured: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem in Http Connection", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line);
                line = bufferedReader.readLine();
            }

        }
        return stringBuilder.toString();
    }
}
