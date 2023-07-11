package com.suonk.oc_project7.ui.restaurants.details;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.suonk.oc_project7.databinding.ActivityRestaurantDetailsBinding;
import com.suonk.oc_project7.events.OnClickEventListener;
import com.suonk.oc_project7.ui.main.MainActivity;
import com.suonk.oc_project7.ui.workmates.WorkmatesListAdapter;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RestaurantDetailsActivity extends AppCompatActivity implements OnClickEventListener {

    public static final String PLACE_ID = "PLACE_ID";

    public static Intent navigate(Context context, String id) {
        return new Intent(context, RestaurantDetailsActivity.class).putExtra(PLACE_ID, id);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityRestaurantDetailsBinding binding = ActivityRestaurantDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        RestaurantDetailsViewModel viewModel = new ViewModelProvider(this).get(RestaurantDetailsViewModel.class);
        getRestaurantFromViewModel(binding, viewModel);
    }

    private void getRestaurantFromViewModel(ActivityRestaurantDetailsBinding binding, RestaurantDetailsViewModel viewModel) {
        viewModel.getRestaurantDetailsViewStateLiveData().observe(this, restaurantItemViewState -> {

            WorkmatesListAdapter listAdapter = new WorkmatesListAdapter(this);
            listAdapter.submitList(restaurantItemViewState.getWorkmatesHaveChosen());
            binding.workmatesRecyclerView.setAdapter(listAdapter);
            binding.workmatesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            binding.workmatesRecyclerView.setHasFixedSize(true);

            binding.restaurantName.setText(restaurantItemViewState.getRestaurantName());
            binding.restaurantAddress.setText(restaurantItemViewState.getAddress());

            Glide.with(this).load(restaurantItemViewState.getPictureUrl()).centerCrop().into(binding.restaurantImage);

            binding.likeTitle.setText(this.getString(restaurantItemViewState.getLikeButtonText()));

            binding.chosenButton.setImageResource(restaurantItemViewState.getSelectButtonIcon());

            setStarVisibility(restaurantItemViewState.getRating(), binding.restaurantRating1, binding.restaurantRating2, binding.restaurantRating3);

            binding.callLayout.setOnClickListener(view -> {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", restaurantItemViewState.getPhoneNumber(), null));
                if (intent.resolveActivity(this.getPackageManager()) != null) {
                    startActivity(intent);
                }
            });

            binding.websiteLayout.setOnClickListener(view -> {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(restaurantItemViewState.getWebsiteLink()));
                startActivity(browserIntent);
            });
        });

        binding.likeLayout.setOnClickListener(view -> viewModel.toggleIsRestaurantLiked());

        binding.chosenButton.setOnClickListener(view -> viewModel.addWorkmate());

        viewModel.getToastMessage().observe(this, message -> Toast.makeText(this, message, Toast.LENGTH_LONG).show());
    }

    public void setStarVisibility(int rating, @NonNull AppCompatImageView... ratingStars) {
        for (int i = 0; i < ratingStars.length; i++) {
            ratingStars[i].setVisibility(rating > i ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onRestaurantClick(View view, String id) {
    }

    @Override
    public void onWorkmateClick(View view, String id) {

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}