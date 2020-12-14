package com.dajjas.tapali;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ChatInviteActivity extends AppCompatActivity {
    int mChatId;
    EditText mUsernameField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_invite);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mChatId = getIntent().getIntExtra("chat_id", -1);
        mUsernameField = (EditText)findViewById(R.id.invite_username);

        Button inviteButton = (Button)findViewById(R.id.invite_user_button);
        inviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inviteUser();
            }
        });

    }

    private void inviteUser() {
        mUsernameField.setError(null);

        if(TextUtils.isEmpty(mUsernameField.getText())) {
            mUsernameField.setError(getString(R.string.error_invalid_invite_username));
            mUsernameField.requestFocus();
            return;
        }

        InviteUserTask inviteUserTask = new InviteUserTask(mChatId, mUsernameField.getText().toString());
        inviteUserTask.execute();
    }

    /**
     * Invite user to specified chat
     */
    public class InviteUserTask extends AsyncTask<Void, Void, Integer> {
        private int chatId;
        private String username;

        InviteUserTask(int chatId, String username) {
            this.chatId = chatId;
            this.username = username;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            return SessionHandler.inviteUserToChat(chatId, username);
        }

        @Override
        protected void onPostExecute(final Integer result) {
            if(result == 0) {
                mUsernameField.setError(getString(R.string.error_invalid_invite_username));
            } else if(result == 1) {
                finish();
            }
        }

        @Override
        protected void onCancelled() {
        }
    }
}
