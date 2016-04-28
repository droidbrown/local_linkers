package com.hbs.hashbrownsys.locallinkers.http;

import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;

public class CommonPostRequestThread extends Thread
{
	
	String url = null;
	String json = null;
	IHttpResponseListener responseListener;
	IHttpExceptionListener exceptionListener;
//	String UserId;
//    String Token;
//    String Type;
	HttpClient client = null;
	
	public final String tag = this.getClass().getSimpleName();

	public CommonPostRequestThread(String url, String json, IHttpResponseListener responseListener, IHttpExceptionListener exceptionListener)
    {
		this.url = url;
		this.json = json;
		this.responseListener = responseListener;
		this.exceptionListener = exceptionListener;
//        this.UserId = UserId;
//        this.Token = Token;
//        this.Type = Type;
	}
	
	@Override
	public void run()
    {
		String data = null;
		try {
			   client = new DefaultHttpClient();

			   // Setup of the post call to the server
			   HttpPost post = new HttpPost(url);
			   post.setHeader("Accept", "application/json");
			   post.setHeader("Content-type", "application/json");
//               post.setHeader("TOKEN",Token);
//               post.setHeader("TOKENTYPE",Type);
//               post.setHeader("UserId",UserId);

                Log.e("", "json" + json);

			  MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

			// HttpPost only accepts arguments as string, so we turn the json into a string.
			       StringEntity se = new StringEntity(json.toString(), HTTP.UTF_8);
			       se.setContentType("application/json");
			       post.setEntity(se);
			       
			       // Here we'll receive the response.
				   HttpResponse response;
				   response = client.execute(post);
				   int code = response.getStatusLine().getStatusCode();
				   Utilities.printE(tag, " Response Code = " + code);
				   
				   switch(code){
					 case 200:
						// Success
						HttpEntity entity = response.getEntity();
						if (entity != null)
                        {
							data = EntityUtils.toString(entity);
							
							Utilities.printE(tag," Data = "+data);
							
							 if( responseListener !=null)
							   responseListener.handleResponse(data);
						}
					  break;
						
					  case 405:
							// Failure
							HttpEntity entityFailure = response.getEntity();
							if (entityFailure != null)
                            {
								data = EntityUtils.toString(entityFailure);
								
								Utilities.printE(tag," Data = "+data);
								
								 if( responseListener !=null)
								   responseListener.handleResponse(data);
							}
					  break;
				   }
		} catch (UnknownHostException e) {
			e.printStackTrace();
			
			if(exceptionListener!=null)
			exceptionListener.handleException("Error!\nServer might be down.\nPlease retry after some time.");
		}catch (FileNotFoundException e) {
			e.printStackTrace();
			
			if(exceptionListener!=null)
			exceptionListener.handleException("Error!\nServer might be down.\nPlease retry after some time.");
		} catch (ConnectTimeoutException e) {
			e.printStackTrace();
			
			if(exceptionListener!=null)
			exceptionListener.handleException("Error!\nConnection Timeout!.\nPlease check you internet connectivity or retry after some time.");
		}  catch(IOException e){
			e.printStackTrace();
				
			if(exceptionListener!=null)
			exceptionListener.handleException(e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			if(exceptionListener!=null)
			exceptionListener.handleException("Error!\nPlease Retry.");
		}
		finally{
			client.getConnectionManager().closeExpiredConnections();
		}
	}
}