package com.example.jahanzeb.hybrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class Dashboard extends Activity {

    //Button b1;
    // EditText ET,ET2,ET3;
    //TextView TV,usrname;

    // Thread thh ;
    //String p1, p2 ,p3,p4,p5,p6;
    GridView grid;
    String[] menu = {
            "   Add Book",
            "   Update Book",
            "   Delete Book",
            "   Lend Book",
            "   Request Book",
            "   List My Books"
    };
    Integer[] icons = {
            R.drawable.add_book,
            R.drawable.update_book,
            R.drawable.del_book,
            R.drawable.lend_book,
            R.drawable.request_book,
            R.drawable.list_book
    };

    /*private static final String NameSpace3 = "http://tempuri.org/";
    public static final String URL3 = "http://10.0.3.2:63232/WebSite4/WebService.asmx";
    private static final String MethodName3 = "AddBook";
    private static final String SOAP_ACTION3 = "http://tempuri.org/AddBook";*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        // usrname.setText(getIntent().getExtras().getString("username"));
        // Intent intnt= getIntent();
        // String extra=intnt.getExtras("username",);
        grid = (GridView) findViewById(R.id.gridView1);
        // b1= (Button)findViewById(R.id.btn_addbook);
        // ET=(EditText) findViewById(R.id.editText);
        // ET2=(EditText) findViewById(R.id.author);
        // ET2=(EditText) findViewById(R.id.edition);
        // TV=(TextView) findViewById(R.id.textView);
        // usrname=(TextView)findViewById(R.id.usernameview);
        grid.setAdapter(new AdaptImages(this));
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                // TODO Auto-generated method stub

                Toast.makeText(Dashboard.this, menu[position], Toast.LENGTH_SHORT).show();
                Intent i;
                switch (position) {

                    case 0:
                        i = new Intent(Dashboard.this, AddBook.class);
                        break;
                    case 1:
                        i = new Intent(Dashboard.this, UpdateBook.class);
                        break;
                    case 2:
                        i = new Intent(Dashboard.this, DeleteBook.class);
                        break;
                    case 3:
                        i = new Intent(Dashboard.this, LendBook.class);
                        break;
                    case 4:
                        i = new Intent(Dashboard.this, SearchBook.class);
                        break;
                    case 5:
                        i = new Intent(Dashboard.this, ListBook.class);
                        break;


                        default:
                        i = null;

                }
                if (i != null) {
                    i.putExtra("method","filltable");
                    startActivity(i);
                }
                else
                    Toast.makeText(Dashboard.this, "File not created yet !!",
                            Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent me) {
        // TODO Auto-generated method stub
        return super.dispatchTouchEvent(me);
    }
    public class AdaptImages extends BaseAdapter{

        private Context context;

        public int getCount() {
            // TODO Auto-generated method stub
            return icons.length;
        }

        final public Object getItem(int position) {
            // TODO Auto-generated method stub
            return icons[position];
        }

        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public AdaptImages(Context c){
            context = c;
        }

        public View getView(int position, View view, ViewGroup parent) {
            // TODO Auto-generated method stub

            ImageView image;

            if (view == null) {

                image = new ImageView(context);
                image.setLayoutParams(new GridView.LayoutParams(150 , 150));
                image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                image.setPadding(8, 8, 8, 8);
            }
            else
                image = (ImageView)view;

            image.setImageResource(icons[position]);
            return image;
        }

    }



}

