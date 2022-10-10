package com.suonk.oc_project7.ui.workmates;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.suonk.oc_project7.databinding.FragmentWorkmatesBinding;
import com.suonk.oc_project7.events.OnClickEventListener;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class WorkmatesFragment extends Fragment implements OnClickEventListener {

    private OnClickEventListener listener;
    private WorkmatesViewModel viewModel;
    private FragmentWorkmatesBinding binding;

    public static WorkmatesFragment newInstance() {
        Bundle args = new Bundle();
        WorkmatesFragment fragment = new WorkmatesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (OnClickEventListener) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWorkmatesBinding.inflate(inflater, container, false);
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

        viewModel = new ViewModelProvider(this).get(WorkmatesViewModel.class);
        getWorkmatesListFromViewModel();
    }

    private void getWorkmatesListFromViewModel() {
        WorkmatesListAdapter listAdapter = new WorkmatesListAdapter(listener);

        viewModel.getWorkmatesLiveData().observe(getViewLifecycleOwner(), listAdapter::submitList);
        binding.workmatesList.setAdapter(listAdapter);
        binding.workmatesList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.workmatesList.setHasFixedSize(true);
    }

    @Override
    public void onRestaurantClick(View view, String id) {
    }

    @Override
    public void onWorkmateClick(View view, String id) {
        listener.onWorkmateClick(view, id);
    }
}