package com.dajjas.tapali;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateChatActivity extends AppCompatActivity {
    EditText mChatTitleField;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        setContentView(R.layout.activity_create_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mChatTitleField = (EditText)findViewById(R.id.new_chat_title);

        Button createChatButton = (Button)findViewById(R.id.create_chat_button);
        createChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createChat();
            }
        });
    }

    private void createChat() {
        mChatTitleField.setError(null);

        if(TextUtils.isEmpty(mChatTitleField.getText())) {
            mChatTitleField.setError(getString(R.string.error_invalid_chat_title));
            mChatTitleField.requestFocus();
            return;
        }

        CreateChatTask createChatTask = new CreateChatTask(mChatTitleField.getText().toString());
        createChatTask.execute();
    }


    /**
     * Invite user to specified chat
     */
    public class CreateChatTask extends AsyncTask<Void, Void, Integer> {
        private String chatTitle;

        CreateChatTask(String chatTitle) {
            this.chatTitle = chatTitle;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            return SessionHandler.createChatGroup(chatTitle);
        }

        @Override
        protected void onPostExecute(final Integer chatId) {
            if(chatId == -1) {
                Toast.makeText(mContext, R.string.error_create_chat, Toast.LENGTH_LONG).show();
            } else {
                setResult(chatId);
                finish();
            }
        }

        @Override
        protected void onCancelled() {
        }
    }
}
