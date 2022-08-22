package com.suonk.oc_project7.ui.workmates;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.suonk.oc_project7.databinding.ItemWorkmateBinding;
import com.suonk.oc_project7.ui.main.MainActivity;

public class WorkmatesListAdapter extends ListAdapter<WorkmateItemViewState, WorkmatesListAdapter.ViewHolder> {

    public WorkmatesListAdapter() {
        super(new WorkmatesListAdapter.ItemCallBack());
    }

    @NonNull
    @Override
    public WorkmatesListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemWorkmateBinding binding = ItemWorkmateBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new WorkmatesListAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkmatesListAdapter.ViewHolder holder, int position) {
        holder.onBind(getItem(position));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemWorkmateBinding binding;

        public ViewHolder(@NonNull ItemWorkmateBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void onBind(WorkmateItemViewState workmateItemViewState) {
            binding.name.setText(workmateItemViewState.getSentence());
            binding.name.setTypeface(binding.name.getTypeface(), workmateItemViewState.getTextStyle());
            binding.name.setTextColor(workmateItemViewState.getTextColor());

            Glide.with(binding.image)
                    .load(workmateItemViewState.getPictureUrl())
                    .centerCrop()
                    .into(binding.image);
        }
    }

    static class ItemCallBack extends DiffUtil.ItemCallback<WorkmateItemViewState> {

        @Override
        public boolean areItemsTheSame(@NonNull WorkmateItemViewState oldItem, @NonNull WorkmateItemViewState newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull WorkmateItemViewState oldItem, @NonNull WorkmateItemViewState newItem) {
            return oldItem.equals(newItem);
        }
    }
}
