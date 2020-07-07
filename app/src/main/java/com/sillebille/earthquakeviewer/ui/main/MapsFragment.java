package com.sillebille.earthquakeviewer.ui.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sillebille.earthquakeviewer.R;
import com.sillebille.earthquakeviewer.data.EarthquakesModel;

public class MapsFragment extends Fragment {
    private final String TAG_NAME = MapsFragment.class.getName();
    private double mLatitude;
    private double mLongitude;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we add a marker to the selected EarthQuake item.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            LatLng selectedLocation = new LatLng(mLatitude, mLongitude);
            googleMap.addMarker(new MarkerOptions()
                    .position(selectedLocation)
                    .title("Selected EarthQuake Location"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(selectedLocation));
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Bundle bundle=getArguments();
        EarthquakesModel.EarthQuake selectedItem;

        //here is your list array
        if(bundle != null) {
            selectedItem = bundle.getParcelable("selected_item");
            assert selectedItem != null;
            mLatitude = selectedItem.latitude;
            mLongitude = selectedItem.longitude;
            Log.d(TAG_NAME, "Latitude: " +mLatitude + " Longitude: " + mLongitude);
        }
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}