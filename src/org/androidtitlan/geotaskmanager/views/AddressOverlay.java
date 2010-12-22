package org.androidtitlan.geotaskmanager.views;

import android.graphics.Canvas;
import android.location.Address;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class AddressOverlay extends Overlay {

	
	private Address address;
	private GeoPoint geopoint;
	
	public AddressOverlay(Address address) {
		super();
		assert(null != address);
		this.setAddress(address);
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Address getAddress() {
		return address;
	}

	public void setGeopoint(GeoPoint geopoint) {
		this.geopoint = geopoint;
	}

	public GeoPoint getGeopoint() {
		return geopoint;
	}

}
