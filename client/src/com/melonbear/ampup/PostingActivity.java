package com.melonbear.ampup;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PostingActivity extends Activity {
	int id;
	String name;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		id = getIntent().getIntExtra("id", 0);
		name = getIntent().getStringExtra("name");
		setContentView(R.layout.activity_posting);
		LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
		LinearLayout horizll = (LinearLayout) findViewById(R.id.horizll);

		final TextView countdownTextView = (TextView) findViewById(R.id.textViewCountDown);
		countdownTextView.setText("00:00");
		countdownTextView.setGravity(Gravity.CENTER);

		mPlayButton = new PlayButton(this);




		mPlayButton.setEnabled(false);
		horizll.addView(mPlayButton, new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, 0));


		mRecordButton = (Button) findViewById(R.id.record);
		mRecordButton.setOnClickListener(new OnClickListener() {
			
			boolean mStartRecording;

			@Override
			public void onClick(View v) {
				mStartRecording = !mStartRecording;
				onRecord(mStartRecording);
				if (mStartRecording) {
					mRecordButton.setText("Stop recording");
				} else {
					mRecordButton.setText("Start recording");
				}
			}
		});

		mPlayMetronomeButton = new PlayMetronomeButton(this);
		horizll.addView(mPlayMetronomeButton, new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, 0));

		mSubmitButton = new Button(this);
		mSubmitButton.setText("Submit");
		ll.addView(mSubmitButton);

		OnClickListener onClick = new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(PostingActivity.this, SubmitActivity.class);
				intent.putExtra("id", id);
				intent.putExtra("name", name);
				startActivityForResult(intent, 0);
			}
		};
		mSubmitButton.setOnClickListener(onClick);

		mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
		String timestamp = new SimpleDateFormat("yyyyMMddhhmm").format(new Date());

		mFileName += "/" + this.username + "_" + timestamp + ".3gp";
		Log.i("tag", mFileName);

		countDownTimer = new CountDownTimer(30 * 1000, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {
				countdownTextView.setText(":"
						+ Integer.toString((int) (millisUntilFinished / 1000.0)));
			}

			@Override
			public void onFinish() {
				countdownTextView.setText("00:00");
			}
		};
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_posting, menu);
		return true;
	}

	private static final String LOG_TAG = "AudioRecordTest";

	private static String mFileName = null;

	private Button mRecordButton = null;
	private MediaRecorder mRecorder = null;

	private PlayButton mPlayButton = null;
	private MediaPlayer mPlayer = null;

	private PlayMetronomeButton mPlayMetronomeButton = null;
	private MediaPlayer mPlayerMetronome = null;
	private String username = null;

	private static int TIMER_DURATION = 3050;
	private static int TIMER_CALLBACK_INTERVAL = 1000;

	private Button mSubmitButton;

	private CountDownTimer countDownTimer;

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
		countDownTimer.start();

	}

	private void stopRecording() {
		mPlayButton.setEnabled(true);
		mRecorder.stop();
		mRecorder.release();
		mRecorder = null;
		countDownTimer.cancel();
		countDownTimer.onFinish();
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

		CountDownTimer timer = new CountDownTimer(TIMER_DURATION,
				TIMER_CALLBACK_INTERVAL) {

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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			data.putExtra("filename", mFileName);
			setResult(RESULT_OK, data);
			this.finish();
		}
	}
}
