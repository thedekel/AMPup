package com.melonbear.ampup;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
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

    // requestWindowFeature(Window.FEATURE_NO_TITLE);
    // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
    // WindowManager.LayoutParams.FLAG_FULLSCREEN);

    setContentView(R.layout.activity_login);

    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
    if (preferences.getBoolean("FirstRun", true)) {
      Log.i("tag", "This is the first run");
      new FirstRunTask().execute(this);
      Editor editor = preferences.edit();
      editor.putBoolean("FirstRun", false);
      editor.commit();
    }

    submitButton = (Button) findViewById(R.id.btnLogin);
    usernameEditText = (EditText) findViewById(R.id.edtxtUsername);
    passwordEditText = (EditText) findViewById(R.id.edtxtPassword);

    OnClickListener clicker = new OnClickListener() {
      public void onClick(View v) {
        String usernameString = usernameEditText.getText().toString();
        Log.i("tag", "username: " + usernameEditText.getText().toString());
        Log.i("tag", "password: " + passwordEditText.getText().toString());
        new LoginTask().execute(getApplicationContext(), usernameString);
      }
    };
    submitButton.setOnClickListener(clicker);
  }

  private class FirstRunTask extends AsyncTask<Object, Integer, Void> {
    private Context mContext;

    @Override
    public Void doInBackground(Object... args) {
      mContext = (Context) args[0];
      onFirstRun();
      return null;
    }

    public void onFirstRun() {
      AssetManager am = mContext.getAssets();
      AssetFileDescriptor afd = null;
      try {
        afd = am.openFd("metronome120bpm.mp3");

        // Create new file to copy into.
        File file = new File(Environment.getExternalStorageDirectory()
            + java.io.File.separator + "metronome120bpm.mp3");
        file.createNewFile();

        copyFdToFile(afd.getFileDescriptor(), file);

      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @SuppressWarnings("resource")
    public void copyFdToFile(FileDescriptor src, File dst) throws IOException {
      FileChannel inChannel = new FileInputStream(src).getChannel();
      FileChannel outChannel = new FileOutputStream(dst).getChannel();
      try {
        inChannel.transferTo(0, inChannel.size(), outChannel);
      } finally {
        if (inChannel != null) {
          inChannel.close();
        }
        if (outChannel != null) {
          outChannel.close();
        }
      }
    }

    @Override
    protected void onPostExecute(Void v) {}
  }
}
