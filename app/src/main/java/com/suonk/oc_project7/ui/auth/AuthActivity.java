package com.suonk.oc_project7.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.suonk.oc_project7.R;
import com.suonk.oc_project7.databinding.ActivityAuthBinding;
import com.suonk.oc_project7.ui.main.MainActivity;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AuthActivity extends AppCompatActivity {

    private final static int RC_SIGN_IN_GOOGLE = 1;

    @NonNull
    private final CallbackManager callbackManager = CallbackManager.Factory.create();

    private AuthViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("FirebaseFirstInstance", "FirebaseAuth.getInstance().getCurrentUser() 2 : " + FirebaseAuth.getInstance().getCurrentUser());

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        ActivityAuthBinding binding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.facebookButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.i("facebookLogin", "onCancel()");
            }

            @Override
            public void onError(@NonNull FacebookException e) {
                Log.i("facebookLogin", "FacebookException : " + e);
            }
        });
        binding.googleButton.setOnClickListener(view -> signIn());
    }

    private void signIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);

        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN_GOOGLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN_GOOGLE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("Nino", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.w("Nino", "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                            viewModel.addWorkmateToFirestore(FirebaseAuth.getInstance().getCurrentUser());
                            updateFirestore();
                            startActivity(new Intent(this, MainActivity.class));
                            finish();
                        }
                    } else {
                        Toast.makeText(this, "Sign in failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void updateFirestore() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Toast.makeText(this, "Hello, " + FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();
        }
    }

    private void handleFacebookToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    viewModel.addWorkmateToFirestore(FirebaseAuth.getInstance().getCurrentUser());
                    updateFirestore();
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                }
            }
        });
    }
}