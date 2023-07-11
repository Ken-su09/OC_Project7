package com.suonk.oc_project7.ui.restaurants.details;

import static com.suonk.oc_project7.ui.restaurants.details.RestaurantDetailsActivity.PLACE_ID;

import android.app.Application;
import android.graphics.Color;
import android.graphics.Typeface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.suonk.oc_project7.R;
import com.suonk.oc_project7.domain.workmates.add.AddWorkmateToHaveChosenTodayUseCase;
import com.suonk.oc_project7.domain.workmates.get.GetWorkmateByIdUseCase;
import com.suonk.oc_project7.domain.workmates.get.GetWorkmatesHaveChosenTodayUseCase;
import com.suonk.oc_project7.domain.workmates.remove.RemoveWorkmateToHaveChosenTodayUseCase;
import com.suonk.oc_project7.model.data.restaurant.RestaurantDetails;
import com.suonk.oc_project7.model.data.workmate.Workmate;
import com.suonk.oc_project7.repositories.restaurants.RestaurantsRepository;
import com.suonk.oc_project7.ui.workmates.WorkmateItemViewState;
import com.suonk.oc_project7.utils.SingleLiveEvent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class RestaurantDetailsViewModel extends ViewModel {

    @NonNull
    private final AddWorkmateToHaveChosenTodayUseCase addWorkmateToHaveChosenTodayUseCase;

    @NonNull
    private final RemoveWorkmateToHaveChosenTodayUseCase removeWorkmateToHaveChosenTodayUseCase;

    @NonNull
    private final RestaurantsRepository restaurantsRepository;

    @NonNull
    private final MediatorLiveData<RestaurantDetailsViewState> restaurantDetailsViewStateLiveData = new MediatorLiveData<>();

    @NonNull
    private final Application application;

    private final String placeId;

    private String restaurantName;
    private Workmate user;

    private final SingleLiveEvent<String> toastMessage = new SingleLiveEvent<>();
    private Boolean isChosen = false;

    @Inject
    public RestaurantDetailsViewModel(@NonNull AddWorkmateToHaveChosenTodayUseCase addWorkmateToHaveChosenTodayUseCase, @NonNull RemoveWorkmateToHaveChosenTodayUseCase removeWorkmateToHaveChosenTodayUseCase, @NonNull GetWorkmatesHaveChosenTodayUseCase getWorkmatesHaveChosenTodayUseCase, @NonNull GetWorkmateByIdUseCase getWorkmateByIdUseCase, @NonNull RestaurantsRepository restaurantsRepository, @NonNull FirebaseAuth firebaseAuth, @NonNull Application application, SavedStateHandle savedStateHandle) {
        this.restaurantsRepository = restaurantsRepository;
        this.addWorkmateToHaveChosenTodayUseCase = addWorkmateToHaveChosenTodayUseCase;
        this.removeWorkmateToHaveChosenTodayUseCase = removeWorkmateToHaveChosenTodayUseCase;
        this.application = application;
        placeId = savedStateHandle.get(PLACE_ID);

        LiveData<List<Workmate>> workmatesHaveChosenLiveData = getWorkmatesHaveChosenTodayUseCase.getWorkmatesHaveChosenTodayLiveData();

        final LiveData<RestaurantDetails> restaurantDetailsLiveData;
        if (placeId != null) {
            restaurantDetailsLiveData = restaurantsRepository.getRestaurantDetailsById(placeId);
        } else {
            restaurantDetailsLiveData = new MutableLiveData<>();
        }

        LiveData<Workmate> currentUserLiveData;

        if (firebaseAuth.getCurrentUser() != null) {
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            currentUserLiveData = getWorkmateByIdUseCase.getWorkmateByIdLiveData(firebaseUser.getUid());
        } else {
            currentUserLiveData = new MutableLiveData<>();
        }

        restaurantDetailsViewStateLiveData.addSource(workmatesHaveChosenLiveData, workmatesHaveChosen -> combine(workmatesHaveChosen, restaurantDetailsLiveData.getValue(), currentUserLiveData.getValue()));

        restaurantDetailsViewStateLiveData.addSource(restaurantDetailsLiveData, restaurantDetails -> combine(workmatesHaveChosenLiveData.getValue(), restaurantDetails, currentUserLiveData.getValue()));

        restaurantDetailsViewStateLiveData.addSource(currentUserLiveData, currentUser -> combine(workmatesHaveChosenLiveData.getValue(), restaurantDetailsLiveData.getValue(), currentUser));
    }

    private void combine(@Nullable List<Workmate> workmatesHaveChosen, @Nullable RestaurantDetails restaurantDetails, @Nullable Workmate currentUser) {
        List<WorkmateItemViewState> workmatesItemViews = new ArrayList<>();
        int selectRestaurantButtonIcon = R.drawable.ic_to_select;
        Workmate finalCurrentUser;

        if (currentUser == null) {
            finalCurrentUser = new Workmate("", "", "", "", "", "", new ArrayList<>());
        } else {
            finalCurrentUser = currentUser;
        }

        if (workmatesHaveChosen != null && placeId != null) {
            for (Workmate workmate : workmatesHaveChosen) {
                if (!finalCurrentUser.getId().equals(workmate.getId()) && placeId.equals(workmate.getRestaurantId())) {
                    workmatesItemViews.add(new WorkmateItemViewState(workmate.getId(), application.getString(R.string.workmate_has_joined, workmate.getName()), workmate.getPictureUrl(), Color.BLACK, Typeface.NORMAL));
                } else if (finalCurrentUser.getId().equals(workmate.getId()) && placeId.equals(workmate.getRestaurantId())) {
                    selectRestaurantButtonIcon = R.drawable.ic_accept;
                    isChosen = true;
                }
            }
        }

        int likeButtonText = R.string.like;

        if (restaurantDetails != null) {
            String picture = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/No_image_available.svg/1024px-No_image_available.svg.png";

            if (restaurantDetails.getImage() != null) {
                picture = restaurantDetails.getImage();
            }

            if (currentUser != null) {
                user = currentUser;
                for (String id : currentUser.getLikedRestaurants()) {
                    if (id.equals(placeId)) {
                        likeButtonText = R.string.dislike;
                        break;
                    }
                }
            }

            double rating = restaurantDetails.getRating() / 1.66666666667;

            restaurantName = restaurantDetails.getRestaurantName();

            restaurantDetailsViewStateLiveData.setValue(new RestaurantDetailsViewState(restaurantDetails.getPlaceId(),
                    restaurantDetails.getRestaurantName(),
                    restaurantDetails.getAddress(),
                    (int) rating,
                    picture, restaurantDetails.getPhoneNumber(), restaurantDetails.getWebsiteLink(), selectRestaurantButtonIcon, likeButtonText, workmatesItemViews));
        } else {
            restaurantDetailsViewStateLiveData.setValue(new RestaurantDetailsViewState("1", "", "", 0, "", "", "", R.drawable.ic_to_select, likeButtonText, workmatesItemViews));
        }
    }

    public LiveData<RestaurantDetailsViewState> getRestaurantDetailsViewStateLiveData() {
        return restaurantDetailsViewStateLiveData;
    }

    public void addWorkmate() {
        if (restaurantName != null && user != null) {
            if (!isChosen) {
                toastMessage.setValue(application.getString(R.string.restaurant_is_chosen));
                isChosen = true;
                addWorkmateToHaveChosenTodayUseCase.addWorkmateToHaveChosenTodayList(user, placeId, restaurantName);
            } else {
                toastMessage.setValue(application.getString(R.string.restaurant_is_un_chosen));
                isChosen = false;
                removeWorkmateToHaveChosenTodayUseCase.invoke(user);
            }
        }
    }

    public void toggleIsRestaurantLiked() {
        if (restaurantName != null && user != null) {
            restaurantsRepository.toggleIsRestaurantLiked(user, placeId, restaurantName);
        }
    }

    public SingleLiveEvent<String> getToastMessage() {
        return toastMessage;
    }
}