package com.traderz.anmolgupta.traderz;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.traderz.anmolgupta.DynamoDB.AmazonClientManager;

/**
 * Created by anmolgupta on 03/04/15.
 */
public class StartingActivity extends Activity {

    public static AmazonClientManager amazonClientManager = null;

    @Override
    public void onCreate(Bundle icicle) {

        super.onCreate(icicle);

        amazonClientManager = new AmazonClientManager(this);
        SharedPreferences settings = getSharedPreferences("Traderz", 0);
        String userId = settings.getString("email", "");

        if(userId.equals("")) {
            startActivity(new Intent(StartingActivity.this, SocialAuth.class));
        } else {
            Intent intent = new Intent(StartingActivity.this, LoadingDataTest.class);
            intent.putExtra("email", userId);
            startActivity(intent);
        }

    }


}
