package com.example.chatsnap.mock;

import android.content.Context;
import android.os.AsyncTask;

import com.example.chatsnap.BuildConfig;
import com.example.chatsnap.persistence.ChatSnapDataBase;
import com.example.chatsnap.persistence.entity.Account;
import com.example.chatsnap.persistence.entity.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * helper to populate Room DB with data (user and messages)
 */
public class MockDataHelper {

    /** Default tag used for logging */
    private static final Logger LOGGER = LoggerFactory.getLogger(MockDataHelper.class);

    /** mock user accounts */
    private final static Account[] accounts = new Account[] {
            new Account("user1@example.com","pwd1", false),
            new Account("user2@example.com","pwd2", false)
    };

    /** mock messages */
    private final static Message[] messages = new Message[] {
            new Message("Bob",1541055600000L, "Hey !", "file:///android_asset/image1.jpg"),
            new Message("Bob",1541059500000L, "Hey !", "file:///android_asset/image2.jpg"),
            new Message("Bob",1541077620000L, "Hey !", "file:///android_asset/image3.jpg"),
            new Message("Bob",1541088420000L, "Hey !", "file:///android_asset/image4.jpg"),
            new Message("Bob",1541088540000L, "Hey !", "file:///android_asset/image5.jpg"),
            new Message("Bob",1541095860000L, "Hey !", "file:///android_asset/image6.jpg"),
            new Message("Bob",1541096280000L, "Hey !", "file:///android_asset/image7.jpg"),
            new Message("Bob",1541143080000L, "Hey !", "file:///android_asset/image8.jpg"),
            new Message("Bob",1541157600000L, "Hey !", "file:///android_asset/image9.jpg"),
            new Message("Bob",1541157680000L, "Hey !", "file:///android_asset/image10.jpg")
    };

    /** populate DB with mock accounts and messages */
    public static void initDbWithMockData(ChatSnapDataBase db, Context context) {
        if (BuildConfig.DEBUG) { LOGGER.debug("initDbWithMockData() "); }
        new PopulateMockDataAsync(db, context).execute();
    }

    private static class PopulateMockDataAsync extends AsyncTask<Void, Void, Void> {

        private final ChatSnapDataBase mDb;
        private Context mContext;

        PopulateMockDataAsync(ChatSnapDataBase db, Context context) {
            mDb = db;
            mContext = context;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            if (BuildConfig.DEBUG) { LOGGER.debug("PopulateMockAccountsAsync - doInBackground() ... "); }
//            mContext.deleteDatabase(ChatSnapDataBase.DB_NAME);

            mDb.getChatSnapDao().deleteAllAccounts();
            mDb.getChatSnapDao().insertAccounts(Arrays.asList(accounts));

            mDb.getChatSnapDao().deleteAllMessages();
            mDb.getChatSnapDao().insertMessages(Arrays.asList(messages));

            return null;
        }
    }
}
