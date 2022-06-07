package com.suonk.oc_project7.ui.restaurants.details;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.suonk.oc_project7.R;
import com.suonk.oc_project7.databinding.FragmentListRestaurantsBinding;
import com.suonk.oc_project7.databinding.FragmentRestaurantDetailsBinding;
import com.suonk.oc_project7.ui.restaurants.list.RestaurantsViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RestaurantDetailsFragment extends Fragment {

    private RestaurantDetailsViewModel viewModel;
    private FragmentRestaurantDetailsBinding binding;
    private String placeId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            this.placeId = bundle.getString("place_id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRestaurantDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(RestaurantDetailsViewModel.class);

        getRestaurantFromViewModel();
    }

    private void getRestaurantFromViewModel() {
        viewModel.getRestaurantDetailsLiveData(placeId).observe(getViewLifecycleOwner(), restaurantItemViewState -> {
            binding.restaurantName.setText(restaurantItemViewState.getRestaurantName());
            binding.restaurantAddress.setText(restaurantItemViewState.getRestaurantName());

//            if(restaurantItemViewState.getRating().contains("4")){
//
//            }
//            binding.restaurantRating2.setVisibility();
//            binding.restaurantRating3.setVisibility();
//            binding.restaurantRating4.setVisibility();
//            binding.restaurantRating5.setVisibility();

            Glide.with(this)
                    .load(restaurantItemViewState.getPictureUrl())
                    .centerCrop()
                    .into(binding.restaurantImage);
        });

    }
}