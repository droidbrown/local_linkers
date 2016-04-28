package com.hbs.hashbrownsys.locallinkers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class Web_View_Activity extends AppCompatActivity {
    private WebView webView;
    String postData;
    long seconds;
    String upload_data;
    String total_prices;
    String orderId, user_id,Role_id,reedem_points;
    String url;
    Map<String, String> extraHeaders = new HashMap<String, String>();
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web__view_);
        webView = (WebView) findViewById(R.id.webView1);

        webView.getSettings().setJavaScriptEnabled(true);

        Calendar c = Calendar.getInstance();
        seconds = c.getTimeInMillis() / 1000;
        Intent intent = getIntent();
        upload_data = intent.getStringExtra("upload_data");
        total_prices = intent.getStringExtra("total_prices");
        orderId = intent.getStringExtra("orderId");
        user_id = intent.getStringExtra("user_id");
        Role_id=intent.getStringExtra("role_id");
        reedem_points=intent.getStringExtra("redeem_point");

        // total_prices="0";
        if (total_prices.equalsIgnoreCase("0"))
        {
            // TODO: apply query for amount 0
            url = "http://locallinkers.com/ccavResponseHandler.aspx?";
            postData = "WithoutPayment=" + orderId + "&ReedemPoints="+reedem_points + "&UserId=" + user_id + "&ProductIds=" + upload_data + "&RoleId="+Role_id;
            Log.v("postData",".........."+postData);
        } else {
            // TODO: apply query for amount greater than 0
            url = "http://locallinkers.com/ccavRequestHandler.aspx";
            postData = "tid=" + seconds + "&merchant_id=89789" + "&order_id=" + orderId + "&redirect_url=http://locallinkers.com/ccavResponseHandler.aspx"
                    + "&cancel_url=http://locallinkers.com/Cancel" + "&amount=" + total_prices + "&currency=INR" + "&merchant_param2=" + upload_data +
                    "&merchant_param3=" + user_id + "&merchant_param4="+reedem_points;
        }

        // Calling async task to get display content
        new RenderView().execute();

    }


    /**
     * Async task class to get json by making HTTP call
     */
    private class RenderView extends AsyncTask<Void, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (!Web_View_Activity.this.isFinishing())
                if (dialog != null)
                    if (dialog.isShowing())
                        dialog.dismiss();
            dialog = new ProgressDialog(Web_View_Activity.this);
            dialog.setMessage("Please wait...");
            dialog.show();


        }

        @Override
        protected String doInBackground(Void... params) {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = null;
            if (!total_prices.equals("0")) {
                httpPost = new HttpPost(url);
            } else {
                url = "http://locallinkers.com/ccavResponseHandler.aspx?" +
                        "WithoutPayment=" + orderId + "&ReedemPoints="+reedem_points + "&UserId=" + user_id
                        + "&ProductIds="+upload_data+"&RoleId="+Role_id;
                httpPost = new HttpPost(url);
            }

            httpPost.setHeader("Referer", url); // example of adding extra header referer
            httpPost.setHeader("content-type", "application/x-www-form-urlencoded");


            // adding post params
            if (postData != null) {
                try {
//                    httpPost.setEntity(new UrlEncodedFormEntity(postParameters, "utf-8"));
                    if (!total_prices.equals("0"))
                        httpPost.setEntity(new ByteArrayEntity(
                                postData.getBytes("UTF-8")));
                    else {
                        url = "http://locallinkers.com/ccavResponseHandler.aspx?" +
                                "WithoutPayment=" + orderId + "&ReedemPoints="+reedem_points + "&UserId=" + user_id
                                + "&ProductIds="+upload_data+"&RoleId="+Role_id;
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace(); //
                }
            }

            Log.d("postData", ".........postData....." + postData);

            String content = "";

            try {
                HttpResponse response = httpclient.execute(httpPost);
                // Get the response content
                String line = "";
                StringBuilder contentBuilder = new StringBuilder();
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                while ((line = rd.readLine()) != null) {
                    contentBuilder.append(line);
                }
                content = contentBuilder.toString();
            } catch (Exception e) {
                Log.e("ERROR", e.toString());
            }

            return content;

        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            if (!Web_View_Activity.this.isFinishing())
                if (dialog != null)
                    if (dialog.isShowing())
                        dialog.dismiss();

            @SuppressWarnings("unused")
            class MyJavaScriptInterface {
                @JavascriptInterface
                public void processHTML(String html) {
                    // process the html as needed by the app
                    String status = null;
                    if (html.indexOf("ThankYou") != -1) {
                        status = "ThankYou";
                    } else if (html.indexOf("Cancel") != -1) {
                        status = "Cancel";
                    } else if (html.indexOf("Aborted") != -1) {
                        status = "Cancel";
                    } else {
                        status = "Cancel";
                    }
                    //Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    setResult(100, intent.putExtra("payment_status", status));
                    finish();
                }
            }

            webView.loadDataWithBaseURL(url, response, "text/html", "UTF-8", null); // important!! is to fill base url

            webView.setWebViewClient(new WebViewClient() {

                public void onPageFinished(WebView view, String url) {
                    // do your stuff here
                    if (url.contains("ThankYou")) {
                        //Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        setResult(Activity.RESULT_OK, intent.putExtra("payment_status", "ThankYou"));
                        finish();
                    } else if (url.contains("Cancel")) {
                        Intent intent = new Intent();
                        setResult(Activity.RESULT_OK, intent.putExtra("payment_status", "Cancel"));
                        finish();
                    }else if(url.contains("WithoutPayment")){
                        Intent intent = new Intent();
                        setResult(Activity.RESULT_OK, intent.putExtra("payment_status", "ThankYou"));
                        finish();
                    }

                    else {

                        if(Integer.parseInt(total_prices)<=0){
                            view.loadUrl(url);
                        }
//                        view.loadUrl(url);
                    }
                }
            });
        }
    }

}