package com.melonbear.ampup;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AMPActivity extends FragmentActivity {

  /**
   * The {@link android.support.v4.view.PagerAdapter} that will provide
   * fragments for each of the sections. We use a
   * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which will
   * keep every loaded fragment in memory. If this becomes too memory intensive,
   * it may be best to switch to a
   * {@link android.support.v4.app.FragmentStatePagerAdapter}.
   */
  SectionsPagerAdapter mSectionsPagerAdapter;

  /**
   * The {@link ViewPager} that will host the section contents.
   */
  ViewPager mViewPager;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_amp);

    SharedPreferences preferences = getPreferences(MODE_PRIVATE);
    if (preferences.getBoolean("FirstRun", true)) {
      Log.i("tag", "This is the first run");
      onFirstRun();
      Editor editor = preferences.edit();
      editor.putBoolean("FirstRun", false);
      editor.commit();
    }

    // Create the adapter that will return a fragment for each of the three
    // primary sections
    // of the app.
    mSectionsPagerAdapter = new SectionsPagerAdapter(
        getSupportFragmentManager());

    // Set up the ViewPager with the sections adapter.
    mViewPager = (ViewPager) findViewById(R.id.pager);
    mViewPager.setAdapter(mSectionsPagerAdapter);

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.activity_amp, menu);
    return true;
  }

  /**
   * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one
   * of the primary sections of the app.
   */
  public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int i) {
      Fragment fragment;

      if (i == 1) {
        fragment = new MediaSectionFragment();
      } else {
        fragment = new DummySectionFragment();
      }

      Bundle args = new Bundle();
      args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, i + 1);
      fragment.setArguments(args);
      return fragment;
    }

    @Override
    public int getCount() {
      return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
      switch (position) {
      case 0:
        return getString(R.string.title_section1).toUpperCase();
      case 1:
        return getString(R.string.title_section2).toUpperCase();
      case 2:
        return getString(R.string.title_section3).toUpperCase();
      }
      return null;
    }
  }

  /**
   * A dummy fragment representing a section of the app, but that simply
   * displays dummy text.
   */
  public static class DummySectionFragment extends Fragment {
    public DummySectionFragment() {
    }

    public static final String ARG_SECTION_NUMBER = "section_number";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
      TextView textView = new TextView(getActivity());
      textView.setGravity(Gravity.CENTER);
      Bundle args = getArguments();
      textView.setText(Integer.toString(args.getInt(ARG_SECTION_NUMBER)));
      return textView;
    }
  }

  public void onFirstRun() {
    AssetManager am = this.getAssets();
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

  public static void copyFdToFile(FileDescriptor src, File dst)
      throws IOException {
    @SuppressWarnings("resource")
    FileChannel inChannel = new FileInputStream(src).getChannel();
    @SuppressWarnings("resource")
    FileChannel outChannel = new FileOutputStream(dst).getChannel();
    try {
      inChannel.transferTo(0, inChannel.size(), outChannel);
    } finally {
      if (inChannel != null)
        inChannel.close();
      if (outChannel != null)
        outChannel.close();
    }
  }
}
