package com.sillebille.earthquakeviewer.ui.main;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class EarthQuakeListFragment extends Fragment implements EarthQuakeListAdapter.OnEarthQuakeClickListener {

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
    private ArrayList<EarthquakesModel.EarthQuake> mEarthQuakeDataList = new ArrayList<>();
    private EarthQuakeListAdapter mAdapter;
    private EarthquakesModel mViewModel;
    private SwipeRefreshLayout mSwipeContainer;
    String mEarthQuakeListUrl;

    public static EarthQuakeListFragment newInstance() {
        return new EarthQuakeListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_fragment, container, false);


        RecyclerView recyclerView = rootView.findViewById(R.id.earthQuakeRecyclerView);
        // Lookup the swipe container view
        mSwipeContainer = rootView.findViewById(R.id.main);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // Set the adapter to fill our recycler view
        mAdapter = new EarthQuakeListAdapter(getActivity(), mEarthQuakeDataList, this);
        recyclerView.setAdapter(mAdapter);

        // Setup refresh listener to allow users to refresh data manually
        mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG_NAME, "Refreshing.... URL: " + mEarthQuakeListUrl);
                mAdapter.clear();
                getEarthQuakeList(mEarthQuakeListUrl);

                // Timeout for hiding the pull-to-refresh animation
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        // Stop animation (This will be after 5 seconds)
                        mSwipeContainer.setRefreshing(false);
                    }
                }, 5000); // Delay in millis
            }
        });

        // Configure the refreshing colors
        mSwipeContainer.setColorSchemeResources(
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_blue_light);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EarthquakesModel.class);

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
        mEarthQuakeListUrl = earthQuakeListUri.toString();
        Log.d(TAG_NAME, "Built URL: " + mEarthQuakeListUrl);

        // Hit the API end point IFF the data is not available
        if (mViewModel.earthQuakes == null || mViewModel.earthQuakes.isEmpty()) {
            getEarthQuakeList(mEarthQuakeListUrl);
        } else {
            // If orientation changes, re-add the data from view model ONLY IF the list is empty
            if (mEarthQuakeDataList.isEmpty()) {
                mEarthQuakeDataList.addAll(mViewModel.earthQuakes);
            }
        }
        getActivity().setTitle(getString(R.string.app_name));
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

                // Map gson to existing EarthquakesModel instance, to store the data inside ViewModel.
                // That is, storing inside ViewModel lets us reuse the data if device orientation changes
                // Workaround: https://github.com/google/gson/issues/431
                Gson gson = new GsonBuilder().registerTypeAdapter(EarthquakesModel.class, creator).create();
                EarthquakesModel eModel = gson.fromJson(response.toString(), EarthquakesModel.class);

                // Add all the retrieved earthquake data to our List to be shown on recyclerview
                mEarthQuakeDataList.addAll(eModel.earthQuakes);
                mAdapter.notifyDataSetChanged();
                mSwipeContainer.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG_NAME, "Error response: " + error.toString());
                mSwipeContainer.setRefreshing(false);
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

    @Override
    public void onItemClick(int position) {
        Log.d(TAG_NAME, "Postion: " + position);
        // Since we use the mEarthQuakeDataList to fill the adapter, we can reuse it to get the
        // selected item
        EarthquakesModel.EarthQuake selectedItem = mEarthQuakeDataList.get(position);

        // Create the MapsFragment and parcel the selected item into a bundle
        Fragment mapsFragment = new MapsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("selected_item", selectedItem);
        mapsFragment.setArguments(bundle);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

        // Replace whatever is in the container view with the Map fragment,
        // and add the transaction to the back stack
        transaction.replace(R.id.container, mapsFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Hack: When configuration changes, the swipe refresh does not stop loading.
        // Set refreshing to false forcefully
        if(newConfig.getLayoutDirection() == Configuration.ORIENTATION_LANDSCAPE){
            if(mSwipeContainer.isRefreshing())
                mSwipeContainer.setRefreshing(false);
        }
    }
}