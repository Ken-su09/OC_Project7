package com.suonk.oc_project7.ui.main;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.LocationCallback;
import com.suonk.oc_project7.repositories.current_location.CurrentLocationRepository;
import com.suonk.oc_project7.utils.TestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MainViewModelTest {

    @Rule
    public final InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private final Application application = Mockito.mock(Application.class);

    private final CurrentLocationRepository locationRepository = Mockito.mock(CurrentLocationRepository.class);
    private final MutableLiveData<Boolean> isPermissionEnabledLiveData = new MutableLiveData<>();

    private MainViewModel viewModel;

    @Before
    public void setup() {
        Mockito.doReturn(isPermissionEnabledLiveData).when(locationRepository).getLocationMutableLiveData();

        isPermissionEnabledLiveData.setValue(true);

        viewModel = new MainViewModel(locationRepository, application);
    }

    @Test
    public void start_location_updates_permissions_enabled_should_true() {
        // GIVEN

        // WHEN
        viewModel.onStart();

        // WHEN
        viewModel.onStop();

        // THEN
        boolean isPermissionsEnabled = TestUtils.getValueForTesting(viewModel.getPermissionsLiveData());
        assertTrue(isPermissionsEnabled);
    }

    @Test
    public void start_location_updates_stop_then_permissions_enabled_should_false() {
        // GIVEN
        viewModel.onStart();

        // WHEN
        viewModel.onStop();

        // THEN
        boolean isPermissionsEnabled = TestUtils.getValueForTesting(viewModel.getPermissionsLiveData());
        assertFalse(isPermissionsEnabled);
    }

    public void onStop() {
        if (ActivityCompat.checkSelfPermission(application, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(application, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationRepository.startLocationUpdates();
            isPermissionEnabledLiveData.setValue(true);
        } else {
            locationRepository.stopLocationUpdates();
            isPermissionEnabledLiveData.setValue(false);
        }
    }
}