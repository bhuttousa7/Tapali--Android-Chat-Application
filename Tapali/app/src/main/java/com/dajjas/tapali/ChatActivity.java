package com.dajjas.tapali;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ChatActivity extends AppCompatActivity {
    GetChatTask mGetChatTask = null;
    SendMessageTask mSendMessageTask = null;
    private List<MessageFull> mMessageListData = null;

    private ChatMessageArrayAdapter adapter;

    private EditText mMessageEditText;
    private Toolbar mToolbar;
    private Chat mChat;
    private ListView mMessageListView;

    private int chatId = -1;
    private int lastMessageId = -1;

    class MessageFull {
        Message message;
        User profile;

        MessageFull() {

        }

        MessageFull(Message message, User profile) {
            this.message = message;
            this.profile = profile;
        }

        Message getMessage() {
            return message;
        }

        User getProfile() {
            return profile;
        }

        void setMessage(Message message) {
            this.message = message;
        }

        void setProfile(User profile) {
            this.profile = profile;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        chatId = getIntent().getIntExtra("chat_id", -1);
        if(chatId < 0) {
            // Exit chat activity maybe?
            Log.e("Tapali_Chat", "Invalid chat id: " + chatId);
        }

        updateChat();

        mMessageEditText = (EditText)findViewById(R.id.message_send_text);

        ImageButton sendButton = (ImageButton)findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = mMessageEditText.getText().toString();
                Log.e("Tapali", mChat.getId() + " => " + message);
                mSendMessageTask = new SendMessageTask(mChat.getId(), message);
                mSendMessageTask.execute((Void) null);
            }
        });

        mMessageListData = new ArrayList<MessageFull>();
        mMessageListView = (ListView)findViewById(R.id.messages);
        adapter = new ChatMessageArrayAdapter(this, R.layout.chat_message, mMessageListData);
        mMessageListView.setAdapter(adapter);

        // Keep getting messages from server
        final Handler handler = new Handler();
        TimerTask getMessagesTimerTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            GetMessagesTask getMessagesTask = new GetMessagesTask(chatId, lastMessageId);
                            getMessagesTask.execute();
                        } catch(Exception e) {
                            // eat exception
                        }
                    }
                });
            }
        };
        Timer timer = new Timer();
        timer.schedule(getMessagesTimerTask, 0, 1000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_change_title) {
            Intent chatChangeTitleIntent = new Intent(this, ChatChangeTitleActivity.class);
            chatChangeTitleIntent.putExtra("chat_id", chatId);
            chatChangeTitleIntent.putExtra("chat_title", mToolbar.getTitle());
            startActivityForResult(chatChangeTitleIntent, 1);
        } else if(id == R.id.action_invite_user) {
            Intent chatInviteIntent = new Intent(this, ChatInviteActivity.class);
            chatInviteIntent.putExtra("chat_id", chatId);
            startActivity(chatInviteIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            // Update chat title
            updateChat();
            Log.d("Tapali_Chat", "Updating chat title");
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    void updateChat() {
        mGetChatTask = new GetChatTask(chatId);
        mGetChatTask.execute((Void) null);
    }


    /**
     * Get chat information
     */
    public class GetChatTask extends AsyncTask<Void, Void, Chat> {
        private int chatId;

        GetChatTask(int chatId) {
            this.chatId = chatId;
        }

        @Override
        protected Chat doInBackground(Void... params) {
            return SessionHandler.getChat(chatId);
        }

        @Override
        protected void onPostExecute(final Chat _chat) {
            mGetChatTask = null;
            mChat = _chat;

            // Do something with mChat title and user list here
            Log.d("Tapali_Chat", mChat.getChatTitle() + ", id: " + mChat.getId());
            mToolbar.setTitle(Utils.getUtf8String(mChat.getChatTitle()));
        }

        @Override
        protected void onCancelled() {
            mGetChatTask = null;
        }
    }

    /**
     * Send message to mChat
     */
    public class SendMessageTask extends AsyncTask<Void, Void, Message> {
        private int chatId;
        private String message;

        SendMessageTask(int chatId, String message) {
            this.chatId = chatId;
            this.message = message;
        }

        @Override
        protected Message doInBackground(Void... params) {
            return SessionHandler.sendMessage(chatId, message);
        }

        @Override
        protected void onPostExecute(final Message message) {
            mSendMessageTask = null;

            if(message != null) {
                // Message successfully sent
                mMessageEditText.setText("");
            }
        }

        @Override
        protected void onCancelled() {
            mSendMessageTask = null;
        }
    }

    /**
     * Get chat messages from server
     */
    public class GetMessagesTask extends AsyncTask<Void, Void, List<MessageFull>> {
        private int chatId;
        private int lastId;

        GetMessagesTask(int chatId, int lastId) {
            this.chatId = chatId;
            this.lastId = lastId;
        }

        @Override
        protected List<MessageFull> doInBackground(Void... params) {
            List<Message> messages = SessionHandler.getChatMessages(chatId, lastId);
            List<MessageFull> messagesFull = new ArrayList<MessageFull>();

            for(int i = 0; i < messages.size(); i++) {
                if (messages.get(0).getMessageId() > lastId)
                    lastMessageId = messages.get(messages.size()-1).getMessageId();

                MessageFull full = new MessageFull();
                Message message = messages.get(i);
                User profile = SessionHandler.getProfileById(message.getUserId());

                message.setMessage(Utils.getUtf8String(message.getMessage()));
                profile.setDisplayName(Utils.getUtf8String(SessionHandler.getProfileById(message.getUserId()).getDisplayName()));

                full.setMessage(message);
                full.setProfile(SessionHandler.getProfileById(message.getUserId()));

                Log.d("Tapali_Chat", message.getMessage());

                messagesFull.add(full);
            }

            return messagesFull;
        }

        @Override
        protected void onPostExecute(final List<MessageFull> messages) {
            if(messages.size() > 0) {
                MessageFull lastMessage = messages.get(messages.size() - 1);
                lastMessageId = lastMessage.getMessage().getMessageId();

                // Display messages
                adapter.addAll(messages);
                mMessageListView.setSelection(adapter.getCount() - 1);
            }
        }

        @Override
        protected void onCancelled() {
        }
    }


    private class ChatMessageArrayAdapter extends ArrayAdapter<MessageFull> {
        private class ChatMessageHolder {
            TextView username;
            TextView message;
            TextView timestamp;
        }

        private Context context;
        private int resource;

        public ChatMessageArrayAdapter(Context context, int resource, List<MessageFull> messages) {
            super(context, resource, messages);
            this.context = context;
            this.resource = resource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ChatMessageHolder messageHolder = null;

            Log.d("Tapali_Chat", "Found position: " + position + ", convertView: " + convertView);

            if(convertView == null)
            {
                LayoutInflater inflater = ((Activity)context).getLayoutInflater();
                convertView = inflater.inflate(resource, parent, false);

                messageHolder = new ChatMessageHolder();
                messageHolder.username = (TextView)convertView.findViewById(R.id.message_username);
                messageHolder.message = (TextView)convertView.findViewById(R.id.message_text);
                messageHolder.timestamp = (TextView)convertView.findViewById(R.id.message_timestamp);

                convertView.setTag(messageHolder);
            }
            else
            {
                messageHolder = (ChatMessageHolder)convertView.getTag();
            }

            MessageFull messageFull = getItem(position);
            User profile = messageFull.getProfile();
            Message message = messageFull.getMessage();

            messageHolder.username.setText(profile.getDisplayName());
            messageHolder.message.setText(message.getMessage());
            messageHolder.timestamp.setText(message.getTimestamp());

            return convertView;
        }
    }
}
