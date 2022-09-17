package com.suonk.oc_project7.repositories.restaurants;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.suonk.oc_project7.BuildConfig;
import com.suonk.oc_project7.api.PlacesApiService;
import com.suonk.oc_project7.model.data.place_details.PlaceDetailsResponse;
import com.suonk.oc_project7.model.data.places.NearbyPlaceResponse;
import com.suonk.oc_project7.model.data.restaurant.RestaurantDetails;
import com.suonk.oc_project7.model.data.workmate.Workmate;
import com.suonk.oc_project7.utils.TestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RunWith(MockitoJUnitRunner.class)
public class RestaurantsRepositoryImplTest {

    @Rule
    public final InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private RestaurantsRepository restaurantsRepository;

    //region ============================================= MOCK =============================================

    @NonNull
    private final PlacesApiService placesApiServiceMock = mock(PlacesApiService.class);

    private final Location locationMock = mock(Location.class);

    private final FirebaseFirestore firebaseFirestoreMock = mock(FirebaseFirestore.class);
    private final CollectionReference collectionReferenceMock = mock(CollectionReference.class);
    private final DocumentReference documentReferenceMock = mock(DocumentReference.class);

    private final Call<NearbyPlaceResponse> mockedCall = Mockito.mock(Call.class);
    private final Response<NearbyPlaceResponse> mockedResponse = Mockito.mock(Response.class);

    private final Call<PlaceDetailsResponse> mockedCallPlaceDetails = Mockito.mock(Call.class);
    private final Response<PlaceDetailsResponse> mockedResponsePlaceDetails = Mockito.mock(Response.class);

    //endregion

    //region ======================================== DEFAULTS VALUES =======================================

    private static final String PLACE_ID_VALUE = "PLACE_ID_VALUE";
    private static final String PLACE_ID_VALUE_NO_PICTURE = "PLACE_ID_VALUE_NO_PICTURE";
    private static final String RESTAURANT_NAME = "PIZZA HUT";

    private static final String EMAIL = "EMAIL";
    private static final String PHOTO_REFERENCE = "PHOTO_REFERENCE";
    private static final String PICTURE_URL = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&key=" + BuildConfig.PLACES_API_KEY + "&photo_reference=" + PHOTO_REFERENCE;

    private static final String UID = "UID";
    private static final String U_NAME = "U_NAME";
    private static final double DEFAULT_LATITUDE = 48.9575329;
    private static final double DEFAULT_LONGITUDE = 2.5594583;
    private String DEFAULT_LOCATION = DEFAULT_LATITUDE + "," + DEFAULT_LONGITUDE;

    private static final String DEFAULT_PLACE_ID = "DEFAULT_PLACE_ID";

    private static final String ALL_WORKMATES = "all_workmates";

    private static final List<String> DEFAULT_LIKED_RESTAURANTS = Arrays.asList(
            PLACE_ID_VALUE,
            PLACE_ID_VALUE_NO_PICTURE,
            DEFAULT_PLACE_ID);

    private static final List<String> DEFAULT_LIKED_RESTAURANTS_AFTER_DISLIKE = Arrays.asList(
            PLACE_ID_VALUE_NO_PICTURE,
            DEFAULT_PLACE_ID);

    private static final List<String> DEFAULT_LIKED_RESTAURANTS_AFTER_FIRST_LIKE = Collections.singletonList(
            PLACE_ID_VALUE);

    //endregion

    @Before
    public void setup() {
        doReturn(DEFAULT_LATITUDE).when(locationMock).getLatitude();
        doReturn(DEFAULT_LONGITUDE).when(locationMock).getLongitude();

        DEFAULT_LOCATION = locationMock.getLatitude() + "," + locationMock.getLongitude();

        restaurantsRepository = new RestaurantsRepositoryImpl(placesApiServiceMock, firebaseFirestoreMock);
    }

    @Test
    public void get_near_restaurants_with_default_location() {
        // GIVEN
        doReturn(mockedCall).when(placesApiServiceMock).getNearbyPlacesResponse(DEFAULT_LOCATION);

        ArgumentCaptor<Callback<NearbyPlaceResponse>> nearbyPlaceResponseCallBackCaptor =
                ArgumentCaptor.forClass(Callback.class);

        // WHEN
        restaurantsRepository.getNearRestaurants(DEFAULT_LOCATION);

        // THEN
        verify(mockedCall).enqueue(nearbyPlaceResponseCallBackCaptor.capture());
        Callback<NearbyPlaceResponse> callback = nearbyPlaceResponseCallBackCaptor.getValue();

        // WHEN II
        callback.onResponse(mockedCall, mockedResponse);

        // THEN
        verify(placesApiServiceMock, atLeastOnce()).getNearbyPlacesResponse(DEFAULT_LOCATION);
        verifyNoMoreInteractions(placesApiServiceMock);
    }

    @Test
    public void get_near_restaurants_with_empty_location() {
        // GIVEN
        doReturn(mockedCall).when(placesApiServiceMock).getNearbyPlacesResponse("");

        ArgumentCaptor<Callback<NearbyPlaceResponse>> nearbyPlaceResponseCallBackCaptor =
                ArgumentCaptor.forClass(Callback.class);

        // WHEN
        restaurantsRepository.getNearRestaurants("");

        // THEN
        verify(mockedCall).enqueue(nearbyPlaceResponseCallBackCaptor.capture());
        Callback<NearbyPlaceResponse> callback = nearbyPlaceResponseCallBackCaptor.getValue();

        // WHEN II
        callback.onResponse(mockedCall, mockedResponse);

        // THEN
        verify(placesApiServiceMock, atLeastOnce()).getNearbyPlacesResponse("");
        verifyNoMoreInteractions(placesApiServiceMock);
    }

    @Test
    public void get_near_restaurants_with_if_call_return_null() {
        // GIVEN
        doReturn(null).when(placesApiServiceMock).getNearbyPlacesResponse(DEFAULT_LOCATION);

        // WHEN
        restaurantsRepository.getNearRestaurants(DEFAULT_LOCATION);

        // THEN
        verify(placesApiServiceMock, atLeastOnce()).getNearbyPlacesResponse(DEFAULT_LOCATION);
        verifyNoMoreInteractions(placesApiServiceMock);
    }

    @Test
    public void get_restaurant_details_by_id_with_default_place_id() {
        // GIVEN
        doReturn(mockedCallPlaceDetails).when(placesApiServiceMock).getPlaceDetailsById(DEFAULT_PLACE_ID);

        ArgumentCaptor<Callback<PlaceDetailsResponse>> placeDetailsResponseCallBackCaptor =
                ArgumentCaptor.forClass(Callback.class);

        // WHEN
        restaurantsRepository.getRestaurantDetailsById(DEFAULT_PLACE_ID);

        // THEN
        verify(mockedCallPlaceDetails).enqueue(placeDetailsResponseCallBackCaptor.capture());
        Callback<PlaceDetailsResponse> callback = placeDetailsResponseCallBackCaptor.getValue();

        // WHEN II
        callback.onResponse(mockedCallPlaceDetails, mockedResponsePlaceDetails);

        // THEN
        verify(placesApiServiceMock, atLeastOnce()).getPlaceDetailsById(DEFAULT_PLACE_ID);
        verifyNoMoreInteractions(placesApiServiceMock);
    }

    @Test
    public void get_restaurant_details_by_id_with_empty_place_id() {
        // GIVEN
        doReturn(mockedCallPlaceDetails).when(placesApiServiceMock).getPlaceDetailsById("");

        // WHEN
        RestaurantDetails restaurantDetails = TestUtils.getValueForTesting(
                restaurantsRepository.getRestaurantDetailsById(""));

        // THEN
        assertEquals(getDefaultRestaurantDetails(), restaurantDetails);
        verify(placesApiServiceMock, atLeastOnce()).getPlaceDetailsById("");
        verifyNoMoreInteractions(placesApiServiceMock);
    }

    @Test
    public void get_restaurant_details_by_id_if_call_return_null() {
        // GIVEN
        doReturn(null).when(placesApiServiceMock).getPlaceDetailsById(DEFAULT_PLACE_ID);

        // WHEN
        RestaurantDetails restaurantDetails = TestUtils.getValueForTesting(
                restaurantsRepository.getRestaurantDetailsById(DEFAULT_PLACE_ID));

        // THEN
        assertEquals(getDefaultRestaurantDetails(), restaurantDetails);
        verify(placesApiServiceMock, atLeastOnce()).getPlaceDetailsById(DEFAULT_PLACE_ID);
        verifyNoMoreInteractions(placesApiServiceMock);
    }

    @Test
    public void test_toggle_is_restaurant_liked() {
        // GIVEN
        doReturn(collectionReferenceMock).when(firebaseFirestoreMock).collection(ALL_WORKMATES);
        doReturn(documentReferenceMock).when(collectionReferenceMock).document(UID);

        // WHEN
        restaurantsRepository.toggleIsRestaurantLiked(getDefaultCurrentUser(),
                PLACE_ID_VALUE,
                RESTAURANT_NAME
        );

        // THEN

        verify(firebaseFirestoreMock).collection(ALL_WORKMATES);
        verify(collectionReferenceMock).document(UID);
        verify(documentReferenceMock).set(getDefaultCurrentUserAfterDislike());
    }

    @Test
    public void test_toggle_is_restaurant_liked_with_current_user_who_has_not_restaurants_liked() {
        // GIVEN
        doReturn(collectionReferenceMock).when(firebaseFirestoreMock).collection(ALL_WORKMATES);
        doReturn(documentReferenceMock).when(collectionReferenceMock).document(UID);

        // WHEN
        restaurantsRepository.toggleIsRestaurantLiked(getDefaultCurrentUserWithNoRestaurantsLiked(),
                PLACE_ID_VALUE,
                RESTAURANT_NAME
        );

        // THEN

        verify(firebaseFirestoreMock).collection(ALL_WORKMATES);
        verify(collectionReferenceMock).document(UID);
        verify(documentReferenceMock).set(getDefaultCurrentUserWithNoRestaurantsLikedAfterLike());
    }

    @Test
    public void test_get_restaurant_picture_url() {
        // WHEN
        String urlToTest = restaurantsRepository.getRestaurantPictureURL(PHOTO_REFERENCE);

        // THEN
        assertEquals(PICTURE_URL, urlToTest);
    }

    public RestaurantDetails getDefaultRestaurantDetails() {
        return new RestaurantDetails(
                "",
                "",
                "",
                "",
                "",
                0.0,
                ""
        );
    }

    private Workmate getDefaultCurrentUser() {
        return new Workmate(UID, U_NAME, EMAIL, PICTURE_URL, PLACE_ID_VALUE, RESTAURANT_NAME,
                DEFAULT_LIKED_RESTAURANTS);
    }

    private Workmate getDefaultCurrentUserAfterDislike() {
        return new Workmate(UID, U_NAME, EMAIL, PICTURE_URL, PLACE_ID_VALUE, RESTAURANT_NAME,
                DEFAULT_LIKED_RESTAURANTS_AFTER_DISLIKE);
    }

    private Workmate getDefaultCurrentUserWithNoRestaurantsLiked() {
        return new Workmate(UID, U_NAME, EMAIL, PICTURE_URL, PLACE_ID_VALUE, RESTAURANT_NAME, new ArrayList<>());
    }

    private Workmate getDefaultCurrentUserWithNoRestaurantsLikedAfterLike() {
        return new Workmate(UID, U_NAME, EMAIL, PICTURE_URL, PLACE_ID_VALUE, RESTAURANT_NAME, DEFAULT_LIKED_RESTAURANTS_AFTER_FIRST_LIKE);
    }
}