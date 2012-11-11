package com.melonbear.ampup;

public class Response {
  public String mUserName = "unknown-user";
  public String mImagePath = "";
  public String mAudioPath = "";
  public String mqID = "";
  public String mID = "";
  
  public Response(String user, String ipath, String apath, String id, String qid) {
    mUserName = user;
    mImagePath = ipath;
    mAudioPath = apath;
    mqID = qid;
    mID = id;
  }

  public String toString() {
    return "response by " + mUserName;
  }
}
