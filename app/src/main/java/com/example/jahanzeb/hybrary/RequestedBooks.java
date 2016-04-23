package com.example.jahanzeb.hybrary;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
            reply= "Deny",
            soapPrimitiveResponse,
            SOAP_ACTION_METHOD = MainActivity.SOAP_ACTION + methodName;

    private Thread getOwnersBooks,CancelRequest;
    ListView list;
    ArrayList<String> book,author,edition,bookid,response,issuedate,duedate;
    SingleBookAdapter adapter;
        private int tableID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_book);

        initializeVariables();

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showResponse(position);
                return true;
            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //  Code incomplete
                showFeedBackDialog(Integer.parseInt(bookid.get(position)));
                return true;
            }
        });
    }

    private void showFeedBackDialog(int bookId) {
        final Dialog listDialog = new Dialog(RequestedBooks.this);
        listDialog.setContentView(R.layout.comments_list);
        final ListView feedbacks = (ListView)listDialog.findViewById(R.id.comments__List);

        API.RetrieveBookReviewsCallback bookReviews = new API.RetrieveBookReviewsCallback() {
            @Override
            public void onSuccess(Feedback_Adapter adapter) {
                feedbacks.setAdapter(adapter);
                listDialog.show();
            }
        };

        API apiCall = new API();
        apiCall.RetrieveBookReviews(RequestedBooks.this, bookId, bookReviews);
    }

    private void initializeVariables() {
        setThreadAction();
        getOwnersBooks.start();
        bookid  =  new ArrayList();
        book    =  new ArrayList();
        author  =  new ArrayList();
        edition =  new ArrayList();
        response=  new ArrayList();
        issuedate= new ArrayList();
        duedate =  new ArrayList();
        list = (ListView)findViewById(R.id.bookList);
    }

    private void showResponse(int position) {
        new AlertDialog.Builder(RequestedBooks.this)
                .setTitle("Request status")
                .setMessage("your request for " + book.get(position) + " is in " +
                        response.get(position).toUpperCase() + " state. Issue Date is"
                        + issuedate + "" + "till"+duedate)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .setCancelable(true).show();
    }
    private void showResponse_deny() {
        new AlertDialog.Builder(RequestedBooks.this)
                .setTitle("Request status")
                .setMessage("This request is in  Deny state.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .setCancelable(true).show();
    }
    public void  checkrequest()
    {
        if
                (response.equals("Accept"))
            showResponse(1);

        else if
                (response.equals("Deny"))
            showResponse_deny();

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
                                                    tableID= Integer.parseInt(bookid.get(position));
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

                    SoapObject request = new SoapObject(MainActivity.nameSpace, "cancelOwnrequest");
                    Log.e("RequestedBooks","Get Soap Request::"+request);

                    //info for borrower table_id
                    PropertyInfo table_id = new PropertyInfo();
                    table_id.setName("id");
                    table_id.setValue(tableID);
                    Log.e("RequestedBooks", "id  is" +tableID );
                    table_id.setType(int.class);
                    request.addProperty(table_id);
                    Log.e("RequestedBooks", "property info" + table_id);

                    // Serialization for reply from soap request
                    SoapSerializationEnvelope envelop = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelop.dotNet = true;
                    envelop.setOutputSoapObject(request);
                    Log.e("RequestedBooks", "Serialization done" + envelop);
                    HttpTransportSE androidHttpTransport = new HttpTransportSE(MainActivity.URL);

                    //send Request
                    Log.e("RequestedBooks", "request for android http transport call::" + envelop);
                    androidHttpTransport.call(MainActivity.SOAP_ACTION + "cancelOwnrequest", envelop); //TODO find issue..
                    Log.e("RequestedBooks", " http transport call::" + envelop);

                    final SoapPrimitive response = (SoapPrimitive)envelop.getResponse();
                    Log.e("RequestedBooks","response reached till soap primitive::");
                    soapPrimitiveResponse = response.toString();

                    hnd.post(new Runnable() {
                        @Override
                        public void run() {
                            if (soapPrimitiveResponse.equals(1))
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
                bookid.add(json.getString("bookid"));
                book.add(json.getString("book"));
                author.add(json.getString("author"));
                edition.add(json.getString("edition"));
                response.add(json.getString("response"));
                issuedate.add(json.getString("issuedate"));
                duedate.add(json.getString("duedate"));
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

