package com.example.jahanzeb.hybrary;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

/**
 * Created by jahanzeb on 11/14/2015.
 */
public class LendBook extends Activity {

    private String
            methodName = "BorrowerRequest",
            soapPrimitiveResponse,
            reply="Deny",
            SOAP_ACTION_METHOD = MainActivity.SOAP_ACTION + methodName;

    private Thread getOwnersBooks,sendRequest,acceptRequest;
    ListView list;
    ArrayList<String> book,author,edition,requestId,response,BorrowerName;
    LendBookAdaptor adapter;
    private int response_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_book);

        initializeVariables();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                showResponse(position);
            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                respondRequest(position);
                return true;

            }
        });

    }

    private void respondRequest(final int position) {
        new AlertDialog.Builder(LendBook.this)
                .setTitle("Respond Request")
                .setMessage("Hybrary has requested to lend '" + book.get(position) + "' from your collection.")
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                       response_id=  Integer.parseInt(requestId.get(position));
                        acceptRequest.start();
                    }
                })
                .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        response_id=  Integer.parseInt(requestId.get(position));
                        reply = "Deny";
                        response.set(position,reply);
                        sendRequest.start();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(true).show();
    }

    private void showResponse(int position) {
        new AlertDialog.Builder(LendBook.this)
                .setTitle("Request status")
                .setMessage("This request is in " + response.get(position).toUpperCase() + " state.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .setCancelable(true).show();

    }

    private void initializeVariables() {
        setThreadAction();
        getOwnersBooks.start();
        book = new ArrayList();
        author = new ArrayList();
        edition = new ArrayList();
        requestId = new ArrayList();
        response = new ArrayList();
        BorrowerName=new ArrayList();
        list = (ListView)findViewById(R.id.bookList);
    }

    private void setThreadAction() {
        final Handler hnd = new Handler();
        getOwnersBooks = new Thread(){
            public void run(){
                try{

                    // create Soap Request to hit Database Query for user details
                    SoapObject request = new SoapObject(MainActivity.nameSpace, methodName);

                    // Property Info for User Id
                    PropertyInfo userId = new PropertyInfo();
                    userId.setName("userId");
                    userId.setValue( MainActivity.userId );
                    userId.setType(int.class);
                    request.addProperty(userId);

                    // Serialization for reply from soap request
                    SoapSerializationEnvelope envelop = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelop.dotNet = true;
                    envelop.setOutputSoapObject(request);
                    HttpTransportSE androidHttpTransport = new HttpTransportSE(MainActivity.URL);

                    androidHttpTransport.call(SOAP_ACTION_METHOD, envelop);
                    final SoapPrimitive response = (SoapPrimitive)envelop.getResponse();
                    soapPrimitiveResponse = response.toString();

                    hnd.post(new Runnable() {
                        @Override
                        public void run() {

                            populateArrayLists();

                        }
                    });
                } catch(Exception e){
                    Log.w("LendBook","setThreadAction EXCEPTION !!");
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "You are not connected to Internet", Toast.LENGTH_LONG).show();
                }

            }
        };
        sendRequest=  new Thread()
        {
            public void run(){
                try{

                    // create Soap Request to hit Database Query for user details
                    SoapObject request = new SoapObject(MainActivity.nameSpace, "LendBook");

                    // Property Info for request Id
                    PropertyInfo requestId = new PropertyInfo();
                    requestId.setName("requestId");
                    requestId.setValue(response_id);
                    requestId.setType(int.class);
                    request.addProperty(requestId);

                    // Property Info for User Id
                    PropertyInfo status = new PropertyInfo();
                    status.setName("status");
                    status.setValue(reply);
                    status.setType(String.class);
                    request.addProperty(status);

                    // Serialization for reply from soap request
                    SoapSerializationEnvelope envelop = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelop.dotNet = true;
                    envelop.setOutputSoapObject(request);
                    HttpTransportSE androidHttpTransport = new HttpTransportSE(MainActivity.URL);

                    androidHttpTransport.call(MainActivity.SOAP_ACTION+"LendBook", envelop);
                    final SoapPrimitive response = (SoapPrimitive)envelop.getResponse();
                    soapPrimitiveResponse = response.toString();

                    hnd.post(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    });
                } catch(Exception e){
                    Log.w("LendBook","setThreadAction EXCEPTION !!");
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "You are not connected to Internet", Toast.LENGTH_LONG).show();
                }

            }
        };
        acceptRequest=  new Thread()
        {
            public void run(){
                try{

                    // create Soap Request to hit Database Query for user details
                    SoapObject request = new SoapObject(MainActivity.nameSpace, "RequestAccept");

                    // Property Info for request Id
                    PropertyInfo requestId = new PropertyInfo();
                    requestId.setName("USERID");
                    requestId.setValue(MainActivity.userId);
                    requestId.setType(int.class);
                    request.addProperty(requestId);

                    // Property Info for User Id
                    PropertyInfo status = new PropertyInfo();
                    status.setName("TABLEID");
                    status.setValue(response_id);
                    status.setType(int.class);
                    request.addProperty(status);

                    // Serialization for reply from soap request
                    SoapSerializationEnvelope envelop = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelop.dotNet = true;
                    envelop.setOutputSoapObject(request);
                    HttpTransportSE androidHttpTransport = new HttpTransportSE(MainActivity.URL);

                    androidHttpTransport.call(MainActivity.SOAP_ACTION+"RequestAccept", envelop);
                    final SoapPrimitive response = (SoapPrimitive)envelop.getResponse();
                    soapPrimitiveResponse = response.toString();

                    hnd.post(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    });
                } catch(Exception e){
                    Log.w("LendBook","setThreadAction EXCEPTION !!");
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "You are not connected to Internet", Toast.LENGTH_LONG).show();
                }

            }
        };

    }

    private void populateArrayLists() {
        Log.e("LendBook", "Response: " + soapPrimitiveResponse + "userId: " + MainActivity.userId);
        try {
            JSONArray jArray = new JSONArray(soapPrimitiveResponse);
            int num = jArray.length();
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json = jArray.getJSONObject(i);
                book.add(json.getString("book"));
                author.add(json.getString("author"));
                edition.add(json.getString("edition"));
                requestId.add(json.getString("requestId"));
                response.add(json.getString("status"));
                BorrowerName.add(json.getString("BorrowerName"));
            }
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("LendBook","populateArrayList EXCEPTION !!");
            e.printStackTrace();
        }
        adapter = new LendBookAdaptor(this, book, author, edition,BorrowerName);
        list.setAdapter(adapter);
    }

}
