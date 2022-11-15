package com.suonk.oc_project7.ui.maps;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.suonk.oc_project7.R;
import com.suonk.oc_project7.ui.main.MainViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MapsFragment extends SupportMapFragment {

    private GoogleMap googleMap;
    private MapsViewModel mapsViewModel;
    private MainViewModel mainViewModel;

    @NonNull
    public static MapsFragment newInstance() {
        Bundle args = new Bundle();

        MapsFragment fragment = new MapsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapsViewModel = new ViewModelProvider(this).get(MapsViewModel.class);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        setupGoogleMap();
    }

    @SuppressLint("MissingPermission")
    private void setupGoogleMap() {
        getMapAsync(mMap -> {
            googleMap = mMap;
            mainViewModel.getPermissionsLiveData().observe(getViewLifecycleOwner(), isPermissionsEnabled -> googleMap.setMyLocationEnabled(isPermissionsEnabled));

            mapsViewModel.getMapMakersLiveData().observe(getViewLifecycleOwner(), mapMarkers -> {
                for (MapMarker mapMaker : mapMarkers) {
                    LatLng currentLatLng = new LatLng(mapMaker.getLatitude(), mapMaker.getLongitude());
                    googleMap.addMarker(new MarkerOptions()
                            .position(currentLatLng)
                            .title(mapMaker.getRestaurantName())
                            .snippet("Marker Description")
                            .icon(BitmapDescriptorFactory.fromBitmap(getBitmap(getContext(), R.drawable.ic_custom_google_marker_blue))));

                    CameraPosition cameraPosition = new CameraPosition.Builder().target(currentLatLng).zoom(15).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            });

            mapsViewModel.getCameraPositionSingleEvent().observe(getViewLifecycleOwner(), currentLatLng -> {
                if (currentLatLng != null) {
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(currentLatLng).zoom(15).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            });
        });
    }

    private Bitmap getBitmap(VectorDrawable vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
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
}