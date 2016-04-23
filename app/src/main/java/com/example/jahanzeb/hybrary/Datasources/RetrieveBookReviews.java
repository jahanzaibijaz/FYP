package com.example.jahanzeb.hybrary.Datasources;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.jahanzeb.hybrary.API;
import com.example.jahanzeb.hybrary.Feedback_Adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

/**
 * Created by jahanzeb on 24/04/2016.
 */
public class RetrieveBookReviews extends AsyncTask<String, Void, String> {

    public String
            nameSpace = "http://tempuri.org/",                         // Web Service Name
            URL = "http://10.0.3.2:53611/WebService1.asmx",            // Web Service URL
            SOAP_ACTION = "http://tempuri.org/";                       // Method URL

    public Thread getBookReviews;

    int BookId;
    String methodName, SOAP_ACTION_METHOD;
    Activity context;
    API.RetrieveBookReviewsCallback callback;

    public RetrieveBookReviews(Activity cont, int bookId, API.RetrieveBookReviewsCallback call) {
        BookId = bookId;
        context = cont;
        callback = call;
        Log.e("RetrieveBookReviews", "Constructor called");
    }

    @Override
    protected void onPreExecute() {
        methodName = "RetrieveFeedBack";
        SOAP_ACTION_METHOD = SOAP_ACTION + methodName;

        Log.e("RetrieveBookReviews", "onPre");
        setThreadAction(context, methodName, SOAP_ACTION_METHOD, BookId);
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        Log.e("RetrieveBookReviews", "doInBackground");
        getBookReviews.start();
        return null;
    }


    private void setThreadAction(Context context, String methodName, String SOAP_ACTION_METHOD, int parameter) {
        final Handler handler = new Handler();
        Log.e("RetrieveBookReviews", "setThread");

        GetBookReviews(handler, context, methodName, SOAP_ACTION_METHOD, parameter);
    }

    private void GetBookReviews(final Handler handler, final Context context, final String methodName, final String SOAP_ACTION_METHOD, final int book_id) {
        Log.e("RetrieveBookReviews", "FUNC");
        getBookReviews = new Thread() {
            public void run() {
                try {

                    Looper.prepare();
                    // create Soap Request to hit Database Query for user details
                    SoapObject request = new SoapObject(nameSpace, methodName);

                    // Property Info for user name
                    PropertyInfo bookId = new PropertyInfo();
                    bookId.setName("BookId");                         // TODO change parameter from usr to userName in Visual Studio
                    bookId.setValue(book_id);
                    bookId.setType(Integer.class);
                    request.addProperty(bookId);

                    // Serialization for reply from soap request
                    SoapSerializationEnvelope envelop = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelop.dotNet = true;
                    envelop.setOutputSoapObject(request);
                    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                    androidHttpTransport.call(SOAP_ACTION_METHOD, envelop);

                    final SoapPrimitive query = (SoapPrimitive) envelop.getResponse();

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("RetrieveBookReviews", "heandler Post");
                            try {
                                Log.e("JHANDU BAAM", book_id + ":= " + query);
                                onPostExecute(new JSONArray(query.toString()));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    Toast.makeText(context, "You are not connected to Internet", Toast.LENGTH_LONG).show();
                    Log.w("RetrieveBookRequest", "setThreadAction EXCEPTION !!");
                    e.printStackTrace();
                }

            }
        };
    }

    protected void onPostExecute(JSONArray response) {
        ArrayList<String> borrowers, ratings, comments;
        borrowers = new ArrayList<>();
        ratings = new ArrayList<>();
        comments = new ArrayList<>();

        Log.e("RetrieveBookReviews", "onPost");
        for (int i = 0; i < response.length(); i++) {
            JSONObject json = new JSONObject();
            try {
                json = response.getJSONObject(i);
                borrowers.add(json.getString("borrower"));
                ratings.add(json.getString("rating"));
                comments.add(json.getString("comment"));
            } catch (JSONException e) {
                Log.e("RetrieveBookReviews", "BHAO RAY  BHAO");
                e.printStackTrace();
            }
        }
        callback.onSuccess(new Feedback_Adapter(context, borrowers, ratings, comments));
    }
}