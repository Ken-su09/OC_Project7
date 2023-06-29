package com.suonk.oc_project7.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.suonk.oc_project7.R;
import com.suonk.oc_project7.databinding.ActivitySettingsBinding;
import com.suonk.oc_project7.ui.auth.AuthActivity;
import com.suonk.oc_project7.ui.main.MainActivity;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SettingsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActivitySettingsBinding binding;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private SettingsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(this, AuthActivity.class));
            finish();
            return;
        }

        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

        setupDrawerLayout();
        setupNavigationView();
        setupActionBar();
        setupNotificationsSwitch();
    }

    //region ================================================================ TOOLBAR ===============================================================

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

    //endregion

    //region ============================================================= DRAWER LAYOUT ============================================================

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.home) {
            startActivity(new Intent(this, MainActivity.class));
        } else if (item.getItemId() == R.id.logout) {
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, AuthActivity.class));
                finish();
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setupDrawerLayout() {
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar, R.string.drawer_opened, R.string.drawer_closed);
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

        viewModel.getMainViewStateLiveData().observe(this, mainViewState -> {
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
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.toolbar_title));
        }
    }

    //endregion

    //region =============================================================== SETUP UI ===============================================================

    private void setupNotificationsSwitch() {
        viewModel.getNotificationEnabled().observe(this, isChecked -> binding.notificationSwitch.setChecked(isChecked));

        binding.notificationSwitch.setOnCheckedChangeListener((compoundButton, isChecked) ->
                viewModel.setNotificationEnabled(isChecked));
    }

    //endregion
}