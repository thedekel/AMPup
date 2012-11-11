package com.melonbear.ampup;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

public class ServerTask extends AsyncTask<Object, Integer, List<Lesson>> {
  private Context mContext;
  private ListView mList;
  private String mRoute;

  @Override
  protected List<Lesson> doInBackground(Object... params) {
    mContext = (Context) params[0];
    mList = (ListView) params[1];
    mRoute = (String) params[2];

    List<Lesson> result = null;
    HttpClient client = new DefaultHttpClient();
    try {
      HttpResponse res = client.execute(new HttpGet(String.format(
          "http://192.168.43.85:3000/%s", mRoute)));
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
      LessonAdapter adapter = new LessonAdapter(mContext, lessons);
      mList.setAdapter(adapter);
    }
  }

  private List<Lesson> jsonToList(JSONObject dekel) throws JSONException {
    Log.i("Response", dekel.toString());
    JSONArray resultArray = dekel.has("result_array") ? dekel
        .getJSONArray("result_array") : null;
    List<Lesson> result = new ArrayList<Lesson>();
    for (int i = 0; i < resultArray.length(); i++) {
      JSONObject obj = (JSONObject) resultArray.get(i);
      Log.i("JSON OBJECT", obj.toString());
      if (obj.has("title")) {
        String title = (String) obj.get("title");
        Lesson temp = new Lesson(title, null, null);
        temp.subtitle = (String) obj.get("subtitle");
        temp.id = (String) obj.get("_id");
        result.add(temp);
      }
    }
    return result;
  }
}