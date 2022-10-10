package com.suonk.oc_project7.ui.chat.list;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.suonk.oc_project7.domain.chat.get.GetAllRoomsFromFirestoreUseCase;
import com.suonk.oc_project7.domain.workmates.get.GetWorkmateByIdUseCase;
import com.suonk.oc_project7.model.data.chat.Room;
import com.suonk.oc_project7.model.data.workmate.Workmate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ChatsListViewModel extends ViewModel {

    @NonNull
    private final MediatorLiveData<List<ChatsListViewState>> viewStateLiveData = new MediatorLiveData<>();

    @Inject
    public ChatsListViewModel(
            @NonNull GetAllRoomsFromFirestoreUseCase getAllRoomsFromFirestoreUseCase,
            @NonNull GetWorkmateByIdUseCase getWorkmateByIdUseCase,
            @NonNull FirebaseAuth firebaseAuth) {

        LiveData<List<Room>> roomsLiveData = getAllRoomsFromFirestoreUseCase.getAllRoomsFromFirestore();

        LiveData<Workmate> currentUserLiveData;
        if (firebaseAuth.getCurrentUser() != null) {
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            currentUserLiveData =
                    getWorkmateByIdUseCase.getWorkmateByIdLiveData(firebaseUser.getUid());
        } else {
            currentUserLiveData = new MutableLiveData<>();
        }

        viewStateLiveData.addSource(roomsLiveData, rooms -> {
            combine(rooms, currentUserLiveData.getValue());
        });

        viewStateLiveData.addSource(currentUserLiveData, user -> {
            combine(roomsLiveData.getValue(), user);
        });
    }

    private void combine(@Nullable List<Room> rooms, @Nullable Workmate user) {
        List<ChatsListViewState> chatsListViewStateList = new ArrayList<>();

        if (rooms != null && user != null) {
            Collections.sort(rooms, Comparator.comparing(Room::getTimestamp));

            for (Room room : rooms) {
                @SuppressLint("SimpleDateFormat")
                String timestamp = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date(room.getTimestamp()));

                String workmateId = "";
                String workmatePictureURL = "";
                if (room.getWorkmateIds().indexOf(user.getId()) == 0) {
                    workmateId = room.getWorkmateIds().get(1);
                    workmatePictureURL = room.getWorkmatePictureURLS().get(1);
                } else {
                    workmateId = room.getWorkmateIds().get(0);
                    workmatePictureURL = room.getWorkmatePictureURLS().get(0);
                }
                room.getWorkmateIds().indexOf(user.getId());

                chatsListViewStateList.add(
                        new ChatsListViewState(
                                room.getId(),
                                room.getWorkmateNames().toString(),
                                workmatePictureURL,
                                room.getLastMessage(),
                                timestamp,
                                workmateId
                        ));
            }
        }

        viewStateLiveData.setValue(chatsListViewStateList);
    }

    public LiveData<List<ChatsListViewState>> getChatsListViewState() {
        return viewStateLiveData;
    }
}