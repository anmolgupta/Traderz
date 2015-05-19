package com.traderz.anmolgupta.traderz;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.traderz.anmolgupta.Content.CustomFields;
import com.traderz.anmolgupta.Content.CustomFieldsEnum;
import com.traderz.anmolgupta.Content.UserContent;
import com.traderz.anmolgupta.DynamoDB.DynamoDBManager;
import com.traderz.anmolgupta.SQLLiteClasses.UserContentAdapter;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by anmolgupta on 19/05/15.
 */
public class DummyViewEditRowActivity extends Fragment {

    public static String ID = "ID";
    public static String ROW_ID = "ROW_ID";
    public static String MAP = "MAP";

    String privateId, importedId, importedRowId;

    EditText editText2,editText3,editText4, editText5,editText6;

    final String CUSTOM_COLUMN_NAME = "Custom";

    boolean originalUser;

    Context context;

    UserContent userContent;

    ProgressDialog pd;
    @Override
    public void onCreate( Bundle savedInstanceState ) {

        super.onCreate(savedInstanceState);

        SharedPreferences settings = this.getActivity().getSharedPreferences("Traderz", 0);
        privateId = settings.getString("email", "");

        importedRowId = getArguments().getString(ROW_ID);
        importedId = getArguments().getString(ID);

        if(privateId.equalsIgnoreCase(importedId))
            originalUser = true;
        else
            originalUser = false;


        context = getActivity();
        pd=new ProgressDialog(context);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Loading");
        pd.setCancelable(true);
        pd.setIndeterminate(true);

        UserContentAdapter userContentAdapter = new UserContentAdapter(getActivity(), importedId);

        userContent = userContentAdapter.getUserContentById(importedRowId);
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

        if(!originalUser) {
            editText2.setEnabled(false);
            editText3.setEnabled(false);
            editText4.setEnabled(false);
            editText5.setEnabled(false);
            editText6.setEnabled(false);
            button.setEnabled(false);
            button.setVisibility(View.INVISIBLE);
        }




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


            userContent.setId(importedRowId);

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
