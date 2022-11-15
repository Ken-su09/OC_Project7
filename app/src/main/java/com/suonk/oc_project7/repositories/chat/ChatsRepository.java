package com.suonk.oc_project7.repositories.chat;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.suonk.oc_project7.model.data.chat.Chat;
import com.suonk.oc_project7.model.data.chat.Room;

import java.util.List;

public interface ChatsRepository {

    LiveData<String> getRoomIdByWorkmateIds(@NonNull List<String> ids);

    LiveData<List<Chat>> getChatListByRoomId(@NonNull String id);

    void addNewRoomToFirestore(@NonNull Room room);

    void addNewChatToRoom(@NonNull String roomId,
                          @NonNull String senderId,
                          @NonNull String message);
}