package com.example.chatsnap.ui.callback;

import android.support.annotation.NonNull;

import com.example.chatsnap.persistence.entity.Account;

public interface AccountCallback {
    void onLoginSuccessfull(@NonNull Account account);
    void onGotoRegister();
    void onGotoLogin();
}
