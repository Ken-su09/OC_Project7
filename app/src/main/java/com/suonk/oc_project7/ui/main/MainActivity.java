package com.suonk.oc_project7.ui.main;

import static com.suonk.oc_project7.utils.Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static com.suonk.oc_project7.utils.Constants.PERMISSIONS_REQUEST_ENABLE_GPS;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.suonk.oc_project7.R;
import com.suonk.oc_project7.databinding.ActivityMainBinding;
import com.suonk.oc_project7.events.OnRestaurantEventListener;
import com.suonk.oc_project7.ui.maps.MapsFragment;
import com.suonk.oc_project7.ui.restaurants.details.RestaurantDetailsFragment;
import com.suonk.oc_project7.ui.restaurants.list.ListRestaurantsFragment;
import com.suonk.oc_project7.ui.workmates.WorkmatesFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity implements OnRestaurantEventListener {

    private ActivityMainBinding binding;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private boolean locationPermissionGranted = false;

    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        isMapsEnabled();
        getLocationPermission();

        setupActionBar();
        setupDrawerLayout();
        setupBottomNavigationView();
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
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //region =========================================== SETUP UI ===========================================

    private void setupFragmentContainer() {
        MapsFragment fragment = new MapsFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment, "");
        fragmentTransaction.commit();
    }

    private void setupDrawerLayout() {
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar,
                R.string.drawer_opened, R.string.drawer_closed);
        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();
    }

    private void setupActionBar() {
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.orange));
        binding.toolbar.setTitleTextColor(AppCompatResources.getColorStateList(this, R.color.white));
        getSupportActionBar().setTitle(getString(R.string.toolbar_title));
    }

    private void setupBottomNavigationView() {
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_restaurant:
                    ListRestaurantsFragment fragment1 = new ListRestaurantsFragment();
                    FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction2.replace(R.id.fragment_container, fragment1, "");
                    fragmentTransaction2.commit();
                    return true;
                case R.id.nav_workmates:
                    WorkmatesFragment fragment2 = new WorkmatesFragment();
                    FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction3.replace(R.id.fragment_container, fragment2, "");
                    fragmentTransaction3.commit();
                    return true;
                default:
                    MapsFragment defaultFragment = new MapsFragment();
                    FragmentTransaction defaultFragmentTransaction = getSupportFragmentManager().beginTransaction();
                    defaultFragmentTransaction.replace(R.id.fragment_container, defaultFragment, "");
                    defaultFragmentTransaction.commit();
                    return true;
            }
        });
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
            if (isPermissionsEnabled) {
                locationPermissionGranted = true;
                setupFragmentContainer();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        });
    }

    //endregion

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationPermissionGranted = false;
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
                setupFragmentContainer();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PERMISSIONS_REQUEST_ENABLE_GPS) {
            if (!locationPermissionGranted) {
                getLocationPermission();
            }
        }
    }

    @Override
    public void onRestaurantClick(View view, String id) {
//        binding.bottomNavigation.setVisibility(View.GONE);
//        binding.drawerLayout.setVisibility(View.GONE);
        RestaurantDetailsFragment restaurantDetailsFragment = new RestaurantDetailsFragment();
        FragmentTransaction defaultFragmentTransaction = getSupportFragmentManager().beginTransaction();
        defaultFragmentTransaction.replace(R.id.fragment_container, restaurantDetailsFragment, "");
        defaultFragmentTransaction.commit();
    }
}