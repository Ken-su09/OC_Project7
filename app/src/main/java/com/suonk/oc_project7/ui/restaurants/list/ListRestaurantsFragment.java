package com.suonk.oc_project7.ui.restaurants.list;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.suonk.oc_project7.databinding.FragmentListRestaurantsBinding;
import com.suonk.oc_project7.events.OnRestaurantEventListener;
import com.suonk.oc_project7.ui.main.MainActivity;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ListRestaurantsFragment extends Fragment implements OnRestaurantEventListener {

    private OnRestaurantEventListener listener;
    private MainActivity activity;

    public static ListRestaurantsFragment newInstance() {
        Bundle args = new Bundle();

        ListRestaurantsFragment fragment = new ListRestaurantsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (OnRestaurantEventListener) context;
        activity = (MainActivity) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentListRestaurantsBinding binding = FragmentListRestaurantsBinding.inflate(inflater, container, false);

        RestaurantsViewModel viewModel = new ViewModelProvider(this).get(RestaurantsViewModel.class);
        getRestaurantsListFromViewModel(viewModel, binding);

        return binding.getRoot();
    }

    private void getRestaurantsListFromViewModel(@NonNull RestaurantsViewModel viewModel, @NonNull FragmentListRestaurantsBinding binding) {
        RestaurantsListAdapter listAdapter = new RestaurantsListAdapter(this, activity);

        viewModel.getRestaurantsLiveData().observe(getViewLifecycleOwner(), listAdapter::submitList);

        binding.restaurantsList.setAdapter(listAdapter);
        binding.restaurantsList.setLayoutManager(new LinearLayoutManager(getContext()));
//        binding.restaurantsList.setHasFixedSize(true);
    }

    @Override
    public void onRestaurantClick(View view, String id) {
        listener.onRestaurantClick(view, id);
    }
}