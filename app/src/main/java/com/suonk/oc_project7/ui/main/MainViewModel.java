package com.suonk.oc_project7.ui.main;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.suonk.oc_project7.repositories.current_location.CurrentLocationRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;

@HiltViewModel
public class MainViewModel extends ViewModel {

    @NonNull
    private final CurrentLocationRepository locationRepository;

    private final MutableLiveData<Boolean> isPermissionEnabledLiveData = new MutableLiveData<>();

    private final Context context;

    @Inject
    public MainViewModel(@NonNull CurrentLocationRepository locationRepository, @ApplicationContext Context context) {
        this.locationRepository = locationRepository;
        this.context = context;
    }

    public void onStart() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationRepository.startLocationUpdates();
            isPermissionEnabledLiveData.setValue(true);
        } else {
            locationRepository.stopLocationUpdates();
            isPermissionEnabledLiveData.setValue(false);
        }
    }

    public void onStop() {
        locationRepository.stopLocationUpdates();
    }

    public LiveData<Boolean> getPermissionsLiveData() {
        return isPermissionEnabledLiveData;
    }
}
