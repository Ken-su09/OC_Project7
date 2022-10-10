package com.suonk.oc_project7.ui.chat.list;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.suonk.oc_project7.databinding.ActivityListChatsBinding;
import com.suonk.oc_project7.events.OnClickEventListener;
import com.suonk.oc_project7.ui.chat.details.ChatDetailsActivity;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ChatsListActivity extends AppCompatActivity implements OnClickEventListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ActivityListChatsBinding binding = ActivityListChatsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ChatsListViewModel viewModel = new ViewModelProvider(this).get(ChatsListViewModel.class);
        getAllRoomsFromViewModel(binding, viewModel);
//        setupActionBar(binding);
    }

    private void getAllRoomsFromViewModel(ActivityListChatsBinding binding, ChatsListViewModel viewModel) {
        RoomsListAdapter listAdapter = new RoomsListAdapter(this);

        viewModel.getChatsListViewState().observe(this, listAdapter::submitList);
        binding.rooms.setAdapter(listAdapter);
        binding.rooms.setHasFixedSize(true);
        binding.rooms.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onRestaurantClick(View view, String id) {

    }

    @Override
    public void onWorkmateClick(View view, String id) {
        startActivity(ChatDetailsActivity.navigate(this, id));
    }
}