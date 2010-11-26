package org.androidtitlan.geotaskmanager.tasks;

import android.location.Address;

public class Task {
	
	private String name;
	private boolean complete;
	private long id;
	private String address;
	private double latitude;
	private double longitude;
	
	
	 
	public void setId(long id) {
		this.id = id;
	}
	
	public long getId() {
		return id;
	}

	public Task(String taskName){
		name = taskName;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public String toString(){
		return name;
	}

	public boolean isComplete() {
		return complete;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public void setAdress(Address a) {
		if (null == a) {
			//if address is null, then this address is null also, and lat long is 0.
			address = null;
			latitude = longitude = 0;
		} else {
			//formatting all the address string to just one String
			int maxAddressLine = a.getMaxAddressLineIndex();
			StringBuffer sb = new StringBuffer("");
			for (int i=0; i<maxAddressLine; i++) {
				sb.append(a.getAddressLine(i) + "");
			}
			address = sb.toString();
			latitude = a.getLatitude();
			longitude = a.getLongitude();
			
		}
	}
	public boolean hasAddress() {
		return null != address;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public boolean hasLocation() {
		return (latitude != 0 && longitude != 0);
	} 

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public void toggleComplete() {
		complete = !complete;		
	}



}
