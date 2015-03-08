package com.bearpluspie.letsdoit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ShowMapActivity extends Activity implements OnMyLocationChangeListener{
	private GoogleMap map;
	static final LatLng TUCSON = new LatLng(32.221743, -110.926479);
	static final LatLng PHOENIX = new LatLng(33.448377, -112.074037);
	private Circle myCircle;
	private DataHelper db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_map_layout);
		db = new DataHelper(this);
		
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		
		if(map != null){
			map.setMyLocationEnabled(true);
			map.setOnMyLocationChangeListener(this);
			map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
			map.setOnMyLocationChangeListener(this);
			
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
			//Move the camera instantly to AZ
			double lat1 = 0f;
			double long1 = 0f;
			Geocoder coder = new Geocoder(getApplicationContext());
			List<Address> geocodeResults;
			try {
				geocodeResults = coder.getFromLocationName("Arizona", 1);
				Iterator<Address> locations = geocodeResults.iterator();
				while (locations.hasNext()) {
	    			Address loc = locations.next();
	    			lat1 = loc.getLatitude();
	    			long1 = loc.getLongitude();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat1, long1), 0));

			// Zoom in, animating the camera.
//			map.animateCamera(CameraUpdateFactory.zoomTo(10), 200, null);
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
		else if(id == R.id.updateDB){
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
			
			db.getOnlineDB(web_get);
			
			Toast toast = Toast.makeText(getApplicationContext(), "Hell Cavern: \n " + db.get_GPS("Hell Cavern")+ "\n", Toast.LENGTH_LONG);
            toast.show();
		}

		private void downloadString() {
			try {
            	URL text = new URL("http://52.10.206.133/maps.txt");
      
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(text.openStream()));

                        String inputLine;
                        web_get = "";
                        while ((inputLine = in.readLine()) != null)
                            web_get += inputLine + "\n";
                        in.close();
                        
            } catch (Exception e) {
            	web_get = "Error in network call";
                Log.e("Net", "Error in network call", e);
            }
		}
	}

	@Override
	public void onMyLocationChange(Location location) {
		LatLng locLatLng = new LatLng(location.getLatitude(), location.getLongitude());
		double accuracy = location.getAccuracy();

		if (myCircle == null) {
			CircleOptions circleOptions = new CircleOptions().center(locLatLng)
					// set center
					.radius(accuracy)
					// set radius in meters
					.strokeColor(Color.BLACK)
					.strokeWidth(5);

			myCircle = map.addCircle(circleOptions);
		} else {
			myCircle.setCenter(locLatLng);
			myCircle.setRadius(accuracy);
		}
		map.animateCamera(CameraUpdateFactory.newLatLng(locLatLng));
	}
	
}
