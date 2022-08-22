package com.suonk.oc_project7.ui.restaurants.details;

import static com.suonk.oc_project7.ui.restaurants.details.RestaurantDetailsActivity.PLACE_ID;

import android.content.Context;
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
import com.suonk.oc_project7.model.data.restaurant.Restaurant;
import com.suonk.oc_project7.model.data.restaurant.RestaurantDetails;
import com.suonk.oc_project7.model.data.workmate.Workmate;
import com.suonk.oc_project7.repositories.restaurants.RestaurantsRepository;
import com.suonk.oc_project7.repositories.workmates.WorkmatesRepository;
import com.suonk.oc_project7.ui.workmates.WorkmateItemViewState;
import com.suonk.oc_project7.utils.SingleLiveEvent;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;

@HiltViewModel
public class RestaurantDetailsViewModel extends ViewModel {

    @NonNull
    private final WorkmatesRepository workmatesRepository;

    @NonNull
    private final RestaurantsRepository restaurantsRepository;

    @NonNull
    private final MediatorLiveData<RestaurantDetailsViewState> restaurantDetailsViewStateLiveData = new MediatorLiveData<>();

    @NonNull
    private final MutableLiveData<List<WorkmateItemViewState>> workmatesViewStateLiveData = new MutableLiveData<>(new ArrayList<>());

    @NonNull
    private final FirebaseUser firebaseUser;

    private final Context context;

    private final String placeId;

    private String restaurantName;

    @Inject
    public RestaurantDetailsViewModel(@NonNull WorkmatesRepository workmatesRepository,
                                      @NonNull RestaurantsRepository restaurantsRepository,
                                      @NonNull FirebaseAuth firebaseAuth,
                                      @ApplicationContext Context context,
                                      SavedStateHandle savedStateHandle) {
        this.restaurantsRepository = restaurantsRepository;
        this.workmatesRepository = workmatesRepository;
        this.context = context;
        firebaseUser = firebaseAuth.getCurrentUser();
        placeId = savedStateHandle.get(PLACE_ID);

        LiveData<RestaurantDetails> restaurantDetailsLiveData = restaurantsRepository.getRestaurantDetailsById(placeId);
        LiveData<List<Workmate>> workmatesLiveData = workmatesRepository.getWorkmatesHaveChosenTodayLiveData();
        LiveData<List<Restaurant>> likedRestaurantsLiveData = restaurantsRepository.getLikedRestaurants();

        restaurantDetailsViewStateLiveData.addSource(likedRestaurantsLiveData, likedRestaurants -> {
            combine(workmatesLiveData.getValue(), restaurantDetailsLiveData.getValue(), likedRestaurants);
        });

        restaurantDetailsViewStateLiveData.addSource(workmatesLiveData, workmates -> {
            combine(workmates, restaurantDetailsLiveData.getValue(), likedRestaurantsLiveData.getValue());
        });

        restaurantDetailsViewStateLiveData.addSource(restaurantDetailsLiveData, restaurantDetails -> {
            combine(workmatesLiveData.getValue(), restaurantDetails, likedRestaurantsLiveData.getValue());
        });
    }

    private void combine(@Nullable List<Workmate> workmates, @Nullable RestaurantDetails restaurantDetails,
                         @Nullable List<Restaurant> likedRestaurants) {
        List<WorkmateItemViewState> workmatesItemViews = new ArrayList<>();
        int selectRestaurantButtonIcon = R.drawable.ic_to_select;

        if (workmates != null && placeId != null) {
            for (Workmate workmate : workmates) {
                if (!firebaseUser.getUid().equals(workmate.getId()) && placeId.equals(workmate.getRestaurantId())) {
                    workmatesItemViews.add(new WorkmateItemViewState(
                            workmate.getId(),
                            context.getString(R.string.workmate_has_joined, workmate.getName()),
                            workmate.getPictureUrl(),
                            Color.BLACK,
                            Typeface.NORMAL
                    ));
                } else if (firebaseUser.getUid().equals(workmate.getId()) && placeId.equals(workmate.getRestaurantId())) {
                    selectRestaurantButtonIcon = R.drawable.ic_accept;
                }
            }
            workmatesViewStateLiveData.setValue(workmatesItemViews);
        }

        int likeButtonText = R.string.like;

        if (restaurantDetails != null && likedRestaurants != null) {
            String picture = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/No_image_available.svg/1024px-No_image_available.svg.png";

            if (restaurantDetails.getImage() != null) {
                picture = restaurantDetails.getImage();
            }

            for (Restaurant restaurant : likedRestaurants) {
                if (restaurant.getRestaurantId().equals(placeId)) {
                    likeButtonText = R.string.dislike;
                    break;
                }
            }

            double rating = restaurantDetails.getRating() / 1.66666666667;

            restaurantName = restaurantDetails.getRestaurantName();

            restaurantDetailsViewStateLiveData.setValue(new RestaurantDetailsViewState(
                    restaurantDetails.getPlaceId(),
                    restaurantDetails.getRestaurantName(),
                    restaurantDetails.getAddress(),
                    (int) rating,
                    picture,
                    restaurantDetails.getPhoneNumber(),
                    restaurantDetails.getWebsiteLink(),
                    selectRestaurantButtonIcon,
                    likeButtonText));
        } else {
            restaurantDetailsViewStateLiveData.setValue(new RestaurantDetailsViewState(
                    "1",
                    "",
                    "",
                    0,
                    "",
                    "",
                    "",
                    R.drawable.ic_to_select,
                    likeButtonText));
        }
    }

    public LiveData<List<WorkmateItemViewState>> getWorkmatesViewStateLiveData() {
        return workmatesViewStateLiveData;
    }

    public LiveData<RestaurantDetailsViewState> getRestaurantDetailsViewStateLiveData() {
        return restaurantDetailsViewStateLiveData;
    }

    public void addWorkmate() {
        if (restaurantName != null) {
            workmatesRepository.addWorkmateToHaveChosenTodayList(firebaseUser, placeId, restaurantName);
        }
    }

    public void toggleIsRestaurantLiked() {
        restaurantsRepository.toggleIsRestaurantLiked(firebaseUser, placeId);
    }
}