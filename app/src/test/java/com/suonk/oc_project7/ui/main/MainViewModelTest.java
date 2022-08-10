package com.suonk.oc_project7.ui.main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.suonk.oc_project7.model.data.permission_checker.PermissionChecker;
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

    private MainViewModel viewModel;

    private final FirebaseAuth auth = Mockito.mock(FirebaseAuth.class);
    private final FirebaseUser firebaseUser = Mockito.mock(FirebaseUser.class);
    private final Application application = Mockito.mock(Application.class);
    private final CurrentLocationRepository locationRepository = Mockito.mock(CurrentLocationRepository.class);
    private final PermissionChecker permissionChecker = Mockito.mock(PermissionChecker.class);

    private static final String DEFAULT_NAME = "DEFAULT_NAME";
    private static final String DEFAULT_MAIL = "DEFAULT_MAIL";
    private static final String PICTURE_URL = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&key=key&photo_reference=PHOTO_REFERENCE";

    @NonNull
    private final MutableLiveData<Boolean> isPermissionEnabledLiveData = new MutableLiveData<>();
    private final MutableLiveData<MainViewState> mainViewStateLiveData = new MutableLiveData<>();

    @Before
    public void setup() {
        doNothing().when(locationRepository).startLocationUpdates();
        doNothing().when(locationRepository).stopLocationUpdates();

        doReturn(true).when(permissionChecker).hasFineLocationPermission();
//        doReturn(true).when(permissionChecker).hasCoarseLocationPermission();

        doReturn(firebaseUser).when(auth).getCurrentUser();
        doReturn(DEFAULT_NAME).when(firebaseUser).getDisplayName();
        doReturn(DEFAULT_MAIL).when(firebaseUser).getEmail();
//        doReturn(myURI).when(firebaseUser).getPhotoUrl();

        isPermissionEnabledLiveData.setValue(true);
        mainViewStateLiveData.setValue(getDefaultMainViewState());

        viewModel = new MainViewModel(locationRepository, auth, permissionChecker, application);

        verify(auth).getCurrentUser();
    }

    @Test
    public void on_start_permissions_enabled_should_true() {
        // GIVEN

        // WHEN
        viewModel.onStart();

        // THEN
        boolean isPermissionsEnabled = TestUtils.getValueForTesting(viewModel.getPermissionsLiveData());
        assertTrue(isPermissionsEnabled);

        verify(locationRepository).startLocationUpdates();
        verify(permissionChecker).hasFineLocationPermission();

        Mockito.verifyNoMoreInteractions(locationRepository, permissionChecker, application);
    }

    @Test
    public void on_start_permissions_enabled_should_true_with_fine_permission_false() {
        // GIVEN
        doReturn(false).when(permissionChecker).hasFineLocationPermission();
        doReturn(true).when(permissionChecker).hasCoarseLocationPermission();

        // WHEN
        viewModel.onStart();

        // THEN
        boolean isPermissionsEnabled = TestUtils.getValueForTesting(viewModel.getPermissionsLiveData());
        assertTrue(isPermissionsEnabled);

        verify(locationRepository).startLocationUpdates();
//        verify(locationRepository).stopLocationUpdates();
        verify(permissionChecker).hasFineLocationPermission();
        verify(permissionChecker).hasCoarseLocationPermission();

        Mockito.verifyNoMoreInteractions(locationRepository, permissionChecker, application);
    }

    @Test
    public void on_start_permissions_enabled_should_false_with_both_permissions_false() {
        // GIVEN
        doReturn(false).when(permissionChecker).hasFineLocationPermission();
        doReturn(false).when(permissionChecker).hasCoarseLocationPermission();

        // WHEN
        viewModel.onStart();

        // THEN
        boolean isPermissionsEnabled = TestUtils.getValueForTesting(viewModel.getPermissionsLiveData());
        assertFalse(isPermissionsEnabled);

        verify(locationRepository).stopLocationUpdates();
        verify(permissionChecker).hasFineLocationPermission();
        verify(permissionChecker).hasCoarseLocationPermission();

        Mockito.verifyNoMoreInteractions(locationRepository, permissionChecker, application);
    }

    @Test
    public void on_stop_then_permissions_enabled_should_false() {
        // GIVEN

        // WHEN
        viewModel.onStop();
        boolean isPermissionsEnabled = TestUtils.getValueForTesting(viewModel.getPermissionsLiveData());

        // THEN
        assertNotNull(isPermissionsEnabled);
        assertFalse(isPermissionsEnabled);
    }

    @Test
    public void on_resume_permissions_enabled_should_true() {
        // GIVEN

        // WHEN
        viewModel.onResume();

        // THEN
        boolean isPermissionsEnabled = TestUtils.getValueForTesting(viewModel.getPermissionsLiveData());
        assertTrue(isPermissionsEnabled);

        verify(permissionChecker).hasFineLocationPermission();

        Mockito.verifyNoMoreInteractions(locationRepository, permissionChecker, application);
    }

    @Test
    public void on_resume_permissions_enabled_should_true_with_fine_permission_false() {
        // GIVEN
        doReturn(false).when(permissionChecker).hasFineLocationPermission();
        doReturn(true).when(permissionChecker).hasCoarseLocationPermission();

        // WHEN
        viewModel.onResume();

        // THEN
        boolean isPermissionsEnabled = TestUtils.getValueForTesting(viewModel.getPermissionsLiveData());
        assertTrue(isPermissionsEnabled);

        verify(permissionChecker).hasFineLocationPermission();
        verify(permissionChecker).hasCoarseLocationPermission();

        Mockito.verifyNoMoreInteractions(locationRepository, permissionChecker, application);
    }

    @Test
    public void on_resume_permissions_enabled_should_false_with_both_permissions_false() {
        // GIVEN
        doReturn(false).when(permissionChecker).hasFineLocationPermission();
        doReturn(false).when(permissionChecker).hasCoarseLocationPermission();

        // WHEN
        viewModel.onResume();

        // THEN
        boolean isPermissionsEnabled = TestUtils.getValueForTesting(viewModel.getPermissionsLiveData());
        assertFalse(isPermissionsEnabled);

        verify(permissionChecker).hasFineLocationPermission();
        verify(permissionChecker).hasCoarseLocationPermission();

        Mockito.verifyNoMoreInteractions(locationRepository, permissionChecker, application);
    }

    @Test
    public void get_main_view_state_live_data() {
        // GIVEN

        // WHEN
        MainViewState mainViewState = TestUtils.getValueForTesting(viewModel.getMainViewStateLiveData());

        // THEN
        assertNotNull(mainViewState);
        assertEquals(getDefaultMainViewState(), mainViewState);

        Mockito.verifyNoMoreInteractions(locationRepository, permissionChecker, application);
    }


    private MainViewState getDefaultMainViewState() {
        return new MainViewState(
                DEFAULT_NAME,
                DEFAULT_MAIL,
                ""
        );
//        PICTURE_URL.toString()
    }
}