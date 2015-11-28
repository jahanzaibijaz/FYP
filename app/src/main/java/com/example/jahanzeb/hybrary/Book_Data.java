/*
package com.example.jahanzeb.hybrary;


import android.os.Handler;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class Book_Data   {
    String bookname,author,edition;
    Thread thh;

    public  Book_Data getbooks()
    {
         final String NameSpace3 = "http://tempuri.org/";
        final String URL3 = "http://10.0.3.2:63232/WebSite4/WebService.asmx";
         final String MethodName3 = "filltable";
        final String SOAP_Action3 = "http://tempuri.org/filltable";
       // URL3: L service
      final   SoapObject table = null;                        // Contains table of dataset that returned through SoapObject
        final SoapObject client =null;                      // Its the client petition to the web service

       final SoapObject tableRow = null;                        // Contains row of table
     final    SoapObject responseBody = null;                    // Contains XML content of dataset
      final   HttpTransportSE androidhttpTransport = null;            // That call webservice
       final SoapSerializationEnvelope sse = null;

        sse = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        sse.addMapping(NameSpace3, "Book_Data", this.getClass());
        //Note if class name isn't "movie" ,you must change
        sse.dotNet = true; // if WebService written .Net is result=true
      androidhttpTransport = new HttpTransportSE(URL3);
        Book_Data setbookdetail=  new Book_Data();
        final Handler hnd = new Handler();
        thh= new Thread(){
            public void run()
            {

                try
                {
                    client = new SoapObject(NameSpace3, MethodName3);
                    sse.setOutputSoapObject(client);
                    sse.bodyOut = client;
                    androidhttpTransport.call(SOAP_Action3, sse);

                    // This step: get file XML
                    responseBody = (SoapObject) sse.getResponse();
                    // remove information XML,only retrieved results that returned
                    responseBody = (SoapObject) responseBody.getProperty(1);
                    // get information XMl of tables that is returned
                    table = (SoapObject) responseBody.getProperty(0);
                    //Get information each row in table,0 is first row
                    tableRow = (SoapObject) table.getProperty(0);
                    setbookdetail.bookname = tableRow.getProperty("bookname").toString();
                    setbookdetail.author = tableRow.getProperty("author").toString();
                    setbookdetail.edition = tableRow.getProperty("edition").toString();
                    return setbookdetail;

                } catch (Exception e)
                {
                    setbookdetail.bookname = e.toString();
                    setbookdetail.author= e.toString();
                    setbookdetail.edition=e.toString();
                    return setbookdetail;
                }

            }
        }


    }

    


}






*/
