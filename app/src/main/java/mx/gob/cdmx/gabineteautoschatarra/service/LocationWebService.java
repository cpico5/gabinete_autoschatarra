package mx.gob.cdmx.gabineteautoschatarra.service;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;

public class LocationWebService extends AsyncTask<String, String, Boolean> {

    public LocationWebService() {
        // TODO Auto-generated constructor stub
    }

    @Override
    protected Boolean doInBackground(String... arg0) {

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("location", arg0[1]));

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(arg0[0]);
        //   HttpParams httpParameters = new BasicHttpParams();

        //z    httpclient = new DefaultHttpClient(httpParameters);

        try {
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = httpclient.execute(httppost);
            HttpEntity httpEntity = response.getEntity();

            /**
             * Fetching response from the server
             */

            String serverResponse = EntityUtils.toString(httpEntity);
            Log.e("Server", "Server Responded OK");
            Log.e("ServerResponse", serverResponse);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
