package com.suonk.oc_project7.ui.restaurants.list;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.suonk.oc_project7.R;
import com.suonk.oc_project7.domain.workmates.get.GetWorkmatesHaveChosenTodayUseCase;
import com.suonk.oc_project7.model.data.places.CurrentLocation;
import com.suonk.oc_project7.model.data.restaurant.Restaurant;
import com.suonk.oc_project7.model.data.workmate.Workmate;
import com.suonk.oc_project7.repositories.current_location.CurrentLocationRepository;
import com.suonk.oc_project7.repositories.current_location.CurrentLocationRepositoryImpl;
import com.suonk.oc_project7.repositories.current_user_search.CurrentUserSearchRepository;
import com.suonk.oc_project7.repositories.restaurants.RestaurantsRepository;
import com.suonk.oc_project7.repositories.restaurants.RestaurantsRepositoryImpl;
import com.suonk.oc_project7.utils.TestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class RestaurantsViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private RestaurantsViewModel restaurantsViewModel;

    //region ============================================= MOCK =============================================

    private final Application application = Mockito.mock(Application.class);
    private final CurrentLocationRepository currentLocationRepositoryMock = mock(CurrentLocationRepositoryImpl.class);

    private final GetWorkmatesHaveChosenTodayUseCase getWorkmatesHaveChosenTodayUseCaseMock = mock(GetWorkmatesHaveChosenTodayUseCase.class);
    
    private final RestaurantsRepository restaurantsRepositoryMock = mock(RestaurantsRepositoryImpl.class);
    private final CurrentLocation currentLocation = mock(CurrentLocation.class);
    private final CurrentUserSearchRepository currentUserSearchRepositoryMock = Mockito.mock(CurrentUserSearchRepository.class);

    //endregion

    //region ======================================== DEFAULTS VALUES =======================================

    private static final String RESTAURANT_NAME = "PIZZA HUT";
    private static final String RESTAURANT_NAME_1 = "PIZZA N PASTA";
    private static final String RESTAURANT_NAME_2 = "PASTA";

    private static final boolean RESTAURANT_IS_OPEN_TRUE = true;
    private static final boolean RESTAURANT_IS_OPEN_FALSE = false;
    private static final String ADDRESS = "ADDRESS";
    private static final String PHOTO_REFERENCE = "PHOTO_REFERENCE";
    private static final String PICTURE_URL = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&key=key&photo_reference=" + PHOTO_REFERENCE;

    private static final String PICTURE_URL_EMPTY = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&key=key&photo_reference=";
    private static final String DEFAULT_PICTURE_URL_EMPTY = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/No_image_available.svg/1024px-No_image_available.svg.png";
    private static final double RATING = 4.0;
    private static final double RESTAURANT_LATITUDE = 1.256546;
    private static final double RESTAURANT_LONGITUDE = 3.256546;

    private static final double LATITUDE = -1.256546;
    private static final double LONGITUDE = 8.256546;
    private static final String LOCATION = LATITUDE + "," + LONGITUDE;
    private static final float DISTANCE_TO_RESTAURANT = 325.6546f;
    private static final int NUMBER_OF_WORKMATES = 2;
    private static final int NUMBER_OF_WORKMATES_1 = 1;

    private static final String FORMATTED_DISTANCE_TO_RESTAURANT = "FORMATTED_DISTANCE_TO_RESTAURANT";
    private static final String FORMATTED_NUMBER_OF_WORKMATES = NUMBER_OF_WORKMATES + " FORMATTED_NUMBER_OF_WORKMATES";
    private static final String FORMATTED_NUMBER_OF_WORKMATES_1 = NUMBER_OF_WORKMATES_1 + " FORMATTED_NUMBER_OF_WORKMATES";
    private static final String FORMATTED_NUMBER_OF_NO_WORKMATES = "FORMATTED_NUMBER_OF_NO_WORKMATES";

    private static final String FORMATTED_OPEN_DESCRIPTION_IS_OPEN = "FORMATTED_OPEN_DESCRIPTION_IS_OPEN";
    private static final String FORMATTED_OPEN_DESCRIPTION_IS_CLOSE = "FORMATTED_OPEN_DESCRIPTION_IS_CLOSE";

    private static final String TEXT_TO_HIGHLIGHT = "PIZ";

    //endregion

    //region =========================================== LIVEDATA ===========================================

    private final MutableLiveData<List<Restaurant>> restaurantsLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Workmate>> workmatesHaveChosenLiveData = new MutableLiveData<>();
    private final MutableLiveData<CurrentLocation> currentLocationMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> searchInputLiveData = new MutableLiveData<>();

    //endregion

    @Before
    public void setup() {
        doReturn(LATITUDE).when(currentLocation).getLat();
        doReturn(LONGITUDE).when(currentLocation).getLng();

        doReturn(currentLocationMutableLiveData).when(currentLocationRepositoryMock).getLocationMutableLiveData();
        doReturn(restaurantsLiveData).when(restaurantsRepositoryMock).getNearRestaurants(LOCATION);

        doReturn(workmatesHaveChosenLiveData).when(getWorkmatesHaveChosenTodayUseCaseMock).getWorkmatesHaveChosenTodayLiveData();

        doReturn(searchInputLiveData).when(currentUserSearchRepositoryMock).getCurrentUserSearchLiveData();

        doReturn(DISTANCE_TO_RESTAURANT).when(currentLocationRepositoryMock).getDistanceFromTwoLocations(
                LATITUDE, LONGITUDE, RESTAURANT_LATITUDE, RESTAURANT_LONGITUDE
        );

        doReturn(FORMATTED_DISTANCE_TO_RESTAURANT).when(application).getString(R.string.distance_restaurant, (int) DISTANCE_TO_RESTAURANT);
        doReturn(FORMATTED_NUMBER_OF_WORKMATES).when(application).getString(R.string.number_of_workmates, NUMBER_OF_WORKMATES);
        doReturn(FORMATTED_NUMBER_OF_WORKMATES_1).when(application).getString(R.string.number_of_workmates, NUMBER_OF_WORKMATES_1);
        doReturn(FORMATTED_NUMBER_OF_NO_WORKMATES).when(application).getString(R.string.number_of_workmates, 0);

        doReturn(FORMATTED_OPEN_DESCRIPTION_IS_OPEN).when(application).getString(R.string.is_open);
        doReturn(FORMATTED_OPEN_DESCRIPTION_IS_CLOSE).when(application).getString(R.string.is_close);

        currentLocationMutableLiveData.setValue(currentLocation);
        restaurantsLiveData.setValue(getDefaultRestaurants());
        workmatesHaveChosenLiveData.setValue(getDefaultWorkmatesHaveChosen());
        searchInputLiveData.setValue(TEXT_TO_HIGHLIGHT);

        restaurantsViewModel = new RestaurantsViewModel(
                currentLocationRepositoryMock,
                getWorkmatesHaveChosenTodayUseCaseMock,
                restaurantsRepositoryMock,
                currentUserSearchRepositoryMock,
                application
        );

        verify(currentLocationRepositoryMock).getLocationMutableLiveData();
        verify(getWorkmatesHaveChosenTodayUseCaseMock).getWorkmatesHaveChosenTodayLiveData();
        verify(currentUserSearchRepositoryMock).getCurrentUserSearchLiveData();
    }

    @Test
    public void get_restaurants_view_state_list_with_no_search() {
        // GIVEN
        searchInputLiveData.setValue("");

        // WHEN
        List<RestaurantItemViewState> restaurantsItemViewState = TestUtils.getValueForTesting(restaurantsViewModel.getRestaurantsLiveData());

        assertNotNull(restaurantsItemViewState);
        assertEquals(3, restaurantsItemViewState.size());
        assertEquals(getDefaultRestaurantsItemViewState(), restaurantsItemViewState);

        verify(currentLocation, atLeastOnce()).getLat();
        verify(currentLocation, atLeastOnce()).getLng();
        verify(restaurantsRepositoryMock).getNearRestaurants(LOCATION);
        verify(currentLocationRepositoryMock, atLeastOnce()).getDistanceFromTwoLocations(LATITUDE,
                LONGITUDE, RESTAURANT_LATITUDE, RESTAURANT_LONGITUDE);

        verify(application, atLeastOnce()).getString(R.string.is_open);
        verify(application, atLeastOnce()).getString(R.string.is_close);
        verify(application, atLeastOnce()).getString(R.string.distance_restaurant, (int) DISTANCE_TO_RESTAURANT);
        verify(application, atLeastOnce()).getString(R.string.number_of_workmates, NUMBER_OF_WORKMATES);
        verify(application, atLeastOnce()).getString(R.string.number_of_workmates, NUMBER_OF_WORKMATES_1);
        verify(application, atLeastOnce()).getString(R.string.number_of_workmates, 0);

        Mockito.verifyNoMoreInteractions(restaurantsRepositoryMock, currentLocation, getWorkmatesHaveChosenTodayUseCaseMock,
                currentUserSearchRepositoryMock, currentLocationRepositoryMock, application);
    }

    @Test
    public void get_restaurants_view_state_list_with_search() {
        // GIVEN

        // WHEN
        List<RestaurantItemViewState> restaurantsItemViewState = TestUtils.getValueForTesting(restaurantsViewModel.getRestaurantsLiveData());

        assertNotNull(restaurantsItemViewState);
        assertEquals(2, restaurantsItemViewState.size());
        assertEquals(getDefaultRestaurantsItemViewStateAfterQuery(), restaurantsItemViewState);

        verify(currentLocation, atLeastOnce()).getLat();
        verify(currentLocation, atLeastOnce()).getLng();
        verify(restaurantsRepositoryMock).getNearRestaurants(LOCATION);
        verify(currentLocationRepositoryMock, atLeastOnce()).getDistanceFromTwoLocations(LATITUDE,
                LONGITUDE, RESTAURANT_LATITUDE, RESTAURANT_LONGITUDE);

        verify(application, atLeastOnce()).getString(R.string.is_open);
        verify(application, atLeastOnce()).getString(R.string.is_close);
        verify(application, atLeastOnce()).getString(R.string.distance_restaurant, (int) DISTANCE_TO_RESTAURANT);
        verify(application, atLeastOnce()).getString(R.string.number_of_workmates, NUMBER_OF_WORKMATES);
        verify(application, atLeastOnce()).getString(R.string.number_of_workmates, NUMBER_OF_WORKMATES_1);

        Mockito.verifyNoMoreInteractions(restaurantsRepositoryMock, currentLocation, getWorkmatesHaveChosenTodayUseCaseMock,
                currentUserSearchRepositoryMock, currentLocationRepositoryMock, application);
    }

    @Test
    public void get_restaurants_view_state_list_with_search_null() {
        // GIVEN
        searchInputLiveData.setValue(null);

        // WHEN
        List<RestaurantItemViewState> restaurantsItemViewState = TestUtils.getValueForTesting(restaurantsViewModel.getRestaurantsLiveData());

        assertNotNull(restaurantsItemViewState);
        assertEquals(3, restaurantsItemViewState.size());
        assertEquals(getDefaultRestaurantsItemViewState(), restaurantsItemViewState);

        verify(currentLocation, atLeastOnce()).getLat();
        verify(currentLocation, atLeastOnce()).getLng();
        verify(restaurantsRepositoryMock).getNearRestaurants(LOCATION);
        verify(currentLocationRepositoryMock, atLeastOnce()).getDistanceFromTwoLocations(LATITUDE,
                LONGITUDE, RESTAURANT_LATITUDE, RESTAURANT_LONGITUDE);

        verify(application, atLeastOnce()).getString(R.string.is_open);
        verify(application, atLeastOnce()).getString(R.string.is_close);
        verify(application, atLeastOnce()).getString(R.string.distance_restaurant, (int) DISTANCE_TO_RESTAURANT);
        verify(application, atLeastOnce()).getString(R.string.number_of_workmates, NUMBER_OF_WORKMATES);
        verify(application, atLeastOnce()).getString(R.string.number_of_workmates, NUMBER_OF_WORKMATES_1);
        verify(application, atLeastOnce()).getString(R.string.number_of_workmates, 0);

        Mockito.verifyNoMoreInteractions(restaurantsRepositoryMock, currentLocation, getWorkmatesHaveChosenTodayUseCaseMock,
                currentUserSearchRepositoryMock, currentLocationRepositoryMock, application);
    }

    @Test
    public void get_restaurants_view_state_if_restaurants_null_with_no_search() {
        // GIVEN
        searchInputLiveData.setValue("");
        restaurantsLiveData.setValue(null);

        // WHEN
        List<RestaurantItemViewState> restaurantsItemViewState = TestUtils.getValueForTesting(restaurantsViewModel.getRestaurantsLiveData());

        assertNotNull(restaurantsItemViewState);
        assertEquals(0, restaurantsItemViewState.size());

        verify(currentLocation, atLeastOnce()).getLat();
        verify(currentLocation, atLeastOnce()).getLng();
        verify(restaurantsRepositoryMock).getNearRestaurants(LOCATION);

        Mockito.verifyNoMoreInteractions(restaurantsRepositoryMock, currentLocation, getWorkmatesHaveChosenTodayUseCaseMock,
                currentUserSearchRepositoryMock, currentLocationRepositoryMock, application);
    }

    @Test
    public void get_restaurants_view_state_if_restaurants_null_with_search() {
        // WHEN
        restaurantsLiveData.setValue(null);
        List<RestaurantItemViewState> restaurantsItemViewState = TestUtils.getValueForTesting(restaurantsViewModel.getRestaurantsLiveData());

        assertNotNull(restaurantsItemViewState);
        assertEquals(0, restaurantsItemViewState.size());

        verify(currentLocation, atLeastOnce()).getLat();
        verify(currentLocation, atLeastOnce()).getLng();
        verify(restaurantsRepositoryMock).getNearRestaurants(LOCATION);

        Mockito.verifyNoMoreInteractions(restaurantsRepositoryMock, currentLocation, getWorkmatesHaveChosenTodayUseCaseMock,
                currentUserSearchRepositoryMock, currentLocationRepositoryMock, application);
    }

    @Test
    public void get_restaurants_view_state_if_workmates_have_chosen_null_with_no_search() {
        // GIVEN
        searchInputLiveData.setValue("");
        workmatesHaveChosenLiveData.setValue(null);

        // WHEN
        List<RestaurantItemViewState> restaurantsItemViewState = TestUtils.getValueForTesting(
                restaurantsViewModel.getRestaurantsLiveData());

        assertNotNull(restaurantsItemViewState);
        assertEquals(3, restaurantsItemViewState.size());

        verify(currentLocation, atLeastOnce()).getLat();
        verify(currentLocation, atLeastOnce()).getLng();
        verify(restaurantsRepositoryMock).getNearRestaurants(LOCATION);
        verify(currentLocationRepositoryMock, atLeastOnce()).getDistanceFromTwoLocations(LATITUDE,
                LONGITUDE, RESTAURANT_LATITUDE, RESTAURANT_LONGITUDE);

        verify(application, atLeastOnce()).getString(R.string.is_open);
        verify(application, atLeastOnce()).getString(R.string.is_close);
        verify(application, atLeastOnce()).getString(R.string.distance_restaurant, (int) DISTANCE_TO_RESTAURANT);
        verify(application, atLeastOnce()).getString(R.string.number_of_workmates, 0);

        Mockito.verifyNoMoreInteractions(restaurantsRepositoryMock, currentLocation, getWorkmatesHaveChosenTodayUseCaseMock,
                currentUserSearchRepositoryMock, currentLocationRepositoryMock, application);
    }

    @Test
    public void get_restaurants_view_state_if_workmates_have_chosen_null_with_search() {
        // GIVEN
        workmatesHaveChosenLiveData.setValue(null);

        // WHEN
        List<RestaurantItemViewState> restaurantsItemViewState = TestUtils.getValueForTesting(
                restaurantsViewModel.getRestaurantsLiveData());

        assertNotNull(restaurantsItemViewState);
        assertEquals(2, restaurantsItemViewState.size());

        verify(currentLocation, atLeastOnce()).getLat();
        verify(currentLocation, atLeastOnce()).getLng();
        verify(restaurantsRepositoryMock).getNearRestaurants(LOCATION);
        verify(currentLocationRepositoryMock, atLeastOnce()).getDistanceFromTwoLocations(LATITUDE,
                LONGITUDE, RESTAURANT_LATITUDE, RESTAURANT_LONGITUDE);

        verify(application, atLeastOnce()).getString(R.string.is_open);
        verify(application, atLeastOnce()).getString(R.string.is_close);
        verify(application, atLeastOnce()).getString(R.string.distance_restaurant, (int) DISTANCE_TO_RESTAURANT);
        verify(application, atLeastOnce()).getString(R.string.number_of_workmates, 0);

        Mockito.verifyNoMoreInteractions(restaurantsRepositoryMock, currentLocation, getWorkmatesHaveChosenTodayUseCaseMock,
                currentUserSearchRepositoryMock, currentLocationRepositoryMock, application);
    }

    @Test
    public void get_restaurants_view_state_if_workmates_have_chosen_and_restaurants_are_null_with_no_search() {
        // WHEN
        searchInputLiveData.setValue("");
        restaurantsLiveData.setValue(null);
        workmatesHaveChosenLiveData.setValue(null);

        List<RestaurantItemViewState> restaurantsItemViewState = TestUtils.getValueForTesting(restaurantsViewModel.getRestaurantsLiveData());

        assertNotNull(restaurantsItemViewState);
        assertEquals(0, restaurantsItemViewState.size());

        verify(currentLocation, atLeastOnce()).getLat();
        verify(currentLocation, atLeastOnce()).getLng();
        verify(restaurantsRepositoryMock).getNearRestaurants(LOCATION);

        Mockito.verifyNoMoreInteractions(restaurantsRepositoryMock, currentLocation, getWorkmatesHaveChosenTodayUseCaseMock,
                currentUserSearchRepositoryMock, currentLocationRepositoryMock, application);
    }

    @Test
    public void get_restaurants_view_state_if_workmates_have_chosen_and_restaurants_are_null_with_search() {
        // WHEN
        restaurantsLiveData.setValue(null);
        workmatesHaveChosenLiveData.setValue(null);

        List<RestaurantItemViewState> restaurantsItemViewState = TestUtils.getValueForTesting(restaurantsViewModel.getRestaurantsLiveData());

        assertNotNull(restaurantsItemViewState);
        assertEquals(0, restaurantsItemViewState.size());

        verify(currentLocation, atLeastOnce()).getLat();
        verify(currentLocation, atLeastOnce()).getLng();
        verify(restaurantsRepositoryMock).getNearRestaurants(LOCATION);

        Mockito.verifyNoMoreInteractions(restaurantsRepositoryMock, currentLocation, getWorkmatesHaveChosenTodayUseCaseMock,
                currentUserSearchRepositoryMock, currentLocationRepositoryMock, application);
    }

    private List<Restaurant> getDefaultRestaurants() {
        List<Restaurant> restaurants = new ArrayList<>();

        restaurants.add(new Restaurant("1", RESTAURANT_NAME, ADDRESS, RESTAURANT_IS_OPEN_TRUE, RATING, RESTAURANT_LATITUDE, RESTAURANT_LONGITUDE, PICTURE_URL));
        restaurants.add(new Restaurant("2", RESTAURANT_NAME_1, ADDRESS, RESTAURANT_IS_OPEN_FALSE, RATING, RESTAURANT_LATITUDE, RESTAURANT_LONGITUDE, PICTURE_URL_EMPTY));
        restaurants.add(new Restaurant("3", RESTAURANT_NAME_2, ADDRESS, RESTAURANT_IS_OPEN_FALSE, RATING, RESTAURANT_LATITUDE, RESTAURANT_LONGITUDE, PICTURE_URL_EMPTY));

        return restaurants;
    }

    private List<RestaurantItemViewState> getDefaultRestaurantsItemViewState() {
        List<RestaurantItemViewState> restaurants = new ArrayList<>();

        restaurants.add(new RestaurantItemViewState("1", RESTAURANT_NAME, ADDRESS, FORMATTED_OPEN_DESCRIPTION_IS_OPEN, FORMATTED_DISTANCE_TO_RESTAURANT, FORMATTED_NUMBER_OF_WORKMATES, 2, PICTURE_URL));
        restaurants.add(new RestaurantItemViewState("2", RESTAURANT_NAME_1, ADDRESS, FORMATTED_OPEN_DESCRIPTION_IS_CLOSE, FORMATTED_DISTANCE_TO_RESTAURANT, FORMATTED_NUMBER_OF_WORKMATES_1, 2, DEFAULT_PICTURE_URL_EMPTY));
        restaurants.add(new RestaurantItemViewState("3", RESTAURANT_NAME_2, ADDRESS, FORMATTED_OPEN_DESCRIPTION_IS_CLOSE, FORMATTED_DISTANCE_TO_RESTAURANT, FORMATTED_NUMBER_OF_NO_WORKMATES, 2, DEFAULT_PICTURE_URL_EMPTY));

        return restaurants;
    }

    private List<RestaurantItemViewState> getDefaultRestaurantsItemViewStateAfterQuery() {
        List<RestaurantItemViewState> restaurants = new ArrayList<>();

        restaurants.add(new RestaurantItemViewState("1", RESTAURANT_NAME, ADDRESS, FORMATTED_OPEN_DESCRIPTION_IS_OPEN, FORMATTED_DISTANCE_TO_RESTAURANT, FORMATTED_NUMBER_OF_WORKMATES, 2, PICTURE_URL));
        restaurants.add(new RestaurantItemViewState("2", RESTAURANT_NAME_1, ADDRESS, FORMATTED_OPEN_DESCRIPTION_IS_CLOSE, FORMATTED_DISTANCE_TO_RESTAURANT, FORMATTED_NUMBER_OF_WORKMATES_1, 2, DEFAULT_PICTURE_URL_EMPTY));

        return restaurants;
    }

    private List<Workmate> getDefaultWorkmatesHaveChosen() {
        List<Workmate> workmates = new ArrayList<>();

        workmates.add(new Workmate("1", "workmate1", "mail", "1", "1", RESTAURANT_NAME, new ArrayList<>()
        ));
        workmates.add(new Workmate("2", "workmate2", "mail", "1", "2", RESTAURANT_NAME_1, new ArrayList<>()));
        workmates.add(new Workmate("3", "workmate3", "mail", "1", "1", RESTAURANT_NAME, new ArrayList<>()));

        return workmates;
    }
}