package com.suonk.oc_project7.ui.main;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.suonk.oc_project7.repositories.location.LocationRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;

@HiltViewModel
public class MainViewModel extends ViewModel {

    @NonNull
    private final LocationRepository locationRepository;

    private final MutableLiveData<Boolean> isPermissionEnabledLiveData = new MutableLiveData<>();

    private final Context context;

    @Inject
    public MainViewModel(@NonNull LocationRepository locationRepository, @ApplicationContext Context context) {
        this.locationRepository = locationRepository;
        this.context = context;
    }

    public void onStart() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationRepository.startLocationUpdate();
            isPermissionEnabledLiveData.setValue(true);
        } else {
            locationRepository.stopLocationUpdate();
            isPermissionEnabledLiveData.setValue(false);
        }
    }

    public void onStop() {
        locationRepository.stopLocationUpdate();
    }

    public LiveData<Boolean> getPermissionsLiveData() {
        return isPermissionEnabledLiveData;
    }
}
