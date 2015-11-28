package com.example.jahanzeb.hybrary;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.InputStream;


public class ShowBooks extends Activity {
    //Book books= new Book();

    TextView TV;
    Button b1;
    Thread thh;
    String OutPut, p7,  Result,tbledata; // Bookname,OutPut1, author, edition;
    SoapObject table = null;
    SoapObject tableRow = null;
     JSONObject jsonobj;
     JSONArray jsonarr;
    Bundle bundleResult= new Bundle();
    ListAdapter booksadapter;
    ListView booksview;

    InputStream isr = null;String reslt = "";

    private static final String NameSpace3 = "http://tempuri.org/";
    public static final String URL3 = "http://10.0.3.2:63232/WebSite4/WebService.asmx";
    private static final String MethodName3 = "filltable";
    private static final String SOAP_Action3 = "http://tempuri.org/filltable";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_books);
       /* TextView tv1= (TextView)findViewById(R.id.txt1);
        TextView tv2 = (TextView)findViewById(R.id.txt2);
        TextView tv3 = (TextView)findViewById(R.id.txt3);*/
        TV = (TextView) findViewById(R.id.bksview);
        b1 = (Button) findViewById(R.id.booksbtn);

      /*  Book_Data newbookdata=  new Book_Data();
       newbookdata= newbookdata.getbooks();
        tv1.setText(newbookdata.bookname);
        tv2.setText(newbookdata.author);
        tv3.setText(newbookdata.edition);*/



        b1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                //  p7 = et1.getText().toString();


                final Handler hnd = new Handler();


                thh = new Thread() {
                    public void run() {
                        try {

                            SoapObject request = new SoapObject(NameSpace3, MethodName3);
                            PropertyInfo pi = new PropertyInfo();
                            pi.setName("tabl");
                            pi.setValue(p7);
                            pi.setType(String.class);
                            request.addProperty(pi);
                            //tbledata=TV.getText().toString();
                       //     tableRow.getPropertyInfo(0,pi);
                         //   Log.d("YESS",pi.getName()+"/"+tableRow.getProperty(0).toString());


                            request.addProperty(pi);


                            SoapSerializationEnvelope envelop = new SoapSerializationEnvelope(SoapEnvelope.VER11);

                            envelop.dotNet = true;
                            envelop.setOutputSoapObject(request);
                            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL3);

                            androidHttpTransport.call(SOAP_Action3, envelop);
                            // SoapPrimitive response = (SoapPrimitive) envelop.getResponse();

                            SoapPrimitive result = (SoapPrimitive) envelop.getResponse();




                         // result = (SoapPrimitive) result;
                      //   table = (SoapObject) result.getProperty(0);
                          // final String res = result.getProperty(0);
//
                       //   tableRow = (SoapObject) result.getProperty(0);

                           //SoapObject S_deal=(SoapObject)tableRow.getProperty(0);



                         Result = result.toString();
                           // Result=result.toString();Result=result.toString();
                          final  String [] Trim=Result.split("/");
                             // OutPut1 = response.toString();

                            // OutPut1 = response.toString();
                        // final String[] Trim1 = Result.split("BookName");


                         //   Pattern p = Pattern.compile("\\d{2}(am|pm)");
                     //     final  Matcher m = p.matcher("go to the shop at 12pm");

                            hnd.post(new Runnable() {

                                @Override
                                public void run() {
                                    // TODO Auto-generated method stub

                                    try{
                                  //      TV.setText(Result);

                                         TV.setText("BookName :"+Trim[0]+ "\n" + "Author :"+Trim[1] + "\n" + "Edition :" + Trim[2]);



                                    }
                                  catch ( Exception e)
                                  {
                                      Log.e("Second TRY", "Error in conversion:" + e.toString());
                                  }

                                    //TV.setText(Result);


/*
                                   /* ArrayList<Book> booklist= new ArrayList<Book>();

                                    for(Book b : Result)
                                    {
                                        booklist
                                    }*//*
                                            try {
                                                if (Result.startsWith("{"))
                                                {
                                                    jsonobj= new JSONObject(Result);
                                                    Iterator<String> itr=jsonobj.keys();
                                     ss               while ( itr.hasNext())
                                                    {
                                                        String key= (String) itr.next();
                                                        String value=jsonobj.getString(key);
                                                        bundleResult.putString(key,value);


                                                    }

                                                }
                                                else if (Result.startsWith("["))
                                                {

                                                    jsonarr= new JSONArray(Result);
                                                    System.out.println("length"+jsonarr.length());
                                                    for (int i=0; i<jsonarr.length();i++) {
                                                        jsonobj = (JSONObject) jsonarr.get(i);
                                                        bundleResult.putString(String.valueOf(i),jsonobj.toString());
                                                    }
                                            }




                                    }catch (Exception e)
                                            {
                                                Log.e("dont know error",e.toString());
                                            }*/
                                 //   if(m.matches()) {

                                   // }
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                };

                thh.start();

            }
        });
    }
}











































