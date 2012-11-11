package com.melonbear.ampup;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Lesson {
  public String mName;
  public String mSubtitle = "no description";
  public String mImagePath = "";
  public String mAudioPath = "";
  public String mID = "";
  public ArrayList<Response> mResponses;
  
  public Lesson(String name) {
    mName = name;
  }

  public String toString() {
    return mName;
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