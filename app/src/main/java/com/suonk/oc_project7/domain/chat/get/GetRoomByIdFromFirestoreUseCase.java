package com.suonk.oc_project7.domain.chat.get;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.suonk.oc_project7.model.data.chat.Room;
import com.suonk.oc_project7.repositories.chat.ChatsRepository;

import java.util.List;

import javax.inject.Singleton;

@Singleton
public class GetRoomByIdFromFirestoreUseCase {

    @NonNull
    private final ChatsRepository chatsRepository;

    public GetRoomByIdFromFirestoreUseCase(@NonNull ChatsRepository chatsRepository) {
        this.chatsRepository = chatsRepository;
    }

    @NonNull
    public LiveData<Room> getRoomByIdFromFirestore(@NonNull List<String> ids) {
        return chatsRepository.getRoomByIdFromFirestoreLiveData(ids);
    }
}