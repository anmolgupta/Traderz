package com.traderz.anmolgupta.traderz;

import android.app.Activity;
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

import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by anmolgupta on 27/04/15.
 */
public class AddRowActivity extends ListActivity {

    List<KeyValue> columnEntity;
    int customColumnCount;
    int definedColumnNumber;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_row);

        customColumnCount = 1;

        columnEntity = new ArrayList<KeyValue>();
        columnEntity.add(new KeyValue("Product Name",""));
        columnEntity.add(new KeyValue("Product Description",""));
        columnEntity.add(new KeyValue("Quantity",""));
        columnEntity.add(new KeyValue("Price",""));
        columnEntity.add(new KeyValue("Units",""));

        definedColumnNumber = columnEntity.size();

        TextView textView = (TextView)findViewById(R.id.tv1);

        Button addRowButton = (Button)findViewById(R.id.tv1);

//        listView = (ListView)findViewById(android.R.id.list);

        addRowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {

                columnEntity.add(new KeyValue("Custom"+customColumnCount,""));

                setAdapter();

                customColumnCount++;

            }
        });

        setAdapter();
    }

    void setAdapter() {

//        listView.
                setListAdapter(new MyAdapter(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                columnEntity));
    }

    KeyValue getKeyValue(String key){

        for(KeyValue keyValue: columnEntity){

            if(keyValue.getKey().equals(key)) {
                return keyValue;
            }
        }
        return null;
    }

    void replaceKeyValue(KeyValue keyValue) {

        for(KeyValue eachKeyValue: columnEntity) {

            if(eachKeyValue.getKey().equals(keyValue.getKey())) {
                eachKeyValue = keyValue;
            }
        }
    }

    class KeyValue{

        private String key;
        private String value;

        public KeyValue() {

        }

        public KeyValue(String key, String value){

            this.key = key;
            this.value = value;
        }

        public String getKey(){

            return key;
        }

        public String getValue(){

            return value;
        }

        public void setKey(String key){

            this.key = key;
        }

        public void setValue(String value){
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {

            if (obj == null) {
                return false;
            }

            if (getClass() != obj.getClass()) {
                return false;
            }

            final KeyValue other = (KeyValue) obj;

            if ((this.key == null) ? (other.key != null) : !this.key.equals(other.key)) {
                return false;
            }

            return true;
        }
    }

    class MyAdapter extends ArrayAdapter<KeyValue> {

        public MyAdapter( Context context, int resource,
                          int textViewResourceId, List<KeyValue> objects ) {

            super(context, resource, textViewResourceId, objects);
            // TODO Auto-generated constructor stub
//            content= objects;
        }

        @Override
        public View getView( final int position, View convertView,
                             ViewGroup parent ) {

            LayoutInflater inflater = (LayoutInflater) getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);

            View row = inflater.inflate(R.layout.fragment_add_row, parent, false);

            final EditText columnName = (EditText) row.findViewById(R.id.column_name);
            columnName.setEnabled(false);

            columnName.setText(columnEntity.get(position).key);

            Button editTextButton = (Button)row.findViewById(R.id.edit_text_button);
//            editTextButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick( View v ) {
//                    columnName.setEnabled(true);
//                    columnName.requestFocus();
//                }
//            });

            if(position < definedColumnNumber) {
                editTextButton.setEnabled(false);
                editTextButton.setVisibility(View.INVISIBLE);
            }

//            columnName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//
//                String originalValue;
//                EditText editText1;
//
//                @Override
//                public void onFocusChange( View v, boolean hasFocus ) {
//
//                    editText1 = (EditText)v;
//
//                    if(hasFocus) {
//
//                        originalValue =  editText1.getText().toString();
//                        return;
//                    }
//
//                    if(!hasFocus) {
//
//                        String newColumnName = editText1.getText().toString();
//
//                        if(newColumnName.equals(originalValue)){
//                            v.setEnabled(false);
//                            v.clearFocus();
//                            return;
//                        }
//
//                        KeyValue keyValue = new KeyValue(newColumnName,
//                                columnEntity.get(position).value);
//
//
//                        if(columnEntity.contains(keyValue)) {
//
//                            AlertDialog alertDialog = new AlertDialog.Builder(AddRowActivity.this).create();
//                            alertDialog.setTitle("Alert");
//                            alertDialog.setMessage("Alert message to be shown");
//                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                                    new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int which) {
//
//                                            editText1.setText(originalValue);
//                                            editText1.requestFocus();
//                                            dialog.dismiss();
//                                        }
//                                    });
//                            alertDialog.show();
//
//                        } else {
//
//                            replaceKeyValue(keyValue);
//                            v.setEnabled(false);
//                            v.clearFocus();
////                            setAdapter();
//
//                        }
//                    }
//
//                }
//            });

            EditText columnValue = (EditText) row.findViewById(R.id.editText);
            columnValue.setText(columnEntity.get(position).value);

            //saving the value
            columnValue.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange( View v, boolean hasFocus ) {

                    if(!hasFocus) {

                        EditText editText1 = (EditText)v;

                        columnEntity.get(position).value = editText1.getText().toString();

                    }
                }
            });

            return row;
        }
    }
}
