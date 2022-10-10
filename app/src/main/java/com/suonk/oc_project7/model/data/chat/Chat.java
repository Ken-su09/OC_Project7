package com.suonk.oc_project7.model.data.chat;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Chat {

    @NonNull
    private String id;

    @NonNull
    private String senderId;

    @NonNull
    private String message;

    @NonNull
    private Long timestamp;

    public Chat(
            @NonNull String id,
            @NonNull String senderId,
            @NonNull String message,
            @NonNull Long timestamp) {
        this.id = id;
        this.senderId = senderId;
        this.message = message;
        this.timestamp = timestamp;
    }

    public Chat() {
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getSenderId() {
        return senderId;
    }

    @NonNull
    public String getMessage() {
        return message;
    }

    @NonNull
    public Long getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chat chat = (Chat) o;
        return id.equals(chat.id) && senderId.equals(chat.senderId) && message.equals(chat.message) && timestamp.equals(chat.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, senderId, message, timestamp);
    }

    @NonNull
    @Override
    public String toString() {
        return "Chat{" +
                "id='" + id + '\'' +
                ", senderId='" + senderId + '\'' +
                ", message='" + message + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}