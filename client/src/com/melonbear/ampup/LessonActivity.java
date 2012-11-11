package com.melonbear.ampup;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class LessonActivity extends Activity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_lesson);
    TextView tv = (TextView) findViewById(R.id.text);
    tv.setText("FUUU");
  }
}
