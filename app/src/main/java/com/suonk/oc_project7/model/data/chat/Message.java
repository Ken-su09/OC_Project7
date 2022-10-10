package com.suonk.oc_project7.model.data.chat;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Objects;

public class Message {

    @NonNull
    private final List<String> content;

    @NonNull
    private final String timestamp;

    public Message(
            @NonNull List<String> content,
            @NonNull String timestamp) {
        this.content = content;
        this.timestamp = timestamp;
    }

    @NonNull
    public List<String> getContent() {
        return content;
    }

    @NonNull
    public String getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return content.equals(message.content) && timestamp.equals(message.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, timestamp);
    }

    @NonNull
    @Override
    public String toString() {
        return "Message{" +
                "content='" + content + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}