package com.suonk.oc_project7.ui.chat.list;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.suonk.oc_project7.databinding.ItemChatBinding;
import com.suonk.oc_project7.events.OnClickEventListener;

public class RoomsListAdapter extends ListAdapter<ChatsListViewState, RoomsListAdapter.ViewHolder> {

    private final OnClickEventListener callback;

    public RoomsListAdapter(OnClickEventListener callback) {
        super(new RoomsListAdapter.ItemCallBack());
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemChatBinding binding = ItemChatBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new RoomsListAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(getItem(position), callback);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemChatBinding binding;

        public ViewHolder(@NonNull ItemChatBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void onBind(ChatsListViewState chat, OnClickEventListener callback) {
            Log.i("getChatDetails", "chat.getWorkmateNames() : " + chat.getWorkmateNames());
            Log.i("getChatDetails", "chat.getLastMessage() : " + chat.getLastMessage());
            Log.i("getChatDetails", "chat.getTimestamp() : " + chat.getTimestamp());

//            chat.
            binding.workmateName.setText(chat.getWorkmateNames());
            binding.lastMessage.setText(chat.getLastMessage());
            binding.date.setText(chat.getTimestamp());

            binding.getRoot().setOnClickListener(view -> callback.onWorkmateClick(view, chat.getWorkmateId()));

            Glide.with(binding.workmateImage)
                    .load(chat.getPictureUrl())
                    .centerCrop()
                    .into(binding.workmateImage);
        }
    }

    static class ItemCallBack extends DiffUtil.ItemCallback<ChatsListViewState> {
        @Override
        public boolean areItemsTheSame(@NonNull ChatsListViewState oldItem, @NonNull ChatsListViewState newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull ChatsListViewState oldItem, @NonNull ChatsListViewState newItem) {
            return oldItem.getId().equals(newItem.getId()) &&
                    oldItem.getWorkmateNames().equals(newItem.getWorkmateNames()) &&
                    oldItem.getPictureUrl().equals(newItem.getPictureUrl()) &&
                    oldItem.getLastMessage().equals(newItem.getLastMessage()) &&
                    oldItem.getTimestamp().equals(newItem.getTimestamp());
        }
    }
}