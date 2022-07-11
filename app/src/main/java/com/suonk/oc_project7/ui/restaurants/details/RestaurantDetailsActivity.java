package com.suonk.oc_project7.ui.restaurants.details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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
        viewModel.setRestaurantDetailsLiveData(getIntent().getStringExtra(PLACE_ID));
        viewModel.getRestaurantDetailsViewStateLiveData().observe(this, restaurantItemViewState -> {
            binding.restaurantName.setText(restaurantItemViewState.getRestaurantName());
            binding.restaurantAddress.setText(restaurantItemViewState.getAddress());

            restaurantId.set(restaurantItemViewState.getPlaceId());

            binding.restaurantImage.setImageURI(Uri.parse(restaurantItemViewState.getPictureUrl()));
//            Glide.with(this)
//                    .load(restaurantItemViewState.getPictureUrl())
//                    .centerCrop()
//                    .into(binding.restaurantImage);

            binding.callIcon.setOnClickListener(view -> {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", restaurantItemViewState.getPhoneNumber() , null));
                startActivity(intent);
            });

            binding.websiteIcon.setOnClickListener(view -> {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(restaurantItemViewState.getWebsiteLink()));
                startActivity(browserIntent);
            });
        });

        viewModel.getHaveChosenSingleLiveEvent().observe(this, binding.chosenButton::setImageResource);

        binding.chosenButton.setOnClickListener(view -> {
            viewModel.addWorkmate(FirebaseAuth.getInstance().getCurrentUser(), restaurantId.get());

            Toast.makeText(this, "You have chosen this restaurant !", Toast.LENGTH_LONG).show();
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