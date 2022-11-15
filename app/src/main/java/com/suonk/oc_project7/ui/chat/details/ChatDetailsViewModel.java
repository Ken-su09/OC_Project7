package com.suonk.oc_project7.ui.chat.details;

import static com.suonk.oc_project7.ui.chat.details.ChatDetailsActivity.WORKMATE_ID;

import android.annotation.SuppressLint;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.suonk.oc_project7.R;
import com.suonk.oc_project7.domain.chat.add.AddNewChatToRoomUseCase;
import com.suonk.oc_project7.domain.chat.get.GetChatListByRoomIdUseCase;
import com.suonk.oc_project7.domain.chat.get.GetRoomIdByWorkmateIdsUseCase;
import com.suonk.oc_project7.model.data.chat.Chat;
import com.suonk.oc_project7.repositories.user.UserRepository;
import com.suonk.oc_project7.utils.SingleLiveEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ChatDetailsViewModel extends ViewModel {

    @NonNull
    private final AddNewChatToRoomUseCase addNewChatToRoomUseCase;

    @NonNull
    private final UserRepository userRepository;

    @NonNull
    private List<String> workmateIds = new ArrayList<>();

    private String roomId;

    @NonNull
    private final MediatorLiveData<List<ChatDetailsViewState>> viewStateLiveData = new MediatorLiveData<>();

    @NonNull
    private final SingleLiveEvent<Boolean> isMessageEmpty = new SingleLiveEvent<>();

    @NonNull
    private final SingleLiveEvent<Boolean> isThereError = new SingleLiveEvent<>();

    @Inject
    public ChatDetailsViewModel(
            @NonNull AddNewChatToRoomUseCase addNewChatToRoomUseCase,
            @NonNull GetRoomIdByWorkmateIdsUseCase getRoomIdByWorkmateIdsUseCase,
            @NonNull GetChatListByRoomIdUseCase getChatListByRoomIdUseCase,
            @NonNull SavedStateHandle savedStateHandle,
            @NonNull UserRepository userRepository) {
        this.addNewChatToRoomUseCase = addNewChatToRoomUseCase;
        this.userRepository = userRepository;

        if (userRepository.getCustomFirebaseUser() != null && savedStateHandle.get(WORKMATE_ID) != null) {
            isThereError.setValue(false);

            String workmateId = savedStateHandle.get(WORKMATE_ID);

            workmateIds = new ArrayList<>();
            workmateIds.add(workmateId);
            workmateIds.add(userRepository.getCustomFirebaseUser().getId());

            System.out.println("userRepository.getCustomFirebaseUser().getId()" + userRepository.getCustomFirebaseUser().getId());
            System.out.println("workmateIds" + workmateIds);

            LiveData<List<Chat>> chatsLiveData = Transformations.switchMap(getRoomIdByWorkmateIdsUseCase.getRoomIdByWorkmateIds(workmateIds), roomId -> {
                this.roomId = roomId;
                return getChatListByRoomIdUseCase.getChatListByRoomId(roomId);
            });

            viewStateLiveData.addSource(chatsLiveData, chats -> combine(chats, workmateId));
        } else {
            isThereError.setValue(true);
        }
    }

    private void combine(@Nullable List<Chat> chats, @Nullable String workmateId) {
        List<ChatDetailsViewState> chatDetailsViewStateList = new ArrayList<>();

        if (chats != null && !chats.isEmpty()) {
            Collections.sort(chats, Comparator.comparing(Chat::getTimestamp));

            for (Chat chat : chats) {
                boolean isSendByMe = true;
                int textColor = Color.WHITE;
                int backgroundColor = R.drawable.custom_message_background_sender;
                String photoUrl = Objects.requireNonNull(userRepository.getCustomFirebaseUser()).getPhotoUrl();

                if (chat.getSenderId().equals(workmateId)) {
                    isSendByMe = false;
                    textColor = Color.BLACK;
                    backgroundColor = R.drawable.custom_message_background_receiver;
                    photoUrl = "";
                }

                @SuppressLint("SimpleDateFormat")
                String timestamp = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date(chat.getTimestamp()));

                chatDetailsViewStateList.add(new ChatDetailsViewState(
                        chat.getId(),
                        textColor,
                        backgroundColor,
                        chat.getMessage(),
                        timestamp,
                        isSendByMe,
                        photoUrl
                ));
            }
        }

        viewStateLiveData.setValue(chatDetailsViewStateList);
    }

    public LiveData<List<ChatDetailsViewState>> getChatDetails() {
        return viewStateLiveData;
    }

    public void addMessage(@NonNull String message) {
        if (message.equals("")) {
            isMessageEmpty.setValue(true);
        } else {
            isMessageEmpty.setValue(false);

            if (userRepository.getCustomFirebaseUser() != null) {
                addNewChatToRoomUseCase.addNewChatToRoom(
                        roomId,
                        workmateIds,
                        userRepository.getCustomFirebaseUser().getId(),
                        message);
            }
        }
    }

    @NonNull
    public SingleLiveEvent<Boolean> getIsMessageEmpty() {
        return isMessageEmpty;
    }

    @NonNull
    public SingleLiveEvent<Boolean> getIsThereError() {
        return isThereError;
    }
}