package com.mravi.earthquakedata;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Earthquake>>, SharedPreferences.OnSharedPreferenceChangeListener {


    private static final String USGS_URL = "https://earthquake.usgs.gov/fdsnews/event/1/query";

    private static final int EARTHQUAKE_LOADER_ID = 1;
    private TextView mEmptyTextView;
    private EarthquakeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView earthquakeListView = findViewById(R.id.list);
        mEmptyTextView = findViewById(R.id.empty_view);
        earthquakeListView.setEmptyView(mEmptyTextView);
        // Obtain a reference to the SharedPreferences file for this app
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        // And register to be notified of preference changes
        // So we know when the user has adjusted the query settings
        prefs.registerOnSharedPreferenceChangeListener(this);

        mAdapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());
        earthquakeListView.setAdapter(mAdapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Earthquake currentEarthquake = mAdapter.getItem(position);
                Uri earthquakeUri = null;
                if (currentEarthquake != null) {
                    earthquakeUri = Uri.parse(currentEarthquake.getUrl());
                }
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);
                startActivity(websiteIntent);
            }
        });
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network = null;
        if (connectivityManager != null) {
            network = connectivityManager.getActiveNetwork();
        }
        if (network != null) {
            android.app.LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null,this);
        } else {
            View progressIndicator = findViewById(R.id.progress_indicator);
            progressIndicator.setVisibility(View.GONE);
            mEmptyTextView.setText(R.string.no_internet_connection);
        }
    }

    @NonNull
    @Override
    public Loader<List<Earthquake>> onCreateLoader(int id, Bundle bundle) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude = sharedPrefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        Uri baseUri = Uri.parse(USGS_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "10");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", orderBy);

        return new EarthquakeLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(android.content.Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {
        View progressIndicator = findViewById(R.id.progress_indicator);
        progressIndicator.setVisibility(View.GONE);
        mEmptyTextView.setText(R.string.no_earthquake);
        if (earthquakes != null && !earthquakes.isEmpty()) {
            mAdapter.addAll(earthquakes);
        }
    }


    @Override
    public void onLoaderReset(android.content.Loader<List<Earthquake>> loader) {
        mAdapter.clear();
    }

    @Override
    // This method initialize the contents of the Activity's options menu.
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the Options Menu we specified in XML
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    // This method is called whenever an item in the options menu is selected.
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        if (key.equals(getString(R.string.settings_min_magnitude_key)) ||
                key.equals(getString(R.string.settings_order_by_key))){
            // Clear the ListView as a new query will be kicked off
            mAdapter.clear();

            // Hide the empty state text view as the loading indicator will be displayed
            mEmptyTextView.setVisibility(View.GONE);

            // Show the loading indicator while new data is being fetched
            View loadingIndicator = findViewById(R.id.progress_indicator);
            loadingIndicator.setVisibility(View.VISIBLE);

            // Restart the loader to requery the USGS as the query settings have been updated
            getLoaderManager().restartLoader(EARTHQUAKE_LOADER_ID, null, this);
        }
    }
}
