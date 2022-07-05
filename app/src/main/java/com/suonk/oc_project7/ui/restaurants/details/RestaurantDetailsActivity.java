package com.suonk.oc_project7.ui.restaurants.details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.suonk.oc_project7.databinding.ActivityRestaurantDetailsBinding;
import com.suonk.oc_project7.ui.workmates.WorkmatesListAdapter;

import java.util.concurrent.atomic.AtomicReference;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RestaurantDetailsActivity extends AppCompatActivity {

    private static final String PLACE_ID = "PLACE_ID";

    public static Intent navigate(Context context, String id) {
        return new Intent(context, RestaurantDetailsActivity.class).putExtra("PLACE_ID", id);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityRestaurantDetailsBinding binding = ActivityRestaurantDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        RestaurantDetailsViewModel viewModel = new ViewModelProvider(this).get(RestaurantDetailsViewModel.class);
        getRestaurantFromViewModel(binding, viewModel);
        getWorkmatesWhoHaveChosenThisRestaurant(binding, viewModel);
    }

    private void getRestaurantFromViewModel(ActivityRestaurantDetailsBinding binding, RestaurantDetailsViewModel viewModel) {
        AtomicReference<String> restaurantId = new AtomicReference<>("");
        viewModel.getRestaurantDetailsLiveData(getIntent().getStringExtra(PLACE_ID)).observe(this, restaurantItemViewState -> {
            binding.restaurantName.setText(restaurantItemViewState.getRestaurantName());
            binding.restaurantAddress.setText(restaurantItemViewState.getAddress());

            restaurantId.set(restaurantItemViewState.getPlaceId());

            Glide.with(this)
                    .load(restaurantItemViewState.getPictureUrl())
                    .centerCrop()
                    .into(binding.restaurantImage);
        });

        binding.chosenButton.setOnClickListener(view -> {
            viewModel.addWorkmate(FirebaseAuth.getInstance().getCurrentUser(), restaurantId.get());
        });
    }

    private void getWorkmatesWhoHaveChosenThisRestaurant(ActivityRestaurantDetailsBinding binding, RestaurantDetailsViewModel viewModel) {
        WorkmatesListAdapter listAdapter = new WorkmatesListAdapter();

        viewModel.getWorkmatesLiveData().observe(this, listAdapter::submitList);
        binding.workmatesRecyclerView.setAdapter(listAdapter);
        binding.workmatesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.workmatesRecyclerView.setHasFixedSize(true);
    }
}