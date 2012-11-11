package com.melonbear.ampup;
public class Comment implements Comparable<Comment>{

	public String comment;
	public String key;
	public String user;
	public String avatar;
	public long dateInMilliseconds;

	@Override
	public int compareTo(Comment another) {
		if (dateInMilliseconds - another.dateInMilliseconds > 0)
			return 1;
		else if (dateInMilliseconds - another.dateInMilliseconds < 0)
			return -1;
		else 
			return 0;
	}
}