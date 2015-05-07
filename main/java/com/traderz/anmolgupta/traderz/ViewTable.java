package com.traderz.anmolgupta.traderz;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedQueryList;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.traderz.anmolgupta.Content.CustomFields;
import com.traderz.anmolgupta.Content.UserContent;
import com.traderz.anmolgupta.DynamoDB.DynamoDBManager;
import com.traderz.anmolgupta.userData.UserConnection;
import com.traderz.anmolgupta.userData.UserContacts;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ViewTable extends Fragment {

    public static final String TITLE = "title";
    public static final String ID = "id";
    private ViewTableCallbacks mCallbacks;
    private List<UserContent> userContents;

    @Override
    public void onAttach( Activity activity ) {

        super.onAttach(activity);
        try {
            mCallbacks = (ViewTableCallbacks) activity;
        } catch ( ClassCastException e ) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onCreate( Bundle savedInstanceState ) {

        super.onCreate(savedInstanceState);
        userContents = new ArrayList<UserContent>();
        new GetData().execute();

    }
        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /* Updating the action bar title */
//        getActivity().getActionBar().setTitle(title);

        /* Getting the WebView target url */
//        String url = getArguments().getString("url");

		/* Creating view corresponding to the fragment */
        View view = inflater.inflate(R.layout.activity_view_table,
                container, false);

        //TODO:: Get the list of CustomFields from UserContent
        List<CustomFields> customFields  = new ArrayList<CustomFields>();

        ListView listView = (ListView)view.findViewById(android.R.id.list);

        listView.setAdapter(new MyAdapter(getActivity(),
                android.R.layout.simple_list_item_1,
                customFields));
		/* Initializing and loading url in WebView */

        Button addRow = (Button)view.findViewById(R.id.add_row);

            addRow.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick( View v ) {
                    mCallbacks.onAddRowInTableCallbacks();
                }
        });

        return view;
    }

    class MyAdapter extends ArrayAdapter<CustomFields>{
        int anmol;
        List<CustomFields> content;
        LayoutInflater inflator;
        public MyAdapter( Context context, int resource,
                         List<CustomFields> objects ) {

            super(context, resource, objects);
            content= objects;
            inflator =
                    (LayoutInflater)context.
                            getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public View getView( final int position, View convertView,
                             ViewGroup parent ) {
//            LayoutInflater inflater = (LayoutInflater) getSystemService(
//                    Context.LAYOUT_INFLATER_SERVICE);

            View row = inflator.inflate(R.layout.fragment_table_row, parent, false);

            final Map<String,String> map = content.get(position).getMap();

            TextView productName = (TextView) row.findViewById(R.id.product_name);
            productName.setText(map.get("productName"));


            TextView productDescription = (TextView) row.findViewById(R.id.product_description);
            productDescription.setText(map.get("productDescription"));

            final String id = map.get("id");
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick( View v ) {

                    mCallbacks.onViewTableCallbacks(id, new HashMap<String,String>(map));
                }
            });
            return row;
        }

    }

    public static interface ViewTableCallbacks {

        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onViewTableCallbacks(String id, HashMap<String,String> map);
        void onAddRowInTableCallbacks();
    }

    class GetData extends AsyncTask<Void,Void,PaginatedQueryList<UserContent>> {

        @Override
        protected PaginatedQueryList<UserContent> doInBackground( Void... params ) {

            UserContent userContent = new UserContent();
            userContent.setUserEmail("anmol007gupta@gmail.com");

            Long timestamp = new Date().getTime();

            Condition rangeKeyCondition = new Condition()
                    .withComparisonOperator(ComparisonOperator.LT.toString())
                    .withAttributeValueList(new AttributeValue().withN("" + timestamp));

            DynamoDBQueryExpression queryExpression = new DynamoDBQueryExpression()
                    .withHashKeyValues(userContent)
                    .withRangeKeyCondition("UpdatedTimestamp", rangeKeyCondition)
                    .withConsistentRead(false);
//                    .withIndexName("UpdatedTimestamp-index");

            PaginatedQueryList<UserContent> result =
                    DynamoDBManager.getQueryResult(UserContent.class, queryExpression);

//            PaginatedScanList<UserContent> result1 =
//                    DynamoDBManager.getScanResult(UserContent.class, new DynamoDBScanExpression());

            return result;
        }

        @Override
        protected void onPostExecute(PaginatedQueryList<UserContent> userConnection) {

            if(userConnection != null) {

                //TODO:: Do something

            }
        }
    }
}
