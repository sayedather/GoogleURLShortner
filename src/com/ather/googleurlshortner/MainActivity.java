package com.ather.googleurlshortner;

import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;

import com.ather.googleurlshortner.URLShortener;

public class MainActivity extends Activity {
Button Submit;
EditText longurl;
TextView shortUrl;
static String address="https://www.googleapis.com/urlshortener/v1/url";
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Submit = (Button)findViewById(R.id.submit);
    longurl = (EditText)findViewById(R.id.LongUrl);
    shortUrl = (TextView)findViewById(R.id.ShortUrl);
    Submit.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        // TODO Auto-generated method stub
        new URLShort().execute();
      }
    });
  }
   private class URLShort extends AsyncTask<String, String, JSONObject> {
       private ProgressDialog pDialog;
       String longUrl;
      @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Contacting Google Servers ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            longUrl = longurl.getText().toString();
            pDialog.show();
      }
      @Override
        protected JSONObject doInBackground(String... args) {
        URLShortener jParser = new URLShortener();
        JSONObject json = jParser.getJSONFromUrl(address,longUrl);
        return json;
      }
      String ShortUrl;
       @Override
         protected void onPostExecute(JSONObject json) {
         pDialog.dismiss();
         try {
            if (json != null){
            ShortUrl = json.getString("id");
            shortUrl.setText(ShortUrl);
            pDialog.dismiss();
            }else{
              shortUrl.setText("Network Error");
              pDialog.dismiss();
            }
        } catch (JSONException e) {
          e.printStackTrace();
        }
       }
    }
   
   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
       // Inflate the menu; this adds items to the action bar if it is present.
       getMenuInflater().inflate(R.menu.main, menu);
       return true;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
       // Handle action bar item clicks here. The action bar will
       // automatically handle clicks on the Home/Up button, so long
       // as you specify a parent activity in AndroidManifest.xml.
       int id = item.getItemId();
      
       if (id == R.id.action_about) {
    	   android.app.AlertDialog.Builder alert = new AlertDialog.Builder(this);
           alert.setTitle("Google URL Shortner");
           alert.setMessage("Google URL Shortner by Ather\n\nThis App uses the publicly available Goo.gl API\n\n\u00A9 2014, Ather Akber");
           alert.setIcon(R.drawable.ic_action_about);
           alert.setPositiveButton("Close", null);
           alert.show();
       }
       if (id == R.id.exit) {
       	
       	finish();
           System.exit(0);
           return true;
       }
       if (id == R.id.share) {
       	Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND); 
           sharingIntent.setType("text/plain");
           String shareBody = "Take a look at \"Google URL Shortner by Ather\" - https://play.google.com/store/apps/details?id=com.ather.googleurlshortner";
     //    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Hey");
           sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
       startActivity(Intent.createChooser(sharingIntent, "Share via"));
           return true;
       }
       
       return super.onOptionsItemSelected(item);
   }
       }