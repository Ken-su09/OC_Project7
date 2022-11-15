package com.suonk.oc_project7.di;

import android.content.Context;
import android.os.Looper;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.suonk.oc_project7.api.PlacesApiHolder;
import com.suonk.oc_project7.api.PlacesApiService;
import com.suonk.oc_project7.domain.chat.add.AddNewChatToRoomUseCase;
import com.suonk.oc_project7.domain.chat.get.GetChatListByRoomIdUseCase;
import com.suonk.oc_project7.domain.workmates.add.AddWorkmateToFirestoreUseCase;
import com.suonk.oc_project7.domain.workmates.add.AddWorkmateToHaveChosenTodayUseCase;
import com.suonk.oc_project7.domain.workmates.get.GetAllWorkmatesFromFirestoreUseCase;
import com.suonk.oc_project7.domain.workmates.get.GetWorkmateByIdUseCase;
import com.suonk.oc_project7.domain.workmates.get.GetWorkmatesHaveChosenTodayUseCase;
import com.suonk.oc_project7.model.data.permission_checker.PermissionChecker;
import com.suonk.oc_project7.repositories.chat.ChatsRepository;
import com.suonk.oc_project7.repositories.user.UserRepository;
import com.suonk.oc_project7.repositories.workmates.WorkmatesRepository;

import java.time.Clock;
import java.time.ZoneId;

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
    public static Clock provideClock() {
        return Clock.systemDefaultZone();
    }

    @Provides
    public static ZoneId provideSystemDefaultZoneId() {
        return ZoneId.systemDefault();
    }

    @Provides
    @Singleton
    public static AddWorkmateToFirestoreUseCase provideAddWorkmateToFirestoreUseCase(WorkmatesRepository workmatesRepository,
                                                                                     UserRepository userRepository) {
        return new AddWorkmateToFirestoreUseCase(workmatesRepository, userRepository);
    }

    @Provides
    @Singleton
    public static AddWorkmateToHaveChosenTodayUseCase provideAddWorkmateToHaveChosenTodayUseCase(WorkmatesRepository workmatesRepository) {
        return new AddWorkmateToHaveChosenTodayUseCase(workmatesRepository);
    }

    @Provides
    @Singleton
    public static GetAllWorkmatesFromFirestoreUseCase provideGetAllWorkmatesFromFirestoreUseCase(WorkmatesRepository workmatesRepository) {
        return new GetAllWorkmatesFromFirestoreUseCase(workmatesRepository);
    }

    @Provides
    @Singleton
    public static GetWorkmatesHaveChosenTodayUseCase provideGetWorkmatesHaveChosenTodayUseCase(WorkmatesRepository workmatesRepository) {
        return new GetWorkmatesHaveChosenTodayUseCase(workmatesRepository);
    }

    @Provides
    @Singleton
    public static GetWorkmateByIdUseCase provideGetWorkmateByIdUseCase(WorkmatesRepository workmatesRepository) {
        return new GetWorkmateByIdUseCase(workmatesRepository);
    }

    @Provides
    @Singleton
    public static AddNewChatToRoomUseCase provideAddNewChatToRoomUseCase(ChatsRepository chatsRepository) {
        return new AddNewChatToRoomUseCase(chatsRepository);
    }

    @Provides
    @Singleton
    public static GetChatListByRoomIdUseCase provideGetChatListByRoomIdUseCase(ChatsRepository chatsRepository) {
        return new GetChatListByRoomIdUseCase(chatsRepository);
    }
}