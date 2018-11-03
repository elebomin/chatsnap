package com.example.chatsnap.persistence;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.chatsnap.BuildConfig;
import com.example.chatsnap.persistence.dao.ChatSnapDao;
import com.example.chatsnap.persistence.entity.Account;
import com.example.chatsnap.persistence.entity.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * provides workers to access DB asynchronously
 */
public class ChatSnapRepository {

    /** Default tag used for logging */
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatSnapRepository.class);

    private ChatSnapDao mDao;
    private LiveData<List<Message>> mMessages;
    private LiveData<Account> mActiveAccount;

    public ChatSnapRepository(Application application) {
        if (BuildConfig.DEBUG) { LOGGER.debug("ChatSnapRepository new instance "); }
        mDao = ChatSnapDataBase.getDatabase(application, false).getChatSnapDao();
        mMessages = mDao.getMessages();
        mActiveAccount = mDao.getActiveAccount();
    }

    public LiveData<List<Message>> getMessages() {
        return mMessages;
    }

    public void deleteMessage(long messageId) {
        new DeleteMessageAsyncTask(mDao).execute(messageId);
    }

    public void insertMessage(Message message) {
        new InsertMessageAsyncTask(mDao).execute(message);
    }

    public void insertMessages(List<Message> messages) {
        new InsertMessagesAsyncTask(mDao).execute(messages);
    }

    public LiveData<Account> getAccount(String login) {
        return mDao.getAccount(login);
    }

    public LiveData<Account> getActiveAccount() {
        return mActiveAccount;
    }

    public void activateAccount(Account account) {
        new InsertAndActivateAccountAsyncTask(mDao).execute(account);
    }

    public void deactivateAccounts() {
        new DeactivateAccountsAsyncTask(mDao).execute();
    }

    /** AsynTasks */
    private static class InsertMessageAsyncTask extends AsyncTask<Message, Void, Void> {

        private ChatSnapDao mDao;

        public InsertMessageAsyncTask(ChatSnapDao dao) {
            mDao = dao;
        }

        @Override
        protected Void doInBackground(final Message... params) {
            mDao.insertMessage(params[0]);
            return null;
        }
    }

    private static class InsertMessagesAsyncTask extends AsyncTask<List<Message>, Void, Void> {

        private ChatSnapDao mDao;

        public InsertMessagesAsyncTask(ChatSnapDao dao) {
            mDao = dao;
        }

        @Override
        protected Void doInBackground(final List<Message>... params) {
            mDao.insertMessages(params[0]);
            return null;
        }
    }

    private static class DeleteMessageAsyncTask extends AsyncTask<Long, Void, Void> {

        private ChatSnapDao mDao;

        public DeleteMessageAsyncTask(ChatSnapDao dao) {
            mDao = dao;
        }

        @Override
        protected Void doInBackground(final Long... params) {
            mDao.deleteMessage(params[0]);
            return null;
        }
    }


    private static class InsertAndActivateAccountAsyncTask extends AsyncTask<Account, Void, Void> {

        private ChatSnapDao mDao;

        public InsertAndActivateAccountAsyncTask(ChatSnapDao dao) {
            mDao = dao;
        }

        @Override
        protected Void doInBackground(final Account... params) {
            Account account = params[0];
            account.setActive(true);
            mDao.deactivateAllAccounts();
            mDao.insertAccount(account);
            return null;
        }
    }

    private static class DeactivateAccountsAsyncTask extends AsyncTask<Void, Void, Void> {

        private ChatSnapDao mDao;

        public DeactivateAccountsAsyncTask(ChatSnapDao dao) {
            mDao = dao;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mDao.deactivateAllAccounts();
            return null;
        }
    }

}
