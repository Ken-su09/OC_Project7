package com.suonk.oc_project7.model.data.chat;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Objects;

public class Room {

    private String id;

    private List<String> workmateIds;

    public Room(@NonNull String id,
                @NonNull List<String> workmateIds) {
        this.id = id;
        this.workmateIds = workmateIds;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return id.equals(room.id) && workmateIds.equals(room.workmateIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, workmateIds);
    }

    @NonNull
    @Override
    public String toString() {
        return "Room{" +
                "id='" + id + '\'' +
                ", workmateIds=" + workmateIds +
                '}';
    }
}