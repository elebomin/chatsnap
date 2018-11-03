package com.example.chatsnap.util;

import android.text.format.DateUtils;

public class TimeUtils {
    private final static long HOURS = 60 * 60 * 60;

    public static String getReadableMessageDate(long timestamp) {
        return DateUtils.getRelativeTimeSpanString(
                timestamp,
                System.currentTimeMillis(),
                DateUtils.SECOND_IN_MILLIS).toString();
    }

}
