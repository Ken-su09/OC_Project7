package com.suonk.oc_project7.ui.main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import android.app.Application;
import android.text.SpannableString;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.suonk.oc_project7.R;
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

//@RunWith(MockitoJUnitRunner.class)
@RunWith(MockitoJUnitRunner.Silent.class)
public class MainViewModelTest {

    @Rule
    public final InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private MainViewModel viewModel;

    private final CurrentLocationRepository currentLocationRepositoryMock = Mockito.mock(CurrentLocationRepository.class);
    private final RestaurantsRepository restaurantsRepositoryMock = Mockito.mock(RestaurantsRepository.class);
    private final PlacesRepository placesRepositoryMock = Mockito.mock(PlacesRepository.class);
    private final UserRepository userRepositoryMock = Mockito.mock(UserRepository.class);
    private final CurrentUserSearchRepository currentUserSearchRepository = Mockito.mock(CurrentUserSearchRepository.class);

    private final Application application = Mockito.mock(Application.class);
    private final PermissionChecker permissionChecker = Mockito.mock(PermissionChecker.class);
    private final CurrentLocation currentLocation = mock(CurrentLocation.class);

    private static final String RESTAURANT_NAME = "PIZZA HUT";
    private static final String RESTAURANT_NAME_2 = "OKONOMIYAKI";
    private static final boolean RESTAURANT_IS_OPEN_TRUE = true;
    private static final boolean RESTAURANT_IS_OPEN_FALSE = false;
    private static final String ADDRESS = "ADDRESS";
    private static final String PHOTO_REFERENCE = "PHOTO_REFERENCE";
    private static final String RESTAURANT_PICTURE_URL = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&key=key&photo_reference=" + PHOTO_REFERENCE;

    private static final String DEFAULT_NAME = "DEFAULT_NAME";
    private static final String DEFAULT_MAIL = "DEFAULT_MAIL";
    private static final String PICTURE_URL = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&key=key&photo_reference=" + PHOTO_REFERENCE;

    private static final double RATING = 4.0;
    private static final double RESTAURANT_LATITUDE = 1.256546;
    private static final double RESTAURANT_LONGITUDE = 3.256546;

    private static final double LATITUDE = -1.256546;
    private static final double LONGITUDE = 8.256546;
    private static final String LOCATION = LATITUDE + "," + LONGITUDE;
    private static final float DISTANCE_TO_RESTAURANT = 325.6546f;
    private static final int NUMBER_OF_WORKMATES = 1;

    private static final String FORMATTED_DISTANCE_TO_RESTAURANT = "FORMATTED_DISTANCE_TO_RESTAURANT";
    private static final String FORMATTED_NUMBER_OF_WORKMATES = "FORMATTED_NUMBER_OF_WORKMATES";

    private static final String FORMATTED_OPEN_DESCRIPTION_IS_OPEN = "FORMATTED_DISTANCE_TO_RESTAURANT";
    private static final String FORMATTED_OPEN_DESCRIPTION_IS_CLOSE = "FORMATTED_NUMBER_OF_WORKMATES";

    private static final SpannableString TEXT_TO_HIGHLIGHT = new SpannableString("TEXT_TO_HIGHLIGHT");
    private static final String DEFAULT_LANGUAGE = "fr";

    private final MutableLiveData<Boolean> isPermissionEnabledLiveData = new MutableLiveData<>();
    private final MutableLiveData<MainViewState> mainViewStateLiveData = new MutableLiveData<>();

    private final MutableLiveData<CurrentLocation> currentLocationMutableLiveData = new MutableLiveData<>();

    private final MutableLiveData<List<MainItemViewState>> mainItemViewStateLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Restaurant>> restaurantsLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<PlaceAutocomplete>> placesAutocompleteLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> searchInputLiveData = new MutableLiveData<>();

    @Before
    public void setup() {
        doNothing().when(currentLocationRepositoryMock).startLocationUpdates();
        doNothing().when(currentLocationRepositoryMock).stopLocationUpdates();

        doReturn(LATITUDE).when(currentLocation).getLat();
        doReturn(LONGITUDE).when(currentLocation).getLng();

        doReturn(currentLocationMutableLiveData).when(currentLocationRepositoryMock).getLocationMutableLiveData();
        doReturn(restaurantsLiveData).when(restaurantsRepositoryMock).getNearRestaurants(LOCATION);
        doReturn(placesAutocompleteLiveData).when(placesRepositoryMock).getPlacesAutocomplete(DEFAULT_LANGUAGE, TEXT_TO_HIGHLIGHT.toString());
//        doReturn(placesAutocompleteLiveData).when(placesRepositoryMock).getPlacesAutocomplete(DEFAULT_LANGUAGE, LOCATION, INPUT_TEXT);

        doReturn(DISTANCE_TO_RESTAURANT).when(currentLocationRepositoryMock).getDistanceFromTwoLocations(
                LATITUDE, LONGITUDE, RESTAURANT_LATITUDE, RESTAURANT_LONGITUDE
        );

        doReturn(FORMATTED_DISTANCE_TO_RESTAURANT).when(application).getString(R.string.distance_restaurant, (int) DISTANCE_TO_RESTAURANT);
        doReturn(FORMATTED_NUMBER_OF_WORKMATES).when(application).getString(R.string.number_of_workmates, NUMBER_OF_WORKMATES);

        doReturn(FORMATTED_OPEN_DESCRIPTION_IS_OPEN).when(application).getString(R.string.is_open);
        doReturn(FORMATTED_OPEN_DESCRIPTION_IS_CLOSE).when(application).getString(R.string.is_close);

        doReturn(true).when(permissionChecker).hasCoarseLocationPermission();

        doReturn(true).when(permissionChecker).hasFineLocationPermission();
        doReturn(getCustomFirebaseUser()).when(userRepositoryMock).getCustomFirebaseUser();

        currentLocationMutableLiveData.setValue(currentLocation);
        isPermissionEnabledLiveData.setValue(true);

        searchInputLiveData.setValue(TEXT_TO_HIGHLIGHT.toString());

        mainViewStateLiveData.setValue(getDefaultMainViewState());

        restaurantsLiveData.setValue(getDefaultRestaurants());

        placesAutocompleteLiveData.setValue(getPlacesAutocomplete());

        mainItemViewStateLiveData.setValue(getDefaultMainItemViewState());

        viewModel = new MainViewModel(
                currentLocationRepositoryMock,
                userRepositoryMock,
                placesRepositoryMock,
                currentUserSearchRepository,
                permissionChecker,
                application);

        verify(currentLocationRepositoryMock).getLocationMutableLiveData();
    }

    @Test
    public void on_start_permissions_enabled_should_true() {
        // GIVEN

        // WHEN
        viewModel.onStart();

        // THEN
        boolean isPermissionsEnabled = TestUtils.getValueForTesting(viewModel.getPermissionsLiveData());
        assertTrue(isPermissionsEnabled);

        verify(currentLocationRepositoryMock).startLocationUpdates();
        verify(permissionChecker).hasFineLocationPermission();

        Mockito.verifyNoMoreInteractions(currentLocationRepositoryMock, userRepositoryMock, placesRepositoryMock,
                restaurantsRepositoryMock, permissionChecker, application);
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

        Mockito.verifyNoMoreInteractions(currentLocationRepositoryMock, userRepositoryMock, placesRepositoryMock,
                restaurantsRepositoryMock, permissionChecker, application);
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

        Mockito.verifyNoMoreInteractions(currentLocationRepositoryMock, userRepositoryMock, placesRepositoryMock,
                restaurantsRepositoryMock, permissionChecker, application);
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

        verify(currentLocationRepositoryMock).stopLocationUpdates();

        Mockito.verifyNoMoreInteractions(currentLocationRepositoryMock, userRepositoryMock, placesRepositoryMock,
                restaurantsRepositoryMock, permissionChecker, application);
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

        Mockito.verifyNoMoreInteractions(currentLocationRepositoryMock, userRepositoryMock, placesRepositoryMock,
                restaurantsRepositoryMock, permissionChecker, application);
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

        Mockito.verifyNoMoreInteractions(currentLocationRepositoryMock, userRepositoryMock, placesRepositoryMock,
                restaurantsRepositoryMock, permissionChecker, application);
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

        Mockito.verifyNoMoreInteractions(currentLocationRepositoryMock, userRepositoryMock, placesRepositoryMock,
                restaurantsRepositoryMock, permissionChecker, application);
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

        Mockito.verifyNoMoreInteractions(currentLocationRepositoryMock, userRepositoryMock, placesRepositoryMock,
                restaurantsRepositoryMock, permissionChecker, application);
    }

    @Test
    public void get_main_item_list_view_state_list() {
        // GIVEN

        // WHEN
        List<MainItemViewState> mainItemViewStates = TestUtils.getValueForTesting(viewModel.getMainItemListViewState());

        assertNotNull(mainItemViewStates);
        assertEquals(0, mainItemViewStates.size());

        verify(currentLocation, atLeastOnce()).getLat();
        verify(currentLocation, atLeastOnce()).getLng();
        verify(restaurantsRepositoryMock).getNearRestaurants(LOCATION);

//        Mockito.verifyNoMoreInteractions(userRepositoryMock, placesRepositoryMock, restaurantsRepositoryMock, permissionChecker, application);
        Mockito.verifyNoMoreInteractions(currentLocationRepositoryMock, userRepositoryMock, placesRepositoryMock,
                restaurantsRepositoryMock, permissionChecker, application);
    }

    @Test
    public void get_main_item_list_view_state_list_with_on_search_changed() {
        // GIVEN
        viewModel.onSearchChanged(TEXT_TO_HIGHLIGHT.toString());

        // WHEN
        List<MainItemViewState> mainItemViewStates = TestUtils.getValueForTesting(viewModel.getMainItemListViewState());

        assertNotNull(mainItemViewStates);
        assertEquals(2, mainItemViewStates.size());
        assertEquals(getDefaultMainItemViewState(), mainItemViewStates);

        verify(currentLocation, atLeastOnce()).getLat();
        verify(currentLocation, atLeastOnce()).getLng();
        verify(restaurantsRepositoryMock).getNearRestaurants(LOCATION);
        verify(currentLocationRepositoryMock, atLeastOnce()).getDistanceFromTwoLocations(LATITUDE, LONGITUDE,
                RESTAURANT_LATITUDE, RESTAURANT_LONGITUDE);
        verify(placesRepositoryMock, atLeastOnce()).getPlacesAutocomplete(DEFAULT_LANGUAGE, TEXT_TO_HIGHLIGHT.toString());

        verify(application, atLeastOnce()).getString(R.string.is_open);
        verify(application, atLeastOnce()).getString(R.string.is_close);
        verify(application, atLeastOnce()).getString(R.string.distance_restaurant, (int) DISTANCE_TO_RESTAURANT);
        verify(application, atLeastOnce()).getString(R.string.number_of_workmates, NUMBER_OF_WORKMATES);

//        Mockito.verifyNoMoreInteractions(userRepositoryMock, placesRepositoryMock, restaurantsRepositoryMock, permissionChecker, application);
        Mockito.verifyNoMoreInteractions(currentLocationRepositoryMock, userRepositoryMock, placesRepositoryMock,
                restaurantsRepositoryMock, permissionChecker, application);
    }

    @Test
    public void get_main_item_list_view_state_list_with_on_search_done() {
        // GIVEN
        viewModel.onSearchDone(TEXT_TO_HIGHLIGHT.toString());

        // WHEN
        List<MainItemViewState> mainItemViewStates = TestUtils.getValueForTesting(viewModel.getMainItemListViewState());

        assertNotNull(mainItemViewStates);
        assertEquals(2, mainItemViewStates.size());
        assertEquals(getDefaultMainItemViewState(), mainItemViewStates);

        verify(currentLocation, atLeastOnce()).getLat();
        verify(currentLocation, atLeastOnce()).getLng();
        verify(restaurantsRepositoryMock).getNearRestaurants(LOCATION);
        verify(currentLocationRepositoryMock, atLeastOnce()).getDistanceFromTwoLocations(LATITUDE, LONGITUDE,
                RESTAURANT_LATITUDE, RESTAURANT_LONGITUDE);
        verify(placesRepositoryMock, atLeastOnce()).getPlacesAutocomplete(DEFAULT_LANGUAGE, TEXT_TO_HIGHLIGHT.toString());

        verify(application, atLeastOnce()).getString(R.string.is_open);
        verify(application, atLeastOnce()).getString(R.string.is_close);
        verify(application, atLeastOnce()).getString(R.string.distance_restaurant, (int) DISTANCE_TO_RESTAURANT);
        verify(application, atLeastOnce()).getString(R.string.number_of_workmates, NUMBER_OF_WORKMATES);

//        Mockito.verifyNoMoreInteractions(userRepositoryMock, placesRepositoryMock, restaurantsRepositoryMock, permissionChecker, application);
        Mockito.verifyNoMoreInteractions(currentLocationRepositoryMock, userRepositoryMock, placesRepositoryMock,
                restaurantsRepositoryMock, permissionChecker, application);
    }

    @Test
    public void get_main_item_list_view_if_restaurants_null() {
        // WHEN
        restaurantsLiveData.setValue(null);
        List<MainItemViewState> restaurantsItemViewState = TestUtils.getValueForTesting(viewModel.getMainItemListViewState());

        assertNotNull(restaurantsItemViewState);
        assertEquals(0, restaurantsItemViewState.size());

        verify(currentLocation, atLeastOnce()).getLat();
        verify(currentLocation, atLeastOnce()).getLng();
        verify(restaurantsRepositoryMock).getNearRestaurants(LOCATION);

        Mockito.verifyNoMoreInteractions(currentLocationRepositoryMock, userRepositoryMock, placesRepositoryMock,
                restaurantsRepositoryMock, permissionChecker, application);
    }

    private MainViewState getDefaultMainViewState() {
        return new MainViewState(
                DEFAULT_NAME,
                DEFAULT_MAIL,
                PICTURE_URL
        );
    }

    private CustomFirebaseUser getCustomFirebaseUser() {
        return new CustomFirebaseUser(
                DEFAULT_NAME,
                DEFAULT_MAIL,
                PICTURE_URL
        );
    }

    private List<Restaurant> getDefaultRestaurants() {
        List<Restaurant> restaurants = new ArrayList<>();

        restaurants.add(new Restaurant("1", RESTAURANT_NAME, ADDRESS, RESTAURANT_IS_OPEN_TRUE, RATING, RESTAURANT_LATITUDE, RESTAURANT_LONGITUDE, RESTAURANT_PICTURE_URL));
        restaurants.add(new Restaurant("2", RESTAURANT_NAME_2, ADDRESS, RESTAURANT_IS_OPEN_FALSE, RATING, RESTAURANT_LATITUDE, RESTAURANT_LONGITUDE, RESTAURANT_PICTURE_URL));

        return restaurants;
    }

    private List<PlaceAutocomplete> getPlacesAutocomplete() {
        List<PlaceAutocomplete> placesAutocomplete = new ArrayList<>();

        placesAutocomplete.add(new PlaceAutocomplete("1", RESTAURANT_NAME, ADDRESS));
        placesAutocomplete.add(new PlaceAutocomplete("2", RESTAURANT_NAME_2, ADDRESS));

        return placesAutocomplete;
    }

    private List<MainItemViewState> getDefaultMainItemViewState() {
        List<MainItemViewState> mainItemViewStates = new ArrayList<>();

        mainItemViewStates.add(new MainItemViewState(
                "1",
                ADDRESS,
                TEXT_TO_HIGHLIGHT));

        mainItemViewStates.add(new MainItemViewState(
                "2",
                ADDRESS,
                TEXT_TO_HIGHLIGHT));

        return mainItemViewStates;
    }
}