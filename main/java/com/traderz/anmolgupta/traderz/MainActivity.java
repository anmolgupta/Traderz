package com.traderz.anmolgupta.traderz;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.*;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.*;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.traderz.anmolgupta.userData.UserData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    Facebook facebook;
    AsyncFacebookRunner mAsyncRunner;
    SharedPreferences mPrefs;
    @Override
    protected void onCreate( Bundle savedInstanceState ) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        facebook = new Facebook(getString(R.string.facebook_app_id));
        mAsyncRunner = new AsyncFacebookRunner(facebook);

        if ( android.os.Build.VERSION.SDK_INT > 9 ) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Button fbButton = (Button) findViewById(R.id.facebook);
        fbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {

//                CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
//                        MainActivity.this, // Context
//                        "788668623372",
//                        "us-east-1:a7c3050c-cdd2-4cce-bbeb-d5a490076a45", // Identity Pool ID,
//                        "arn:aws:iam::788668623372:role/Cognito_traderb2b1Auth_Role",
//                        null,
//                        Regions.US_EAST_1 // Region
//                );


                CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                        MainActivity.this,
                        "us-east-1:a7c3050c-cdd2-4cce-bbeb-d5a490076a45",
                        Regions.US_EAST_1);


                AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);
                DynamoDBMapper mapper = new DynamoDBMapper(ddbClient);

                UserData userData = new UserData("anmol.gupta91@gmail.com");

                try {
                    mapper.save(userData);
                } catch ( Exception e ) {
                    e.printStackTrace();
                }

            }
        });

        Button gmailButton = (Button) findViewById(R.id.gmail);
        gmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {

                showAccessTokens();

            }
        });

//        Button loginButton  = (Button) findViewById(R.id.login);
//        loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick( View v ) {
//                openActiveSession(MainActivity.this, true,
//                        Arrays.asList("email"),
//                        new Session.StatusCallback() {
//
//                            @Override
//                            public void call( Session session, SessionState sessionState, Exception e ) {
//                                if (e != null) {
//                                    Log.d("Facebook", e.getMessage());
//                                }
//                                Log.d("Facebook", "Session State: " + session.getState());
//                                // you can make request to the /me API or do other stuff like post, etc. here
//                            }
//                        });
//            }
//        });

        Button loginButton  = (Button) findViewById(R.id.login);
        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick( View v ) {
                loginToFacebook();
            }
        });

        Button doAfter = (Button)findViewById(R.id.after_login);
        doAfter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                getProfileInformation();
            }
        });

    }

    public void loginToFacebook() {

        mPrefs = getPreferences(MODE_PRIVATE);
        String access_token = mPrefs.getString("access_token", null);
        long expires = mPrefs.getLong("access_expires", 0);

        if (access_token != null) {
            facebook.setAccessToken(access_token);

            Log.d("FB Sessions", "" + facebook.isSessionValid());
        }

        if (expires != 0) {
            facebook.setAccessExpires(expires);
        }

        if (!facebook.isSessionValid()) {
            facebook.authorize(this,
                    new String[] { "email"},
                    new Facebook.DialogListener() {

                        @Override
                        public void onCancel() {
                            // Function to handle cancel event
                        }

                        @Override
                        public void onComplete(Bundle values) {
                            // Function to handle complete event
                            // Edit Preferences and update facebook acess_token
                            SharedPreferences.Editor editor = mPrefs.edit();
                            editor.putString("access_token",
                                    facebook.getAccessToken());
                            editor.putLong("access_expires",
                                    facebook.getAccessExpires());
                            editor.commit();

                        }

                        @Override
                        public void onError(DialogError error) {
                            // Function to handle error

                        }

                        @Override
                        public void onFacebookError(FacebookError fberror) {
                            // Function to handle Facebook errors

                        }

                    });
        }
    }

    public void showAccessTokens() {
        String access_token = facebook.getAccessToken();

        Toast.makeText(getApplicationContext(),
                "Access Token: " + access_token, Toast.LENGTH_LONG).show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        facebook.authorizeCallback(requestCode, resultCode, data);
    }

    private static Session openActiveSession(Activity activity, boolean allowLoginUI, List permissions, Session.StatusCallback callback) {
        Session.OpenRequest openRequest = new Session.OpenRequest(activity).setPermissions(permissions).setCallback(callback);
        Session session = new Session.Builder(activity).build();
        if (SessionState.CREATED_TOKEN_LOADED.equals(session.getState()) || allowLoginUI) {
            Session.setActiveSession(session);
            session.openForRead(openRequest);
            return session;
        }
        return null;
    }

    public void getProfileInformation() {

        mAsyncRunner.request("me", new AsyncFacebookRunner.RequestListener() {
            @Override
            public void onComplete( String response, Object state ) {

                Log.d("Profile", response);
                String json = response;

                try {
                    // Facebook Profile JSON data
                    JSONObject profile = new JSONObject(json);

                    // getting name of the user
                    final String name = profile.getString("name");

                    // getting email of the user
                    final String email = profile.getString("email");

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            Toast.makeText(getApplicationContext(), "Name: " + name + "\nEmail: " + email, Toast.LENGTH_LONG).show();
                        }

                    });


                } catch ( JSONException e ) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onIOException( IOException e, Object o ) {

            }

            @Override
            public void onFileNotFoundException( FileNotFoundException e, Object o ) {

            }

            @Override
            public void onMalformedURLException( MalformedURLException e, Object o ) {

            }

            @Override
            public void onFacebookError( FacebookError facebookError, Object o ) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void callback(String userId) {

        SharedPreferences settings = getSharedPreferences("MYPREFS", 0);

        SharedPreferences.Editor editor = settings.edit();
        editor.putString("user_id", userId);
        editor.commit();

        Intent intent = new Intent(MainActivity.this, MainAdminNavigation.class);
        intent.putExtra("userId", userId);
        startActivity(intent);

    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}

