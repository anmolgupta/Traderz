package com.traderz.anmolgupta.traderz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.traderz.anmolgupta.Content.UserContent;
import com.traderz.anmolgupta.DynamoDB.DynamoDBManager;
import com.traderz.anmolgupta.userData.EmailMappingToFullName;
import com.traderz.anmolgupta.userData.Social;
import com.traderz.anmolgupta.userData.UserContacts;
import com.traderz.anmolgupta.userData.UserData;
import com.traderz.anmolgupta.userData.UserKey;

import org.brickred.socialauth.Profile;
import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SocialAuth extends ActionBarActivity {

    SocialAuthAdapter adapter;
    Social social = null;

    public SocialAuth(){

    }

    @Override
    protected void onCreate( Bundle savedInstanceState ) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_auth);

        adapter = new SocialAuthAdapter(new ResponseListener());

        //Facebook

        Button facebookButton = (Button) findViewById(R.id.facebook_social_auth);

        facebookButton.setOnClickListener(new View.OnClickListener() {
            public void onClick( View v ) {

                try {
                    social = Social.FACEBOOK;
                    adapter.authorize(SocialAuth.this, SocialAuthAdapter.Provider.FACEBOOK);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //Twitter
        Button twitterButton = (Button)findViewById(R.id.twitter_social_auth);

        twitterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {

                try {
                    social = Social.TWITTER;
                    adapter.authorize(SocialAuth.this, SocialAuthAdapter.Provider.TWITTER);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //LinkedIn

        Button linkedInButton = (Button)findViewById(R.id.linkedin_social_auth);

        linkedInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {

                try {
                    social = Social.LINKEDIN;
                    adapter.authorize(SocialAuth.this, SocialAuthAdapter.Provider.LINKEDIN);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //Google
        Button googleButton = (Button)findViewById(R.id.gmail_social_auth);

        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {

                try {
                    social = Social.GOOGLE;
                    adapter.authorize(SocialAuth.this, SocialAuthAdapter.Provider.GOOGLE);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //Google+

        Button googlePlusButton = (Button)findViewById(R.id.google_plus_social_auth);

        googlePlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {

                try {
                    social = Social.GOOGLEPLUS;
                    adapter.authorize(SocialAuth.this, SocialAuthAdapter.Provider.GOOGLEPLUS);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_social_auth, menu);
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

    private final class ResponseListener implements DialogListener
    {
        public void onComplete(Bundle values) {

            adapter.getUserProfileAsync(new ProfileDataListener());
        }

        @Override
        public void onError( SocialAuthError socialAuthError ) {

            //TODO:: navigate to other activity

        }

        @Override
        public void onCancel() {
            //TODO:: navigate to other activity

        }

        @Override
        public void onBack() {
            //TODO:: navigate to other activity

        }
    }

    // To receive the profile response after authentication
    private final class ProfileDataListener implements SocialAuthListener<Profile> {

        @Override
        public void onExecute( String s, Profile t ) {

            if(t != null)
                new SaveUserInfo().execute(t);

            //TODO:: DO something

        }

        @Override
        public void onError( SocialAuthError socialAuthError ) {

            //TODO:: Do something

        }
    }

    public SocialAuth( SocialAuthAdapter adapter ) {

        this.adapter = adapter;
    }

    class SaveUserInfo extends AsyncTask<Profile, Void, String> {

        protected String doInBackground(Profile... profiles) {

                Profile t = profiles[0];

                String email = t.getEmail();
                String socialId = t.getValidatedId();
                String firstName = t.getFirstName();
                String lastName = t.getLastName();
                String gender = t.getGender();
                String country = t.getCountry();
                String location = t.getLocation();
                String language = t.getLanguage();
                String imageURL = t.getProfileImageURL();

                String dob =null;
                if(t.getDob() != null)
                     dob = t.getDob().toString();

                if(email == null || email.equals("")) {
                    return null;
                }

//                email = getUserKey(email);

                UserData userData =
                        DynamoDBManager.loadObject(new UserData(email));

                if(userData == null) {

                    userData = new UserData(
                            email, country, dob, firstName,
                            lastName, language, imageURL,
                            gender, location, socialId, social);

                } else {

                    userData.setSocialId(social, socialId);
                }

                DynamoDBManager.saveObject(userData);

                if(t.getContactInfo() != null) {

                    UserContacts userContacts =
                            DynamoDBManager.loadObject(new UserContacts(email));

                    if( userContacts == null ) {

                        userContacts = new UserContacts();

                        userContacts.setEmail(email);
                        userContacts.setContacts(new EmailMappingToFullName(t.getContactInfo()));

                        DynamoDBManager.saveObject(userContacts);

                    } else{
                        //Add new information if any to user data
                        userContacts.getContacts().add(t.getContactInfo());
                        DynamoDBManager.saveObject(userContacts);
                    }
                }

            return email;
        }

        public String getUserKey(String email) {

            UserKey userContent = new UserKey();
            userContent.setEmail("anmol007gupta@gmail.com");

            Long timestamp = new Date().getTime();

            Condition rangeKeyCondition = new Condition()
                    .withComparisonOperator(ComparisonOperator.LT.toString())
                    .withAttributeValueList(new AttributeValue().withN("" + timestamp));

            DynamoDBQueryExpression queryExpression = new DynamoDBQueryExpression()
                    .withHashKeyValues(userContent)
                    .withRangeKeyCondition("Timestamp", rangeKeyCondition)
                    .withConsistentRead(false);
//                    .withIndexName("UpdatedTimestamp-index");

            PaginatedQueryList<UserKey> result =
                    DynamoDBManager.getQueryResult(UserKey.class, queryExpression);

            if(result.size() == 0) {
                userContent.setTimestamp(timestamp);
                DynamoDBManager.saveObject(userContent);
                 return userContent.getId();
            }

            else {
                return result.get(0).getId();
            }
        }

        protected void onPostExecute(String email) {

            if(email == null){
                Toast.makeText(SocialAuth.this, "Please try again", Toast.LENGTH_LONG).show();
                return;
            }

            SharedPreferences settings = getSharedPreferences("Traderz", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("email", email);
            editor.commit();

            Intent intent = new Intent(SocialAuth.this, MainAdminNavigation.class);
            intent.putExtra("email", email);
            startActivity(intent);
        }

    }
}