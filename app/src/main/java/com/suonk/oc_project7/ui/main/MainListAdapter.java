package com.suonk.oc_project7.ui.main;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.suonk.oc_project7.databinding.ItemRestaurantBinding;
import com.suonk.oc_project7.events.OnClickEventListener;

public class MainListAdapter extends ListAdapter<MainItemViewState, MainListAdapter.ViewHolder> {

    private final OnClickEventListener callback;

    public MainListAdapter(OnClickEventListener callback) {
        super(new MainListAdapter.ItemCallBack());
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRestaurantBinding binding = ItemRestaurantBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(getItem(position), callback);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemRestaurantBinding binding;

        public ViewHolder(@NonNull ItemRestaurantBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void onBind(MainItemViewState restaurant, OnClickEventListener callback) {
            Spannable wordToSpan = new SpannableString(restaurant.getRestaurantName());
            wordToSpan.setSpan(new BackgroundColorSpan(Color.LTGRAY), restaurant.getStart(),
                    restaurant.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            binding.name.setText(wordToSpan, TextView.BufferType.SPANNABLE);
            binding.address.setText(restaurant.getAddress());

            Glide.with(binding.image)
                    .load(restaurant.getPictureUrl())
                    .centerCrop()
                    .into(binding.image);

            binding.getRoot().setOnClickListener(view -> {
                binding.getRoot().setEnabled(false);
                callback.onRestaurantClick(view, restaurant.getPlaceId());
            });
        }
    }

    static class ItemCallBack extends DiffUtil.ItemCallback<MainItemViewState> {

        @Override
        public boolean areItemsTheSame(@NonNull MainItemViewState oldItem, @NonNull MainItemViewState newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull MainItemViewState oldItem, @NonNull MainItemViewState newItem) {
            return oldItem.getPlaceId().equals(newItem.getPlaceId()) &&
                    oldItem.getAddress().equals(newItem.getAddress()) &&
                    oldItem.getStart() == newItem.getStart() &&
                    oldItem.getEnd() == newItem.getEnd();
        }
    }
}