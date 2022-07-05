package com.suonk.oc_project7.ui.restaurants.list;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.suonk.oc_project7.databinding.ItemRestaurantBinding;
import com.suonk.oc_project7.events.OnRestaurantEventListener;
import com.suonk.oc_project7.ui.main.MainActivity;

public class RestaurantsListAdapter extends ListAdapter<RestaurantItemViewState, RestaurantsListAdapter.ViewHolder> {

    private final OnRestaurantEventListener callback;
    private final MainActivity activity;

    public RestaurantsListAdapter(OnRestaurantEventListener callback, MainActivity activity) {
        super(new RestaurantsListAdapter.ItemCallBack());
        this.callback = callback;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRestaurantBinding binding = ItemRestaurantBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(getItem(position), callback, activity);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemRestaurantBinding binding;

        public ViewHolder(@NonNull ItemRestaurantBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void onBind(RestaurantItemViewState restaurant, OnRestaurantEventListener callback,
                           MainActivity activity) {
            binding.name.setText(restaurant.getRestaurantName());
            binding.address.setText(restaurant.getAddress());
            binding.openText.setText(restaurant.getOpenDescription());
            binding.distance.setText(restaurant.getDistance());
            binding.numberOfPeopleText.setText(restaurant.getNumberOfWorkmates());

            Glide.with(activity)
                    .load(restaurant.getPictureUrl())
                    .centerCrop()
                    .into(binding.image);

            binding.getRoot().setOnClickListener(view -> {
                callback.onRestaurantClick(view, restaurant.getPlaceId());
            });
        }
    }

    static class ItemCallBack extends DiffUtil.ItemCallback<RestaurantItemViewState> {

        @Override
        public boolean areItemsTheSame(@NonNull RestaurantItemViewState oldItem, @NonNull RestaurantItemViewState newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull RestaurantItemViewState oldItem, @NonNull RestaurantItemViewState newItem) {
            return oldItem.getPlaceId().equals(newItem.getPlaceId()) &&
                    oldItem.getAddress().equals(newItem.getAddress()) &&
                    oldItem.getDistance().equals(newItem.getDistance()) &&
                    oldItem.getNumberOfWorkmates().equals(newItem.getNumberOfWorkmates()) &&
                    oldItem.getRating().equals(newItem.getRating()) &&
                    oldItem.getRestaurantName().equals(newItem.getRestaurantName()) &&
                    oldItem.getOpenDescription().equals(newItem.getOpenDescription());
        }
    }
}