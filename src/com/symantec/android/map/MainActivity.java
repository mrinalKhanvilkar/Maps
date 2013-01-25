package com.symantec.android.map;

import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;

public class MainActivity extends MapActivity {
	private MapView mapView;
	private MyLocationOverlay myLocation;
	private MapController mapController;
	private LocationManager locationManager;
	private HelloItemizedOverlay itemizedoverlay;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		mapController = mapView.getController();
		List<Overlay> mapOverlays = mapView.getOverlays();
		Drawable drawable = this.getResources().getDrawable(
				R.drawable.androidmarker);
		itemizedoverlay = new HelloItemizedOverlay(drawable, this);
		GeoPoint point = new GeoPoint(19240000, -99120000);
		OverlayItem overlayitem = new OverlayItem(point, "Hola, Mundo!",
				"I'm in Mexico City!");

		GeoPoint point2 = new GeoPoint(35410000, 139460000);
		OverlayItem overlayitem2 = new OverlayItem(point2, "Sekai, konichiwa!",
				"I'm in Japan!");

		
		myLocation = new MyLocationOverlay(this, mapView);
		itemizedoverlay.addOverlay(overlayitem);
		 itemizedoverlay.addOverlay(overlayitem2);
		 
		// myLocation.onTap(myLocation.getMyLocation(), mapView);

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, new GeoUpdateHandler());

		mapOverlays.add(myLocation);
		//mapOverlays.add(itemizedoverlay);
		myLocation.runOnFirstFix(new Runnable() {
			public void run() {
				mapView.getController().animateTo(myLocation.getMyLocation());
			}
		});
		//createMarker();
		// mapOverlays.add(myLocation);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	protected void onResume() {
		super.onResume();// This line has to stay
		// Enable my location
		myLocation.enableMyLocation();
		myLocation.enableCompass();
		
		OverlayItem overlayitem3 = new OverlayItem(myLocation.getMyLocation(), "My Location",
				"I'm in Japan!");
		//itemizedoverlay.addOverlay(overlayitem3);
	}

	@Override
	protected void onPause() {
		super.onPause();// This line has to stay
		// Enable my location
		myLocation.disableMyLocation();
		myLocation.disableCompass();
	}

	public class GeoUpdateHandler implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			int lat = (int) (location.getLatitude() * 1E6);
			int lng = (int) (location.getLongitude() * 1E6);
			GeoPoint point = new GeoPoint(lat, lng);
			createMarker();
			mapController.animateTo(point); 
			mapController.setCenter(point);
			mapController.setZoom(14);

		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

	}

	private void createMarker() {
		GeoPoint p = mapView.getMapCenter();
		OverlayItem overlayitem = new OverlayItem(p, "", "");
		itemizedoverlay.addOverlay(overlayitem);
		if (itemizedoverlay.size() > 0) {
			mapView.getOverlays().add(itemizedoverlay);
		}
	}

}
