package com.traderz.anmolgupta.traderz;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.traderz.anmolgupta.Content.CustomFields;
import com.traderz.anmolgupta.Content.UserContent;
import com.traderz.anmolgupta.DynamoDB.DynamoDBManager;

import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by anmolgupta on 27/04/15.
 */
public class AddRowActivity extends Fragment {

    EditText [] columnTitles;
    EditText [] columnValues;

    final String CUSTOM_COLUMN_NAME = "Custom";
    String userId;
    LinkedHashMap<String,String> columnEntity;
    int customColumnCount;
    int definedColumnNumber;
    ListView listView;
    List<KeyValue> list;
    UserContent userContent;
    @Override
    public void onCreate( Bundle savedInstanceState ) {

        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_row);

        customColumnCount = 1;

//        columnEntity = new ArrayList<KeyValue>();
//        columnEntity.add(new KeyValue("Product Name",""));
//        columnEntity.add(new KeyValue("Product Description",""));
//        columnEntity.add(new KeyValue("Quantity",""));
//        columnEntity.add(new KeyValue("Price",""));
//        columnEntity.add(new KeyValue("Units",""));

        SharedPreferences settings = getActivity().getPreferences(0);
        userId = settings.getString("email", "");

        columnEntity = new LinkedHashMap<String,String>();

        for(String field : CustomFields.getColumns())
            columnEntity.put(field,"");
//        columnEntity.put("Product Description","");
//        columnEntity.put("Quantity","");
//        columnEntity.put("Price","");
//        columnEntity.put("Units","");

        definedColumnNumber = columnEntity.size();

    }

    void setAdapter(){

        list = new ArrayList<KeyValue>();

        for(Map.Entry<String, String> entry: columnEntity.entrySet()) {
            list.add(new KeyValue(entry.getKey(), entry.getValue()));
        }

        listView.setAdapter(new MyAdapter(getActivity(),
                android.R.layout.simple_list_item_1,
                list));
    }

    public void populateMap() {
        columnEntity = new LinkedHashMap<String,String>();

        for(int i = 0; i < columnTitles.length; i++) {
            columnEntity.put(columnTitles[i].getText().toString().trim(),
                    columnValues[i].getText().toString().trim());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /* Updating the action bar title */
//        getActivity().getActionBar().setTitle(title);

        /* Getting the WebView target url */
//        String url = getArguments().getString("url");

		/* Creating view corresponding to the fragment */
        View view = inflater.inflate(R.layout.activity_add_row,
                container, false);

        listView = (ListView)view.findViewById(android.R.id.list);

        Button addRow = (Button) view.findViewById(R.id.add_row);
        addRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {

                populateMap();

                CustomFields customFields = new CustomFields(columnEntity);
                String validString = customFields.isValid();

                if(validString != null) {

                    return;
                }

                userContent = new UserContent(userId, customFields);

                if(!userContent.validateObject()) {

                    return;
                }

                new AddRow().execute();
            }
        });

        Button button = (Button)view.findViewById(R.id.tv1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {

                populateMap();

                int temp = customColumnCount;
                String customString = CUSTOM_COLUMN_NAME+temp;

                while(columnEntity.containsKey(customString)) {

                    customString = CUSTOM_COLUMN_NAME+(++temp);
                }

                ++temp;

                columnEntity.put(customString, "");

                customColumnCount = temp;

                setAdapter();
            }
        });

        setAdapter();
        return view;
    }

    class KeyValue {
        String key;
        String value;

        public KeyValue() {

        }

        public KeyValue(String key, String value) {

            this.key = key;
            this.value = value;
        }

        public String getKey() {

            return key;
        }

        public void setKey( String key ) {

            this.key = key;
        }

        public String getValue() {

            return value;
        }

        public void setValue( String value ) {

            this.value = value;
        }
    }

    class MyAdapter extends ArrayAdapter<KeyValue> {

        LayoutInflater inflater;
        List<KeyValue> objects;

        public MyAdapter( Context context, int resource,
                          List<KeyValue> objects ) {

            super(context, resource, objects);
            // TODO Auto-generated constructor stub
            columnTitles = new EditText[objects.size()];
            columnValues = new EditText[objects.size()];
            inflater = (LayoutInflater)context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            this.objects = objects;
        }

        @Override
        public View getView( final int position, View convertView,
                             ViewGroup parent ) {

            View row = inflater.inflate(R.layout.fragment_add_row, parent, false);

            columnTitles[position] = (EditText) row.findViewById(R.id.column_name);
            columnTitles[position].setEnabled(false);

            columnTitles[position].setText(objects.get(position).key);

            Button editTextButton = (Button)row.findViewById(R.id.edit_text_button);
            editTextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick( View v ) {

                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.dialog_change_column_name);
                    dialog.setTitle("Change Column Name");

                    final EditText text = (EditText) dialog.findViewById(R.id.change);
                    text.setText(columnTitles[position].getText().toString());
                    text.selectAll();

                    Button button = (Button) dialog.findViewById(R.id.change_button);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick( View v ) {
                            String newColumn = text.getText().toString();

                            if(columnEntity.containsKey(newColumn)) {

                                AlertDialog.Builder builder =
                                        new AlertDialog.Builder(getActivity());
                                builder.setMessage("The Column Name is Already Present")
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                                builder.create().show();

                                return;
                            }
                            objects.get(position).key = text.getText().toString();
                            dialog.dismiss();

                        }
                    });

                    dialog.show();
                }
            });

            if(position < definedColumnNumber) {
                editTextButton.setEnabled(false);
                editTextButton.setVisibility(View.INVISIBLE);
            }

            columnValues[position] = (EditText) row.findViewById(R.id.editText);
            columnValues[position].setText(objects.get(position).value);

            return row;
        }


    }

    class AddRow extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground( Void... params ) {

            DynamoDBManager.saveObject(userContent);
            return null;
        }
    }

}
