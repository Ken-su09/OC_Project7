package com.suonk.oc_project7.di;

import android.content.Context;
import android.os.Looper;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.suonk.oc_project7.api.PlacesApiHolder;
import com.suonk.oc_project7.api.PlacesApiService;
import com.suonk.oc_project7.domain.workmates.WorkmatesUseCases;
import com.suonk.oc_project7.domain.workmates.add.AddWorkmateToFirestoreUseCase;
import com.suonk.oc_project7.domain.workmates.add.AddWorkmateToHaveChosenTodayUseCase;
import com.suonk.oc_project7.domain.workmates.get.GetAllWorkmatesFromFirestoreUseCase;
import com.suonk.oc_project7.domain.workmates.get.GetCurrentUserUseCase;
import com.suonk.oc_project7.domain.workmates.get.GetWorkmatesHaveChosenTodayUseCase;
import com.suonk.oc_project7.model.data.permission_checker.PermissionChecker;
import com.suonk.oc_project7.repositories.user.UserRepository;
import com.suonk.oc_project7.repositories.workmates.WorkmatesRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    @Provides
    @Singleton
    public PlacesApiService providePlacesApiService() {
        return PlacesApiHolder.getInstance();
    }

    @Provides
    @Singleton
    public FusedLocationProviderClient provideFusedLocationClient(@ApplicationContext Context context) {
        return LocationServices.getFusedLocationProviderClient(context);
    }

    @Provides
    @Singleton
    public PermissionChecker providePermissionChecker(@ApplicationContext Context context) {
        return new PermissionChecker(context);
    }

    @Provides
    @Singleton
    public static FirebaseAuth provideFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    @Provides
    @Singleton
    public static FirebaseFirestore provideFirebaseFirestore() {
        return FirebaseFirestore.getInstance();
    }

    @Provides
    @Singleton
    public static Looper provideMainLooper() {
        return Looper.getMainLooper();
    }

    @Provides
    @Singleton
    public static WorkmatesUseCases provideWorkmatesUseCases(WorkmatesRepository workmatesRepository,
                                                             UserRepository userRepository) {
        return new WorkmatesUseCases(
                new AddWorkmateToFirestoreUseCase(workmatesRepository, userRepository),
                new AddWorkmateToHaveChosenTodayUseCase(workmatesRepository),
                new GetAllWorkmatesFromFirestoreUseCase(workmatesRepository),
                new GetWorkmatesHaveChosenTodayUseCase(workmatesRepository),
                new GetCurrentUserUseCase(workmatesRepository)
        );
    }
}