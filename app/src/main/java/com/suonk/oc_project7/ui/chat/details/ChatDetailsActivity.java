package com.suonk.oc_project7.ui.chat.details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.suonk.oc_project7.R;
import com.suonk.oc_project7.databinding.ActivityChatDetailsBinding;
import com.suonk.oc_project7.ui.main.MainActivity;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ChatDetailsActivity extends AppCompatActivity {

    public static final String WORKMATE_ID = "WORKMATE_ID";

    public static Intent navigate(Context context, String id) {
        return new Intent(context, ChatDetailsActivity.class).putExtra(WORKMATE_ID, id);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityChatDetailsBinding binding = ActivityChatDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ChatDetailsViewModel viewModel = new ViewModelProvider(this).get(ChatDetailsViewModel.class);

        viewModel.getIsThereError().observe(this, isThereError -> {
            if (isThereError) {
                Toast.makeText(this, R.string.chat_details_error_message, Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                getAllMessagesFromViewModel(binding, viewModel);
                sendMessage(binding, viewModel);

                setupActionBar(binding);
            }
        });
    }

    private void getAllMessagesFromViewModel(
            @NonNull ActivityChatDetailsBinding binding,
            @NonNull ChatDetailsViewModel viewModel) {
        ChatDetailsListAdapter listAdapter = new ChatDetailsListAdapter();

        viewModel.getChatDetails().observe(this, listAdapter::submitList);
        binding.messages.setAdapter(listAdapter);
        binding.messages.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        binding.messages.setLayoutManager(linearLayoutManager);
    }

    private void sendMessage(
            @NonNull ActivityChatDetailsBinding binding,
            @NonNull ChatDetailsViewModel viewModel) {
        viewModel.getIsMessageEmpty().observe(this, isMessageEmpty -> {
            if (isMessageEmpty) {
                Toast.makeText(this, R.string.toast_empty_message, Toast.LENGTH_LONG).show();
            }
        });

        binding.sendMessage.setOnClickListener(view -> {
            if (binding.messageEditText.getText() != null) {
                viewModel.addMessage(binding.messageEditText.getText().toString());
                binding.messageEditText.getText().clear();
            }
        });
    }

    private void setupActionBar(@NonNull ActivityChatDetailsBinding binding) {
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.orange));
        binding.toolbar.setTitleTextColor(AppCompatResources.getColorStateList(this, R.color.white));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.main_toolbar_title));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}