package com.example.jahanzeb.hybrary;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class AddBook extends Activity {

    private String
            methodName = "AddBook",
            soapPrimitiveResponse,
            SOAP_ACTION_METHOD = MainActivity.SOAP_ACTION + methodName;

    private Thread addMyBook;

    private EditText
            book,author,edition;

    private ImageView save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_book);

        initializeVariables();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMyBook.start();
                finish();
            }
        });
    }

    private void initializeVariables() {
        setThreadAction();
        book = (EditText)findViewById(R.id.book);
        author = (EditText)findViewById(R.id.author);
        edition = (EditText)findViewById(R.id.edition);
        save = (ImageView)findViewById(R.id.done);
    }

    private void setThreadAction() {


        final Handler hnd = new Handler(Looper.getMainLooper());

        addMyBook = new Thread(){
            public void run(){
                try{
                    Looper.prepare();

                    // create Soap Request to hit Database Query for user details
                    SoapObject request = new SoapObject(MainActivity.nameSpace, methodName);

                    // Property Info for Book Name
                    PropertyInfo bookName = new PropertyInfo();
                    bookName.setName("book");
                    bookName.setValue( book.getText().toString() );
                    bookName.setType(String.class);
                    request.addProperty(bookName);

                    // Property Info for Author Name
                    PropertyInfo authorName = new PropertyInfo();
                    authorName.setName("author");
                    authorName.setValue(author.getText().toString());
                    authorName.setType(String.class);
                    request.addProperty(authorName);

                    // Property Info for Edition Number
                    PropertyInfo editionNumber = new PropertyInfo();
                    editionNumber.setName("edition");
                    editionNumber.setValue( Integer.parseInt(edition.getText().toString()) );
                    editionNumber.setType(int.class);
                    request.addProperty(editionNumber);

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
                            Log.e("AddBook","BookAddMethod"+soapPrimitiveResponse);

                        }
                    });
                } catch(Exception e){
                    Log.w("AddBook", "setThreadAction EXCEPTION !!");
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "You are not connected to Internet", Toast.LENGTH_LONG).show();
                }

            }
        };
    }
    private void populateArrayLists() {
        Log.e("AddBook","Response: "+soapPrimitiveResponse);
    }

}
