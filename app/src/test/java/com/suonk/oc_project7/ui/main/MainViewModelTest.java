package com.suonk.oc_project7.ui.main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.suonk.oc_project7.model.data.permission_checker.PermissionChecker;
import com.suonk.oc_project7.model.data.place_auto_complete.PlaceAutocomplete;
import com.suonk.oc_project7.model.data.places.CurrentLocation;
import com.suonk.oc_project7.model.data.restaurant.Restaurant;
import com.suonk.oc_project7.model.data.user.CustomFirebaseUser;
import com.suonk.oc_project7.repositories.current_location.CurrentLocationRepository;
import com.suonk.oc_project7.repositories.current_user_search.CurrentUserSearchRepository;
import com.suonk.oc_project7.repositories.places.PlacesRepository;
import com.suonk.oc_project7.repositories.restaurants.RestaurantsRepository;
import com.suonk.oc_project7.repositories.user.UserRepository;
import com.suonk.oc_project7.utils.TestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.Silent.class)
public class MainViewModelTest {

    @Rule
    public final InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private MainViewModel viewModel;

    //region ============================================= MOCK =============================================

    private final CurrentLocationRepository currentLocationRepositoryMock = Mockito.mock(CurrentLocationRepository.class);
    private final RestaurantsRepository restaurantsRepositoryMock = Mockito.mock(RestaurantsRepository.class);
    private final UserRepository userRepositoryMock = Mockito.mock(UserRepository.class);
    private final PlacesRepository placesRepositoryMock = Mockito.mock(PlacesRepository.class);
    private final CurrentUserSearchRepository currentUserSearchRepositoryMock = Mockito.mock(CurrentUserSearchRepository.class);
    private final PermissionChecker permissionChecker = Mockito.mock(PermissionChecker.class);

    //endregion

    //region ======================================== DEFAULTS VALUES =======================================

    private static final String RESTAURANT_NAME = "PIZZA HUT";
    private static final String RESTAURANT_NAME_1 = "PIZZA N PASTA";
    private static final String RESTAURANT_NAME_2 = "PASTA";

    private static final String TEXT_TO_HIGHLIGHT = "PIZ";

    private static final int start = 0;
    private static final int end = 3;

    private static final String ADDRESS = "ADDRESS";
    private static final String PHOTO_REFERENCE = "PHOTO_REFERENCE";

    private static final String DEFAULT_ID = "DEFAULT_ID";
    private static final String DEFAULT_NAME = "DEFAULT_NAME";
    private static final String DEFAULT_MAIL = "DEFAULT_MAIL";
    private static final String PICTURE_URL = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&key=key&photo_reference=" + PHOTO_REFERENCE;

    private static final String DEFAULT_LANGUAGE = "fr";

    private static final double DEFAULT_LATITUDE = 48.9575329;
    private static final double DEFAULT_LONGITUDE = 2.5594583;
    private static final String DEFAULT_LAT_LNG = DEFAULT_LATITUDE + "," + DEFAULT_LONGITUDE;

    //endregion

    //region =========================================== LIVEDATA ===========================================

    private final MutableLiveData<Boolean> isPermissionEnabledLiveData = new MutableLiveData<>();
    private final MutableLiveData<MainViewState> mainViewStateLiveData = new MutableLiveData<>();

    private final MutableLiveData<List<MainItemViewState>> mainItemViewStatesLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<PlaceAutocomplete>> placesAutocompleteLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> searchInputLiveData = new MutableLiveData<>();

    private final MutableLiveData<CurrentLocation> currentLocationMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Restaurant>> restaurantsMutableLiveData = new MutableLiveData<>();

    //endregion

    @Before
    public void setup() {
        doNothing().when(currentLocationRepositoryMock).startLocationUpdates();
        doNothing().when(currentLocationRepositoryMock).stopLocationUpdates();

        doReturn(true).when(permissionChecker).hasCoarseLocationPermission();
        doReturn(true).when(permissionChecker).hasFineLocationPermission();

        doReturn(currentLocationMutableLiveData).when(currentLocationRepositoryMock).getLocationMutableLiveData();
        doReturn(restaurantsMutableLiveData).when(restaurantsRepositoryMock).getNearRestaurants(DEFAULT_LAT_LNG);

        doReturn(placesAutocompleteLiveData).when(placesRepositoryMock).getPlacesAutocomplete(DEFAULT_LAT_LNG, DEFAULT_LANGUAGE, TEXT_TO_HIGHLIGHT);

        doReturn(getCustomFirebaseUser()).when(userRepositoryMock).getCustomFirebaseUser();

        isPermissionEnabledLiveData.setValue(true);

        currentLocationMutableLiveData.setValue(new CurrentLocation(DEFAULT_LATITUDE, DEFAULT_LONGITUDE));
        placesAutocompleteLiveData.setValue(getPlacesAutocomplete());
        restaurantsMutableLiveData.setValue(getRestaurants());
        searchInputLiveData.setValue(TEXT_TO_HIGHLIGHT);

        mainViewStateLiveData.setValue(getDefaultMainViewState());
        mainItemViewStatesLiveData.setValue(getDefaultMainItemViewStates());

        viewModel = new MainViewModel(currentLocationRepositoryMock, restaurantsRepositoryMock, userRepositoryMock, placesRepositoryMock, currentUserSearchRepositoryMock, permissionChecker);
    }

    @Test
    public void on_start_permissions_enabled_should_true() {
        // GIVEN

        // WHEN
        viewModel.onStart();

        // THEN
        boolean isPermissionsEnabled = TestUtils.getValueForTesting(viewModel.getPermissionsLiveData());
        assertTrue(isPermissionsEnabled);

        verify(permissionChecker).hasFineLocationPermission();
        verify(currentLocationRepositoryMock).startLocationUpdates();
        verify(currentLocationRepositoryMock).getLocationMutableLiveData();

        verifyNoMoreInteractions(currentLocationRepositoryMock, userRepositoryMock, placesRepositoryMock, currentUserSearchRepositoryMock, restaurantsRepositoryMock, permissionChecker);
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

        verify(currentLocationRepositoryMock).startLocationUpdates();
        verify(permissionChecker).hasFineLocationPermission();
        verify(permissionChecker).hasCoarseLocationPermission();
        verify(currentLocationRepositoryMock).getLocationMutableLiveData();

        verifyNoMoreInteractions(currentLocationRepositoryMock, userRepositoryMock, placesRepositoryMock, currentUserSearchRepositoryMock, restaurantsRepositoryMock, permissionChecker);
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

        verify(currentLocationRepositoryMock).stopLocationUpdates();
        verify(permissionChecker).hasFineLocationPermission();
        verify(permissionChecker).hasCoarseLocationPermission();
        verify(currentLocationRepositoryMock).getLocationMutableLiveData();

        verifyNoMoreInteractions(currentLocationRepositoryMock, userRepositoryMock, placesRepositoryMock, currentUserSearchRepositoryMock, restaurantsRepositoryMock, permissionChecker);
    }

    @Test
    public void on_stop_then_permissions_enabled_should_false() {
        // GIVEN

        // WHEN
        viewModel.onStop();
        boolean isPermissionsEnabled = TestUtils.getValueForTesting(viewModel.getPermissionsLiveData());

        // THEN
        assertFalse(isPermissionsEnabled);

        verify(currentLocationRepositoryMock).stopLocationUpdates();
        verify(currentLocationRepositoryMock).getLocationMutableLiveData();

        verifyNoMoreInteractions(currentLocationRepositoryMock, userRepositoryMock, placesRepositoryMock, currentUserSearchRepositoryMock, restaurantsRepositoryMock, permissionChecker);
    }

    @Test
    public void on_resume_permissions_enabled_should_true() {
        // GIVEN

        // WHEN
        viewModel.onResume();

        // THEN
        boolean isPermissionsEnabled = TestUtils.getValueForTesting(viewModel.getPermissionsLiveData());
        assertTrue(isPermissionsEnabled);

        verify(currentLocationRepositoryMock).startLocationUpdates();
        verify(permissionChecker).hasFineLocationPermission();
        verify(currentLocationRepositoryMock).getLocationMutableLiveData();

        verifyNoMoreInteractions(currentLocationRepositoryMock, userRepositoryMock, placesRepositoryMock, currentUserSearchRepositoryMock, restaurantsRepositoryMock, permissionChecker);
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

        verify(currentLocationRepositoryMock).startLocationUpdates();
        verify(permissionChecker).hasFineLocationPermission();
        verify(permissionChecker).hasCoarseLocationPermission();
        verify(currentLocationRepositoryMock).getLocationMutableLiveData();

        verifyNoMoreInteractions(currentLocationRepositoryMock, userRepositoryMock, placesRepositoryMock, currentUserSearchRepositoryMock, restaurantsRepositoryMock, permissionChecker);
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

        verify(currentLocationRepositoryMock).stopLocationUpdates();
        verify(permissionChecker).hasFineLocationPermission();
        verify(permissionChecker).hasCoarseLocationPermission();
        verify(currentLocationRepositoryMock).getLocationMutableLiveData();

        verifyNoMoreInteractions(currentLocationRepositoryMock, userRepositoryMock, placesRepositoryMock, currentUserSearchRepositoryMock, restaurantsRepositoryMock, permissionChecker);
    }

    @Test
    public void get_main_view_state_live_data() {
        // GIVEN

        // WHEN
        MainViewState mainViewState = TestUtils.getValueForTesting(viewModel.getMainViewStateLiveData());

        // THEN
        assertNotNull(mainViewState);
        assertEquals(getDefaultMainViewState(), mainViewState);

        verify(userRepositoryMock, atLeastOnce()).getCustomFirebaseUser();
        verify(currentLocationRepositoryMock).getLocationMutableLiveData();

        verifyNoMoreInteractions(currentLocationRepositoryMock, userRepositoryMock, placesRepositoryMock, currentUserSearchRepositoryMock, restaurantsRepositoryMock, permissionChecker);
    }

    @Test
    public void get_main_view_state_live_data_if_get_custom_firebase_user_null() {
        // GIVEN
        doReturn(null).when(userRepositoryMock).getCustomFirebaseUser();

        // WHEN
        MainViewState mainViewState = TestUtils.getValueForTesting(viewModel.getMainViewStateLiveData());

        // THEN
        assertNotNull(mainViewState);
        assertEquals(getDefaultMainViewStateEmpty(), mainViewState);

        verify(userRepositoryMock, atLeastOnce()).getCustomFirebaseUser();
        verify(currentLocationRepositoryMock).getLocationMutableLiveData();

        verifyNoMoreInteractions(currentLocationRepositoryMock, userRepositoryMock, placesRepositoryMock, currentUserSearchRepositoryMock, restaurantsRepositoryMock, permissionChecker);
    }

    @Test
    public void get_main_item_list_view_state_list_with_on_search_changed() {
        // GIVEN
        viewModel.onSearchChanged(TEXT_TO_HIGHLIGHT);

        // WHEN
        List<MainItemViewState> mainItemViewStates = TestUtils.getValueForTesting(viewModel.getMainItemListViewState());

        assertNotNull(mainItemViewStates);
        assertEquals(2, mainItemViewStates.size());
        assertEquals(getDefaultMainItemViewStates(), mainItemViewStates);

        verify(placesRepositoryMock, atLeastOnce()).getPlacesAutocomplete(DEFAULT_LAT_LNG, DEFAULT_LANGUAGE, TEXT_TO_HIGHLIGHT);
        verify(currentLocationRepositoryMock).getLocationMutableLiveData();
        verify(restaurantsRepositoryMock).getNearRestaurants(DEFAULT_LAT_LNG);

        verifyNoMoreInteractions(currentLocationRepositoryMock, userRepositoryMock, placesRepositoryMock, currentUserSearchRepositoryMock, restaurantsRepositoryMock, permissionChecker);
    }

    @Test
    public void get_main_item_list_view_state_list_with_on_search_changed_empty() {
        // GIVEN
        viewModel.onSearchChanged("");

        // WHEN
        List<MainItemViewState> mainItemViewStates = TestUtils.getValueForTesting(viewModel.getMainItemListViewState());

        assertNotNull(mainItemViewStates);
        assertEquals(0, mainItemViewStates.size());

        verify(placesRepositoryMock, atLeastOnce()).getPlacesAutocomplete(DEFAULT_LAT_LNG, DEFAULT_LANGUAGE, "");
        verify(currentLocationRepositoryMock).getLocationMutableLiveData();
        verify(restaurantsRepositoryMock).getNearRestaurants(DEFAULT_LAT_LNG);

        verifyNoMoreInteractions(currentLocationRepositoryMock, userRepositoryMock, placesRepositoryMock, currentUserSearchRepositoryMock, restaurantsRepositoryMock, permissionChecker);
    }

    @Test
    public void get_main_item_list_view_state_list_with_on_search_changed_null() {
        // GIVEN
        viewModel.onSearchChanged(null);

        // WHEN
        List<MainItemViewState> mainItemViewStates = TestUtils.getValueForTesting(viewModel.getMainItemListViewState());

        assertNotNull(mainItemViewStates);
        assertEquals(0, mainItemViewStates.size());

        verify(placesRepositoryMock, atLeastOnce()).getPlacesAutocomplete(DEFAULT_LAT_LNG, DEFAULT_LANGUAGE, "");
        verify(currentLocationRepositoryMock).getLocationMutableLiveData();
        verify(restaurantsRepositoryMock).getNearRestaurants(DEFAULT_LAT_LNG);

        verifyNoMoreInteractions(currentLocationRepositoryMock, userRepositoryMock, placesRepositoryMock, currentUserSearchRepositoryMock, restaurantsRepositoryMock, permissionChecker);
    }

    @Test
    public void get_main_item_list_view_state_list_with_search_null() {
        // GIVEN
        searchInputLiveData.setValue(null);
        viewModel.onSearchChanged(null);

        // WHEN
        List<MainItemViewState> mainItemViewStates = TestUtils.getValueForTesting(viewModel.getMainItemListViewState());

        assertNotNull(mainItemViewStates);
        assertEquals(0, mainItemViewStates.size());

        verify(placesRepositoryMock, atLeastOnce()).getPlacesAutocomplete(DEFAULT_LAT_LNG, DEFAULT_LANGUAGE, "");
        verify(currentLocationRepositoryMock).getLocationMutableLiveData();
        verify(restaurantsRepositoryMock).getNearRestaurants(DEFAULT_LAT_LNG);

        verifyNoMoreInteractions(currentLocationRepositoryMock, userRepositoryMock, placesRepositoryMock, currentUserSearchRepositoryMock, restaurantsRepositoryMock, permissionChecker);
    }

    @Test
    public void get_main_item_list_view_state_list_with_on_search_changed_empty_with_search_null() {
        // GIVEN
        viewModel.onSearchChanged("");
        searchInputLiveData.setValue(null);

        // WHEN
        List<MainItemViewState> mainItemViewStates = TestUtils.getValueForTesting(viewModel.getMainItemListViewState());

        assertNotNull(mainItemViewStates);
        assertEquals(0, mainItemViewStates.size());

        verify(placesRepositoryMock, atLeastOnce()).getPlacesAutocomplete(DEFAULT_LAT_LNG, DEFAULT_LANGUAGE, "");
        verify(currentLocationRepositoryMock).getLocationMutableLiveData();
        verify(restaurantsRepositoryMock).getNearRestaurants(DEFAULT_LAT_LNG);

        verifyNoMoreInteractions(currentLocationRepositoryMock, userRepositoryMock, placesRepositoryMock, currentUserSearchRepositoryMock, restaurantsRepositoryMock, permissionChecker);
    }

    @Test
    public void get_main_item_list_view_state_list_with_on_search_done() {
        // WHEN
        viewModel.onSearchDone(TEXT_TO_HIGHLIGHT);
        verify(currentUserSearchRepositoryMock, atLeastOnce()).setCurrentUserSearch(TEXT_TO_HIGHLIGHT);
        verify(currentLocationRepositoryMock).getLocationMutableLiveData();

        verifyNoMoreInteractions(currentLocationRepositoryMock, userRepositoryMock, placesRepositoryMock, currentUserSearchRepositoryMock, restaurantsRepositoryMock, permissionChecker);
    }

    private MainViewState getDefaultMainViewState() {
        return new MainViewState(DEFAULT_NAME, DEFAULT_MAIL, PICTURE_URL);
    }

    private MainViewState getDefaultMainViewStateWithOnlyName() {
        return new MainViewState(DEFAULT_NAME, "", "");
    }

    private MainViewState getDefaultMainViewStateEmpty() {
        return new MainViewState("", "", "");
    }

    private CustomFirebaseUser getCustomFirebaseUser() {
        return new CustomFirebaseUser(DEFAULT_ID, DEFAULT_NAME, DEFAULT_MAIL, PICTURE_URL);
    }

    private CustomFirebaseUser getCustomFirebaseUserWithOnlyName() {
        return new CustomFirebaseUser(DEFAULT_ID, DEFAULT_NAME, "", "");
    }

    private List<PlaceAutocomplete> getPlacesAutocomplete() {
        List<PlaceAutocomplete> placesAutocomplete = new ArrayList<>();

        placesAutocomplete.add(new PlaceAutocomplete("1", RESTAURANT_NAME, ADDRESS));
        placesAutocomplete.add(new PlaceAutocomplete("2", RESTAURANT_NAME_1, ADDRESS));
        placesAutocomplete.add(new PlaceAutocomplete("3", RESTAURANT_NAME_2, ADDRESS));

        return placesAutocomplete;
    }

    private List<Restaurant> getRestaurants() {
        List<Restaurant> restaurants = new ArrayList<>();

        restaurants.add(new Restaurant("1", RESTAURANT_NAME, ADDRESS, false, 0.0, 1.0, 1.0, ""));
        restaurants.add(new Restaurant("2", RESTAURANT_NAME_1, ADDRESS, false, 0.0, 1.0, 1.0, ""));
        restaurants.add(new Restaurant("3", RESTAURANT_NAME_2, ADDRESS, false, 0.0, 1.0, 1.0, ""));

        return restaurants;
    }

    private List<MainItemViewState> getDefaultMainItemViewStates() {
        List<MainItemViewState> mainItemViewStates = new ArrayList<>();

        mainItemViewStates.add(new MainItemViewState("1", RESTAURANT_NAME, ADDRESS, start, end, ""));

        mainItemViewStates.add(new MainItemViewState("2", RESTAURANT_NAME_1, ADDRESS, start, end, ""));

        return mainItemViewStates;
    }
}