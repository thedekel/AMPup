package com.melonbear.ampup;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {

  Button submitButton;
  EditText usernameEditText;
  EditText passwordEditText;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    submitButton = (Button) findViewById(R.id.btnLogin);
    usernameEditText = (EditText) findViewById(R.id.edtxtUsername);
    passwordEditText = (EditText) findViewById(R.id.edtxtPassword);

    OnClickListener clicker = new OnClickListener() {
      public void onClick(View v) {
        Log.i("tag", "username: " + usernameEditText.getText().toString());
        Log.i("tag", "password: " + passwordEditText.getText().toString());
      }
    };
    submitButton.setOnClickListener(clicker);

  }

}
