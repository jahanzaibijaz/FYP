package com.example.jahanzeb.hybrary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class SearchBook extends Activity {

    EditText bookName;
    ImageView reques,search;
    public static String searchThisBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_book);

        initializeVariables();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchBook = new Intent(SearchBook.this, RequestBook.class);
                searchThisBook = bookName.getText().toString();
                startActivity(searchBook);
                finish();
            }
        });

        reques.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent requests = new Intent(SearchBook.this, RequestedBooks.class);
                startActivity(requests);
                finish();
            }
        });
    }

    private void initializeVariables() {
        search = (ImageView)findViewById(R.id.button1);
        reques = (ImageView)findViewById(R.id.button2);
        bookName = (EditText)findViewById(R.id.editText1);
        bookName.setHint("BookName");
    }

}
