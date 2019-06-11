package com.gyro.webapp;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

public class LocationMgr implements LocationListener{
	private LocationManager lManager;
	private Location loc;
	private Context mContext;
	
	public LocationMgr(Context c){
		mContext = c;
		lManager = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);
	   	lManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, this);
	}
	public Location getLocation(){
		return loc;
	}
	
	public void onLocationChanged(android.location.Location location) {
		// TODO Auto-generated method stub
		loc = location;
	}

	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		Toast.makeText(mContext, provider, Toast.LENGTH_LONG).show();
	}

	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
}