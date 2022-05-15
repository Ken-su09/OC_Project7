package com.suonk.oc_project7.ui.maps;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.suonk.oc_project7.R;
import com.suonk.oc_project7.databinding.FragmentMapsBinding;
import com.suonk.oc_project7.model.data.places.Place;
import com.suonk.oc_project7.ui.main.MainViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MapsFragment extends Fragment {

    private GoogleMap googleMap;
    private FragmentMapsBinding binding;
    private MapsViewModel mapsViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMapsBinding.inflate(inflater, container, false);
        binding.mapView.onCreate(savedInstanceState);
        binding.mapView.onResume();

        mapsViewModel = new ViewModelProvider(this).get(MapsViewModel.class);
        setupGoogleMap();
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding.mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        binding.mapView.onLowMemory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @SuppressLint("MissingPermission")
    private void setupGoogleMap() {
        binding.mapView.getMapAsync(mMap -> {
            googleMap = mMap;
            googleMap.setMyLocationEnabled(true);

            mapsViewModel.getMapMakersLiveData().observe(getViewLifecycleOwner(), mapMarkers -> {
                for (MapMarker mapMaker : mapMarkers) {
                    LatLng currentLatLng = new LatLng(mapMaker.getLatitude(), mapMaker.getLongitude());
                    googleMap.addMarker(new MarkerOptions()
                            .position(currentLatLng)
                            .title(mapMaker.getRestaurantName())
                            .snippet("Marker Description")
//                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_facebook)));
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(currentLatLng).zoom(15).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            });
        });
    }
}