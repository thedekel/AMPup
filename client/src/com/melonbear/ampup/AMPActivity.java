package com.melonbear.ampup;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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
      switch (i) {
        case 0:
          return new ListSectionFragment();
        case 1:
          return new MediaSectionFragment();
        case 2:
          Fragment dummy = new DummySectionFragment();
          Bundle args = new Bundle();
          args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, 3);
          dummy.setArguments(args);
          return dummy;
      }
      return null;
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

  public static class ListSectionFragment extends ListFragment {
    private ListView mList;

    public ListSectionFragment() {}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
      super.onActivityCreated(savedInstanceState);
      mList = getListView();
      new AsyncTask<Void, Integer, List<Lesson>>() {
        @Override
        protected List<Lesson> doInBackground(Void... params) {
          List<Lesson> result = null;
          HttpClient client = new DefaultHttpClient();
          try {
            HttpResponse res = client.execute(new HttpGet("http://192.168.43.85:3000/lessons"));
            StatusLine statusLine = res.getStatusLine();
            int status = statusLine.getStatusCode();
            if (status == HttpStatus.SC_OK) {
              ByteArrayOutputStream out = new ByteArrayOutputStream();
              res.getEntity().writeTo(out);
              out.close();
              String response = out.toString();
              JSONObject dekel = new JSONObject(response);
              result = jsonToList(dekel);
            } else {
              res.getEntity().getContent().close();
              throw new IOException(statusLine.getReasonPhrase());
            }

          } catch (ClientProtocolException e) {
            e.printStackTrace();
          } catch (IOException e) {
            e.printStackTrace();
          } catch (JSONException e) {
            e.printStackTrace();
          }
          return result;
        }

        @Override
        protected void onPostExecute(List<Lesson> lessons) {
          if (mList != null) {
            LessonAdapter adapter = new LessonAdapter(getActivity(), lessons);
            mList.setAdapter(adapter);
          }
        }

      }.execute();
    }

    private List<Lesson> jsonToList(JSONObject dekel) throws JSONException {
      Log.i("Response", dekel.toString());
      JSONArray resultArray = dekel.has("result_array") ?
          dekel.getJSONArray("result_array") : null;
      List<Lesson> result = new ArrayList<Lesson>();
      for (int i = 0; i < resultArray.length(); i++) {
        JSONObject arr = (JSONObject) resultArray.get(i);
        String title = (String) arr.get("title");
        result.add(new Lesson(title));
      }
      return result;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
      View v = inflater.inflate(R.layout.list_view, null);
      return v;
    }
  }

  /**
   * A dummy fragment representing a section of the app, but that simply
   * displays dummy text.
   */
  public static class DummySectionFragment extends Fragment {
    public DummySectionFragment() {}

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

  @SuppressWarnings("resource")
  public static void copyFdToFile(FileDescriptor src, File dst)
      throws IOException {
    FileChannel inChannel = new FileInputStream(src).getChannel();
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
