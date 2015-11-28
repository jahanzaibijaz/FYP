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




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);



        grid = (GridView) findViewById(R.id.gridView1);

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

