package com.example.chatsnap;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.Fade;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.example.chatsnap.persistence.entity.Account;
import com.example.chatsnap.persistence.entity.Message;
import com.example.chatsnap.ui.callback.AccountCallback;
import com.example.chatsnap.ui.callback.MessageCallback;
import com.example.chatsnap.ui.fragment.LoginFragment;
import com.example.chatsnap.ui.fragment.MessagesFragment;
import com.example.chatsnap.ui.fragment.RegisterFragment;
import com.example.chatsnap.viewmodel.ChatSnapViewModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Main activity
 */
public class MainActivity extends AppCompatActivity implements MessageCallback, AccountCallback {

    /** Default tag used for logging */
    private static final Logger LOGGER = LoggerFactory.getLogger(MainActivity.class);

    private ChatSnapViewModel mViewModel;
    private Account mCurrentActiveAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (BuildConfig.DEBUG) { LOGGER.debug("onCreate() "); }
        setContentView(R.layout.activity_layout);
        mViewModel = ViewModelProviders.of(this).get(ChatSnapViewModel.class);

        /** listen for active account to enter the app, or load the login fragment */
        mViewModel.getActiveAccount().observe(this, new Observer<Account>() {
            @Override
            public void onChanged(@Nullable final Account account) {
                if (BuildConfig.DEBUG) { LOGGER.debug("ActiveAccount - onChanged({}) ", account != null ? account.getLogin() : "null"); }
                if (account != null) {
                    mCurrentActiveAccount = account;
                    load(new MessagesFragment(), MessagesFragment.TAG);
                } else {
                    onGotoLogin();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.disconnect:
                mViewModel.logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onImageWatched(long messageId) {
        if (BuildConfig.DEBUG) { LOGGER.debug("onImageWatched(messageID={}) ", messageId); }
        mViewModel.deleteMessage(messageId);
    }

    @Override
    public void onNewMessage() {
        if (BuildConfig.DEBUG) { LOGGER.debug("onNewMessage "); }
        ImagePicker.create(this)
                .limit(1)
                .includeVideo(false)
                .start();
    }

    @Override
    public void onLoginSuccessfull(@NonNull Account account) {
        if (BuildConfig.DEBUG) { LOGGER.debug("onLoginSuccessfull({}} "); }
        mViewModel.activateAccount(account);
    }

    @Override
    public void onGotoRegister() {
        if (BuildConfig.DEBUG) { LOGGER.debug("onGotoRegister() "); }
        load(new RegisterFragment(), RegisterFragment.TAG);
    }

    @Override
    public void onGotoLogin() {
        if (BuildConfig.DEBUG) { LOGGER.debug("onGotoLogin() "); }
        load(new LoginFragment(), LoginFragment.TAG);
    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            Image image = ImagePicker.getFirstImageOrNull(data);
            if (BuildConfig.DEBUG) { LOGGER.debug("onActivityResult - image:{}", image.getPath()); }
            if (image != null) {
                mViewModel.addMessage(new Message(mCurrentActiveAccount.getLogin(), System.currentTimeMillis(), "", "file://" + image.getPath()));
            } else {
                Toast.makeText(this, "oups ! Something got wrong ... :/", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void load(Fragment fragment, String tag) {
        if (BuildConfig.DEBUG) { LOGGER.debug("load({}) ", tag); }
        Fade enterFade = new Fade();
        enterFade.setDuration(300);

        fragment.setEnterTransition(enterFade);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment, tag)
                .commitNow();
    }

}
