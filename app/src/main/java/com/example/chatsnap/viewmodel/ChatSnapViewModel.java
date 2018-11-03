package com.example.chatsnap.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.chatsnap.persistence.ChatSnapRepository;
import com.example.chatsnap.persistence.entity.Account;
import com.example.chatsnap.persistence.entity.Message;

import java.util.List;

/**
 * ViewModel to manage accounts and messages in the activity and the fragments
 */
public class ChatSnapViewModel extends AndroidViewModel {

    private ChatSnapRepository mRepository;

    private LiveData<List<Message>> mMessages;
    private LiveData<Account> mActiveAccount;

    public ChatSnapViewModel(@NonNull Application application) {
        super(application);
        mRepository = new ChatSnapRepository(application);
        mActiveAccount = mRepository.getActiveAccount();
        mMessages = mRepository.getMessages();
    }

    public LiveData<List<Message>> getMessages() {
        return mMessages;
    }

    public void addMessage(Message message) {
        mRepository.insertMessage(message);
    }

    public void deleteMessage(long messageId) {
        mRepository.deleteMessage(messageId);
    }

    public LiveData<Account> getAccount(String login) {
        return mRepository.getAccount(login);
    }

    public LiveData<Account> getActiveAccount() {
        return mActiveAccount;
    }

    public void activateAccount(Account account) {
        mRepository.activateAccount(account);
    }

    public void logout() {
        mRepository.deactivateAccounts();
    }
}
