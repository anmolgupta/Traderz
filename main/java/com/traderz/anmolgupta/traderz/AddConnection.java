package com.traderz.anmolgupta.traderz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.traderz.anmolgupta.DynamoDB.DynamoDBManager;
import com.traderz.anmolgupta.userData.UserConnection;
import com.traderz.anmolgupta.userData.UserData;
import com.traderz.anmolgupta.utilities.GenericConverters;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * create an instance of this fragment.
 */
public class  AddConnection extends Fragment {

    String contactId;
    String privateId;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_add_connection, container, false);

		/* Initializing and loading url in WebView */
        final EditText connectionUsername = (EditText) v.findViewById(R.id.connectionUserName);

        final Button connectionButton = (Button) v.findViewById(R.id.connectionButton);

        context = getActivity();

        SharedPreferences settings = getActivity().getSharedPreferences("Traderz", 0);
        privateId = settings.getString("email", "");

        connectionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick( View v ) {

                contactId = connectionUsername.getText().toString();
                new AddConnectionTask().execute();
            }
        });
        return v;
    }

    enum ContactStatus {CONTACT_NOT_PRESENT, CONTACT_ALREADY_PRESENT,CONTACT_ADDED};

    class AddConnectionTask extends AsyncTask<Void, Void, ContactStatus> {

        @Override
        protected ContactStatus doInBackground( Void... params ) {


            UserData contactData = new UserData(contactId);

            contactData = DynamoDBManager.loadObject(contactData);

            if(contactData == null)
                return ContactStatus.CONTACT_NOT_PRESENT;

            String contactFullName = contactData.getFullName();

            contactId = contactData.getEmail();

            UserConnection userConnection = new UserConnection();
            userConnection.setUserId(privateId);
            userConnection.setContactId(contactId);

            userConnection = DynamoDBManager.loadObject(userConnection);

            if(userConnection != null)
                return ContactStatus.CONTACT_ALREADY_PRESENT;

            userConnection = new UserConnection(privateId, contactId);

            DynamoDBManager.saveObject(userConnection);

            SharedPreferences settings = context.getSharedPreferences("Traderz", 0);

            String privateMap = settings.getString("userConnection", "");

            SharedPreferences.Editor editor = settings.edit();

            Map<String,String> userConnectionMap = new HashMap<>();

            if(privateMap ==null || privateMap.equals("")) {

                userConnectionMap.put(contactId, contactFullName);

            }else {

                userConnectionMap = GenericConverters.convertStringToObject(privateMap,Map.class);
                userConnectionMap.put(contactId, contactFullName);

            }

            editor.putString("userConnection", GenericConverters.convertObjectToString(userConnectionMap));
            editor.commit();

            return ContactStatus.CONTACT_ADDED;

        }

        protected void onPostExecute(ContactStatus contactStatus) {

            switch(contactStatus) {
                case CONTACT_ADDED:
                    break;
                case CONTACT_ALREADY_PRESENT:
                    break;
                case CONTACT_NOT_PRESENT:
                    break;

            }
        }
    }

}
