package com.sillebille.earthquakeviewer.ui.main;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.sillebille.earthquakeviewer.R;
import com.sillebille.earthquakeviewer.data.EarthQuakeListAdapter;
import com.sillebille.earthquakeviewer.data.EarthquakesModel;
import com.sillebille.earthquakeviewer.network.FetchResultQueue;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class EarthQuakeListFragment extends Fragment {

    final String TAG_NAME = EarthQuakeListFragment.class.getName();
    // Our API end point to get Earth Quake list in JSON format
    final String END_POINT = "earthquakesJSON";

    //Given params:
    //      ?formatted=true&north=44.1&south=-9.9&east=-22.4&west=55.2&username=mkoppelman
    final boolean FORMATTED = true;
    final double NORTH = 44.1;
    final double SOUTH = -9.9;
    final double EAST = -22.4;
    final double WEST = 55.2;
    final String USER = "mkoppelman";

    // Define the member variable to be shared in multiple methods
    private RecyclerView mRecyclerView;
    private ArrayList<EarthquakesModel.EarthQuake> mEarthQuakeDataList = new ArrayList<>();
    private EarthQuakeListAdapter mAdapter;
    private EarthquakesModel mViewModel;

    public static EarthQuakeListFragment newInstance() {
        return new EarthQuakeListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_fragment, container, false);

        mRecyclerView = rootView.findViewById(R.id.earthQuakeRecyclerView);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new EarthQuakeListAdapter(getActivity(), mEarthQuakeDataList);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EarthquakesModel.class);

        Log.d(TAG_NAME, "Debug: " + mViewModel.toString());
        Uri earthQuakeListUri = Uri.parse(getString(R.string.BASE_URL))
                .buildUpon()
                .appendPath(END_POINT)
                .appendQueryParameter("formatted", String.valueOf(FORMATTED))
                .appendQueryParameter("north", String.valueOf(NORTH))
                .appendQueryParameter("south", String.valueOf(SOUTH))
                .appendQueryParameter("east", String.valueOf(EAST))
                .appendQueryParameter("west", String.valueOf(WEST))
                .appendQueryParameter("username", USER)
                .build();
        String earthQuakeListUrl = earthQuakeListUri.toString();
        Log.d(TAG_NAME, "Built URL: " + earthQuakeListUrl);
        if (mViewModel.earthQuakes == null || mViewModel.earthQuakes.isEmpty()) {
            getEarthQuakeList(earthQuakeListUrl);
        } else {
            // If orientation changes, re-add the data from view model
            mEarthQuakeDataList.addAll(mViewModel.earthQuakes);
        }
    }

    /*
        Get earth quake list from the URL passed and map it to the POJO class EarthquakesModel
    */
    private void getEarthQuakeList(String url) {
        final InstanceCreator<EarthquakesModel> creator = new InstanceCreator<EarthquakesModel>() {
            public EarthquakesModel createInstance(Type type) {
                return mViewModel;
            }
        };
        VolleyLog.DEBUG = true;
        RequestQueue queue = FetchResultQueue.getInstance(getActivity()).getRequestQueue();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                VolleyLog.wtf(response.toString(), "utf-8");

                // Map gson to existing EarthquakesModel instance
                // Workaround: https://github.com/google/gson/issues/431
                Gson gson = new GsonBuilder().registerTypeAdapter(EarthquakesModel.class, creator).create();
                EarthquakesModel eModel = gson.fromJson(response.toString(), EarthquakesModel.class);

                // Add all the retrieved earthquake data to our List to be shown on recyclerview
                mEarthQuakeDataList.addAll(eModel.earthQuakes);
                mAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG_NAME, "Error response: " + error.toString());
                if (error instanceof NetworkError) {
                    Toast.makeText(getActivity(), getString(R.string.network_unavailable), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
        
        // Add the generated jsonObjectReqeust to the Volley queue
        queue.add(jsonObjectRequest);
    }
}