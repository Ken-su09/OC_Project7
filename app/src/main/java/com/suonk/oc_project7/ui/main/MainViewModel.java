package com.suonk.oc_project7.ui.main;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.suonk.oc_project7.model.data.permission_checker.PermissionChecker;
import com.suonk.oc_project7.model.data.place_auto_complete.CustomSpannable;
import com.suonk.oc_project7.model.data.place_auto_complete.PlaceAutocomplete;
import com.suonk.oc_project7.repositories.current_location.CurrentLocationRepository;
import com.suonk.oc_project7.repositories.current_user_search.CurrentUserSearchRepository;
import com.suonk.oc_project7.repositories.places.PlacesRepository;
import com.suonk.oc_project7.repositories.user.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MainViewModel extends ViewModel {

    @NonNull
    private final CurrentLocationRepository locationRepository;

    @NonNull
    private final UserRepository userRepository;

    @NonNull
    private final CurrentUserSearchRepository currentUserSearchRepository;

    private final MutableLiveData<Boolean> isPermissionEnabledLiveData = new MutableLiveData<>();

    private final MutableLiveData<MainViewState> mainViewStateLiveData = new MutableLiveData<>();

    private final MediatorLiveData<List<MainItemViewState>> itemViewStates = new MediatorLiveData<>();

    private final MutableLiveData<CharSequence> searchInputLiveData = new MutableLiveData<>();

    private final PermissionChecker permissionChecker;

    @Inject
    public MainViewModel(@NonNull CurrentLocationRepository locationRepository,
                         @NonNull UserRepository userRepository,
                         @NonNull PlacesRepository placesRepository,
                         @NonNull CurrentUserSearchRepository currentUserSearchRepository,
                         @NonNull PermissionChecker permissionChecker) {
        this.locationRepository = locationRepository;
        this.userRepository = userRepository;
        this.currentUserSearchRepository = currentUserSearchRepository;
        this.permissionChecker = permissionChecker;

        LiveData<List<PlaceAutocomplete>> placesAutocompleteLiveData = Transformations.switchMap(searchInputLiveData, input -> {
            if (input != null) {
                return placesRepository.getPlacesAutocomplete(Locale.getDefault().getLanguage(), input.toString());
            } else {
                return placesRepository.getPlacesAutocomplete(Locale.getDefault().getLanguage(), "");
            }
        });

        itemViewStates.addSource(placesAutocompleteLiveData, placesAutocomplete ->
                combine(placesAutocomplete, searchInputLiveData.getValue()));

        itemViewStates.addSource(searchInputLiveData, input ->
                combine(placesAutocompleteLiveData.getValue(), input));
    }

    private void combine(@Nullable List<PlaceAutocomplete> placesAutocomplete, @Nullable CharSequence input) {
        List<MainItemViewState> mainItemViewStates = new ArrayList<>();

        if (placesAutocomplete != null) {
            for (PlaceAutocomplete placeAutocomplete : placesAutocomplete) {
                if (input != null) {
                    if (placeAutocomplete.getRestaurantName().contains(input)) {
                        CustomSpannable textToHighlight = setHighLightedText(
                                placeAutocomplete.getRestaurantName(),
                                input.toString());

                        mainItemViewStates.add(new MainItemViewState(
                                placeAutocomplete.getPlaceId(),
                                placeAutocomplete.getRestaurantName(),
                                placeAutocomplete.getAddress(),
                                textToHighlight.getStart(),
                                textToHighlight.getEnd()
                        ));
                    }
                }
            }
        }

        itemViewStates.setValue(mainItemViewStates);
    }

    public CustomSpannable setHighLightedText(String restaurantName, String textToHighlight) {
        // restaurantName : PIZZA N PASTA
        // textToHighlight : PIZ

        int start = 0;
        int end = 0;

        // 0
        int indexOf = restaurantName.toLowerCase(Locale.ROOT).indexOf(textToHighlight.toLowerCase(Locale.ROOT));

        // 2

        for (int cpt = 0; cpt < restaurantName.length() && indexOf != -1; cpt = indexOf + 1) {
            indexOf = restaurantName.indexOf(textToHighlight, cpt);

            if (indexOf == -1) {
                break;
            } else {
                start = indexOf;
                end = start + textToHighlight.length();
            }
        }

        return new CustomSpannable(start, end);
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