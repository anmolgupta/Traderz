package com.traderz.anmolgupta.traderz;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * create an instance of this fragment.
 */
public class AddConnection extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /* Updating the action bar title */
//        getActivity().getActionBar().setTitle(title);

        /* Getting the WebView target url */
//        String url = getArguments().getString("url");

		/* Creating view corresponding to the fragment */
        View v = inflater.inflate(R.layout.activity_add_connection, container, false);

		/* Initializing and loading url in WebView */
        TextView connectionUsername = (TextView) v.findViewById(R.id.connectionUserName);

        Button connectionButton = (Button) v.findViewById(R.id.connectionButton);

        connectionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick( View v ) {
                Log.d("add Connection1", "anmol");
            }
        });
        return v;
    }
}
