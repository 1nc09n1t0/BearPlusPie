package com.bearpluspie.letsdoit;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ShowMapActivity extends Activity{
	private GoogleMap map;
	static final LatLng TUCSON = new LatLng(32.221743, -110.926479);
	static final LatLng PHOENIX = new LatLng(33.448377, -112.074037);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_map_layout);
		
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		
		if(map != null){
			/*Marker Tucson = */map.addMarker(new MarkerOptions()
			.position(TUCSON)
			.title("Tucson")
			.snippet("Tucson is lush"));
			/*Marker Phoenix = */map.addMarker(new MarkerOptions()
			.position(PHOENIX)
			.title("Phoenix title")
			.snippet("Phoenix has lots of asphalt")
			/*.icon(BitmapDescriptorFactory
					.fromResource(R.drawable.ic_launcher))*/);
			
			//including some animation
			//Move the camera instantly to Tucson with a zoom of 15.
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(TUCSON, 15));

			// Zoom in, animating the camera.
			map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
		}
		/*
		 * for all rows in the db,
		 * 		map.addMarker(new MarkerOptions()
		 * 		.position(new LatLng(coordinate1, coordinate2))
		 * 		.title(name)
		 * 		.snippet(description);
		 */
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.map_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		int id = item.getItemId();
		if(id == R.id.backToMain)
			startActivity(new Intent(getApplicationContext(), MainActivity.class));
		else if(id == R.id.showList)
			startActivity(new Intent(getApplicationContext(), AllHikesList.class));
		else if(id == R.id.getTest){
	       	 new TextDownloader().execute("http://52.10.206.133/maps.txt");
		}
		
		return true;
	}
	
	private class TextDownloader extends AsyncTask <String, Void, Bitmap>{		
		
		String web_get = "undefined";
		
		@Override
		protected Bitmap doInBackground(String... params) {
			downloadString();
			return null;
		}

		@Override
		protected void onPreExecute() {
			Log.i("Async-Example", "onPreExecute Called");
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			Log.i("Async-Example", "onPostExecute Called");
			Toast toast = Toast.makeText(getApplicationContext(), web_get, Toast.LENGTH_LONG);
            toast.show();
		}

		private void downloadString() {
			try {
            	URL text = new URL("http://52.10.206.133/maps.txt");
//                HttpURLConnection http= (HttpURLConnection) text.openConnection();
//                
                             
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(text.openStream()));

                        String inputLine;
                        
                        web_get = "";
                        
                        while ((inputLine = in.readLine()) != null)
                            web_get += inputLine;
                        in.close();
               
//                InputStream IS = http.getInputStream();
//                StringBuffer SB = new StringBuffer();
//                int data;
//                
//                while((data = IS.read()) != -1) {
//                     SB.append((char) data);
//                }
// 
//                IS.close();
//                http.disconnect();
                
            } catch (Exception e) {
            	web_get = "Error in network call";
                Log.e("Net", "Error in network call", e);
            }
			
		}
	}
	
	
}
