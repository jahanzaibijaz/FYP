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

public class ListBook extends Activity {

    private String
            methodName = "filltable",
            soapPrimitiveResponse,
            SOAP_ACTION_METHOD = MainActivity.SOAP_ACTION + methodName;

    private Thread getOwnersBooks,DeleteRequest;
    ListView list;
    ArrayList<String> id,book,author,edition;
    SingleBookAdapter adapter;
    public  int bookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_book);

        initializeVariables();

        // single click
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long lid) {

            }
        });

        //Long click
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deleteBook(position);
                return true;
            }
        });
    }

    private void initializeVariables() {
        setThreadAction();
        id=  new ArrayList();
        book = new ArrayList();
        author = new ArrayList();
        edition = new ArrayList();
        list = (ListView)findViewById(R.id.bookList);
        getOwnersBooks.start();
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
                    Log.w("ListBook","setThreadAction EXCEPTION !!");
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "You are not connected to Internet", Toast.LENGTH_LONG).show();
                }

            }
        };
        DeleteRequest = new Thread(){
            public void run(){
                try{
                    // create Soap Request to hit Database Query for user details
                    SoapObject request = new SoapObject(MainActivity.nameSpace, "DeleteOwnBook");

                    //info for borrower table_id
                    PropertyInfo table_id = new PropertyInfo();
                    table_id.setName("Bookid");
                    table_id.setValue(bookId);
                    table_id.setType(int.class);
                    request.addProperty(table_id);

                    // Serialization for reply from soap request
                    SoapSerializationEnvelope deleteBookEnvelop = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    deleteBookEnvelop.dotNet = true;
                    deleteBookEnvelop.setOutputSoapObject(request);
                    HttpTransportSE androidHttpTransport = new HttpTransportSE(MainActivity.URL);

                    //send Request
                    androidHttpTransport.call(MainActivity.SOAP_ACTION + "DeleteOwnBook", deleteBookEnvelop);
                    Log.e("ListBook", " android http transport call done::" + deleteBookEnvelop);

                    final SoapPrimitive response = (SoapPrimitive)deleteBookEnvelop.getResponse();
                    Log.e("ListBook","response reached till soap primitive::");
                    soapPrimitiveResponse = response.toString();

                    hnd.post(new Runnable() {
                        @Override
                        public void run() {
                            if (soapPrimitiveResponse=="1" || soapPrimitiveResponse=="2" )
                            {
                                Log.e("ListBook", "Book Deleted"+soapPrimitiveResponse);
                            }
                            else

                                Log.e("ListBook", "Response   Got From WebService: : " + soapPrimitiveResponse );
                            finish();
                        }
                    });
                } catch(Exception e){
                    Log.w("ListBook","setThreadAction request EXCEPTION !!"+e);
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "You are not connected to Internet", Toast.LENGTH_LONG).show();
                }
            }
        };

    }

    private void populateArrayLists() {
        Log.e("ListBook","Response: "+soapPrimitiveResponse + "userId: "+MainActivity.userId);
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
            Log.e("ListBook","populateArrayList EXCEPTION !!");
            e.printStackTrace();
        }

        adapter = new SingleBookAdapter(this, book, author, edition);
        list.setAdapter(adapter);
    }

    private void deleteBook(int position) {
        bookId = Integer.parseInt(id.get(position));
        new AlertDialog.Builder(ListBook.this)
                .setTitle("DeleteBook")
                .setMessage("Are you sure You want Delete'" + book.get(position) + " From Database")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        DeleteRequest.start();
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

}

