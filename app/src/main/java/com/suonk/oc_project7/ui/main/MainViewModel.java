package com.suonk.oc_project7.ui.main;

import static com.suonk.oc_project7.utils.Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static com.suonk.oc_project7.utils.Constants.PERMISSIONS_REQUEST_ENABLE_GPS;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.suonk.oc_project7.repositories.current_location.CurrentLocationRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;

@HiltViewModel
public class MainViewModel extends ViewModel {

    @NonNull
    private final CurrentLocationRepository locationRepository;

    private final MutableLiveData<Boolean> isPermissionEnabledLiveData = new MutableLiveData<>();

    private MutableLiveData<MainViewState> mainViewStateLiveData = new MutableLiveData<>();

    private final Context context;

    private final FirebaseAuth auth;

    @Inject
    public MainViewModel(@NonNull CurrentLocationRepository locationRepository, @ApplicationContext Context context, FirebaseAuth auth) {
        this.locationRepository = locationRepository;
        this.context = context;
        this.auth = auth;
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

    public void onResume() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            isPermissionEnabledLiveData.setValue(false);
        }
    }

    public void onStop() {
        locationRepository.stopLocationUpdates();
    }

    public LiveData<MainViewState> getMainViewStateLiveData() {
        if (auth.getCurrentUser() != null) {
            mainViewStateLiveData.setValue(new MainViewState(
                    auth.getCurrentUser().getDisplayName(),
                    auth.getCurrentUser().getEmail(),
                    auth.getCurrentUser().getPhotoUrl().toString()
            ));
        }

        return mainViewStateLiveData;
    }

    public LiveData<Boolean> getPermissionsLiveData() {
        return isPermissionEnabledLiveData;
    }
}