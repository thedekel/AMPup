package com.melonbear.ampup;

public class Lesson {
	public String title, subtitle, description;
	
	public Lesson(String mTitle, String mSubtitle, String mDescription) {
		this.title = mTitle;
		this.subtitle = mSubtitle;
		this.description = mDescription;
	}



	public String toString() {
		return title;
	}
}