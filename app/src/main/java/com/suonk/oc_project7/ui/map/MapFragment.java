package com.suonk.oc_project7.ui.map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.suonk.oc_project7.R;
import com.suonk.oc_project7.databinding.FragmentMapBinding;
import com.suonk.oc_project7.ui.main.MainViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private MapViewModel mapsViewModel;
    private MainViewModel mainViewModel;
    private FragmentMapBinding binding;

    @NonNull
    public static MapFragment newInstance() {
        Bundle args = new Bundle();

        MapFragment fragment = new MapFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMapBinding.inflate(inflater, container, false);

        mapsViewModel = new ViewModelProvider(this).get(MapViewModel.class);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_fragment);
        if (mapFragment != null) {
            mapFragment.onCreate(savedInstanceState);
            mapFragment.getMapAsync(this);

            setupGoogleMap();
        }
    }

    private void jumpToLocation(LatLng location) {
        binding.jumpTo.setOnClickListener(view -> googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f)));
    }

    @SuppressLint("MissingPermission")
    private void setupGoogleMap() {
        mainViewModel.getPermissionsLiveData().observe(getViewLifecycleOwner(), isPermissionsEnabled -> googleMap.setMyLocationEnabled(isPermissionsEnabled));

        mapsViewModel.getMapViewStateLiveData().observe(getViewLifecycleOwner(), mapMarkers -> {
            for (MapMarker mapMarker : mapMarkers) {
                LatLng currentLatLng = new LatLng(mapMarker.getLatitude(), mapMarker.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions().position(currentLatLng).title(mapMarker.getRestaurantName()).snippet(mapMarker.getRestaurantAddress()).icon(BitmapDescriptorFactory.fromBitmap(getBitmap(getContext(), mapMarker.getMarkerIcon())));

                if (googleMap != null) {
                    googleMap.addMarker(markerOptions);

                    CameraPosition cameraPosition = new CameraPosition.Builder().target(currentLatLng).zoom(15).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    if (mapMarker.getMarkerIcon() == R.drawable.custom_google_marker_user) {
                        jumpToLocation(currentLatLng);
                    }
                }
            }
        });
    }

    private Bitmap getBitmap(VectorDrawable vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }

    private Bitmap getBitmap(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof VectorDrawable) {
            return getBitmap((VectorDrawable) drawable);
        } else {
            throw new IllegalArgumentException("unsupported drawable type");
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMapReady) {
        this.googleMap = googleMapReady;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}