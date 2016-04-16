package com.example.jahanzeb.hybrary.Helpers;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jahanzeb.hybrary.R;

/**
 * Created by jahanzeb on 10/04/2016.
 */
public class NavigationItemAdapter extends ArrayAdapter<String>{

    Context me;
    String[] items;
    Integer[] icons;

    public NavigationItemAdapter(Context context, String[] resource, Integer[] images) {
        super(context, R.layout.single_nav_item, resource);
        me = context;
        items = resource;
        icons = images;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = View.inflate(me, R.layout.single_nav_item, null);
        Log.e("Nawaz",position+") Ïcon: "+icons[position]);
        ((TextView)rowView.findViewById(R.id.nav_text)).setText(items[position]);
        ((ImageView)rowView.findViewById(R.id.nav_icon)).setImageResource(icons[position]);

        return rowView;
    }
}
