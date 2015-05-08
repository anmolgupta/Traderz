package com.traderz.anmolgupta.traderz;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.traderz.anmolgupta.Content.CustomFields;
import com.traderz.anmolgupta.Content.UserContent;
import com.traderz.anmolgupta.Content.UserTest;
import com.traderz.anmolgupta.DynamoDB.DynamoDBManager;
import com.traderz.anmolgupta.userData.EmailMappingToFullName;
import com.traderz.anmolgupta.userData.UserConnection;
import com.traderz.anmolgupta.userData.UserContacts;

import java.util.HashMap;
import java.util.Map;


public class LoadingDataTest extends ActionBarActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState ) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_data_test);


        final EditText keyText = (EditText) findViewById(R.id.keyText);
//        keyText.setText(getIntent().getExtras().getString("email"));

        final EditText valueText1 = (EditText) findViewById(R.id.valueText1);

        final EditText valueText2 = (EditText) findViewById(R.id.valueText2);

        final EditText valueText3 = (EditText) findViewById(R.id.valueText3);

        final EditText valueText4 = (EditText) findViewById(R.id.valueText4);

        Button addContentButton = (Button) findViewById(R.id.add_content);

        Button addMapContentButton = (Button) findViewById(R.id.map_content);

        Button mainAdminPanelButton = (Button) findViewById(R.id.main_admin_panel);

        Button testButton = (Button) findViewById(R.id.test_button);


        mainAdminPanelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                startActivity(new Intent(LoadingDataTest.this, MainAdminNavigation.class));
            }
        });


        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {


                new SaveTestTask().execute();
            }
        });

        addMapContentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {

                new SaveConnectionsTask().execute(valueText1.getText().toString(),
                        valueText2.getText().toString(),
                        valueText3.getText().toString(),
                        valueText4.getText().toString(),
                        keyText.getText().toString());
            }
        });

        addContentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {

//                DynamoDBManager.saveObject(userContent);
                new SaveContentTask().execute(valueText1.getText().toString(),
                        valueText2.getText().toString(),
                        valueText3.getText().toString(),
                        valueText4.getText().toString(),
                        keyText.getText().toString());
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_loading_data_test, menu);
        return true;
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

    class SaveContentTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params ) {

            Map connections = new HashMap();

            connections.put("productName", params[0]);
            connections.put("productDescription", params[1]);
            connections.put("quantity", params[2]);
            connections.put("Price", params[3]);

            UserContent userContent = new UserContent(params[4],
                    new CustomFields(connections));

            DynamoDBManager.saveObject(userContent);

            return null;
        }
    }

    class SaveConnectionsTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params ) {

            Map connections = new HashMap();

            connections.put("one", params[0]);
            connections.put("two", params[1]);
            connections.put("three", params[2]);
            connections.put("four", params[3]);

            UserConnection userContent = new UserConnection(params[4],
                    new EmailMappingToFullName(connections));

            DynamoDBManager.saveObject(userContent);

            return null;
        }
    }

    class SaveTestTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params ) {

            Map<String, Integer> connections = new HashMap<String, Integer>();

            connections.put("one", 1);
            connections.put("two", 2);
            connections.put("three", 3);
            connections.put("four", 4);


            UserTest userTest = new UserTest();
            userTest.setEmail("anmol");
            userTest.setDemo(connections);

            DynamoDBManager.saveObject(userTest);

            return null;
        }
    }
}
