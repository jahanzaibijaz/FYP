package com.example.jahanzeb.hybrary;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
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


public class UpdateSingleBook extends Activity {
    private String
            methodName = "updateBook",
            soapPrimitiveResponse,
            SOAP_ACTION_METHOD = MainActivity.SOAP_ACTION + methodName;

    private Thread updatemybook;

    private EditText
            book,author,edition;

    private ImageView update;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.updatebook);

        initializeVariables();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatemybook.start();

            }
        });

    }
    private void initializeVariables() {

        book = (EditText)findViewById(R.id.update_book);
        author = (EditText)findViewById(R.id.update_author);
        edition = (EditText)findViewById(R.id.update_edition);
        book.setText(getIntent().getExtras().getString("bookName"));
        author.setText(getIntent().getExtras().getString("author"));
        edition.setText(getIntent().getExtras().getString("edition"));
        update = (ImageView)findViewById(R.id.done);
        setThreadAction();
    }
    private void setThreadAction() {
        final Handler hnd = new Handler();
        updatemybook = new Thread(){
            public void run(){
                try{

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
                    userId.setName("book_id");
                    userId.setValue(getIntent().getExtras().getString("bookId"));
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

                            finish();
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
}
