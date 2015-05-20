package com.traderz.anmolgupta.traderz;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

/**
 * Created by anmolgupta on 19/05/15.
 */
public class CustomDialogBox {

    public static void createAlertBox(Context context, String msg) {

        AlertDialog.Builder builder =
                new AlertDialog.Builder(context);

        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick( DialogInterface dialog, int id ) {

                        dialog.cancel();
                    }
                });
        builder.create().show();
    }

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();

    }
}
