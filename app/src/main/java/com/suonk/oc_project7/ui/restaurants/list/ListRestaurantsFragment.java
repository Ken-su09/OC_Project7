package com.suonk.oc_project7.ui.restaurants.list;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.suonk.oc_project7.databinding.FragmentListRestaurantsBinding;
import com.suonk.oc_project7.events.OnRestaurantEventListener;
import com.suonk.oc_project7.ui.main.MainActivity;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ListRestaurantsFragment extends Fragment implements OnRestaurantEventListener {

    private RestaurantsViewModel viewModel;
    private FragmentListRestaurantsBinding binding;
    private OnRestaurantEventListener listener;
    private MainActivity activity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (OnRestaurantEventListener) context;
        activity = (MainActivity) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListRestaurantsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(RestaurantsViewModel.class);

        getRestaurantsListFromViewModel();
    }

    private void getRestaurantsListFromViewModel() {
        RestaurantsListAdapter listAdapter = new RestaurantsListAdapter(this, activity);

        viewModel.getRestaurantsLiveData().observe(getViewLifecycleOwner(), listAdapter::submitList);

        binding.restaurantsList.setAdapter(listAdapter);
        binding.restaurantsList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.restaurantsList.setHasFixedSize(true);
    }

    @Override
    public void onRestaurantClick(View view, String id) {
        listener.onRestaurantClick(view, id);
    }
}