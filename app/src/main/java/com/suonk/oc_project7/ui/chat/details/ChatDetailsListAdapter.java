package com.suonk.oc_project7.ui.chat.details;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.suonk.oc_project7.databinding.ItemMessageBinding;

public class ChatDetailsListAdapter extends ListAdapter<ChatDetailsViewState, ChatDetailsListAdapter.ViewHolder> {

    public ChatDetailsListAdapter() {
        super(new ChatDetailsListAdapter.ItemCallBack());
    }

    @NonNull
    @Override
    public ChatDetailsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMessageBinding binding = ItemMessageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ChatDetailsListAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatDetailsListAdapter.ViewHolder holder, int position) {
        holder.onBind(getItem(position));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemMessageBinding binding;

        public ViewHolder(@NonNull ItemMessageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void onBind(ChatDetailsViewState chat) {
            binding.messageLayout.setBackgroundResource(chat.getBackgroundColor());

            binding.messageContent.setTextColor(chat.getTextColor());
            binding.messageContent.setText(chat.getContent());
            binding.messageTimestamp.setTextColor(chat.getTextColor());
            binding.messageTimestamp.setText(chat.getTimestamp());

            if (chat.getIsSendByMe()) {
                binding.userImageLayout.setVisibility(View.VISIBLE);
                binding.userImage.setVisibility(View.VISIBLE);
                binding.workmateImageLayout.setVisibility(View.INVISIBLE);
                binding.workmateImage.setVisibility(View.INVISIBLE);

                Glide.with(binding.userImage)
                        .load(chat.getPictureUrl())
                        .centerCrop()
                        .into(binding.userImage);
            } else {
                binding.userImageLayout.setVisibility(View.INVISIBLE);
                binding.userImage.setVisibility(View.INVISIBLE);
                binding.workmateImageLayout.setVisibility(View.VISIBLE);
                binding.workmateImage.setVisibility(View.VISIBLE);
            }
        }
    }

    static class ItemCallBack extends DiffUtil.ItemCallback<ChatDetailsViewState> {
        @Override
        public boolean areItemsTheSame(@NonNull ChatDetailsViewState oldItem, @NonNull ChatDetailsViewState newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull ChatDetailsViewState oldItem, @NonNull ChatDetailsViewState newItem) {
            return oldItem.getContent().equals(newItem.getContent()) &&
                    oldItem.getId().equals(newItem.getId()) &&
                    oldItem.getTimestamp().equals(newItem.getTimestamp()) &&
                    oldItem.getBackgroundColor() == newItem.getBackgroundColor() &&
                    oldItem.getTextColor() == newItem.getTextColor();
        }
    }
}
