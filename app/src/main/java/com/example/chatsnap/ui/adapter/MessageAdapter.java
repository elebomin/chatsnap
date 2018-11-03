package com.example.chatsnap.ui.adapter;


import android.content.Context;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.chatsnap.BuildConfig;
import com.example.chatsnap.R;
import com.example.chatsnap.persistence.entity.Message;
import com.example.chatsnap.ui.callback.MessageCallback;
import com.example.chatsnap.util.TimeUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class MessageAdapter extends RecyclerView.Adapter {

    /** Default logger used for logging */
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageAdapter.class);

    private List<Message> mMessages;

    private MessageCallback mMessageCallback;

    public MessageAdapter(MessageCallback messageCallback) {
        this.mMessageCallback = messageCallback;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View messageView = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_list_item, parent, false);
        return new MessageViewHolder(messageView, parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (BuildConfig.DEBUG) { LOGGER.debug("onBindViewHolder({}) ", position); }
        ((MessageViewHolder)holder).setData(mMessages.get(position));
    }

    @Override
    public int getItemCount() {
        return mMessages != null ? mMessages.size() : 0;
    }

    public void setMessages(List<Message> messages) {
        if (BuildConfig.DEBUG) { LOGGER.info("setMessages({}) ", messages.size()); }
        this.mMessages = messages;
        notifyDataSetChanged();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Context mContext;
        private boolean countDownStarted = false;

        private Message mMessage;

        private final AppCompatTextView sender;
        private final AppCompatTextView date;
        private final AppCompatImageView image;
        private final AppCompatTextView text;
        private final ProgressBar countdownProgress;

        public MessageViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            mContext = context;
            sender = itemView.findViewById(R.id.message_sender);
            date = itemView.findViewById(R.id.message_date);
            image = itemView.findViewById(R.id.message_image);
            text = itemView.findViewById(R.id.message_text);
            countdownProgress = itemView.findViewById(R.id.countdown_progress);

            itemView.setOnClickListener(this);
        }

        void setData(Message message) {
            mMessage = message;
            sender.setText(message.getSender());
            date.setText(TimeUtils.getReadableMessageDate(message.getTimeStanp()));
            text.setText(message.getText());
            Glide.with(mContext)
                    .load(Uri.parse(message.getImgeUri()))
                    .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 5)))
                    .into(image);
        }

        @Override
        public void onClick(View view) {
            if (BuildConfig.DEBUG) { LOGGER.debug("onClick(messageId={})", mMessage.getId()); }
            if (!countDownStarted) launchCountDown();
        }

        private final static long COUNTDOWN_DURATION = 10000; // 10s
        private final static long COUNTDOWN_INTERVAL = 1000; // 1s
        private void launchCountDown() {
            if (BuildConfig.DEBUG) { LOGGER.debug("launchCountDown() "); }

            // block new countdown
            countDownStarted = true;

            Glide.with(mContext)
                    .load(Uri.parse(mMessage.getImgeUri()))
                    .into(image);

            countdownProgress.setVisibility(View.VISIBLE);

            CountDownTimer mCountDownTimer=new CountDownTimer(COUNTDOWN_DURATION,COUNTDOWN_INTERVAL) {
                @Override
                public void onTick(long millisUntilFinished) {
                    if (BuildConfig.DEBUG) { LOGGER.debug("CountDownTimer - onTick({}) ", millisUntilFinished); }
                    int newProgress = (int)((COUNTDOWN_DURATION - millisUntilFinished)*100/COUNTDOWN_DURATION)+10;
                    if (BuildConfig.DEBUG) { LOGGER.debug("CountDownTimer - new progress = {} ", newProgress); }
                    countdownProgress.setProgress(newProgress);
                }

                @Override
                public void onFinish() {
                    if (BuildConfig.DEBUG) { LOGGER.debug("CountDownTimer - onFinish() "); }
                    countdownProgress.setProgress(100);
                    mMessageCallback.onImageWatched(mMessage.getId());
                }
            };
            mCountDownTimer.start();
        }
    }
}
