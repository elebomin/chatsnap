package com.example.chatsnap.ui.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.chatsnap.BuildConfig;
import com.example.chatsnap.MainActivity;
import com.example.chatsnap.R;
import com.example.chatsnap.mock.MockLatencyHelper;
import com.example.chatsnap.persistence.entity.Account;
import com.example.chatsnap.ui.callback.AccountCallback;
import com.example.chatsnap.ui.callback.MessageCallback;
import com.example.chatsnap.util.StringUtils;
import com.example.chatsnap.viewmodel.ChatSnapViewModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class RegisterFragment extends Fragment {
    /** Default logger used for logging */
    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterFragment.class);

    public static final String TAG = "RegisterFragment";

    private TextInputEditText mLoginTextView;
    private TextInputEditText mPasswordTextView;
    private Button mCreateAccountButton;
    private Button mGoToLoginButton;
    private ProgressBar mProgress;

    private ChatSnapViewModel mViewModel;

    private AccountCallback mAccountCallback;


    @Override
    public void onAttach(Context context) {
        if (BuildConfig.DEBUG) { LOGGER.info("onAttach() "); }
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mAccountCallback = (AccountCallback) context;
        } else {
            if (BuildConfig.DEBUG) {
                throw new RuntimeException("onAttach() context is not a " + MessageCallback.class.getSimpleName());
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.register_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLoginTextView = view.findViewById(R.id.register_userid);
        mPasswordTextView = view.findViewById(R.id.register_pwd);
        mCreateAccountButton = view.findViewById(R.id.register_button);
        mGoToLoginButton = view.findViewById(R.id.register_login);
        mProgress = view.findViewById(R.id.register_progress);

        mCreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgress(true);
                final String login = mLoginTextView.getText().toString().trim();
                final String pwd = mPasswordTextView.getText().toString().trim();
                if (BuildConfig.DEBUG) { LOGGER.debug("Register - onClick({}/{})", login, pwd); }
                if (TextUtils.isEmpty(login) || TextUtils.isEmpty(pwd)) {
                    showProgress(false);
                    showToast("Wrong credentials");
                } else if (!StringUtils.isEmailValid(login)) {
                    showProgress(false);
                    showToast("Please use a valid email address");
                } else {
                    if (BuildConfig.DEBUG) { LOGGER.debug("onClick - submit register l/p:{}/{}", login, pwd); }
                    mViewModel.getAccount(login).observe(RegisterFragment.this, new Observer<Account>() {
                        @Override
                        public void onChanged(@Nullable Account account) {
                            MockLatencyHelper.simulateNetworkLatency();
                            showProgress(false);
                            if (account != null) {
                                showToast("Account already exist");
                            } else {
                                mAccountCallback.onLoginSuccessfull(new Account(login, pwd, true));
                            }
                        }
                    });
                }
            }
        });

        mGoToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BuildConfig.DEBUG) { LOGGER.debug("Register - onClick()"); }
                mAccountCallback.onGotoLogin();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ChatSnapViewModel.class);
    }

    private void showProgress(boolean show) {
        mProgress.setVisibility(show ? VISIBLE : GONE);
    }

    private void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
