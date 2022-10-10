package com.suonk.oc_project7.domain.chat.get;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.suonk.oc_project7.model.data.chat.Room;
import com.suonk.oc_project7.repositories.chat.ChatsRepository;

import java.util.List;

import javax.inject.Singleton;

@Singleton
public class GetAllRoomsFromFirestoreUseCase {

    @NonNull
    private final ChatsRepository chatsRepository;

    public GetAllRoomsFromFirestoreUseCase(@NonNull ChatsRepository chatsRepository) {
        this.chatsRepository = chatsRepository;
    }

    public LiveData<List<Room>> getAllRoomsFromFirestore() {
        return chatsRepository.getAllRoomsFromFirestoreLiveData();
    }
}