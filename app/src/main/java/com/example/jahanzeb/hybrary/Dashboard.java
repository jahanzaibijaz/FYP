package com.example.jahanzeb.hybrary;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class Dashboard extends AppCompatActivity implements CustomDrawer.NavigationDrawerCallbacks {

    Activity me = this;
    String[] titles;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private CustomDrawer myDrawer;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        titles = me.getResources().getStringArray(R.array.dashboard_items);
        Log.d("Nawaz","Worked fine");
        Log.d("NAWAZ","Array: "+titles.toString());
        myDrawer = (CustomDrawer)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        myDrawer.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        mTitle = titles[number-1];
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!myDrawer.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.dashboard, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((Dashboard) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
//    GridView grid;
//    Integer[] icons = {
//            R.drawable.add_book,
//            R.drawable.update_book,
//            R.drawable.del_book,
//            R.drawable.lend_book,
//            R.drawable.request_book,
//            R.drawable.list_book
//    };
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_dashboard);
//
//        grid = (GridView) findViewById(R.id.gridView1);
//
//        grid.setAdapter(new AdaptImages(this));
//        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            public void onItemClick(AdapterView<?> parent, View view, int position,
//                                    long id) {
//                // TODO Auto-generated method stub
//
////                Toast.makeText(Dashboard.this, menu[position], Toast.LENGTH_SHORT).show();
//                Intent i;
//                switch (position) {
//
//                    case 0:
//                        i = new Intent(Dashboard.this, AddBook.class);
//                        break;
//                    case 1:
//                        i = new Intent(Dashboard.this, UpdateBook.class);
//                        break;
//                    case 2:
//                        i = new Intent(Dashboard.this, DeleteBook.class);
//                        break;
//                    case 3:
//                        i = new Intent(Dashboard.this, LendBook.class);
//                        break;
//                    case 4:
//                        i = new Intent(Dashboard.this, SearchBook.class);
//                        break;
//                    case 5:
//                        i = new Intent(Dashboard.this, ListBook.class);
//                        break;
//
//                        default:
//                        i = null;
//
//                }
//                if (i != null) {
//                    i.putExtra("method","filltable");
//                    startActivity(i);
//                }
//                else
//                    Toast.makeText(Dashboard.this, "File not created yet !!",
//                            Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent me) {
//        // TODO Auto-generated method stub
//        return super.dispatchTouchEvent(me);
//    }
//    public class AdaptImages extends BaseAdapter{
//
//        private Context context;
//
//        public int getCount() {
//            // TODO Auto-generated method stub
//            return icons.length;
//        }
//
//        final public Object getItem(int position) {
//            // TODO Auto-generated method stub
//            return icons[position];
//        }
//
//        public long getItemId(int position) {
//            // TODO Auto-generated method stub
//            return position;
//        }
//
//        public AdaptImages(Context c){
//            context = c;
//        }
//
//        public View getView(int position, View view, ViewGroup parent) {
//            // TODO Auto-generated method stub
//
//            ImageView image;
//
//            if (view == null) {
//
//                image = new ImageView(context);
//                image.setLayoutParams(new GridView.LayoutParams(150 , 150));
//                image.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                image.setPadding(8, 8, 8, 8);
//            }
//            else
//                image = (ImageView)view;
//
//            image.setImageResource(icons[position]);
//            return image;
//        }
//
//    }
//
//
//
//}
//
