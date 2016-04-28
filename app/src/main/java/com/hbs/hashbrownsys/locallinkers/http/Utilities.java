package com.hbs.hashbrownsys.locallinkers.http;

import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.ContactsContract;
import android.util.Log;

import com.hbs.hashbrownsys.locallinkers.listener.PhoneContactFetchListener;
import com.hbs.hashbrownsys.locallinkers.model.ContactModel;

import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class Utilities
{
    public static  int counter = 0 ;
	public static final boolean DEBUG_E = true;
	public static final boolean DEBUG_I = true;
	public static final boolean DEBUG_D = true;
	
	public final static String tag = "Utilities";

	public static void printE(String tag, String msg) {
		if (DEBUG_E) {
			Log.e(tag, msg);
		}
	}
	
	public static void printI(String tag, String msg)
    {
		if (DEBUG_I) {
			Log.i(tag, msg);
		}
	}
	
	public static void printD(String tag, String msg) {
		if (DEBUG_D) {
			Log.d(tag, msg);
		}
	}
	
	public static Boolean isNetWorking(Context context) {
		  try {
			   ConnectivityManager ConMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			   if (ConMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED
						 || ConMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED)
				   return true;
			   else 
			       return false;
		      }catch (Exception e) {
				 e.printStackTrace();
				 return false;  
			  }
    }
	
	  public static String getJSONStringValue(JSONObject LObject, String name,
				String defaultValue) {
			try {
				return LObject.getString(name);
			} catch (Exception e) {
				return defaultValue;
			}
	  }

	  public static int getJSONIntValue(JSONObject LObject, String name, int defaultValue) {
			try {
				return LObject.getInt(name);
			} catch (Exception e) {
				return defaultValue;
			}
	  }
	  
	  public static boolean getJSONBooleanValue(JSONObject LObject, String name, boolean defaultValue) {
			try {
				return LObject.getBoolean(name);
			} catch (Exception e) {
				return defaultValue;
			}
	  }
	  
	  public static double[] getGPSLocation(Context context)
      {
	        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
	        
	        // This fetches a list of available location providers
	        // This should return an array like this, depending on what is available:
	        // ['network', 'gps']
	        // The last one will always be the most accurate, so you can then do
	        List<String> providers = lm.getProviders(true);

	        /* Loop over the array backwards, and if you get an accurate location, then break out the loop*/
	        Location l = null;
	        
	        for (int i=providers.size()-1; i>=0; i--)
            {
	                l = lm.getLastKnownLocation(providers.get(i));
	                if (l != null) break;
	        }
	        
	        double[] gps = new double[2];
	        if (l != null) {
	                gps[0] = l.getLatitude();
	                gps[1] = l.getLongitude();
                Log.e("qqqqqqqqqq" + gps[1], "" + gps[1]);
	        }
          Log.e("qqqqqqqqqq" + gps[1], "" + gps[1]);
	    return gps;
	}
	  
	public static ArrayList<ContactModel> getContactsList(Context context, PhoneContactFetchListener listener){
		    // http://ganeshtiwaridotcomdotnp.blogspot.in/2011/10/android-code-for-reading-phone-contacts.html
			ArrayList<ContactModel> arrayList = new ArrayList<ContactModel>();
			Cursor cur = null;
			Cursor pCur = null;

		    try{
		    	// Cursor of Contacts
			    cur = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
                Log.e("", "cur.getCount()" + cur.getCount());
			    if (cur.getCount() > 0) {
			    	
			    	while (cur.moveToNext()) {
			    		
			    	        // read id
			    	        String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
			    	        /** read names **/
			    	        String displayName = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			    	        
			    	        
			    	            /** Phone Numbers **/
			    	            pCur = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
			    	                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[] { id }, null);
                                Log.e("", "pCur.getCount()" + pCur.getCount());
			    	            while (pCur.moveToNext()) {
			    	                String number = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			    	               
			    	                // Only take those contacts if they have phone number, otherwise reject them
							        ContactModel contactModel = new ContactModel();
							        contactModel.setContactNumber( number);
							        contactModel.setContactName(displayName);
							        arrayList.add(contactModel);
//                                    listener.getPhoneContactsResponce(cur.getCount(),++counter);
//                                    Log.e(++counter+"/----"+cur.getCount(),"/"+pCur.getCount());
							        Log.d("", "Contact Name " + displayName + "  Phone No " + number);
			    	            }
//                        Log.e(++counter+"/----"+cur.getCount(),"/"+pCur.getCount());
                        listener.getPhoneContactsResponce(cur.getCount(),++counter);
			    	            pCur.close();
			    	}
			    }
		    }catch (Exception e) {
			   e.printStackTrace();
		    } 
		    finally{
		    	try{
		    		if( cur != null && !cur.isClosed())
		    		cur.close();
		    		
		    		if( pCur != null && !pCur.isClosed())
			    	pCur.close();
		    	}
		    	catch (Exception e) {
					e.printStackTrace();
				}
		    }
		   return arrayList;
  }
    /*
    method will return number after removing white spaces and adding +91 to numbers
    not having this and remove 0 before the mobile numbers and also remove - hyphens
     */
    public static String getRefinedNumber(String rawNumber){
        String refined_number = "";
//        Log.e("Romoved Spaces",""+rawNumber.replaceAll("\\s",""));
//        Log.e("Romoved Hyphens",""+rawNumber.replaceAll("\\s",""));
        return null;
    }
    public static String getUTCTime(){
        String time = null;
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss  mmm");
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));

//Local time zone
        SimpleDateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss mmm");
        try {
            time = dateFormatLocal.parse(dateFormatGmt.format(new Date())) + "";
            Log.e("", "time" + time);
        }catch (Exception e){
            e.printStackTrace();
        }
//Time in GMT
        return time;

    }
    public static String generateLocalMessageId(){
        long time =00000000000000;
        try {
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:mm");//dd/MM/yyyy
            Date now = new Date();
            String strDate = sdfDate.format(now);
            Log.e("", "strDate" + strDate);
            Date TimeinMilli = sdfDate.parse(strDate);

            Log.e("", "cal.getTimeInMillis()" + TimeinMilli.getTime());
            time = TimeinMilli.getTime();
        }catch(Exception e){
            e.printStackTrace();
        }
        return  time+"";
    }
}