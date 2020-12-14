package com.dajjas.tapali;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsMenuActivity extends AppCompatActivity {

    EditText mDisplayName;
    EditText mPassword;
    EditText mPasswordConfirm;
    CheckBox mStoreLoginInformation;
    CheckBox mAutomaticLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDisplayName = (EditText)findViewById(R.id.display_name);

        String displayName = getIntent().getStringExtra("display_name");
        mDisplayName.setText(displayName);

        mPassword = (EditText)findViewById(R.id.password);
        mPasswordConfirm = (EditText)findViewById(R.id.password_confirm);

        mStoreLoginInformation = (CheckBox)findViewById(R.id.remember_password);
        mStoreLoginInformation.setChecked(SessionHandler.getStoreLoginInformation());

        mAutomaticLogin = (CheckBox)findViewById(R.id.auto_login);
        mAutomaticLogin.setChecked(SessionHandler.getAutomaticLogin());

        Button saveButton = (Button)findViewById(R.id.save_profile_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettings();
            }
        });

        Button logoutButton = (Button)findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionHandler.setAutomaticLogin(false);
                setResult(1);
                finish();
            }
        });
    }

    private void saveSettings() {
        SessionHandler.setAutomaticLogin(mAutomaticLogin.isChecked());
        SessionHandler.setStoreLoginInformation(mStoreLoginInformation.isChecked());

        mPassword.setError(null);
        mPasswordConfirm.setError(null);
        mDisplayName.setError(null);

        String displayName = mDisplayName.getText().toString();
        if(TextUtils.isEmpty(displayName)) {
            mDisplayName.setError("Display name cannot be empty");
            mDisplayName.requestFocus();
            return;
        }

        String password = null;
        String passwordConfirm = null;
        if(!TextUtils.isEmpty(mPassword.getText()) || !TextUtils.isEmpty(mPasswordConfirm.getText())) {
            // Check passwords if there's a password or password confirm entered

            password = mPassword.getText().toString();
            passwordConfirm = mPasswordConfirm.getText().toString();

            if (TextUtils.isEmpty(password) && !TextUtils.isEmpty(passwordConfirm)) {
                mPassword.setError("Must enter password to continue");
                mPassword.requestFocus();
                return;
            }

            if (!TextUtils.isEmpty(password) && TextUtils.isEmpty(passwordConfirm)) {
                mPasswordConfirm.setError("Must enter password again to continue");
                mPasswordConfirm.requestFocus();
                return;
            }

            if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(passwordConfirm) &&
                    !password.equals(passwordConfirm)) {
                mPasswordConfirm.setError("Passwords must match to continue");
                mPasswordConfirm.requestFocus();
                return;
            }
        }

        UpdateUserTask updateUserTask = new UpdateUserTask(displayName, password);
        updateUserTask.execute();
    }

    /**
     * Represents an asynchronous login task used to authenticate
     * the user.
     */
    public class UpdateUserTask extends AsyncTask<Void, Void, Boolean> {

        private final String mDisplayName;
        private final String mPassword;

        UpdateUserTask(String displayName, String password) {
            mDisplayName = displayName;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return SessionHandler.updateUser(mDisplayName, mPassword);
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                finish();
            } else {
                Toast.makeText(getApplicationContext(), R.string.error_profile_update, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
        }
    }
}
