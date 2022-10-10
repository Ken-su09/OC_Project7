package com.suonk.oc_project7.domain.chat.add;

import androidx.annotation.NonNull;

import com.suonk.oc_project7.model.data.chat.Room;
import com.suonk.oc_project7.repositories.chat.ChatsRepository;

import javax.inject.Singleton;

@Singleton
public class AddNewRoomToFirestoreUseCase {

    @NonNull
    private final ChatsRepository chatsRepository;

    public AddNewRoomToFirestoreUseCase(@NonNull ChatsRepository chatsRepository) {
        this.chatsRepository = chatsRepository;
    }

    public void addNewRoomToFirestore(@NonNull String id, @NonNull Room room) {
        chatsRepository.addNewRoomToFirestore(id, room);
    }
}
