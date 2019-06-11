package com.gyro.webapp;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.util.Base64;
import android.util.Log;
import android.webkit.WebView;
import android.widget.DatePicker;
import android.widget.Toast;

public class JavaScriptInterface{
	public static final int ID_NOTIFICATION = 1988;
	private LocationMgr locmgr;
	private Context mContext;
	private WebView www;

    /** Instantiate the interface and set the context */
    JavaScriptInterface(Context c, WebView w) {
        mContext = c;
        www = w;
        locmgr = new LocationMgr(c);
    }

    /** Show a toast from the web page */
    public void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_LONG).show();
    }
    
    public void Log(String msg){
    	Log.e("Android JavaScript", msg);
    }

    /** get image in ressources of app in base64 for web page*/
    public String getRImages(int id){
    	JSONObject object = new JSONObject();
		
        Bitmap bm = BitmapFactory.decodeResource(mContext.getResources(), id);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object   
        byte[] b = baos.toByteArray();
        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        
        try {
			object.put(String.valueOf(id),encodedImage);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return object.toString();
    }
    
    /** get user position*/
    public String getGeoLocation(){
    	JSONObject object = new JSONObject();
    	try {
    		Location loc = locmgr.getLocation();
			object.put("latitude", loc.getLatitude());
			object.put("longitude", loc.getLongitude());
	    	object.put("altitude", loc.getAltitude());
	    	object.put("HasAltitude", loc.hasAltitude());
	    	object.put("speed", loc.getSpeed());
	    	object.put("HasSpeed", loc.hasSpeed());
	    	object.put("accuracy", loc.getAccuracy());
	    	object.put("HasAccuracy", loc.hasAccuracy());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return object.toString();
    }
    
    /**get DatePicker*/
    public void selectDate(final String callback){
    	final Calendar c = Calendar.getInstance();
		DatePickerDialog p  = new DatePickerDialog(mContext,0, new OnDateSetListener(){

			public void onDateSet(DatePicker dp, int year, int monthofyear, int dayofmonth) {
				// TODO Auto-generated method stub
				www.loadUrl("javascript:"+callback+"("+dp.getYear()+","+dp.getMonth()+","+dp.getDayOfMonth()+")");
			}
			
		}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
		p.show();
    }
    
    @TargetApi(16)
	public void createNotify(String text){
    	String ns = Context.NOTIFICATION_SERVICE;
    	NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(ns);      
    	Builder notification = new Notification.Builder(mContext);
        notification.setContentTitle("GyroApp");
        notification.setContentText(text);
        notification.setSmallIcon(R.drawable.ic_launcher);
        Notification nt = notification.build();
        Intent suite = new Intent(mContext, MainActivity.class);
        suite.putExtra("EXTRA_URL", "http://gyro.o2switch.net/dev/gyro.html");
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, suite, 0);
        String titreNotification = "GyroApp !";
        nt.setLatestEventInfo(mContext, titreNotification, text, pendingIntent);
        nt.vibrate = new long[] {0,200,100,200,100,200};
        mNotificationManager.notify(ID_NOTIFICATION, nt);
    }
}
