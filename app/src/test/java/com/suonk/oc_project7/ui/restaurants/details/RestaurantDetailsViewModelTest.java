package com.suonk.oc_project7.ui.restaurants.details;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import android.app.Application;
import android.graphics.Color;
import android.graphics.Typeface;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.suonk.oc_project7.R;
import com.suonk.oc_project7.model.data.restaurant.RestaurantDetails;
import com.suonk.oc_project7.model.data.workmate.Workmate;
import com.suonk.oc_project7.repositories.restaurants.RestaurantsRepository;
import com.suonk.oc_project7.repositories.restaurants.RestaurantsRepositoryImpl;
import com.suonk.oc_project7.repositories.workmates.WorkmatesRepository;
import com.suonk.oc_project7.repositories.workmates.WorkmatesRepositoryImpl;
import com.suonk.oc_project7.ui.workmates.WorkmateItemViewState;
import com.suonk.oc_project7.utils.TestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class RestaurantDetailsViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private RestaurantDetailsViewModel restaurantDetailsViewModel;

    //region ============================================= MOCK =============================================

    private final WorkmatesRepository workmatesRepositoryMock = mock(WorkmatesRepositoryImpl.class);
    private final RestaurantsRepository restaurantsRepositoryMock = mock(RestaurantsRepositoryImpl.class);
    private final FirebaseAuth auth = mock(FirebaseAuth.class);
    private final Application application = Mockito.mock(Application.class);
    private final SavedStateHandle savedStateHandle = mock(SavedStateHandle.class);
    private final FirebaseUser firebaseUser = mock(FirebaseUser.class);

    //endregion

    //region ======================================== DEFAULTS VALUES =======================================

    private static final String PLACE_ID_VALUE = "PLACE_ID_VALUE";
    private static final String PLACE_ID = "PLACE_ID";
    private static final String RESTAURANT_NAME = "RESTAURANT_NAME";
    private static final String PHOTO_REFERENCE = "PHOTO_REFERENCE";
    private static final String PICTURE_URL = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&key=key&photo_reference=" + PHOTO_REFERENCE;
    private static final String PHONE_NUMBER = "PHONE_NUMBER";
    private static final String ADDRESS = "ADDRESS";

    private static final String PLACE_ID_VALUE_NO_PICTURE = "PLACE_ID_VALUE_NO_PICTURE";
    private static final String RESTAURANT_NAME_NO_PICTURE = "RESTAURANT_NAME_NO_PICTURE";
    private static final String PICTURE_URL_NO_REFERENCE = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&key=key&photo_reference=";
    private static final String PICTURE_URL_NO_PICTURE = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/No_image_available.svg/1024px-No_image_available.svg.png";
    private static final String ADDRESS_2 = "ADDRESS_2";

    private static final double RATING = 4.0;
    private static final int DEFAULT_RATING = 2;
    private static final double DEFAULT_LATITUDE = 1.256546;
    private static final double DEFAULT_LONGITUDE = 3.256546;
    private static final String WEBSITE = "WEBSITE";
    private static final String DISPLAY_NAME = "DISPLAY_NAME";
    private static final String CURRENT_FIREBASE_USER_ID = "CURRENT_FIREBASE_USER_ID";
    private static final String FORMATTED_WORKMATES_HAS_JOINED = "FORMATTED_WORKMATES_HAS_JOINED";

    private static final List<String> DEFAULT_LIKED_RESTAURANTS = Arrays.asList(
            PLACE_ID_VALUE,
            PLACE_ID_VALUE_NO_PICTURE,
            "sup3");

    private static final List<String> DEFAULT_LIKED_RESTAURANTS_2 = Arrays.asList(
            "1",
            PLACE_ID_VALUE_NO_PICTURE,
            "sup3");

    private static final int SELECT_BUTTON_IS_SELECTED = R.drawable.ic_accept;
    private static final int SELECT_BUTTON_IS_NOT_SELECTED = R.drawable.ic_to_select;

    private static final int TEXT_COLOR_HAS_DECIDED = Color.BLACK;
    private static final int TEXT_STYLE_HAS_DECIDED = Typeface.NORMAL;
    private static final int TEXT_COLOR_HAS_NOT_DECIDED = -7829368;
    private static final int TEXT_STYLE_HAS_NOT_DECIDED = 2;

    //endregion

    //region =========================================== LIVEDATA ===========================================

    private final MutableLiveData<RestaurantDetails> restaurantDetailsMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Workmate>> workmatesHaveChosenMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<Workmate> currentUserLiveData = new MutableLiveData<>();

    //endregion

    @Before
    public void setup() {
        doReturn(PLACE_ID_VALUE).when(savedStateHandle).get(PLACE_ID);
        doReturn(restaurantDetailsMutableLiveData).when(restaurantsRepositoryMock).getRestaurantDetailsById(PLACE_ID_VALUE);
        doReturn(workmatesHaveChosenMutableLiveData).when(workmatesRepositoryMock).getWorkmatesHaveChosenTodayLiveData();
        doReturn(currentUserLiveData).when(workmatesRepositoryMock).getCurrentUserLiveData(CURRENT_FIREBASE_USER_ID);
        doReturn(FORMATTED_WORKMATES_HAS_JOINED).when(application).getString(R.string.workmate_has_joined, DISPLAY_NAME);
        doReturn(CURRENT_FIREBASE_USER_ID).when(firebaseUser).getUid();
        doReturn(firebaseUser).when(auth).getCurrentUser();

        restaurantDetailsMutableLiveData.setValue(getDefaultRestaurantDetails());
        workmatesHaveChosenMutableLiveData.setValue(getDefaultWorkmatesHaveChosen());
        currentUserLiveData.setValue(getDefaultCurrentUser());

        restaurantDetailsViewModel = new RestaurantDetailsViewModel(
                workmatesRepositoryMock,
                restaurantsRepositoryMock,
                auth,
                application,
                savedStateHandle
        );

        verify(savedStateHandle, atLeastOnce()).get(PLACE_ID);
        verify(restaurantsRepositoryMock).getRestaurantDetailsById(PLACE_ID_VALUE);
        verify(workmatesRepositoryMock).getWorkmatesHaveChosenTodayLiveData();
        verify(workmatesRepositoryMock).getCurrentUserLiveData(CURRENT_FIREBASE_USER_ID);

        verify(auth, atLeastOnce()).getCurrentUser();
    }

    //region ==================================== GET RESTAURANT DETAILS ====================================

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
    public void get_restaurant_details_when_workmates_are_null() {
        // GIVEN

        // WHEN
        workmatesHaveChosenMutableLiveData.setValue(null);

        RestaurantDetailsViewState restaurantDetailsViewState = TestUtils.getValueForTesting(
                restaurantDetailsViewModel.getRestaurantDetailsViewStateLiveData());

        // THEN
        assertNotNull(restaurantDetailsViewState);
        verify(savedStateHandle).get(PLACE_ID);
        Mockito.verifyNoMoreInteractions(restaurantsRepositoryMock, workmatesRepositoryMock, savedStateHandle, auth, application);
    }

    @Test
    public void get_restaurant_details_when_current_user_null_details_not_null() {
        // GIVEN
        currentUserLiveData.setValue(null);

        // WHEN
        RestaurantDetailsViewState restaurantDetailsViewState = TestUtils.getValueForTesting(
                restaurantDetailsViewModel.getRestaurantDetailsViewStateLiveData());

        // THEN
        assertNotNull(restaurantDetailsViewState);
        assertEquals(getDefaultRestaurantDetailsViewStateNotLikedNotSelected(), restaurantDetailsViewState);

        Mockito.verifyNoMoreInteractions(restaurantsRepositoryMock, workmatesRepositoryMock, savedStateHandle, auth, application);
    }

    @Test
    public void get_restaurant_details_when_restaurant_image_null() {
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

        // WHEN
        RestaurantDetailsViewState restaurantDetailsViewState = TestUtils.getValueForTesting(
                restaurantDetailsViewModel.getRestaurantDetailsViewStateLiveData());

        // THEN
        assertNotNull(restaurantDetailsViewState);
        assertEquals(getDefaultRestaurantDetailsViewStateIfPlaceIdNotCorrectlyMatch(), restaurantDetailsViewState);

        verify(application, atLeastOnce()).getString(R.string.workmate_has_joined, DISPLAY_NAME);
        Mockito.verifyNoMoreInteractions(restaurantsRepositoryMock, workmatesRepositoryMock, savedStateHandle, auth, application);
    }

    @Test
    public void get_restaurant_details_liked_restaurants_that_do_not_contain_place_id() {
        // GIVEN
        currentUserLiveData.setValue(getDefaultCurrentUserWithLikedRestaurantsThatDoNotContainPlaceId());

        // WHEN
        RestaurantDetailsViewState restaurantDetailsViewState = TestUtils.getValueForTesting(
                restaurantDetailsViewModel.getRestaurantDetailsViewStateLiveData());

        List<WorkmateItemViewState> workmates = TestUtils.getValueForTesting(restaurantDetailsViewModel.getWorkmatesViewStateLiveData());

        // THEN
        assertNotNull(restaurantDetailsViewState);
        assertNotNull(workmates);
        assertEquals(getDefaultWorkmatesItemViewStateHaveChosen(), workmates);
        assertEquals(getDefaultRestaurantDetailsViewStateNotLiked(), restaurantDetailsViewState);

        verify(application, atLeastOnce()).getString(R.string.workmate_has_joined, DISPLAY_NAME);
        Mockito.verifyNoMoreInteractions(restaurantsRepositoryMock, workmatesRepositoryMock, savedStateHandle, auth, application);
    }

    //endregion

    @Test
    public void get_workmates_who_have_chosen_this_restaurant() {
        // GIVEN

        // WHEN
        List<WorkmateItemViewState> workmatesItemViewState = TestUtils.getValueForTesting(restaurantDetailsViewModel.getWorkmatesViewStateLiveData());

        assertNotNull(workmatesItemViewState);

        Mockito.verifyNoMoreInteractions(restaurantsRepositoryMock, workmatesRepositoryMock, savedStateHandle, auth, application);
    }

    @Test
    public void add_workmate_to_firestore_have_chosen_list() {
        // GIVEN
        TestUtils.getValueForTesting(restaurantDetailsViewModel.getRestaurantDetailsViewStateLiveData());

        // WHEN
        restaurantDetailsViewModel.addWorkmate();

        // THEN
        verify(workmatesRepositoryMock).addWorkmateToHaveChosenTodayList(getDefaultCurrentUser(), PLACE_ID_VALUE, RESTAURANT_NAME);
        verify(application, atLeastOnce()).getString(R.string.workmate_has_joined, DISPLAY_NAME);
        Mockito.verifyNoMoreInteractions(restaurantsRepositoryMock, workmatesRepositoryMock, savedStateHandle, auth, application);
    }

    @Test
    public void add_workmate_to_firestore_have_chosen_list_without_observe() {
        // GIVEN

        // WHEN
        restaurantDetailsViewModel.addWorkmate();

        // THEN
        Mockito.verifyNoMoreInteractions(restaurantsRepositoryMock, workmatesRepositoryMock, savedStateHandle, auth, application);
    }

    @Test
    public void add_workmate_to_firestore_have_chosen_list_with_current_user_null() {
        // GIVEN
        currentUserLiveData.setValue(null);
        TestUtils.getValueForTesting(restaurantDetailsViewModel.getRestaurantDetailsViewStateLiveData());

        // WHEN
        restaurantDetailsViewModel.addWorkmate();

        // THEN
        Mockito.verifyNoMoreInteractions(restaurantsRepositoryMock, workmatesRepositoryMock, savedStateHandle, auth, application);
    }

    @Test
    public void add_workmate_to_firestore_have_chosen_list_if_restaurant_details_null() {
        // GIVEN
        restaurantDetailsMutableLiveData.setValue(null);

        // WHEN
        restaurantDetailsViewModel.addWorkmate();

        // THEN
        Mockito.verifyNoMoreInteractions(restaurantsRepositoryMock, workmatesRepositoryMock, savedStateHandle, auth, application);
    }

    @Test
    public void toggle_is_restaurant_liked() {
        // GIVEN
        TestUtils.getValueForTesting(restaurantDetailsViewModel.getRestaurantDetailsViewStateLiveData());

        // WHEN
        restaurantDetailsViewModel.toggleIsRestaurantLiked();

        // THEN
        verify(restaurantsRepositoryMock).toggleIsRestaurantLiked(getDefaultCurrentUser(), PLACE_ID_VALUE, RESTAURANT_NAME);
        verify(application, atLeastOnce()).getString(R.string.workmate_has_joined, DISPLAY_NAME);
        Mockito.verifyNoMoreInteractions(restaurantsRepositoryMock, workmatesRepositoryMock, savedStateHandle, auth, application);
    }

    @Test
    public void toggle_is_restaurant_liked_without_observe() {
        // GIVEN

        // WHEN
        restaurantDetailsViewModel.toggleIsRestaurantLiked();

        // THEN
        Mockito.verifyNoMoreInteractions(restaurantsRepositoryMock, workmatesRepositoryMock, savedStateHandle, auth, application);
    }

    @Test
    public void toggle_is_restaurant_liked_with_current_user_null() {
        // GIVEN
        currentUserLiveData.setValue(null);
        TestUtils.getValueForTesting(restaurantDetailsViewModel.getRestaurantDetailsViewStateLiveData());

        // WHEN
        restaurantDetailsViewModel.toggleIsRestaurantLiked();

        // THEN
//        verify(application, atLeastOnce()).getString(R.string.workmate_has_joined, DISPLAY_NAME);
        Mockito.verifyNoMoreInteractions(restaurantsRepositoryMock, workmatesRepositoryMock, savedStateHandle, auth, application);
    }

    @Test
    public void toggle_is_restaurant_liked_if_restaurant_details_null() {
        // GIVEN
        restaurantDetailsMutableLiveData.setValue(null);

        // WHEN
        restaurantDetailsViewModel.toggleIsRestaurantLiked();

        // THEN
        Mockito.verifyNoMoreInteractions(restaurantsRepositoryMock, workmatesRepositoryMock, savedStateHandle, auth, application);
    }

    //region ====================================== RESTAURANT DETAILS ======================================

    private RestaurantDetails getDefaultRestaurantDetails() {
        return new RestaurantDetails(
                PLACE_ID_VALUE,
                RESTAURANT_NAME,
                PHONE_NUMBER,
                ADDRESS,
                PICTURE_URL,
                RATING,
                WEBSITE
        );
    }

    private RestaurantDetails getDefaultRestaurantDetailsWithoutPicture() {
        return new RestaurantDetails(
                PLACE_ID_VALUE_NO_PICTURE,
                RESTAURANT_NAME,
                PHONE_NUMBER,
                ADDRESS,
                null,
                RATING,
                WEBSITE
        );
    }

    private RestaurantDetailsViewState getDefaultRestaurantDetailsViewState() {
        return new RestaurantDetailsViewState(
                PLACE_ID_VALUE,
                RESTAURANT_NAME,
                ADDRESS,
                DEFAULT_RATING,
                PICTURE_URL,
                PHONE_NUMBER,
                WEBSITE,
                SELECT_BUTTON_IS_SELECTED,
                R.string.dislike
        );
    }

    private RestaurantDetailsViewState getDefaultRestaurantDetailsViewStateNotLiked() {
        return new RestaurantDetailsViewState(
                PLACE_ID_VALUE,
                RESTAURANT_NAME,
                ADDRESS,
                DEFAULT_RATING,
                PICTURE_URL,
                PHONE_NUMBER,
                WEBSITE,
                SELECT_BUTTON_IS_SELECTED,
                R.string.like
        );
    }

    private RestaurantDetailsViewState getDefaultRestaurantDetailsViewStateNotLikedNotSelected() {
        return new RestaurantDetailsViewState(
                PLACE_ID_VALUE,
                RESTAURANT_NAME,
                ADDRESS,
                DEFAULT_RATING,
                PICTURE_URL,
                PHONE_NUMBER,
                WEBSITE,
                SELECT_BUTTON_IS_NOT_SELECTED,
                R.string.like
        );
    }

    private RestaurantDetailsViewState getDefaultRestaurantDetailsViewStateNoPicture() {
        return new RestaurantDetailsViewState(
                PLACE_ID_VALUE_NO_PICTURE,
                RESTAURANT_NAME,
                ADDRESS,
                DEFAULT_RATING,
                PICTURE_URL_NO_PICTURE,
                PHONE_NUMBER,
                WEBSITE,
                SELECT_BUTTON_IS_SELECTED,
                R.string.dislike
        );
    }

    private RestaurantDetailsViewState getDefaultRestaurantDetailsViewStateIfPlaceIdNotCorrectlyMatch() {
        return new RestaurantDetailsViewState(
                "1",
                "",
                "",
                0,
                "",
                "",
                "",
                SELECT_BUTTON_IS_NOT_SELECTED,
                R.string.like);
    }

    //endregion

    //region ======================================= GET DEFAULT USER =======================================

    private Workmate getDefaultCurrentUser() {
        return new Workmate(
                CURRENT_FIREBASE_USER_ID,
                FORMATTED_WORKMATES_HAS_JOINED,
                "mail",
                PICTURE_URL,
                PLACE_ID_VALUE,
                RESTAURANT_NAME,
                DEFAULT_LIKED_RESTAURANTS
        );
    }

    private Workmate getDefaultCurrentUserWithNoRestaurantsLiked() {
        return new Workmate(
                CURRENT_FIREBASE_USER_ID,
                FORMATTED_WORKMATES_HAS_JOINED,
                "mail",
                PICTURE_URL,
                PLACE_ID_VALUE,
                RESTAURANT_NAME,
                new ArrayList<>()
        );
    }

    private Workmate getDefaultCurrentUserWithLikedRestaurantsThatDoNotContainPlaceId() {
        return new Workmate(
                CURRENT_FIREBASE_USER_ID,
                FORMATTED_WORKMATES_HAS_JOINED,
                "mail",
                PICTURE_URL,
                PLACE_ID_VALUE,
                RESTAURANT_NAME,
                DEFAULT_LIKED_RESTAURANTS_2
        );
    }

    //endregion

    private List<Workmate> getDefaultWorkmatesHaveChosen() {
        List<Workmate> workmates = new ArrayList<>();

        workmates.add(getDefaultCurrentUser());
        workmates.add(new Workmate("2", DISPLAY_NAME, "mail", PICTURE_URL, PLACE_ID_VALUE, RESTAURANT_NAME, new ArrayList<>()));
        workmates.add(new Workmate("3", DISPLAY_NAME, "mail", PICTURE_URL, PLACE_ID_VALUE, RESTAURANT_NAME, new ArrayList<>()));
        workmates.add(new Workmate("4", DISPLAY_NAME, "mail", PICTURE_URL, PLACE_ID_VALUE, RESTAURANT_NAME, new ArrayList<>()));
        workmates.add(new Workmate("5", DISPLAY_NAME, "mail", PICTURE_URL, PLACE_ID_VALUE, RESTAURANT_NAME, new ArrayList<>()));
        workmates.add(new Workmate("6", DISPLAY_NAME, "mail", PICTURE_URL, PLACE_ID_VALUE, RESTAURANT_NAME, new ArrayList<>()));

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