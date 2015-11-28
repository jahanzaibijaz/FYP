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

public class RequestedBooks extends Activity {

    private String
            methodName = "requestedBooks",
            soapPrimitiveResponse,
            SOAP_ACTION_METHOD = MainActivity.SOAP_ACTION + methodName;

    private Thread getOwnersBooks,CancelRequest;
    ListView list;
    ArrayList<String> book,author,edition,id;
    SingleBookAdapter adapter;
        private int BookID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_book);

        initializeVariables();
        DeleteBookMethod();

    }


    private void DeleteBookMethod() {

        String Deletebook=id.toString().toString();
    }
    private void initializeVariables() {
        setThreadAction();
        getOwnersBooks.start();
        id = new ArrayList();
        book = new ArrayList();
        author = new ArrayList();
        edition = new ArrayList();
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
                            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override

                                public void onItemClick(AdapterView<?> parent, View view, final int position, long lid) {

                                    new AlertDialog.Builder(RequestedBooks.this)
                                            .setTitle("Cancel Request")
                                            .setMessage("Are you sure You want to Cancel your Request for'" + book.get(position) + "'")
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    CancelRequest.start();
                                                }
                                            })
                                            .setNegativeButton("No",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {

                                                        }
                                                    })
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .setCancelable(true).show();
                                }
                            });
                        }
                    });
                } catch(Exception e){
                    Log.w("RequestedBooks","setThreadAction EXCEPTION !!");
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "You are not connected to Internet", Toast.LENGTH_LONG).show();
                }

            }
        };


        CancelRequest = new Thread(){
            public void run(){
                try{

                    // create Soap Request to hit Database Query for user details
                    Log.e("RequestedBooks","1st attempt");
                    SoapObject request = new SoapObject(MainActivity.nameSpace, "CancelOwnRequest");
                    Log.e("RequestedBooks","Get Soap Request::"+request);
                    // Property Info for User Id
                   /* PropertyInfo userId = new PropertyInfo();
                    userId.setName("userid");
                    userId.setValue( MainActivity.userId );
                    userId.setType(int.class);
                    request.addProperty(userId); */

                    //info for borrower table_id
                    PropertyInfo table_id = new PropertyInfo();
                    table_id.setName("id");
                    table_id.setValue(MainActivity.userId);
                    Log.e("RequestedBooks", "id  is"+id);
                    table_id.setType(int.class);
                    request.addProperty(table_id);

                    // Serialization for reply from soap request
                    SoapSerializationEnvelope envelop = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelop.dotNet = true;

                    envelop.setOutputSoapObject(request);
                    Log.e("RequestedBooks", "Serialization done" + envelop);
                    HttpTransportSE androidHttpTransport = new HttpTransportSE(MainActivity.URL);
                    //send Request

                    Log.e("RequestedBooks", "request for android http transport call::" + envelop);
                    androidHttpTransport.call(MainActivity.SOAP_ACTION + "CancelOwnRequest", envelop);

                    final SoapPrimitive response = (SoapPrimitive)envelop.getResponse();
                    Log.e("RequestedBooks","response reached till soap primitive::");
                    soapPrimitiveResponse = response.toString();

                    hnd.post(new Runnable() {
                        @Override
                        public void run() {
                            if (soapPrimitiveResponse=="1")
                            {
                                Log.e("RequestedBooks", "Response is positive"+soapPrimitiveResponse);
                            }
                            else

                            Log.e("RequestedBooks", "Response   Got From WebService:: " + soapPrimitiveResponse );
                            finish();
                        }
                    });
                } catch(Exception e){
                    Log.w("RequestedBooks","setThreadAction request EXCEPTION !!"+e);
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "You are not connected to Internet", Toast.LENGTH_LONG).show();
                }
            }
        };

    }

    private void populateArrayLists() {
        Log.e("RequestedBooks","Response: "+soapPrimitiveResponse + "userId: "+MainActivity.userId);
        try {
            JSONArray jArray = new JSONArray(soapPrimitiveResponse);
            int num = jArray.length();
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json = jArray.getJSONObject(i);
                id.add(json.getString("id"));
                book.add(json.getString("book"));
                author.add(json.getString("author"));
                edition.add(json.getString("edition"));
            }
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("RequestedBooks","populateArrayList EXCEPTION !!");
            e.printStackTrace();
        }

        adapter = new SingleBookAdapter(this, book, author, edition);
        list.setAdapter(adapter);
    }

}

