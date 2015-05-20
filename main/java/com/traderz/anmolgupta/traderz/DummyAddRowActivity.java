package com.traderz.anmolgupta.traderz;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedQueryList;
import com.traderz.anmolgupta.Content.CustomFields;
import com.traderz.anmolgupta.Content.CustomFieldsEnum;
import com.traderz.anmolgupta.Content.UserContent;
import com.traderz.anmolgupta.DynamoDB.DynamoDBManager;
import com.traderz.anmolgupta.SQLLiteClasses.UserContentAdapter;

import java.util.HashMap;
import java.util.Map;


public class DummyAddRowActivity extends Fragment {

    String privateId;

    EditText editText2,editText3,editText4, editText5,editText6;

    Context context;

    ProgressDialog pd;
    @Override
    public void onCreate( Bundle savedInstanceState ) {

        super.onCreate(savedInstanceState);

        SharedPreferences settings = getActivity().getSharedPreferences("Traderz", 0);
        privateId = settings.getString("email", "");
        context = getActivity();

        pd=new ProgressDialog(context);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Loading");
        pd.setCancelable(true);
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
        View view = inflater.inflate(R.layout.activity_dummy_add_row,
                container, false);

          editText2 = (EditText) view.findViewById(R.id.editText2);
          editText3 = (EditText) view.findViewById(R.id.editText3);
          editText4 = (EditText) view.findViewById(R.id.editText4);
          editText5 = (EditText) view.findViewById(R.id.editText5);
          editText6 = (EditText) view.findViewById(R.id.editText6);

        Button button = (Button) view.findViewById(R.id.add_row);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                pd.show();

                new AddRowActivityTask().execute();
            }
        });

        return view;
    }

    class AddRowActivityTask extends AsyncTask<Void,Void,String> {

        @Override
        protected String doInBackground( Void... params ) {

            Map<String, String> map = new HashMap<String, String>();
            map.put(CustomFieldsEnum.PRODUCT_NAME.getName(), editText2.getText().toString());
            map.put(CustomFieldsEnum.PRODUCT_DESCRIPTION.getName(), editText3.getText().toString());
            map.put(CustomFieldsEnum.QUANTITY.getName(), editText4.getText().toString());
            map.put(CustomFieldsEnum.PRICE.getName(), editText5.getText().toString());
            map.put(CustomFieldsEnum.UNITS.getName(), editText6.getText().toString());

            CustomFields customFields = new CustomFields(map);
            String validateString = customFields.isValid();

            if ( validateString != null ) {
                return validateString;

            }
            UserContent userContent =
                    new UserContent(privateId, customFields);


            DynamoDBManager.saveObject(userContent);

            UserContentAdapter adapter = new UserContentAdapter(context,privateId);

            adapter.addUserConent(userContent);

            return null;
        }

        @Override
        protected void onPostExecute( String str ) {

            pd.cancel();
            if(str != null ){

                CustomDialogBox.showToast(context, "Error: "+str);

            }else {
                CustomDialogBox.showToast(context, "Added successfully");
            }

        }
    }


}
