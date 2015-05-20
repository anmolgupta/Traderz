package com.traderz.anmolgupta.traderz;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.traderz.anmolgupta.Content.CustomFields;
import com.traderz.anmolgupta.Content.UserContent;
import com.traderz.anmolgupta.DynamoDB.DynamoDBManager;
import com.traderz.anmolgupta.SQLLiteClasses.UserContentAdapter;

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
    private Context context;
    private ListView listView;

    private String privateId,importedRowId,importedId;

    boolean originalUser = false;

    ProgressDialog pd;

    SwipeRefreshLayout mSwipeRefreshLayout;
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

        context = getActivity();

        SharedPreferences settings = getActivity().getSharedPreferences("Traderz", 0);
        privateId = settings.getString("email", "");

        importedRowId = getArguments().getString(TITLE);
        importedId = getArguments().getString(ID);

        if(importedId.equals(privateId))
            originalUser = true;

        pd=new ProgressDialog(context);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.setIndeterminate(true);

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

        listView = (ListView)view.findViewById(android.R.id.list);

		/* Initializing and loading url in WebView */

        Button addRow = (Button)view.findViewById(R.id.add_row);

            addRow.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick( View v ) {
                    mCallbacks.onAddRowInTableCallbacks();
                }
        });

        if(!originalUser){
            addRow.setEnabled(false);
            addRow.setVisibility(View.INVISIBLE);
        }

        mSwipeRefreshLayout = (SwipeRefreshLayout)
                view.findViewById(R.id.activity_main_swipe_refresh_layout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new GetData().execute();
            }
        });

        UserContentAdapter userContentAdapter = new UserContentAdapter(context, importedId);

            List<UserContent> userContentList = userContentAdapter.getUserContentByQuery("Select * from <tableName> order by "
                    +UserContentAdapter.UPDATE_TIMESTAMP);

            if(userContentList.isEmpty()) {
                pd.show();
                new GetData().execute();
                setAdapter(null);
            }
            else
                setAdapter(userContentList);

        return view;
    }

    public void setAdapter(List<UserContent> userContents1) {

        if(userContents1 != null) {

            userContents.addAll(userContents1);
        }


        listView.setAdapter(new MyAdapter(getActivity(),
                android.R.layout.simple_list_item_1,
                userContents));
    }

    class MyAdapter extends ArrayAdapter<UserContent>{

        List<UserContent> content;
        LayoutInflater inflator;
        public MyAdapter( Context context, int resource,
                         List<UserContent> objects ) {

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

            final Map<String,String> map = content.get(position).getCustomFields().getMap();

            List<String> list = CustomFields.getColumns();
            TextView productName = (TextView) row.findViewById(R.id.product_name);
            productName.setText(map.get(list.get(0)));


            TextView productDescription = (TextView) row.findViewById(R.id.product_description);
            productDescription.setText(map.get(list.get(1)));


            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick( View v ) {

                    mCallbacks.onViewTableCallbacks(content.get(position).getId(),
                            content.get(position).getUserEmail(), new HashMap<String,String>(map));
                }
            });
            return row;
        }

    }

    public static interface ViewTableCallbacks {

        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onViewTableCallbacks(String rowId, String id, HashMap<String,String> map);
        void onAddRowInTableCallbacks();
    }

    class GetData extends AsyncTask<Void,Void,PaginatedQueryList<UserContent>> {

        @Override
        protected PaginatedQueryList<UserContent> doInBackground( Void... params ) {

            UserContent userContent = new UserContent();
            userContent.setUserEmail(importedId);

            UserContentAdapter userContentAdapter = new UserContentAdapter(context,importedId);

                Long timestamp = null;

                Cursor cursor =
                        userContentAdapter.fireRandomQuery("select max("+ UserContentAdapter.UPDATE_TIMESTAMP+") from <tableName>");

                Condition rangeKeyCondition = null;
                if(cursor == null) {

                    timestamp = new Date().getTime();
                    rangeKeyCondition = new Condition()
                            .withComparisonOperator(ComparisonOperator.LT.toString())
                            .withAttributeValueList(new AttributeValue().withN("" + timestamp));

                }else{

                    timestamp = cursor.getLong(0);

                    if(timestamp == null || timestamp == 0) {

                        timestamp = new Date().getTime();
                        rangeKeyCondition = new Condition()
                                .withComparisonOperator(ComparisonOperator.LT.toString())
                                .withAttributeValueList(new AttributeValue().withN("" + timestamp));

                    }else {

                        rangeKeyCondition = new Condition()
                                .withComparisonOperator(ComparisonOperator.GT.toString())
                                .withAttributeValueList(new AttributeValue().withN("" + timestamp));
                    }
                }




            DynamoDBQueryExpression queryExpression = new DynamoDBQueryExpression()
                    .withHashKeyValues(userContent)
                    .withRangeKeyCondition("UpdatedTimestamp", rangeKeyCondition)
                    .withConsistentRead(false);

            PaginatedQueryList<UserContent> result =
                    DynamoDBManager.getQueryResult(UserContent.class, queryExpression);

            return result;
        }

        @Override
        protected void onPostExecute(PaginatedQueryList<UserContent> userConnection) {

            if(pd.isShowing())
                pd.cancel();

            if(mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
            }

            List<UserContent> userContents1 = new ArrayList<>();
            if(userConnection != null) {

                UserContentAdapter userContentAdapter = new UserContentAdapter(context,importedId);

                for(UserContent userContent : userConnection) {

                    userContents1.add(userContent);

                    userContentAdapter.addUserConent(userContent);
                }

                setAdapter(userContents1);
            }
        }
    }
}
