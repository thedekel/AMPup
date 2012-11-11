package com.melonbear.ampup;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
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
    TextView tv;
    tv = convertView != null ? (TextView) convertView : new TextView(mContext);
    tv.setText(getItem(position).toString());
    return tv;
  }
}
