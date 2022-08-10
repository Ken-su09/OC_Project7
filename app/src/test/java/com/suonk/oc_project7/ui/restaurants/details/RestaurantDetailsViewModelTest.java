package com.suonk.oc_project7.ui.restaurants.details;

import static org.junit.Assert.*;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import android.app.Application;
import android.graphics.Color;
import android.graphics.Typeface;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.suonk.oc_project7.R;
import com.suonk.oc_project7.model.data.restaurant.Restaurant;
import com.suonk.oc_project7.model.data.restaurant.RestaurantDetails;
import com.suonk.oc_project7.model.data.workmate.Workmate;
import com.suonk.oc_project7.repositories.restaurants.RestaurantsRepository;
import com.suonk.oc_project7.repositories.restaurants.RestaurantsRepositoryImpl;
import com.suonk.oc_project7.repositories.workmates.WorkmatesRepository;
import com.suonk.oc_project7.repositories.workmates.WorkmatesRepositoryImpl;
import com.suonk.oc_project7.ui.workmates.WorkmateItemViewState;
import com.suonk.oc_project7.utils.SingleLiveEvent;
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
public class RestaurantDetailsViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private RestaurantDetailsViewModel restaurantDetailsViewModel;

    private final WorkmatesRepository workmatesRepositoryMock = mock(WorkmatesRepositoryImpl.class);
    private final RestaurantsRepository restaurantsRepositoryMock = mock(RestaurantsRepositoryImpl.class);
    private final FirebaseAuth auth = mock(FirebaseAuth.class);
    private final Application application = Mockito.mock(Application.class);
    private final SavedStateHandle savedStateHandle = mock(SavedStateHandle.class);
    private final FirebaseUser firebaseUser = mock(FirebaseUser.class);

    private static final String PLACE_ID_VALUE = "PLACE_ID_VALUE";
    private static final String PLACE_ID = "PLACE_ID";
    private static final String RESTAURANT_NAME = "RESTAURANT_NAME";
    private static final String PHONE_NUMBER = "PHONE_NUMBER";
    private static final String ADDRESS = "ADDRESS";
    private static final String PHOTO_REFERENCE = "PHOTO_REFERENCE";
    private static final String PICTURE_URL = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&key=key&photo_reference=" + PHOTO_REFERENCE;
    private static final String PICTURE_URL_NO_PICTURE = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/No_image_available.svg/1024px-No_image_available.svg.png";
    private static final int RATING = 4;
    private static final String WEBSITE = "WEBSITE";
    private static final String DISPLAY_NAME = "DISPLAY_NAME";
    private static final String CURRENT_FIREBASE_USER_ID = "CURRENT_FIREBASE_USER_ID";
    private static final String FORMATTED_WORKMATES_HAS_JOINED = "FORMATTED_WORKMATES_HAS_JOINED";
    private static final int TEXT_COLOR_HAS_DECIDED = Color.BLACK;
    private static final int TEXT_STYLE_HAS_DECIDED = Typeface.NORMAL;
    private static final int TEXT_COLOR_HAS_NOT_DECIDED = -7829368;
    private static final int TEXT_STYLE_HAS_NOT_DECIDED = 2;

    private final MutableLiveData<RestaurantDetails> restaurantDetailsMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Workmate>> workmatesHaveChosenMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Restaurant>> likedRestaurantsMutableLiveData = new MutableLiveData<>();
    private final SingleLiveEvent<Integer> selectRestaurantButtonIcon = new SingleLiveEvent<>();

    @Before
    public void setup() {
        doReturn(PLACE_ID_VALUE).when(savedStateHandle).get(PLACE_ID);

        doReturn(restaurantDetailsMutableLiveData).when(restaurantsRepositoryMock).getRestaurantDetailsById(PLACE_ID_VALUE);
        doReturn(workmatesHaveChosenMutableLiveData).when(workmatesRepositoryMock).getWorkmatesHaveChosenTodayLiveData();
        doReturn(likedRestaurantsMutableLiveData).when(restaurantsRepositoryMock).getLikedRestaurants();

        doReturn(FORMATTED_WORKMATES_HAS_JOINED).when(application).getString(R.string.workmate_has_joined, DISPLAY_NAME);

        doReturn(CURRENT_FIREBASE_USER_ID).when(firebaseUser).getUid();
        doReturn(firebaseUser).when(auth).getCurrentUser();

        restaurantDetailsMutableLiveData.setValue(getDefaultRestaurantDetails());
        workmatesHaveChosenMutableLiveData.setValue(getDefaultWorkmatesHaveChosen());
        likedRestaurantsMutableLiveData.setValue(getDefaultRestaurantsLiked());

        restaurantDetailsViewModel = new RestaurantDetailsViewModel(
                workmatesRepositoryMock,
                restaurantsRepositoryMock,
                auth,
                application,
                savedStateHandle
        );

        verify(savedStateHandle).get(PLACE_ID);
        verify(restaurantsRepositoryMock).getRestaurantDetailsById(PLACE_ID_VALUE);
        verify(workmatesRepositoryMock).getWorkmatesHaveChosenTodayLiveData();
        verify(restaurantsRepositoryMock).getLikedRestaurants();

        verify(auth).getCurrentUser();
    }

    @Test
    public void get_restaurant_details() {
        // GIVEN

        // WHEN
        RestaurantDetailsViewState restaurantDetailsViewState = TestUtils.getValueForTesting(
                restaurantDetailsViewModel.getRestaurantDetailsViewStateLiveData());

        List<WorkmateItemViewState> workmates = TestUtils.getValueForTesting(restaurantDetailsViewModel.getWorkmatesViewStateLiveData());

        // THEN
        assertNotNull(restaurantDetailsViewState);
        assertNotNull(workmates);
        assertEquals(getDefaultWorkmatesItemViewStateHaveChosen(), workmates);
        assertEquals(getDefaultRestaurantDetailsViewState(), restaurantDetailsViewState);

        verify(application, atLeastOnce()).getString(R.string.workmate_has_joined, DISPLAY_NAME);
        Mockito.verifyNoMoreInteractions(restaurantsRepositoryMock, workmatesRepositoryMock, savedStateHandle, auth, application);
    }

    @Test
    public void get_restaurant_details_when_workmates_and_place_id_are_null() {
        // GIVEN

        // WHEN
        workmatesHaveChosenMutableLiveData.setValue(null);
        RestaurantDetailsViewState restaurantDetailsViewState = TestUtils.getValueForTesting(
                restaurantDetailsViewModel.getRestaurantDetailsViewStateLiveData());

        // THEN
        assertNotNull(restaurantDetailsViewState);
//        assertEquals(getDefaultRestaurantDetailsViewState(), restaurantDetailsViewState);
        verify(savedStateHandle).get(PLACE_ID);
        Mockito.verifyNoMoreInteractions(restaurantsRepositoryMock, workmatesRepositoryMock, savedStateHandle, auth, application);
    }

    @Test
    public void get_restaurant_details_when_liked_restaurant_null_details_not_null() {
        // GIVEN
        likedRestaurantsMutableLiveData.setValue(null);

        // WHEN
        RestaurantDetailsViewState restaurantDetailsViewState = TestUtils.getValueForTesting(
                restaurantDetailsViewModel.getRestaurantDetailsViewStateLiveData());

        // THEN
        assertNotNull(restaurantDetailsViewState);
        verify(application, atLeastOnce()).getString(R.string.workmate_has_joined, DISPLAY_NAME);
        Mockito.verifyNoMoreInteractions(restaurantsRepositoryMock, workmatesRepositoryMock, savedStateHandle, auth, application);
    }

    @Test
    public void get_restaurant_details_when_liked_restaurant_is_empty() {
        // GIVEN
        likedRestaurantsMutableLiveData.setValue(new ArrayList<>());

        // WHEN
        RestaurantDetailsViewState restaurantDetailsViewState = TestUtils.getValueForTesting(
                restaurantDetailsViewModel.getRestaurantDetailsViewStateLiveData());

        // THEN
        assertNotNull(restaurantDetailsViewState);
        verify(application, atLeastOnce()).getString(R.string.workmate_has_joined, DISPLAY_NAME);
        Mockito.verifyNoMoreInteractions(restaurantsRepositoryMock, workmatesRepositoryMock, savedStateHandle, auth, application);
    }

    @Test
    public void get_restaurant_details_when_liked_restaurant_don_t_contains_this_restaurant() {
        // GIVEN
        likedRestaurantsMutableLiveData.setValue(getDefaultRestaurantsWithNoLiked());

        // WHEN
        RestaurantDetailsViewState restaurantDetailsViewState = TestUtils.getValueForTesting(
                restaurantDetailsViewModel.getRestaurantDetailsViewStateLiveData());

        // THEN
        assertNotNull(restaurantDetailsViewState);
        verify(application, atLeastOnce()).getString(R.string.workmate_has_joined, DISPLAY_NAME);
        Mockito.verifyNoMoreInteractions(restaurantsRepositoryMock, workmatesRepositoryMock, savedStateHandle, auth, application);
    }

    @Test
    public void get_restaurant_details_when_get_image_null() {
        // GIVEN
        restaurantDetailsMutableLiveData.setValue(getDefaultRestaurantDetailsWithoutPicture());

        // WHEN
        RestaurantDetailsViewState restaurantDetailsViewState = TestUtils.getValueForTesting(
                restaurantDetailsViewModel.getRestaurantDetailsViewStateLiveData());

        // THEN
        assertNotNull(restaurantDetailsViewState);
        assertEquals(getDefaultRestaurantDetailsViewStateNoPicture(), restaurantDetailsViewState);

        verify(application, atLeastOnce()).getString(R.string.workmate_has_joined, DISPLAY_NAME);
        Mockito.verifyNoMoreInteractions(restaurantsRepositoryMock, workmatesRepositoryMock, savedStateHandle, auth, application);
    }

    @Test
    public void get_restaurant_details_when_restaurant_details_null_and_liked_restaurants_null_return_empty_details() {
        // GIVEN
        restaurantDetailsMutableLiveData.setValue(null);
        likedRestaurantsMutableLiveData.setValue(null);

        // WHEN
        RestaurantDetailsViewState restaurantDetailsViewState = TestUtils.getValueForTesting(
                restaurantDetailsViewModel.getRestaurantDetailsViewStateLiveData());

        // THEN
        assertNotNull(restaurantDetailsViewState);

        Mockito.verifyNoMoreInteractions(restaurantsRepositoryMock, workmatesRepositoryMock, savedStateHandle, auth);
//        Mockito.verifyNoMoreInteractions(restaurantsRepositoryMock, workmatesRepositoryMock, savedStateHandle, auth, application);
    }

    @Test
    public void get_workmates_who_have_chosen_this_restaurant() {
        // GIVEN

        // WHEN
        List<WorkmateItemViewState> workmatesItemViewState = TestUtils.getValueForTesting(restaurantDetailsViewModel.getWorkmatesViewStateLiveData());

        assertNotNull(workmatesItemViewState);

        Mockito.verifyNoMoreInteractions(restaurantsRepositoryMock, workmatesRepositoryMock, savedStateHandle, auth, application);
    }

//    @Test
//    public void get_select_restaurant_button_icon_if_restaurant_not_selected() {
//        // GIVEN
//
//        // WHEN
//        Integer selectRestaurantButtonIcon = TestUtils.getValueForTesting(restaurantDetailsViewModel.getSelectRestaurantButtonIcon());
//
//        assertNotNull(selectRestaurantButtonIcon);
//        assertEquals(R.drawable.ic_to_select, selectRestaurantButtonIcon.intValue());
//    }
//
//    @Test
//    public void get_select_restaurant_button_icon_if_restaurant_selected() {
//        // GIVEN
//
//        // WHEN
//        Integer selectRestaurantButtonIcon = TestUtils.getValueForTesting(restaurantDetailsViewModel.getSelectRestaurantButtonIcon());
//
//        assertNotNull(selectRestaurantButtonIcon);
//        assertEquals(R.drawable.ic_accept, selectRestaurantButtonIcon.intValue());
//    }

    @Test
    public void add_workmate_to_firestore_have_chosen_list() {
        // GIVEN

        // WHEN
        restaurantDetailsViewModel.addWorkmate();

        // THEN
        verify(workmatesRepositoryMock).addWorkmateToHaveChosenTodayList(firebaseUser, PLACE_ID_VALUE);
        verifyNoMoreInteractions(workmatesRepositoryMock);
    }

    @Test
    public void toggle_is_restaurant_liked() {
        // GIVEN

        // WHEN
        restaurantDetailsViewModel.toggleIsRestaurantLiked();

        // THEN
        verify(restaurantsRepositoryMock).toggleIsRestaurantLiked(firebaseUser, PLACE_ID_VALUE);
        verifyNoMoreInteractions(restaurantsRepositoryMock);
    }

    private RestaurantDetails getDefaultRestaurantDetails() {
        return new RestaurantDetails(
                PLACE_ID_VALUE,
                RESTAURANT_NAME,
                PHONE_NUMBER,
                ADDRESS,
                PICTURE_URL,
                4.0,
                WEBSITE
        );
    }

    private RestaurantDetails getDefaultRestaurantDetailsWithoutPicture() {
        return new RestaurantDetails(
                PLACE_ID_VALUE,
                RESTAURANT_NAME,
                PHONE_NUMBER,
                ADDRESS,
                null,
                4.0,
                WEBSITE
        );
    }

    private RestaurantDetailsViewState getDefaultRestaurantDetailsViewState() {
        return new RestaurantDetailsViewState(
                PLACE_ID_VALUE,
                RESTAURANT_NAME,
                ADDRESS,
                2,
                PICTURE_URL,
                PHONE_NUMBER,
                WEBSITE,
                R.string.like
        );
    }

    private RestaurantDetailsViewState getDefaultRestaurantDetailsViewStateNoPicture() {
        return new RestaurantDetailsViewState(
                PLACE_ID_VALUE,
                RESTAURANT_NAME,
                ADDRESS,
                2,
                PICTURE_URL_NO_PICTURE,
                PHONE_NUMBER,
                WEBSITE,
                R.string.like
        );
    }

    private List<Restaurant> getDefaultRestaurantsLiked() {
        List<Restaurant> restaurants = new ArrayList<>();

        restaurants.add(new Restaurant(PLACE_ID_VALUE, "restaurantName1", "address", true, 4.0, 1.256546, 3.256546, PICTURE_URL));
        restaurants.add(new Restaurant("2", "restaurantName2", "address", false, 3.5, 1.256546, 3.256546, PICTURE_URL));

        return restaurants;
    }

    private List<Restaurant> getDefaultRestaurantsWithNoLiked() {
        List<Restaurant> restaurants = new ArrayList<>();

        restaurants.add(new Restaurant("1", "restaurantName1", "address", true, 4.0, 1.256546, 3.256546, PICTURE_URL));
        restaurants.add(new Restaurant("2", "restaurantName2", "address", false, 3.5, 1.256546, 3.256546, PICTURE_URL));

        return restaurants;
    }

    private List<Workmate> getDefaultWorkmatesHaveChosen() {
        List<Workmate> workmates = new ArrayList<>();

        workmates.add(new Workmate(CURRENT_FIREBASE_USER_ID, FORMATTED_WORKMATES_HAS_JOINED, "mail", PICTURE_URL, PLACE_ID_VALUE));
        workmates.add(new Workmate("2", DISPLAY_NAME, "mail", PICTURE_URL, PLACE_ID_VALUE));
        workmates.add(new Workmate("3", DISPLAY_NAME, "mail", PICTURE_URL, PLACE_ID_VALUE));
        workmates.add(new Workmate("4", DISPLAY_NAME, "mail", PICTURE_URL, PLACE_ID_VALUE));
        workmates.add(new Workmate("5", DISPLAY_NAME, "mail", PICTURE_URL, PLACE_ID_VALUE));
        workmates.add(new Workmate("6", DISPLAY_NAME, "mail", PICTURE_URL, PLACE_ID_VALUE));

        return workmates;
    }

    private List<WorkmateItemViewState> getDefaultWorkmatesItemViewStateHaveChosen() {
        List<WorkmateItemViewState> workmateItemViewStates = new ArrayList<>();

        workmateItemViewStates.add(new WorkmateItemViewState("2", FORMATTED_WORKMATES_HAS_JOINED, PICTURE_URL, TEXT_COLOR_HAS_DECIDED, TEXT_STYLE_HAS_DECIDED));
        workmateItemViewStates.add(new WorkmateItemViewState("3", FORMATTED_WORKMATES_HAS_JOINED, PICTURE_URL, TEXT_COLOR_HAS_DECIDED, TEXT_STYLE_HAS_DECIDED));
        workmateItemViewStates.add(new WorkmateItemViewState("4", FORMATTED_WORKMATES_HAS_JOINED, PICTURE_URL, TEXT_COLOR_HAS_DECIDED, TEXT_STYLE_HAS_DECIDED));
        workmateItemViewStates.add(new WorkmateItemViewState("5", FORMATTED_WORKMATES_HAS_JOINED, PICTURE_URL, TEXT_COLOR_HAS_DECIDED, TEXT_STYLE_HAS_DECIDED));
        workmateItemViewStates.add(new WorkmateItemViewState("6", FORMATTED_WORKMATES_HAS_JOINED, PICTURE_URL, TEXT_COLOR_HAS_DECIDED, TEXT_STYLE_HAS_DECIDED));

        return workmateItemViewStates;
    }
}