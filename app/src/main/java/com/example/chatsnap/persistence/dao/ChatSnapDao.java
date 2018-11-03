package com.example.chatsnap.persistence.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.chatsnap.persistence.entity.Account;
import com.example.chatsnap.persistence.entity.Message;

import java.util.List;

@Dao
public interface ChatSnapDao {

    /** Accounts */
    @Query("SELECT * FROM accounts")
    LiveData<List<Account>> getAccounts();

    @Query("SELECT * FROM accounts WHERE login = :login")
    LiveData<Account> getAccount(String login);

    @Query("SELECT * FROM accounts WHERE active = 1 LIMIT 1")
    LiveData<Account> getActiveAccount();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAccount(Account accountsEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAccounts(List<Account> accountsEntities);

    @Query("UPDATE accounts SET active = 0")
    void deactivateAllAccounts();

    @Query("DELETE FROM accounts WHERE login = :login")
    void deleteAccount(String login);

    @Query("DELETE FROM accounts")
    void deleteAllAccounts();

    /** Messages */
    @Query("SELECT * FROM messages ORDER BY timestamp DESC")
    LiveData<List<Message>> getMessages();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMessage(Message message);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMessages(List<Message> messages);

    @Query("SELECT * FROM messages WHERE id = :messageId")
    Message getMessage(long messageId);

    @Query("DELETE FROM messages WHERE id = :messageId")
    void deleteMessage(long messageId);

    @Query("DELETE FROM messages")
    void deleteAllMessages();
}
