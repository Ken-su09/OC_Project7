package com.suonk.oc_project7.repositories.chat;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.FirebaseFirestore;
import com.suonk.oc_project7.model.data.chat.Chat;
import com.suonk.oc_project7.model.data.chat.Room;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class ChatsRepositoryImpl implements ChatsRepository {

    @NonNull
    private final FirebaseFirestore firebaseFirestore;

    @NonNull
    private final Clock clock;

    private static final String ALL_ROOMS = "all_rooms";
    private static final String ALL_CHATS = "all_chats";

    @Inject
    public ChatsRepositoryImpl(@NonNull FirebaseFirestore firebaseFirestore, @NonNull Clock clock) {
        this.firebaseFirestore = firebaseFirestore;
        this.clock = clock;
    }

    @NonNull
    @Override
    public LiveData<String> getRoomIdByWorkmateIds(@NonNull List<String> ids) {
        final MutableLiveData<String> roomIdLiveData = new MutableLiveData<>();

        firebaseFirestore.collection(ALL_ROOMS).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                try {
                    List<Room> rooms = task.getResult().toObjects(Room.class);
                    boolean isNotExisted = true;

                    for (Room room : rooms) {
                        if (room.getWorkmateIds().equals(ids)) {
                            roomIdLiveData.setValue(room.getId());
                            isNotExisted = false;
                            break;
                        } else {
                            Collections.reverse(ids);
                            if (room.getWorkmateIds().equals(ids)) {
                                roomIdLiveData.setValue(room.getId());
                                isNotExisted = false;
                                break;
                            }
                        }
                    }

                    if (rooms.isEmpty() || isNotExisted) {
                        roomIdLiveData.setValue(firebaseFirestore.collection(ALL_ROOMS).document().getId());
                    }
                } catch (Exception e) {
                    Log.e("getRoomId", "" + e);
                }
            } else {
                Exception exception = task.getException();
                if (exception != null) {
                    Log.e("getRoomId", "Exception : "+ exception);
                }
            }
        });

        return roomIdLiveData;
    }

    @NonNull
    @Override
    public LiveData<List<Chat>> getChatListByRoomId(@NonNull String id) {
        final MutableLiveData<List<Chat>> chatListLiveData = new MutableLiveData<>();

        firebaseFirestore.collection(ALL_ROOMS).document(id).collection(ALL_CHATS).addSnapshotListener((querySnapshot, error) -> {
            if (querySnapshot != null) {
                try {
                    chatListLiveData.setValue(querySnapshot.toObjects(Chat.class));
                } catch (Exception e) {
                    Log.e("getChatListByRoomId", "" + e);
                }
            }
        });

        return chatListLiveData;
    }

    @Override
    public void addNewRoomToFirestore(@NonNull Room room) {
        firebaseFirestore.collection(ALL_ROOMS).document(room.getId()).set(room);
    }

    @Override
    public void addNewChatToRoom(@NonNull String roomId, @NonNull String senderId, @NonNull String message) {
        Chat chat = new Chat(
            firebaseFirestore.collection(ALL_ROOMS).document().collection(ALL_CHATS).getId(),
            senderId,
            message,
            ZonedDateTime.now(clock).toInstant().toEpochMilli()
        );


        firebaseFirestore.collection(ALL_ROOMS).document(roomId).collection(ALL_CHATS).add(chat);
    }
}