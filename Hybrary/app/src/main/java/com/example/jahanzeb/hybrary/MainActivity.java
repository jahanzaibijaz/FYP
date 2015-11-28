package com.example.jahanzeb.hybrary;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class MainActivity extends Activity {

    // Edit Texts
    private EditText
            userName,           //
            password;           //

    // Image Views
    private ImageView
            signIn,             // to start Soap Action to check sign in details
            signUp;             // to start Soap Action to create user entry in Database

    // Thread
    Thread checkSignInDetails ;

    public static final String
            nameSpace = "http://tempuri.org/",                         // Web Service Name
            URL = "http://10.0.3.2:63232/WebSite4/WebService.asmx",    // Web Service URL
            SOAP_ACTION = "http://tempuri.org/";                       // Method URL

    private String
            methodName = "login",                                      // Method Name
            SOAP_ACTION_METHOD = SOAP_ACTION + methodName;

    public static int userId;
    private int responseValue;              // response from Soap Action

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeVariables();

        signIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Log.d("MainActivity", "ImageView Clicked: signIn");
                setThreadAction();
                checkSignInDetails.start();
            }
        });

        signUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "ImageView Clicked: signUp");
                Intent intnt = new Intent(getApplicationContext(), Signup.class);
                startActivity(intnt);
            }
        });
    }

    private void initializeVariables() {
        signIn      = (ImageView)findViewById(R.id.signIn);
        signUp      = (ImageView)findViewById(R.id.signUp);
        userName    = (EditText) findViewById(R.id.user);
        password    = (EditText) findViewById(R.id.pass);

        userName.setHint("User Name");
        password.setHint("Password");
    }

    private void setThreadAction() {
        final Handler hnd = new Handler();
        checkSignInDetails = new Thread(){
            public void run(){
            try{

                Looper.prepare();
                Log.d("MainActivity","SoapAction: "+SOAP_ACTION_METHOD);
                // create Soap Request to hit Database Query for user details
                SoapObject request = new SoapObject(nameSpace, methodName);

                // Property Info for user name
                PropertyInfo userDetails = new PropertyInfo();
                userDetails.setName("usr");                         // TODO change parameter from usr to userName in Visual Studio
                userDetails.setValue(userName.getText().toString());
                userDetails.setType(String.class);
                request.addProperty(userDetails);

                // Property Info for password
                PropertyInfo passwordDetail = new PropertyInfo();
                passwordDetail.setName("pass");                     // TODO change parameter from pass to password in Visual Studio
                passwordDetail.setValue(password.getText().toString());
                passwordDetail.setType(String.class);
                request.addProperty(passwordDetail);

                // Serialization for reply from soap request
                SoapSerializationEnvelope envelop = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelop.dotNet = true;
                envelop.setOutputSoapObject(request);
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                Log.e("MainActivity","soapaction issue"+androidHttpTransport);
                androidHttpTransport.call(SOAP_ACTION_METHOD, envelop);
                final SoapPrimitive response = (SoapPrimitive)envelop.getResponse();
                responseValue = Integer.parseInt(response.toString());

                hnd.post(new Runnable() {
                    @Override
                    public void run() {

                        if (responseValue > 0) {
                            MainActivity.userId = responseValue;
                            validUser();
                        } else if (responseValue == 0)
                            inValidUser("User does not exist");
                        else
                            inValidUser("Wrong Password");
                    }
                });
            } catch(Exception e){
                Toast.makeText(MainActivity.this, "You are not connected to Internet", Toast.LENGTH_LONG).show();
                Log.w("MainActivity", "setThreadAction EXCEPTION !!");
                e.printStackTrace();
            }

            }
        };
    }

    private void validUser() {
        Intent intent = new Intent(MainActivity.this, Dashboard.class);
        startActivity(intent);
        finish();
    }

    private void inValidUser(final String reason) {
        password.setText("");
        new AlertDialog.Builder(MainActivity.this)
            .setTitle("Error Message...")
            .setMessage(reason)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            })
            .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(true).show();
    }

}
