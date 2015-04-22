package com.traderz.anmolgupta.traderz;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * Created by anmolgupta on 03/04/15.
 */
public class StartingActivity extends Activity {

    @Override
    public void onCreate(Bundle icicle) {

        super.onCreate(icicle);

        SharedPreferences settings = getSharedPreferences("MYPREFS", 0);
        String userId = settings.getString("user_id", "");

        if(userId.equals("")) {
            startActivity(new Intent(StartingActivity.this, SocialAuth.class));
        } else {
            Intent intent = new Intent(StartingActivity.this, SocialAuth.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        }

    }


}
