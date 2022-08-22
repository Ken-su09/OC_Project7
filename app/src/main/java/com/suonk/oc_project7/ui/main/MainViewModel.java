package com.suonk.oc_project7.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.suonk.oc_project7.R;
import com.suonk.oc_project7.model.data.permission_checker.PermissionChecker;
import com.suonk.oc_project7.model.data.place_auto_complete.PlaceAutocomplete;
import com.suonk.oc_project7.model.data.places.CurrentLocation;
import com.suonk.oc_project7.model.data.restaurant.Restaurant;
import com.suonk.oc_project7.repositories.current_location.CurrentLocationRepository;
import com.suonk.oc_project7.repositories.current_user_search.CurrentUserSearchRepository;
import com.suonk.oc_project7.repositories.places.PlacesRepository;
import com.suonk.oc_project7.repositories.restaurants.RestaurantsRepository;
import com.suonk.oc_project7.repositories.user.UserRepository;
import com.suonk.oc_project7.ui.restaurants.list.RestaurantItemViewState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;

@HiltViewModel
public class MainViewModel extends ViewModel {

    @NonNull
    private final CurrentLocationRepository locationRepository;

    @NonNull
    private final UserRepository userRepository;

    @NonNull
    private final PlacesRepository placesRepository;

    @NonNull
    private final CurrentUserSearchRepository currentUserSearchRepository;

    private final MutableLiveData<Boolean> isPermissionEnabledLiveData = new MutableLiveData<>();

    private MutableLiveData<MainViewState> mainViewStateLiveData = new MutableLiveData<>();

    private final MediatorLiveData<List<MainItemViewState>> itemViewStates = new MediatorLiveData<>();

    private MutableLiveData<CharSequence> searchInputLiveData = new MutableLiveData<>();

    private final Context context;

    @NonNull
    private CurrentLocation currentLocation;

    private final PermissionChecker permissionChecker;

    @Inject
    public MainViewModel(@NonNull CurrentLocationRepository locationRepository,
                         @NonNull UserRepository userRepository,
                         @NonNull PlacesRepository placesRepository,
                         @NonNull CurrentUserSearchRepository currentUserSearchRepository,
                         @NonNull PermissionChecker permissionChecker,
                         @ApplicationContext Context context) {
        this.locationRepository = locationRepository;
        this.userRepository = userRepository;
        this.placesRepository = placesRepository;
        this.currentUserSearchRepository = currentUserSearchRepository;
        this.permissionChecker = permissionChecker;
        this.context = context;

        LiveData<List<PlaceAutocomplete>> placesAutocompleteLiveData = Transformations.switchMap(searchInputLiveData, input -> {
//            return placesRepository.getPlacesAutocomplete(Locale.getDefault().getLanguage(),
//                    currentLocation.getLat() + "," + currentLocation.getLng(), input);
            return placesRepository.getPlacesAutocomplete(Locale.getDefault().getLanguage(), input.toString());
        });

        itemViewStates.addSource(placesAutocompleteLiveData, placesAutocomplete -> {
            combine(placesAutocomplete, searchInputLiveData.getValue());
        });

        itemViewStates.addSource(searchInputLiveData, input -> {
            combine(placesAutocompleteLiveData.getValue(), input);
        });
    }

    private void combine(@Nullable List<PlaceAutocomplete> placesAutocomplete, @Nullable CharSequence input) {
        List<MainItemViewState> restaurantsItemViews = new ArrayList<>();

        if (placesAutocomplete != null) {
            for (PlaceAutocomplete placeAutocomplete : placesAutocomplete) {
                SpannableString textToHighlight;

                if (input == null) {
                    textToHighlight = new SpannableString("");
                } else {
                    textToHighlight = (SpannableString) setHighLightedText(placeAutocomplete.getRestaurantName().toLowerCase(Locale.ROOT),
                            input.toString().toLowerCase(Locale.ROOT));
                }

                restaurantsItemViews.add(new MainItemViewState(
                        placeAutocomplete.getPlaceId(),
                        placeAutocomplete.getAddress(),
                        textToHighlight
                ));
            }
        }

        itemViewStates.setValue(restaurantsItemViews);
    }

    public Spannable setHighLightedText(String restaurantName, String textToHighlight) {
        int start = restaurantName.indexOf(textToHighlight);
        Spannable wordToSpan = new SpannableString(restaurantName);

        for (int cpt = 0; cpt < restaurantName.length() && start != -1; cpt = start + 1) {
            start = restaurantName.indexOf(textToHighlight, cpt);
            if (start == -1) {
                break;
            } else {
                wordToSpan.setSpan(new BackgroundColorSpan(Color.LTGRAY), start,
                        start + textToHighlight.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return wordToSpan;
    }

    @SuppressLint("MissingPermission")
    public void onStart() {
        if (permissionChecker.hasFineLocationPermission() || permissionChecker.hasCoarseLocationPermission()) {
            locationRepository.startLocationUpdates();
            isPermissionEnabledLiveData.setValue(true);
        } else {
            locationRepository.stopLocationUpdates();
            isPermissionEnabledLiveData.setValue(false);
        }
    }

    public void onResume() {
        if (permissionChecker.hasFineLocationPermission() || permissionChecker.hasCoarseLocationPermission()) {
            isPermissionEnabledLiveData.setValue(true);
        } else {
            isPermissionEnabledLiveData.setValue(false);
        }
    }

    public void onStop() {
        locationRepository.stopLocationUpdates();
        isPermissionEnabledLiveData.setValue(false);
    }

    public LiveData<MainViewState> getMainViewStateLiveData() {
        mainViewStateLiveData.setValue(new MainViewState(
                userRepository.getCustomFirebaseUser().getDisplayName(),
                userRepository.getCustomFirebaseUser().getEmail(),
                userRepository.getCustomFirebaseUser().getPhotoUrl()
        ));

        return mainViewStateLiveData;
    }

    public LiveData<List<MainItemViewState>> getMainItemListViewState() {
        return itemViewStates;
    }

    public LiveData<Boolean> getPermissionsLiveData() {
        return isPermissionEnabledLiveData;
    }

    public void onSearchChanged(CharSequence input) {
        searchInputLiveData.setValue(input);
    }

    public void onSearchDone(CharSequence query) {
        currentUserSearchRepository.setCurrentUserSearch(query);
    }
}