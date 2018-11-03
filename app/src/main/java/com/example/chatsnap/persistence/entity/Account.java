package com.example.chatsnap.persistence.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


@Entity(tableName = "accounts")
public class Account {
    @PrimaryKey
    @ColumnInfo(name = "login")
    @NonNull
    private String login;

    @ColumnInfo(name = "password")
    @NonNull
    private String password;

    @ColumnInfo(name = "active")
    private Boolean active;


    public Account(String login, String password, Boolean active) {
        this.login = login;
        this.password = password;
        this.active = active;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
