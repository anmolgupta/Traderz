package com.traderz.anmolgupta.traderz;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;


public class EditTextActivity extends ActionBarActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState ) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text);


        final EditText editText = (EditText) findViewById(R.id.locked_text);
        editText.setEnabled(false);
//        editText.setSelectAllOnFocus(true);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {

//                editText.setEnabled(true);
//                editText.requestFocus();
//                editText.setSelection(editText.getText().length());
//                editText.selectAll();

                final Dialog dialog = new Dialog(EditTextActivity.this);
                dialog.setContentView(R.layout.dialog_change_column_name);
                dialog.setTitle("Title...");

                final EditText text = (EditText) dialog.findViewById(R.id.change);
                text.setText(editText.getText().toString());
                text.selectAll();

                Button button = (Button) dialog.findViewById(R.id.change_button);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick( View v ) {
                        editText.setText(text.getText().toString());
                        dialog.dismiss();
                    }
                });
                dialog.show();


            }
        });

//        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction( TextView v, int actionId, KeyEvent event ) {
//
//                editText.setEnabled(false);
//                return true;
//            }
//        });

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange( View v, boolean hasFocus ) {

                if(!hasFocus) {
                    if(editText.isEnabled()) {
                        editText.setEnabled(false);
                    }
                }
            }
        });

//        editText.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey( View v, int keyCode, KeyEvent event ) {
//
//                if(keyCode == KeyEvent.KEYCODE_ENTER) {
//                    hideKeyboard(v);
//                    editText.setEnabled(true);
//                    editText.clearFocus();
//                    return true;
//                }
//                return false;
//            }
//        });

        editText.setKeyListener(new KeyListener() {
            @Override
            public int getInputType() {

                return 0;
            }

            @Override
            public boolean onKeyDown( View view, Editable text, int keyCode, KeyEvent event ) {

                return false;
            }

            @Override
            public boolean onKeyUp( View view, Editable text, int keyCode, KeyEvent event ) {

                if(keyCode == KeyEvent.KEYCODE_ENTER) {
                    editText.clearFocus();
                    return true;

                }
                return false;
            }

            @Override
            public boolean onKeyOther( View view, Editable text, KeyEvent event ) {

                return false;
            }

            @Override
            public void clearMetaKeyState( View view, Editable content, int states ) {

            }
        });
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction( TextView v, int actionId, KeyEvent event ) {

                if(event != null) {

                    hideKeyboard(v);
                    editText.setEnabled(true);
                    editText.clearFocus();
                    return true;
                }
                return false;
            }
        });
    }
    private void hideKeyboard(View view) {
        InputMethodManager manager = (InputMethodManager) view.getContext()
                .getSystemService(INPUT_METHOD_SERVICE);
        if (manager != null)
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_text, menu);
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
}
