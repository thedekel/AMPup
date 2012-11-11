package com.melonbear.ampup;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Lesson {
	public String title, subtitle, description, imagePath, audioPath, id;
	public ArrayList<Response> mResponses;
	
	public Lesson(String mTitle, String mSubtitle, String mDescription) {
		this.title = mTitle;
		this.subtitle = mSubtitle;
		this.description = mDescription;
	}

	public String toString() {
		return title;
	}
  
  public void getResponsesFromJson(JSONArray responses){
    JSONObject curr;
    mResponses = new ArrayList<Response>();
    for (int i = 0; i < responses.length(); i ++){
      try {
        curr = responses.getJSONObject(i);
        mResponses.add(new Response(curr.getString("user"), curr.getString("image"), 
            curr.getString("audio"), curr.getString("_id"), curr.getString("question")));
      } catch (JSONException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }
}