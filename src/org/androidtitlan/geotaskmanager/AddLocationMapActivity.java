package org.androidtitlan.geotaskmanager;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.MotionEvent;
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

import de.android1.overlaymanager.ManagedOverlay;
import de.android1.overlaymanager.ManagedOverlayGestureDetector;
import de.android1.overlaymanager.ManagedOverlayItem;
import de.android1.overlaymanager.OverlayManager;
import de.android1.overlaymanager.ZoomEvent;

import org.androidtitlan.geotaskmanager.R;
import org.androidtitlan.geotaskmanager.views.AddressOverlay;


public class AddLocationMapActivity extends MapActivity {
	
	public static final String ADDRESS_RESULT = "address";
	
	private Button mapLocationButton;
	private Button useLocationButton;
	private EditText addressText;
	public MapView mapView;
	private Address address;
	private MyLocationOverlay myLocationOverlay;
	OverlayManager overlayManager;

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
		//mapView.setBuiltInZoomControls(true);
		mapView.setSatellite(true);
		mapView.setStreetView(true);
		
		overlayManager = new OverlayManager(getApplication(), mapView);
		createOverlayWithListener();
		

		
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
		
	
	private void createOverlayWithListener() {
		/*  With this OnOverlayGestureListener you can react on high-level user-interaction.
	 	 * 	If the user makes a tap, double-tap or long-press on the map it will invoke
		 *	the appropriate listener-method and will pass two interesting parameter:
		 *	GeoPoint: the point where the user hit the map.
		 *  ManagedOverlayItem: if the user also hit a marker, otherwise this will be null.
	     */
		   
			
			 final AlertDialog.Builder builder = new AlertDialog.Builder(this);

			 ManagedOverlay managedOverlay = overlayManager.createOverlay("listenerOverlay", getResources().getDrawable(R.drawable.marker));
			//here we call the GeoHelper Class which helps us print our markers
			for (int i = 0; i < 40; i = i + 3) {
				managedOverlay.createItem(GeoHelper.geopoint[i], "Item" + i);
			}
			managedOverlay.setOnOverlayGestureListener(new ManagedOverlayGestureDetector.OnOverlayGestureListener() {


				public boolean onZoom(ZoomEvent zoom, ManagedOverlay overlay) {
					return false;
				}
				
				public boolean onDoubleTap(MotionEvent e, ManagedOverlay overlay, GeoPoint point, ManagedOverlayItem item) 
				{
					//Creating a point that uses the point onDoubleTap
					final GeoPoint point1 = point;
					//Converting to an Human-Readable Address
					List<Address> addresses;
					String add = "";
					Geocoder geoCoder = new Geocoder(
			                   getBaseContext(), Locale.getDefault());
			               try {
			            	       addresses= geoCoder.getFromLocation(
			                       point.getLatitudeE6()  / 1E6, 
			                       point.getLongitudeE6() / 1E6, 1);
			       				   address = addresses.get(0);
			       				   List<Overlay> mapOverlays = mapView.getOverlays();
			       				   AddressOverlay addressOverlay = new AddressOverlay(address);
			       				   mapOverlays.add(addressOverlay);

			       				   


			                   
			                   if (addresses.size() > 0) 
			                   {
			                       for (int i=0; i<addresses.get(0).getMaxAddressLineIndex(); 
			                            i++)
			                          add += addresses.get(0).getAddressLine(i) + "\n";
			                   }
			               }
			               catch (IOException ex) {                
			                   ex.printStackTrace();
			               }  
			               
					
			               

				    builder.setTitle("Is this the Location?")
				    .setMessage("You created a Marker in: \n" + add)
				           .setCancelable(false)
				           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				               public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
				            	   printMarker(point1);
				            	   if (null != address){
				   					Intent intent = new Intent();
				   					intent.putExtra(ADDRESS_RESULT, address);
				   					setResult(RESULT_OK, intent);
				            	   }
				   					finish();
				               }

							private void printMarker(GeoPoint point) {
								 //Print the Human-readable Address of the touch
								
								Drawable defaultmarker = getResources().getDrawable(R.drawable.marker);     

							    ManagedOverlay managedOverlay = overlayManager.createOverlay(defaultmarker);
							   
							    //creating some marker:
							    managedOverlay.createItem(point);
							   
							    //registers the ManagedOverlayer to the MapView
							    overlayManager.populate(); 	
							}
				           })
				           .setNeutralButton("Discard", new DialogInterface.OnClickListener(){
				        	   public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
				            	   finish();
				        	   }
				        	   })
				           .setNegativeButton("No", new DialogInterface.OnClickListener() {
				               public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
				        		   dialog.cancel();

				               }
				           });
				    final AlertDialog alert = builder.create();
				    alert.show();
				
				    
					return true;
				}	

				public void onLongPress(MotionEvent arg0, ManagedOverlay arg1) {
					return;
				}


				public void onLongPressFinished(MotionEvent arg0,
						ManagedOverlay arg1, GeoPoint arg2, ManagedOverlayItem arg3) {
					return;
				}


				public boolean onScrolled(MotionEvent arg0, MotionEvent arg1,
						float arg2, float arg3, ManagedOverlay arg4) {
					return false;
				}


				public boolean onSingleTap(MotionEvent arg0, ManagedOverlay arg1,
						GeoPoint arg2, ManagedOverlayItem arg3) {
					return false;
				}			
			});
			overlayManager.populate();

					
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