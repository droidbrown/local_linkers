package com.hbs.hashbrownsys.locallinkers.http;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by ajit kumar on 4/21/2016.
 */
public class HttpConn {

    final String SERVER_ADDRESS="http://www.locallinkers.com/api/";


    public String getMethods(JSONObject object, String url)
            throws JSONException, IOException
    {
        // TODO Auto-generated method stub

        HttpClient httpclient = new DefaultHttpClient();
        HttpGet post = new HttpGet(SERVER_ADDRESS+url);
        Log.v("HTTPGET",SERVER_ADDRESS+url);

        post.setHeader("Content-Type", "application/json");
        post.setHeader("Accept", "application/json");
        String responseBody;

        try{
            HttpResponse response = httpclient.execute(post);
            responseBody = EntityUtils.toString(response.getEntity());
        }
        catch(Exception e)
        {
            Log.e("ERROR",e.toString());
            responseBody=null;
        }

        Log.v("Response","order Update "+responseBody);
        return responseBody;

    }

}
