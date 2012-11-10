package com.melonbear.ampup;

/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * This class provides a basic demonstration of how to write an Android
 * activity. Inside of its window, it places a single view: an EditText that
 * displays and edits some internal text.
 */
public class MediaSectionFragment extends Fragment {

  private static final String LOG_TAG = "AudioRecordTest";
  private static String mFileName = null;

  private RecordButton mRecordButton = null;
  private MediaRecorder mRecorder = null;

  private PlayButton mPlayButton = null;
  private MediaPlayer mPlayer = null;

  private PlayMetronomeButton mPlayMetronomeButton = null;
  private MediaPlayer mPlayerMetronome = null;
  private String username = null;

  private void onRecord(boolean start) {
    if (start) {
      startRecording();
    } else {
      stopRecording();
    }
  }

  private void onPlay(boolean start) {
    if (start) {
      startPlaying();
    } else {
      stopPlaying();
    }
  }

  private void onPlayMetronome(boolean start) {
    if (start) {
      startPlayingMetronome();
    } else {
      stopPlayingMetronome();
    }
  }

  private void startPlaying() {
    mPlayer = new MediaPlayer();

    OnCompletionListener completionListener = new OnCompletionListener() {

      public void onCompletion(MediaPlayer mp) {
        mPlayButton.setText("Start Playing");
        mPlayButton.mStartPlaying = !mPlayButton.mStartPlaying;
        Log.i(LOG_TAG, "Finished Playing");
      }
    };

    mPlayer.setOnCompletionListener(completionListener);
    try {
      mPlayer.setDataSource(mFileName);
      mPlayer.prepare();
      mPlayer.start();
    } catch (IOException e) {
      Log.e(LOG_TAG, "prepare() failed");
    }
  }

  private void stopPlaying() {
    mPlayer.release();
    mPlayer = null;
  }

  private void stopPlayingMetronome() {
    mPlayerMetronome.release();
    mPlayerMetronome = null;
  }

  private void startPlayingMetronome() {
    mPlayerMetronome = new MediaPlayer();
    OnCompletionListener completionListener = new OnCompletionListener() {

      public void onCompletion(MediaPlayer mp) {
        mPlayMetronomeButton.setText("Start Playing");
        Log.i(LOG_TAG, "Finished Playing");
      }
    };

    mPlayerMetronome.setOnCompletionListener(completionListener);
    try {
      String metronomeFileName = Environment.getExternalStorageDirectory()
          .getAbsolutePath();
      metronomeFileName += "/metronome120bpm.mp3";

      mPlayerMetronome.setDataSource(metronomeFileName);
      mPlayerMetronome.prepare();
      mPlayerMetronome.start();
    } catch (IOException e) {
      Log.e(LOG_TAG, "prepare() failed");
    }
  }

  private void startRecording() {

    mRecorder = new MediaRecorder();
    mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
    mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
    mRecorder.setOutputFile(mFileName);
    mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

    try {
      mRecorder.prepare();
    } catch (IOException e) {
      Log.e(LOG_TAG, "prepare() failed");
    }

    mRecorder.start();
  }

  private void stopRecording() {
    mRecorder.stop();
    mRecorder.release();
    mRecorder = null;
  }

  class RecordButton extends Button {
    boolean mStartRecording = true;

    OnClickListener clicker = new OnClickListener() {
      public void onClick(View v) {
        onRecord(mStartRecording);
        if (mStartRecording) {
          setText("Stop recording");
        } else {
          setText("Start recording");
        }
        mStartRecording = !mStartRecording;
      }
    };

    public RecordButton(Context ctx) {
      super(ctx);
      setText("Start recording");
      setOnClickListener(clicker);
    }
  }

  class PlayButton extends Button {
    boolean mStartPlaying = true;

    OnClickListener clicker = new OnClickListener() {
      public void onClick(View v) {
        onPlay(mStartPlaying);
        if (mStartPlaying) {
          setText("Stop playing");
        } else {
          setText("Start playing");
        }
        mStartPlaying = !mStartPlaying;
      }
    };

    public PlayButton(Context ctx) {
      super(ctx);
      setText("Start playing");
      setOnClickListener(clicker);

    }
  }

  class PlayMetronomeButton extends Button {
    boolean mStartPlayingMetronome = true;

    CountDownTimer timer = new CountDownTimer(3050, 1000) {

      @Override
      public void onTick(long arg0) {
        Log.i("tag", Long.toString(arg0));
        setText(Integer.toString(Math.round(Math.round(arg0 / 1000.0))));
      }

      @Override
      public void onFinish() {
        onPlayMetronome(mStartPlayingMetronome);
        if (mStartPlayingMetronome) {
          setText("Stop Metronome");
        } else {
          setText("Start Metronome");
        }
        mStartPlayingMetronome = !mStartPlayingMetronome;
      }
    };
    OnClickListener clicker = new OnClickListener() {

      public void onClick(View v) {
        if (mStartPlayingMetronome) {
          timer.start();
        } else {
          onPlayMetronome(mStartPlayingMetronome);
          mStartPlayingMetronome = !mStartPlayingMetronome;
          setText("MN");
        }
      }
    };

    public PlayMetronomeButton(Context ctx) {
      super(ctx);
      setText("MN");
      setOnClickListener(clicker);
    }
  }

  public MediaSectionFragment() {

    mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
    String timestamp = new SimpleDateFormat("yyyyMMddhhmm").format(new Date());

    mFileName += "/" + this.username + "_" + timestamp + ".3gp";
    Log.i("tag", mFileName);
  }

  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    LinearLayout ll = new LinearLayout(getActivity());

    mRecordButton = new RecordButton(getActivity());
    ll.addView(mRecordButton, new LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT, 0));
    mPlayButton = new PlayButton(getActivity());
    ll.addView(mPlayButton, new LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT, 0));
    mPlayMetronomeButton = new PlayMetronomeButton(getActivity());
    ll.addView(mPlayMetronomeButton, new LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT, 0));
    return ll;
  }

  @Override
  public void onPause() {
    super.onPause();
    if (mRecorder != null) {
      mRecorder.release();
      mRecorder = null;
    }

    if (mPlayer != null) {
      mPlayer.release();
      mPlayer = null;
    }
  }

}