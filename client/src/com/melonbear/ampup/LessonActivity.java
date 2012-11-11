package com.melonbear.ampup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TextView;

public class LessonActivity extends FragmentActivity {

	private Lesson lesson;

	private ImageView image;
	private Button submitRecording, addComment, moreComments;
	private LayoutInflater inflater;

	// number of hours between refreshing data
	private static final long REFRESH_INTERVAL = 2;

	private final int MAX_COMMENTS = 4;

	private Uri imageUri;
	private final int GET_RECORDING = 0;

	private SharedPreferences totalComments;
	private SharedPreferences lastSpaceRefresh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lesson );
		
		inflater = getLayoutInflater();

		TextView title = (TextView) findViewById(R.id.title);
		TextView subtitle = (TextView) findViewById(R.id.subtitle);
		TextView description = (TextView) findViewById(R.id.description);

		lesson = new Lesson("LESSON 4", "FINDING YOUR PASSION", "Lorem Ipsum is simply dummy text of the printing and typesetting Lorem Ipsum is simply dummy text of the printing and typesetting Lorem Ipsum is simply dummy text of the printing and typesetting ");
		
		setTitle(lesson.title);

		title.setText(lesson.title);
		subtitle.setText(lesson.subtitle);
		description.setText(lesson.description);
		
		moreComments = (Button) findViewById(R.id.more_comments);
		addComment = (Button) findViewById(R.id.add_comment);
		loadComments();

	}

	private void loadComments() {
		
		List<Comment> comments = getComments();

		LinearLayout holder = (LinearLayout) findViewById(R.id.comments);
		holder.removeViews(1, holder.getChildCount() - 1);

		moreComments.setVisibility(comments.size() > MAX_COMMENTS ? View.VISIBLE : View.GONE);
		findViewById(R.id.empty).setVisibility(comments.size() > 0 ? View.GONE : View.VISIBLE);


		for (Comment comment: comments) {
			addComment(comment);
		}
	}
	

	private List<Comment> getComments() {
		List<Comment> comments = new ArrayList<Comment>();
		for (int i = 0; i < 4; i++) {
			Comment comment = new Comment();
			comment.comment = "This is a comment!";
			comment.user = "Jackie Chan";
			comments.add(comment);
		}
		return comments;
	}

	private void addComment(Comment comment) {
		LinearLayout commentsHolder = (LinearLayout) findViewById(R.id.comments);
		View v = inflater.inflate(R.layout.comment_item, null);

		TextView c = (TextView) v.findViewById(R.id.comment);
		c.setText(comment.comment);

		TextView u = (TextView) v.findViewById(R.id.user);
		SimpleDateFormat format = new SimpleDateFormat("MMM d, yyyy");
		u.setText("by " + comment.user + " on " + format.format(new Date(comment.dateInMilliseconds)));

		ImageView avatar = (ImageView) v.findViewById(R.id.avatar);
		//avatarLoader.DisplayImage(comment.avatar, avatar);

		commentsHolder.addView(v);

		LinearLayout line = new LinearLayout(this);
		line.setBackgroundColor(0xFFCCCCCC);
		commentsHolder.addView(line, new LayoutParams(LayoutParams.MATCH_PARENT, 1));
	}
}
