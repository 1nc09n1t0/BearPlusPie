package com.bearpluspie.letsdoit;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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
			Marker Tucson = map.addMarker(new MarkerOptions()
			.position(TUCSON)
			.title("Tucson")
			.snippet("Tucson is lush"));
			Marker Phoenix = map.addMarker(new MarkerOptions()
			.position(PHOENIX)
			.title("Phoenix title")
			.snippet("Phoenix has lots of asphalt")
			.icon(BitmapDescriptorFactory
					.fromResource(R.drawable.ic_launcher)));
			
			//including some animation
			//Move the camera instantly to Tucson with a zoom of 15.
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(TUCSON, 15));

			// Zoom in, animating the camera.
			map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mainmenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}
	
	
}
