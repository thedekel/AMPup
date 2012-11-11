package com.melonbear.ampup;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

public class LoginTask extends AsyncTask<Object, Integer, Void> {
  private static final String ROUTE = "/sessions";

  private Context mContext;
  private String mUsername;
  private SharedPreferences mPrefs;

  @Override
  protected Void doInBackground(Object... params) {
    mContext = (Context) params[0];
    mUsername = (String) params[1];
    mPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);

    HttpClient client = new DefaultHttpClient();
    HttpPost post = new HttpPost(String.format("http://192.168.43.85:3000%s",
        ROUTE));
    List<NameValuePair> nvp = new ArrayList<NameValuePair>();
    nvp.add(new BasicNameValuePair("user", mUsername));
    try {
      post.setEntity(new UrlEncodedFormEntity(nvp));
      HttpResponse res = client.execute(post);
      StatusLine statusLine = res.getStatusLine();
      int status = statusLine.getStatusCode();
      if (status == HttpStatus.SC_OK) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        res.getEntity().writeTo(out);
        out.close(); String response = out.toString();
        JSONObject dekel = new JSONObject(response);
        if (dekel.has("sid")) {
          if (!mPrefs.contains("session")) {
            Editor editor = mPrefs.edit();
            editor.putString("user_name", mUsername);
            editor.putString("session", dekel.getString("sid"));
            editor.commit();
          }
          if (!mPrefs.contains("user_name")) {
            throw new RuntimeException("This should never happen");
          }
        }
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
    return null;
  }

  @Override
  protected void onPostExecute(Void v) {
    if (mPrefs.contains("session")) {
      Intent i = new Intent(mContext, AMPActivity.class);
      i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      mContext.startActivity(i);
    }
  }
}
