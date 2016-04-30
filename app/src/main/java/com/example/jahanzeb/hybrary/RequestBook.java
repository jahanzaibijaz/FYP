package com.example.jahanzeb.hybrary;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

public class RequestBook extends Activity {

    private String
            methodName = "searchBook",

            soapPrimitiveResponse,
            SOAP_ACTION_METHOD = MainActivity.SOAP_ACTION;

    private Thread getSearchedBooks, requestBook;
    ListView list;
    ArrayList<String> book,author,edition,id;
    SingleBookAdapter adapter;
    private int BookID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_book);

        initializeVariables();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long lid) {

                new AlertDialog.Builder(RequestBook.this)
                        .setTitle("Confirm...")
                        .setMessage("Send request to Hybrary for Borrowing '" + book.get(position) + "'")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (requestBook.getThreadGroup() != null) {
                                    requestBook.getThreadGroup().interrupt();
                                }
                                BookID = Integer.parseInt(id.get(position));
                                requestBook.start();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setCancelable(true).show();
            }
        });
/*
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //  Code incomplete
                Dialog listDialog = new Dialog(RequestBook.this);
                listDialog.setContentView(R.layout.comments_list);
                listDialog.show();
                return true;
            }
        });*/

    }

    private void initializeVariables() {
        setThreadAction();
        getSearchedBooks.start();
        id = new ArrayList();
        book = new ArrayList();
        author = new ArrayList();
        edition = new ArrayList();

        list = (ListView)findViewById(R.id.bookList);
    }


    private void setThreadAction() {
        final Handler hnd = new Handler();
        getSearchedBooks = new Thread(){
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

                    // Property Info for Book Name
                    PropertyInfo book_name = new PropertyInfo();
                    book_name.setName("bookName");
                    book_name.setValue(SearchBook.searchThisBook);
                    book_name.setType(String.class);
                    request.addProperty(book_name);

                    // Serialization for reply from soap request
                    SoapSerializationEnvelope envelop = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelop.dotNet = true;
                    envelop.setOutputSoapObject(request);
                    HttpTransportSE androidHttpTransport = new HttpTransportSE(MainActivity.URL);

                    androidHttpTransport.call(SOAP_ACTION_METHOD+methodName, envelop);
                    final SoapPrimitive response = (SoapPrimitive)envelop.getResponse();
                    soapPrimitiveResponse = response.toString();

                    hnd.post(new Runnable() {
                        @Override
                        public void run() {

                            populateArrayLists();
                        }
                    });
                } catch(Exception e){
                    Log.w("RequestBook","setThreadAction getsearched EXCEPTION !!");
                    e.printStackTrace();
                }

            }
        };

            requestBook = new Thread(){
            public void run(){
                try{
                    Looper.prepare();
                    // create Soap Request to hit Database Query for user details
                    SoapObject request = new SoapObject(MainActivity.nameSpace, "sendRequest");
                    
                    // Property Info for User Id
                    PropertyInfo userId = new PropertyInfo();
                    userId.setName("userId");
                    userId.setValue( MainActivity.userId );
                    userId.setType(int.class);
                    request.addProperty(userId);

                    // Property Info for Book ID
                    final PropertyInfo book_id = new PropertyInfo();
                    book_id.setName("bookId");
                    book_id.setValue(BookID);
                    book_id.setType(int.class);
                    request.addProperty(book_id);
                    Log.e("RequestBook", "propertyinfo: " + book_id);

                    // Serialization for reply from soap request
                    SoapSerializationEnvelope envelop = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelop.dotNet = true;
                    envelop.setOutputSoapObject(request);
                    HttpTransportSE androidHttpTransport = new HttpTransportSE(MainActivity.URL);
                    //send Request
                    androidHttpTransport.call(SOAP_ACTION_METHOD+"sendRequest", envelop);
                    final SoapPrimitive response = (SoapPrimitive)envelop.getResponse();
                    soapPrimitiveResponse = response.toString();

                    hnd.post(new Runnable() {
                        @Override
                        public void run() {
                        if(soapPrimitiveResponse.equals("Book is not availble for Lend")) {
                           bookIS_();
                        }
                        else finish();

                        Log.e("RequestBook", "Response: " + soapPrimitiveResponse);
                       // finish();
                        }
                    });
                    requestBook.wait();
                } catch(Exception e){
                    Log.w("RequestBook","setThreadAction request EXCEPTION !!");
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "You are not connected to Internet", Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    private void populateArrayLists() {
        Log.e("RequestBook","Response: "+soapPrimitiveResponse + "userId: "+MainActivity.userId);
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
            Log.e("RequestBook","populateArrayList EXCEPTION !!");
            e.printStackTrace();
        }

        adapter = new SingleBookAdapter(this, book, author, edition);
        list.setAdapter(adapter);
    }

    private void bookIS_() {

        new AlertDialog.Builder(RequestBook.this)
                .setTitle("Request Denied")
                .setMessage("Another User has already Borrowed  this book")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(true)
                        dialog.cancel();
                        finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .setCancelable(true).show();
    }
}

