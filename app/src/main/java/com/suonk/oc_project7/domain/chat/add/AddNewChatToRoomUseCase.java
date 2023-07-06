package com.suonk.oc_project7.domain.chat.add;

import androidx.annotation.NonNull;

import com.suonk.oc_project7.model.data.chat.Room;
import com.suonk.oc_project7.repositories.chat.ChatsRepository;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AddNewChatToRoomUseCase {

    @NonNull
    private final ChatsRepository chatsRepository;

    @Inject
    public AddNewChatToRoomUseCase(
            @NonNull ChatsRepository chatsRepository
    ) {
        this.chatsRepository = chatsRepository;
    }

    public void addNewChatToRoom(
            @NonNull String roomId,
            @NonNull List<String> workmateIds,
            @NonNull String senderId,
            @NonNull String message
    ) {
        chatsRepository.addNewRoomToFirestore(new Room(roomId, workmateIds));

        chatsRepository.addNewChatToRoom(
                roomId,
                senderId,
                message
        );
    }
}
