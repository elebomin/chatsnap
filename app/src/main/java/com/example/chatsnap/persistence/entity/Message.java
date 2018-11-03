package com.example.chatsnap.persistence.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "messages")
public class Message {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "sender")
    @NonNull
    private String sender;

    @ColumnInfo(name = "timestamp")
    @NonNull
    private long timeStanp;

    @ColumnInfo(name = "text")
    private String text;

    @ColumnInfo(name = "image_uri")
    private String imgeUri;

    public Message(/*long id,*/ @NonNull String sender, long timeStanp, String text, String imgeUri) {
//        this.id = id;
        this.sender = sender;
        this.timeStanp = timeStanp;
        this.text = text;
        this.imgeUri = imgeUri;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public long getTimeStanp() {
        return timeStanp;
    }

    public void setTimeStanp(long timeStanp) {
        this.timeStanp = timeStanp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImgeUri() {
        return imgeUri;
    }

    public void setImgeUri(String imgeUri) {
        this.imgeUri = imgeUri;
    }
}
