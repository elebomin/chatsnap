package com.example.chatsnap.ui.fragment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.chatsnap.BuildConfig;
import com.example.chatsnap.MainActivity;
import com.example.chatsnap.R;
import com.example.chatsnap.mock.MockLatencyHelper;
import com.example.chatsnap.persistence.entity.Message;
import com.example.chatsnap.ui.adapter.MessageAdapter;
import com.example.chatsnap.ui.callback.MessageCallback;
import com.example.chatsnap.viewmodel.ChatSnapViewModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MessagesFragment extends Fragment {
    /** Default logger used for logging */
    private static final Logger LOGGER = LoggerFactory.getLogger(MessagesFragment.class);

    public static final String TAG = "MessagesFragment";

    private RecyclerView mRecyclerView;
    private MessageAdapter mAdapter;
    private ProgressBar mProgress;

    private LiveData<List<Message>> mMessages;

    private MessageCallback mMessageCallback;

    @Override
    public void onAttach(Context context) {
        if (BuildConfig.DEBUG) { LOGGER.info("onAttach() "); }
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mMessageCallback = (MessageCallback) context;
        } else {
            if (BuildConfig.DEBUG) {
                throw new RuntimeException("onAttach() context is not a " + MessageCallback.class.getSimpleName());
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.messages_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.messages_recyclerview);
        mAdapter = new MessageAdapter(mMessageCallback);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);

        mProgress = view.findViewById(R.id.messages_progress);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgress(true);
                mMessageCallback.onNewMessage();
            }
        });

        showProgress(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ChatSnapViewModel mViewModel = ViewModelProviders.of(this).get(ChatSnapViewModel.class);

        mMessages = mViewModel.getMessages();
        mMessages.observe(this, new Observer<List<Message>>() {
            @Override
            public void onChanged(@Nullable List<Message> messages) {
                MockLatencyHelper.simulateNetworkLatency();
                showProgress(false);
                mAdapter.setMessages(messages);
            }
        });
    }

    private void showProgress(boolean show) {
        mProgress.setVisibility(show ? VISIBLE : GONE);
    }
}
