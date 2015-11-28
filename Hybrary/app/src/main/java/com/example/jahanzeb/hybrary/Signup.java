package com.example.jahanzeb.hybrary;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class Signup extends AppCompatActivity {

    EditText edt1, edt2, edt3, edt4;
    Thread thh ;
    String p1, p2 ,p3,p4,out;
    ImageView btnCreateProduct;
    TextView tv;

    private static final String NameSpace3 = "http://tempuri.org/";
    public static final String URL3 = "http://10.0.3.2:63232/WebSite4/WebService.asmx";
    private static final String MethodName3 = "signup";
    private static final String SOAP_ACTION3 = "http://tempuri.org/signup";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        initializevariables( );


        btnCreateProduct.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                p1 = edt1.getText().toString();
                p2 = edt2.getText().toString();
                p3 = edt3.getText().toString();
                p4 = edt4.getText().toString();


                final Handler hnd = new Handler();
                thh = new Thread() {
                    public void run() {
                        try {

                            SoapObject request = new SoapObject(NameSpace3, MethodName3);

                            PropertyInfo pi = new PropertyInfo();
                            pi.setName("usrname");
                            pi.setValue(p1);
                            pi.setType(String.class);
                            request.addProperty(pi);



                            PropertyInfo pi3 = new PropertyInfo();
                            pi3.setName("contact");
                            pi3.setValue(p2);
                            pi3.setType(String.class);
                            request.addProperty(pi3);

                            PropertyInfo pi4 = new PropertyInfo();
                            pi4.setName("pass");
                            pi4.setValue(p3);
                            pi4.setType(String.class);
                            request.addProperty(pi4);

                            PropertyInfo pi5 = new PropertyInfo();
                            pi5.setName("email");
                            pi5.setValue(p4);
                            pi5.setType(String.class);
                            request.addProperty(pi5);

                            SoapSerializationEnvelope envelop = new SoapSerializationEnvelope(SoapEnvelope.VER11);

                            envelop.dotNet = true;
                            envelop.setOutputSoapObject(request);
                            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL3);

                            androidHttpTransport.call(SOAP_ACTION3, envelop);
                            SoapObject response = (SoapObject) envelop.bodyIn;
                            out = response.toString();
                            Log.e("Signup","NEW USER INSERTED");


                            hnd.post(new Runnable() {
                                @Override
                                public void run() {

                                    if (out.toString().equalsIgnoreCase("New user has added")) {
                                //  tv.setText(out);

                                        tv.setText("try again");
                                    } else {

                                        Intent  inntn= new Intent(getApplicationContext(),MainActivity.class);
                                        startActivity(inntn);
                                        finish();
                                    }

                                   // pd.dismiss();
                                }
                            });
                        } catch (Exception e) {
                            Log.e("Second TRY", "Error in conversion:" + e.toString());
                        }

                    }
                };

                thh.start();
            }
        });

    }

    private void initializevariables() {
        edt1=(EditText)findViewById(R.id.username);
        edt2=(EditText)findViewById(R.id.phone);
        edt3=(EditText)findViewById(R.id.password);
        edt4=(EditText)findViewById(R.id.email);
        btnCreateProduct = (ImageView) findViewById(R.id.save);

    }


}
