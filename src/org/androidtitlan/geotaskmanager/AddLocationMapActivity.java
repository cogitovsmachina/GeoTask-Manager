package org.androidtitlan.geotaskmanager;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;

import org.androidtitlan.geotaskmanager.R;
import org.androidtitlan.geotaskmanager.views.AddressOverlay;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddLocationMapActivity extends MapActivity {
	
	public static final String ADDRESS_RESULT = "address";
	
	private Button mapLocationButton;
	private Button useLocationButton;
	private EditText addressText;
	public MapView mapView;
	private Address address;
	private MyLocationOverlay myLocationOverlay;

	private AlertDialog AddressStringMustHaveSomethingDialog;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.add_location);
		setUpViews();
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		myLocationOverlay.enableMyLocation();
		
	}

	@Override
	protected void onPause(){
		super.onPause();
		myLocationOverlay.disableMyLocation();
		finish();


		
		
	}
	
	protected void mapCurrentAddress() {
		String addressString = addressText.getText().toString();
		if(addressString.equals("")){
			showAddressStringMustHaveSomethingDialog();
		}
		else{
		Geocoder g = new Geocoder(this);
		List<Address> addresses;
		try {
			addresses = g.getFromLocationName(addressString, 1);
			if (addresses.size() > 0) {
				address = addresses.get(0);
				List<Overlay> mapOverlays = mapView.getOverlays();
				AddressOverlay addressOverlay = new AddressOverlay(address);
				mapOverlays.add(addressOverlay);
				mapOverlays.add(myLocationOverlay);
				mapView.invalidate();
				final MapController mapController = mapView.getController();
				mapController.animateTo(addressOverlay.getGeopoint(), new Runnable() {
					public void run() {
						mapController.setZoom(18);
					}
				});
				useLocationButton.setEnabled(true);
			} else {
		        Toast.makeText(this, "Problem finding the address, don't use commas (,) and check if you have internet access.", Toast.LENGTH_SHORT).show();
			}
		} catch (IOException e) {
	        Toast.makeText(this, "Problem finding the address, don't use commas (,) and check if you have internet access.", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	//Using this method to tell the user need to add something to the address =)
	private void showAddressStringMustHaveSomethingDialog() {
		AddressStringMustHaveSomethingDialog = new AlertDialog.Builder(this)
		.setTitle(R.string.the_address_must_have_something_title)
		.setMessage(R.string.the_address_must_have_something_message)
		.setNeutralButton(R.string.ok_ill_never_doit_again, new AlertDialog.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				}
		})	
		.create();
		AddressStringMustHaveSomethingDialog.show();			
	}

	private void setUpViews() {
		addressText = (EditText)findViewById(R.id.task_address);
		mapLocationButton = (Button)findViewById(R.id.map_location_button);
		useLocationButton = (Button)findViewById(R.id.use_this_location_button);
		useLocationButton.setEnabled(false);
		mapView = (MapView)findViewById(R.id.map);
		myLocationOverlay = new MyLocationOverlay(this, mapView);
		mapView.getOverlays().add(myLocationOverlay);
		mapView.invalidate();
		mapView.setBuiltInZoomControls(true);
		mapView.setSatellite(true);
		mapView.setStreetView(true);
		

		
		useLocationButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (null != address) {
					Intent intent = new Intent();
					intent.putExtra(ADDRESS_RESULT, address);
					setResult(RESULT_OK, intent);
				}
				
				finish();
			}
		});
		mapLocationButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				mapCurrentAddress();
			}
		});
		
		useLocationButton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				if (null != address){
					Intent intent = new Intent();
					intent.putExtra(ADDRESS_RESULT, address);
					setResult(RESULT_OK, intent);
				}
				finish();
			}
		});
}	
		
	
	@Override
	protected boolean isLocationDisplayed() {
		return true;
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
}