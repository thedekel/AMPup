package com.melonbear.ampup;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LessonAdapter extends BaseAdapter {
  private Context mContext;
  private List<Lesson> mLessons;

  public LessonAdapter(Context context, List<Lesson> lessons) {
    mContext = context;
    mLessons = lessons == null ? new ArrayList<Lesson>() : lessons;
  }

  public int getCount() {
    return mLessons.size();
  }

  public Object getItem(int position) {
    return mLessons.get(position);
  }

  public long getItemId(int position) {
    return position;
  }

  public View getView(int position, View convertView, ViewGroup parent) {
    View v;
    v = convertView != null ? convertView
      : LayoutInflater.from(mContext).inflate(R.layout.lesson_list_item, null);
    final Lesson lesson = (Lesson) getItem(position);
    v.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        Intent i = new Intent(mContext, LessonActivity.class);
        i.putExtra("lesson", lesson.toString());
        mContext.startActivity(i);
      }
    });
    TextView tv = (TextView) v.findViewById(R.id.lesson_title);
    tv.setText(getItem(position).toString());
    ((TextView) v.findViewById(R.id.lesson_subtitle)).setText("Test");
    return v;
  }
}
