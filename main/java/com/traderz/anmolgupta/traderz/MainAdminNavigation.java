package com.traderz.anmolgupta.traderz;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;

import com.traderz.anmolgupta.DynamoDB.DynamoDBManager;
import com.traderz.anmolgupta.userData.UserConnection;
import com.traderz.anmolgupta.userData.UserContacts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainAdminNavigation extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        ViewTable.ViewTableCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private String _email;
    private String _fullName;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin_navigation);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        SharedPreferences settings = getSharedPreferences("Traderz", 0);
        _email = settings.getString("email", "");
        _fullName = settings.getString("fullName", "");
    }


    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onNavigationDrawerItemSelected( int position ) {
        // update the main content by replacing fragments
        setFragment(getFragment(position));
    }

    public void setFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();

    }

    public Fragment getFragment( int position ) {

            Fragment fragment = null;
            Bundle args = new Bundle();

            switch (position) {
                case 0:  fragment = new ViewTable();
                    args.putString(ViewTable.TITLE, "anmol");
                    args.putString(ViewTable.ID, _email);
                    break;
                case 1:  fragment = new AddConnection();
                    break;
                default:
                    fragment = new ViewTable();
                    args.putString(ViewTable.TITLE, "anmol");
                    args.putString(ViewTable.ID, "anmol007gupta@gmail.com");
                    break;
            }

            fragment.setArguments(args);

            return fragment;
    }


    public void onSectionAttached( int number ) {
        //TODO:: to be modified
        switch ( number ) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = "Add Connection";
                break;
        }
    }

    public void restoreActionBar() {

        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {

        if ( !mNavigationDrawerFragment.isDrawerOpen() ) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main_admin_navigation, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if ( id == R.id.action_settings ) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewTableCallbacks( String rowId, String id, HashMap<String,String> map) {

        Bundle args = new Bundle();
        args.putString(ViewEditRowActivity.ID, id);
        args.putString(ViewEditRowActivity.ROW_ID, rowId);
        args.putSerializable(ViewEditRowActivity.MAP,  map);

        Fragment fragment = new ViewEditRowActivity();
        fragment.setArguments(args);

        setFragment(fragment);


    }

    @Override
    public void onAddRowInTableCallbacks() {

        Fragment fragment = new DummyAddRowActivity();
        setFragment(fragment);

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
        public static PlaceholderFragment newInstance( int sectionNumber ) {

            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {

        }

        @Override
        public View onCreateView( LayoutInflater inflater, ViewGroup container,
                                  Bundle savedInstanceState ) {

            View rootView = inflater.inflate(R.layout.fragment_main_admin_navigation, container, false);
            return rootView;
        }

        @Override
        public void onAttach( Activity activity ) {

            super.onAttach(activity);
            ((MainAdminNavigation) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }

    }
}
