package com.suonk.oc_project7.ui.main;

import static com.suonk.oc_project7.utils.Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static com.suonk.oc_project7.utils.Constants.PERMISSIONS_REQUEST_ENABLE_GPS;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.suonk.oc_project7.R;
import com.suonk.oc_project7.databinding.ActivityMainBinding;
import com.suonk.oc_project7.events.OnRestaurantEventListener;
import com.suonk.oc_project7.ui.auth.AuthActivity;
import com.suonk.oc_project7.ui.maps.MapsFragment;
import com.suonk.oc_project7.ui.restaurants.details.RestaurantDetailsActivity;
import com.suonk.oc_project7.ui.restaurants.list.ListRestaurantsFragment;
import com.suonk.oc_project7.ui.workmates.WorkmatesFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity implements OnRestaurantEventListener, NavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding binding;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private final boolean locationPermissionGranted = false;

    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(this, AuthActivity.class));
            finish();
            return;
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, MapsFragment.newInstance())
                    .commit();
        }
        isMapsEnabled();
        getLocationPermission();

        setupActionBar();
        setupDrawerLayout();
        setupNavigationView();
        setupBottomNavigationView();
        setupRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainViewModel.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mainViewModel.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mainViewModel.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    if (query.length() > 2) {
                        mainViewModel.onSearchDone(query);
                    }
                    hideFragmentAndShowList(query);

                    return false;
                }

                @Override
                public boolean onQueryTextChange(String input) {
                    if (input.length() > 2) {
                        mainViewModel.onSearchChanged(input);
                    }
                    hideFragmentAndShowList(input);

                    return true;
                }
            });
        }

        return super.onCreateOptionsMenu(menu);
    }

    private void hideFragmentAndShowList(String input) {
        if (!input.equals("") || !input.isEmpty()) {
            binding.recyclerView.setVisibility(View.VISIBLE);
            binding.fragmentContainer.setVisibility(View.GONE);
        } else {
            binding.recyclerView.setVisibility(View.GONE);
            binding.fragmentContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home: {

                break;
            }
            case R.id.settings: {

                break;
            }
            case R.id.logout: {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(this, AuthActivity.class));
                    finish();
                }
                break;
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    //region =========================================== SETUP UI ===========================================

    private void setupDrawerLayout() {
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar,
                R.string.drawer_opened, R.string.drawer_closed);
        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();
    }

    private void setupNavigationView() {
        binding.navView.setNavigationItemSelectedListener(this);
        View header = binding.navView.getHeaderView(0);
        AppCompatTextView name = header.findViewById(R.id.user_name);
        AppCompatTextView email = header.findViewById(R.id.user_mail);
        AppCompatImageView image = header.findViewById(R.id.user_image);

        mainViewModel.getMainViewStateLiveData().observe(this, mainViewState -> {
            name.setText(mainViewState.getDisplayName());
            email.setText(mainViewState.getEmail());
            Glide.with(this)
                    .load(mainViewState.getPhotoUrl())
                    .centerCrop()
                    .into(image);
        });

    }

    private void setupActionBar() {
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.orange));
        binding.toolbar.setTitleTextColor(AppCompatResources.getColorStateList(this, R.color.white));
        getSupportActionBar().setTitle(getString(R.string.toolbar_title));
    }

    private void setupBottomNavigationView() {
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment fragmentToCommit;

            switch (item.getItemId()) {
                case R.id.nav_restaurant:
                    fragmentToCommit = ListRestaurantsFragment.newInstance();
                    break;
                case R.id.nav_workmates:
                    fragmentToCommit = WorkmatesFragment.newInstance();
                    break;
                default:
                    fragmentToCommit = MapsFragment.newInstance();
                    break;
            }

            getSupportFragmentManager().beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.fragment_container, fragmentToCommit)
                    .commit();

            return true;
        });
    }

    private void setupRecyclerView() {
        MainListAdapter listAdapter = new MainListAdapter(this);

        mainViewModel.getMainItemListViewState().observe(this, listAdapter::submitList);

        binding.recyclerView.setAdapter(listAdapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    //endregion

    //region ========================================= GOOGLE MAPS ==========================================

    private void alertDialogGpsIsDisabled() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.gps_disabled_msg))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.positive_button), (dialog, id) -> {
                    Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent, PERMISSIONS_REQUEST_ENABLE_GPS);
                })
                .create()
                .show();
    }

    public void isMapsEnabled() {
        final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            alertDialogGpsIsDisabled();
        }
    }

    private void getLocationPermission() {
        mainViewModel.getPermissionsLiveData().observe(this, isPermissionsEnabled -> {
            if (!isPermissionsEnabled) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        });
    }

    //endregion

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRestaurantClick(View view, String id) {
        startActivity(RestaurantDetailsActivity.navigate(this, id));
    }

    @Override
    public void onBackPressed() {
        getSupportFragmentManager().popBackStack();
    }
}