package com.melonbear.ampup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SubmitActivity extends Activity {

  Button submitButton;
  EditText commentsTextEdit;
  TextView lessonName;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_submit);
    id = getIntent().getIntExtra("id", 0);
    name = getIntent().getStringExtra("name");

    lessonName = (TextView) findViewById(R.id.textViewlessonName);
    commentsTextEdit = (EditText) findViewById(R.id.commentsTextEdit);
    submitButton = (Button) findViewById(R.id.finalSubmitButton);

    lessonName.setText(name);
    OnClickListener clicker = new OnClickListener() {

      public void onClick(View arg0) {
        Intent intent = new Intent();

        intent.putExtra("comments", commentsTextEdit.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
      }
    };

    submitButton.setOnClickListener(clicker);

  }

  int id;
  String name;

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.activity_submit, menu);

    return true;
  }
}
