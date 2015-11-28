package com.example.jahanzeb.hybrary;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jahanzeb on 11/15/2015.
 */
public class SingleBookAdapter extends ArrayAdapter<String> {

    // Activity for inflator
    private Activity me;

    // Array Lists for list population
    private ArrayList<String>
        book,
        author,
        edition;


    public SingleBookAdapter(Activity context, ArrayList<String> resource1, ArrayList<String> resource2, ArrayList<String> resource3) {
        super(context, R.layout.single_book ,resource1);

        me = context;
        book = resource1;
        author = resource2;
        edition = resource3;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // getting INFLATOR from Activity Context (Where thi adapter is called from)
        LayoutInflater inflator = me.getLayoutInflater();
        // create single row for the list
        View rowView = inflator.inflate(R.layout.single_book, null, false);

        // setting the UI for each row
        TextView
                bookName = (TextView)rowView.findViewById(R.id.bookName),
                authorName = (TextView)rowView.findViewById(R.id.authorName),
                editionNumber = (TextView)rowView.findViewById(R.id.edition);
        bookName.setText(book.get(position));
        authorName.setText(author.get(position));
        editionNumber.setText(edition.get(position));

        // return this row
        return rowView;
    }
}
