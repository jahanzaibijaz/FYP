package com.example.jahanzeb.hybrary;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jahanzeb on 17/04/2016.
 */
public class Feedback_Adapter extends ArrayAdapter<String> {
    // Activity for inflator
    private Activity me;
    // Array Lists for list population
    private ArrayList<String>
            borrowername,
            rating,
            comments;

    public  Feedback_Adapter(Activity context,ArrayList<String> resource1,ArrayList<String> resource2,ArrayList<String> resource3)
    {
        super(context,R.layout.comments,resource1);
        me = context;
        borrowername = resource1;
        rating = resource2;
        comments = resource3;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // getting INFLATOR from Activity Context (Where thi adapter is called from)
        LayoutInflater inflator = me.getLayoutInflater();
        // create single row for the list
        View rowView = inflator.inflate(R.layout.comments, null, false);

        // setting the UI for each row
        TextView   Borrower__Name = (TextView)rowView.findViewById(R.id.Borrower_Name);
        RatingBar ratingBar=(RatingBar)rowView.findViewById(R.id.ratingBar);
        EditText ET_comments=(EditText)rowView.findViewById(R.id.ET_comments);

        Borrower__Name.setText(borrowername.get(position));
        ET_comments.setText(comments.get(position));

        ratingBar.setMax(10);
        ratingBar.setProgress(Integer.parseInt(rating.get(position)));

        //Return
        return  rowView;

    }
}
