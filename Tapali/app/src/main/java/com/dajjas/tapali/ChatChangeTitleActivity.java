package com.dajjas.tapali;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ChatChangeTitleActivity extends AppCompatActivity {
    int mChatId;
    EditText mChatTitleField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_change_title);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mChatId = getIntent().getIntExtra("chat_id", -1);

        String chatTitle = getIntent().getStringExtra("chat_title");
        mChatTitleField = (EditText)findViewById(R.id.new_chat_title);
        mChatTitleField.setText(chatTitle);

        Button changeTitleButton = (Button)findViewById(R.id.change_chat_title_button);
        changeTitleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTitle();
            }
        });
    }

    private void changeTitle() {
        mChatTitleField.setError(null);

        if(TextUtils.isEmpty(mChatTitleField.getText())) {
            mChatTitleField.setError(getString(R.string.error_invalid_chat_title));
            mChatTitleField.requestFocus();
            return;
        }

        ChangeChatTitleTask changeChatTitleTask = new ChangeChatTitleTask(mChatId, mChatTitleField.getText().toString());
        changeChatTitleTask.execute();
    }


    /**
     * Invite user to specified chat
     */
    public class ChangeChatTitleTask extends AsyncTask<Void, Void, Boolean> {
        private int chatId;
        private String chatTitle;

        ChangeChatTitleTask(int chatId, String chatTitle) {
            this.chatId = chatId;
            this.chatTitle = chatTitle;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return SessionHandler.updateChat(chatId, chatTitle);
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if(!success) {
                mChatTitleField.setError(getString(R.string.error_invalid_chat_title));
            } else {
                finish();
            }
        }

        @Override
        protected void onCancelled() {
        }
    }
}
