package com.example.jahanzeb.hybrary;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jahanzeb on 05/12/2015.
 */
public class LendBookAdaptor extends ArrayAdapter <String> {
    private Activity mee;
    private ArrayList<String>
            book,
            author,
            edition,
            Borrowername;
    public LendBookAdaptor  (Activity context, ArrayList<String> resource1, ArrayList<String> resource2, ArrayList<String> resource3,ArrayList<String> resource4) {
        super(context, R.layout.lendbookinfo, resource1);

        mee = context;
        book = resource1;
        author = resource2;
        edition = resource3;
        Borrowername = resource4;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // getting INFLATOR from Activity Context (Where thi adapter is called from)
        LayoutInflater inflator = mee.getLayoutInflater();
        // create single row for the list
        View rowView = inflator.inflate(R.layout.lendbookinfo, null, false);

        // setting the UI for each row
        TextView
                bookName = (TextView)rowView.findViewById(R.id.bookName),
                authorName = (TextView)rowView.findViewById(R.id.authorName),
                editionNumber = (TextView)rowView.findViewById(R.id.edition),
                Borrwrnme=(TextView)rowView.findViewById(R.id.borrower);

        bookName.setText(book.get(position));
        authorName.setText(author.get(position));
        editionNumber.setText(edition.get(position));
        Borrwrnme.setText(Borrowername.get(position));

        // return this row
        return rowView;
    }

}
