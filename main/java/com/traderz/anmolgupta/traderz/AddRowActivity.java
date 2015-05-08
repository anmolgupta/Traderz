package com.traderz.anmolgupta.traderz;

import android.app.Activity;
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

//    List<KeyValue> columnEntity;
    EditText [] columnTitles;
    EditText [] columnValues;

    LinkedHashMap<String,String> columnEntity;
    int customColumnCount;
    int definedColumnNumber;
    ListView listView;

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

        columnEntity = new LinkedHashMap<String,String>();
        columnEntity.put("Product Name","");
        columnEntity.put("Product Description","");
        columnEntity.put("Quantity","");
        columnEntity.put("Price","");
        columnEntity.put("Units","");

        definedColumnNumber = columnEntity.size();

    }

    void setAdapter() {

        listView.setAdapter(new MyAdapter(getActivity(),
                        android.R.layout.simple_list_item_1,
                        columnEntity));
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

        Button button = (Button)view.findViewById(R.id.tv1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {

                columnEntity.put("Custom"+customColumnCount++,"");

                setAdapter();
            }
        });

        setAdapter();
        return view;
    }

//
//    KeyValue getKeyValue(String key){
//
//        for(KeyValue keyValue: columnEntity){
//
//            if(keyValue.getKey().equals(key)) {
//                return keyValue;
//            }
//        }
//        return null;
//    }
//
//    void replaceKeyValue(KeyValue keyValue) {
//
//        for(KeyValue eachKeyValue: columnEntity) {
//
//            if(eachKeyValue.getKey().equals(keyValue.getKey())) {
//                eachKeyValue = keyValue;
//            }
//        }
//    }
//
//    class KeyValue{
//
//        private String key;
//        private String value;
//
//        public KeyValue() {
//
//        }
//
//        public KeyValue(String key, String value){
//
//            this.key = key;
//            this.value = value;
//        }
//
//        public String getKey(){
//
//            return key;
//        }
//
//        public String getValue(){
//
//            return value;
//        }
//
//        public void setKey(String key){
//
//            this.key = key;
//        }
//
//        public void setValue(String value){
//            this.value = value;
//        }
//
//        @Override
//        public boolean equals(Object obj) {
//
//            if (obj == null) {
//                return false;
//            }
//
//            if (getClass() != obj.getClass()) {
//                return false;
//            }
//
//            final KeyValue other = (KeyValue) obj;
//
//            if ((this.key == null) ? (other.key != null) : !this.key.equals(other.key)) {
//                return false;
//            }
//
//            return true;
//        }
//    }

    class MyAdapter extends ArrayAdapter<KeyValue> {

        LayoutInflater inflater;
        public MyAdapter( Context context, int resource,
                          List<KeyValue> objects ) {

            super(context, resource, objects);
            // TODO Auto-generated constructor stub
            columnTitles = new EditText[objects.size()];
            columnValues = new EditText[objects.size()];
            inflater = (LayoutInflater)context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView( final int position, View convertView,
                             ViewGroup parent ) {


            View row = inflater.inflate(R.layout.fragment_add_row, parent, false);

            columnTitles[position] = (EditText) row.findViewById(R.id.column_name);
            columnTitles[position].setEnabled(false);

            columnTitles[position].setText(columnEntity.get(position).key);

            Button editTextButton = (Button)row.findViewById(R.id.edit_text_button);

            if(position < definedColumnNumber) {
                editTextButton.setEnabled(false);
                editTextButton.setVisibility(View.INVISIBLE);
            }

            editTextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick( View v ) {

                    columnTitles[position].setEnabled(true);
                    columnTitles[position].requestFocus();
                    columnTitles[position].setSelection(
                            columnTitles[position].getText().length());
                    columnTitles[position].selectAll();
                }
            });

            columnValues[position] = (EditText) row.findViewById(R.id.editText);
            columnValues[position].setText(columnEntity.get(position).value);

            return row;
        }
    }
}
