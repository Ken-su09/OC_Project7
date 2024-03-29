package com.suonk.oc_project7.ui.main;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
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
import com.suonk.oc_project7.databinding.NavHeaderMainBinding;
import com.suonk.oc_project7.events.OnClickEventListener;
import com.suonk.oc_project7.ui.auth.AuthActivity;
import com.suonk.oc_project7.ui.chat.details.ChatDetailsActivity;
import com.suonk.oc_project7.ui.map.MapFragment;
import com.suonk.oc_project7.ui.restaurants.details.RestaurantDetailsActivity;
import com.suonk.oc_project7.ui.restaurants.list.ListRestaurantsFragment;
import com.suonk.oc_project7.ui.settings.SettingsActivity;
import com.suonk.oc_project7.ui.workmates.WorkmatesFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity implements OnClickEventListener, NavigationView.OnNavigationItemSelectedListener {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2;

    private ActivityMainBinding binding;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private MainViewModel mainViewModel;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    public static Intent navigate(Context context) {
        return new Intent(context, MainActivity.class);
    }

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

        isMapsEnabled();
        getLocationPermission(getIntent());

        setupActionBar();
        setupDrawerLayout();
        setupNavigationView();
        setupBottomNavigationView();
        setupRecyclerView();

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        });
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
            searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    mainViewModel.onSearchDone(query);
                    showFragmentAndHideList();

                    return false;
                }

                @Override
                public boolean onQueryTextChange(String input) {
                    mainViewModel.onSearchChanged(input);
                    hideFragmentAndShowList(input);

                    return true;
                }
            });

            searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                    return true;
                }

                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
                    mainViewModel.onSearchDone("");
                    showFragmentAndHideList();
                    return true;
                }
            });
        }

        return super.onCreateOptionsMenu(menu);
    }

    private void showFragmentAndHideList() {
        binding.recyclerView.setVisibility(View.GONE);
        binding.fragmentContainer.setVisibility(View.VISIBLE);
    }

    private void hideFragmentAndShowList(@NonNull String input) {
        if (!input.equals("")) {
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
        if (item.getItemId() == R.id.settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        } else if (item.getItemId() == R.id.logout) {
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                FirebaseAuth.getInstance().getCurrentUser().delete();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, AuthActivity.class));
                finish();
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    //region =========================================== SETUP UI ===========================================

    private void setupDrawerLayout() {
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar, R.string.drawer_opened, R.string.drawer_closed);
        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();
    }

    private void setupNavigationView() {
        binding.navView.setNavigationItemSelectedListener(this);
        View header = binding.navView.getHeaderView(0);

        NavHeaderMainBinding headerBinding = NavHeaderMainBinding.bind(header);

        mainViewModel.getMainViewStateLiveData().observe(this, mainViewState -> {
            headerBinding.userName.setText(mainViewState.getDisplayName());
            headerBinding.userMail.setText(mainViewState.getEmail());
            Glide.with(this).load(mainViewState.getPhotoUrl()).centerCrop().into(headerBinding.userImage);
        });
    }

    private void setupActionBar() {
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.orange));
        binding.toolbar.setTitleTextColor(AppCompatResources.getColorStateList(this, R.color.white));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.main_toolbar_title));
        }
    }

    private void setupBottomNavigationView() {
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment fragmentToCommit;

            if (item.getItemId() == R.id.nav_restaurant) {
                showFragmentAndHideList();
                fragmentToCommit = ListRestaurantsFragment.newInstance();
            } else if (item.getItemId() == R.id.nav_workmates) {
                showFragmentAndHideList();
                fragmentToCommit = WorkmatesFragment.newInstance();
            } else {
                showFragmentAndHideList();
                fragmentToCommit = MapFragment.newInstance();
            }

            getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fragment_container, fragmentToCommit).commit();

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
        builder.setMessage(getString(R.string.gps_disabled_msg)).setCancelable(false).setPositiveButton(getString(R.string.positive_button), (dialog, id) -> {
            Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            activityResultLauncher.launch(intent);
        }).create().show();
    }

    public void isMapsEnabled() {
        final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            alertDialogGpsIsDisabled();
        }
    }

    private void getLocationPermission(Intent intent) {
        mainViewModel.getPermissionsLiveData().observe(this, isPermissionsEnabled -> {
            if (!isPermissionsEnabled) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            } else {
                if (intent.getFlags() == Intent.FLAG_ACTIVITY_CLEAR_TOP) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ListRestaurantsFragment()).commit();
                    binding.bottomNavigation.getMenu().findItem(R.id.nav_restaurant).setChecked(true);
                } else if (intent.getFlags() == Intent.FLAG_ACTIVITY_CLEAR_TASK) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new WorkmatesFragment()).commit();
                    binding.bottomNavigation.getMenu().findItem(R.id.nav_workmates).setChecked(true);
                } else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, MapFragment.newInstance()).commit();
                }
            }
        });
    }

    //endregion

    @Override
    public void onRestaurantClick(View view, String id) {
        startActivity(RestaurantDetailsActivity.navigate(this, id));
    }

    @Override
    public void onWorkmateClick(View view, String id) {
        startActivity(ChatDetailsActivity.navigate(this, id));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent.getFlags() == Intent.FLAG_ACTIVITY_CLEAR_TOP) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ListRestaurantsFragment()).commit();
            binding.bottomNavigation.getMenu().findItem(R.id.nav_restaurant).setChecked(true);
        } else if (intent.getFlags() == Intent.FLAG_ACTIVITY_CLEAR_TASK) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new WorkmatesFragment()).commit();
            binding.bottomNavigation.getMenu().findItem(R.id.nav_workmates).setChecked(true);
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, MapFragment.newInstance()).commit();
        }
    }
}