package com.suonk.oc_project7.model.data.chat;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Objects;

public class Room {

    @NonNull
    private String id;

    @NonNull
    private List<String> workmateIds;

    @NonNull
    private List<String> workmateNames;

    @NonNull
    private List<String> workmatePictureURLS;

    @NonNull
    private String lastMessage;

    @NonNull
    private Long timestamp;

    public Room(@NonNull String id,
                @NonNull List<String> workmateIds,
                @NonNull List<String> workmateNames,
                @NonNull List<String> workmatePictureURLS,
                @NonNull String lastMessage,
                @NonNull Long timestamp) {
        this.id = id;
        this.workmateIds = workmateIds;
        this.workmateNames = workmateNames;
        this.workmatePictureURLS = workmatePictureURLS;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
    }

    public Room() {
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public List<String> getWorkmateIds() {
        return workmateIds;
    }

    @NonNull
    public List<String> getWorkmateNames() {
        return workmateNames;
    }

    @NonNull
    public List<String> getWorkmatePictureURLS() {
        return workmatePictureURLS;
    }

    @NonNull
    public String getLastMessage() {
        return lastMessage;
    }

    @NonNull
    public Long getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return id.equals(room.id) && workmateIds.equals(room.workmateIds) && workmateNames.equals(room.workmateNames) && workmatePictureURLS.equals(room.workmatePictureURLS) && lastMessage.equals(room.lastMessage) && timestamp.equals(room.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, workmateIds, workmateNames, workmatePictureURLS, lastMessage, timestamp);
    }

    @NonNull
    @Override
    public String toString() {
        return "Room{" +
                "id='" + id + '\'' +
                ", workmateIds=" + workmateIds +
                ", workmateNames=" + workmateNames +
                ", workmatePictureURLS=" + workmatePictureURLS +
                ", lastMessage='" + lastMessage + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}