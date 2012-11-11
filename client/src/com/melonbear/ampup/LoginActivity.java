package com.melonbear.ampup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {

  Button submitButton;
  EditText usernameEditText;
  EditText passwordEditText;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
//    requestWindowFeature(Window.FEATURE_NO_TITLE);
//    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
//    		WindowManager.LayoutParams.FLAG_FULLSCREEN);

    setContentView(R.layout.activity_login);

    submitButton = (Button) findViewById(R.id.btnLogin);
    usernameEditText = (EditText) findViewById(R.id.edtxtUsername);
    passwordEditText = (EditText) findViewById(R.id.edtxtPassword);

    OnClickListener clicker = new OnClickListener() {
      public void onClick(View v) {
        Log.i("tag", "username: " + usernameEditText.getText().toString());
        Log.i("tag", "password: " + passwordEditText.getText().toString());
        Intent myIntent = new Intent(getApplicationContext(), AMPActivity.class);
        startActivity(myIntent);
      }
    };
    submitButton.setOnClickListener(clicker);

  }
}
