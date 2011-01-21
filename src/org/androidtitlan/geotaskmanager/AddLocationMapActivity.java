package org.androidtitlan.geotaskmanager;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
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
	
	public MapView mapView;
	private Address address;
	private MyLocationOverlay myLocationOverlay;
	OverlayManager overlayManager;


	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.add_location);

		setUpViews();
		isOnline();
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
	}

	private void setUpViews() {
		mapView = (MapView)findViewById(R.id.map);
		myLocationOverlay = new MyLocationOverlay(this, mapView);
		mapView.getOverlays().add(myLocationOverlay);
		mapView.invalidate();
		//mapView.setBuiltInZoomControls(true);
		mapView.setSatellite(true);
		mapView.setStreetView(true);
		
		overlayManager = new OverlayManager(getApplication(), mapView);
		createOverlayWithListener();
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
	
	//method to detect if device is connected by any means
	public boolean isOnline() {		
		ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

		// are we connected to the net(wifi or phone)
		if ( cm.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED ||
				//cm.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING ||
				//cm.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING ||
				cm.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED ) {
	    			Log.e("Testing Internet Connection", "We have internet");
			return true; 

		} else if (cm.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED 
				||  cm.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED){
			showNoInternetConnectionDialog();
			Log.e("Testing Internet Connection", "We dont have internet");
			return false; 
		}
		return false;
		
	}
	//Showing the No internet connection Custom Dialog =)
	public void showNoInternetConnectionDialog(){
		Log.e("Testing Internet Connection", "Entering showNoInternetConnectionDialog Method");
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setMessage("Whoops! Its seems you don't have internet connection, please try again later!")
	            .setTitle("No Internet Connection")
	    		.setCancelable(false)
	           .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
	               public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
	            	   finish();
	               }
	           });
	    final AlertDialog alert = builder.create();
	    alert.show();
		Log.e("Testing Internet Connection", "Showed NoIntenetConnectionDialog");

	
	}
	
}