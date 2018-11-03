package com.example.chatsnap.persistence;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.chatsnap.BuildConfig;
import com.example.chatsnap.mock.MockDataHelper;
import com.example.chatsnap.persistence.dao.ChatSnapDao;
import com.example.chatsnap.persistence.entity.Account;
import com.example.chatsnap.persistence.entity.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Database(entities = {Account.class, Message.class}, version = 1)
public abstract class ChatSnapDataBase extends RoomDatabase {

    /** Default tag used for logging */
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatSnapDataBase.class);

    public static final String DB_NAME = "chatsnap-db";

    public abstract ChatSnapDao getChatSnapDao();

    private static volatile ChatSnapDataBase INSTANCE;

    private static Context mContext;

    public static ChatSnapDataBase getDatabase(final Context context, boolean reset) {
        if (BuildConfig.DEBUG) { LOGGER.debug("getDatabase() - INSTANCE={}", INSTANCE == null ? "null" : "non null"); }
        mContext = context;

        if (reset) {
            context.deleteDatabase(DB_NAME);
        }

        if (INSTANCE == null) {
            synchronized (ChatSnapDataBase.class) {
                if (INSTANCE == null) {
                    if (BuildConfig.DEBUG) { LOGGER.debug("getDatabase - building DB"); }
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), ChatSnapDataBase.class, DB_NAME)
                            .allowMainThreadQueries()
                            .addCallback(roomDatabaseInitCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback roomDatabaseInitCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen (@NonNull SupportSQLiteDatabase db) {
            if (BuildConfig.DEBUG) { LOGGER.debug("onOpen DB v.{}", db.getVersion()); }
            super.onOpen(db);
            MockDataHelper.initDbWithMockData(INSTANCE, mContext);
        }
    };


}
