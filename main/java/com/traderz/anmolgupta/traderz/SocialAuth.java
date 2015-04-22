package com.traderz.anmolgupta.traderz;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.traderz.anmolgupta.userData.EmailMappingToFullName;
import com.traderz.anmolgupta.userData.Social;
import com.traderz.anmolgupta.userData.UserContacts;
import com.traderz.anmolgupta.userData.UserData;
import com.traderz.anmolgupta.userData.UserDataTest;
import com.traderz.anmolgupta.userData.UserDataTestTools;
import com.traderz.anmolgupta.userData.UserTools;

import org.brickred.socialauth.Profile;
import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;

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

        if ( android.os.Build.VERSION.SDK_INT > 9 ) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

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

        Button saveButton = (Button)findViewById(R.id.save);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {

                UserDataTest userDataTest = new UserDataTest();
                userDataTest.setEmail("anmol@gmail.com");
                userDataTest.setCustom("abc");
                userDataTest.setCustom1("xyz");
                UserDataTestTools userTools = UserDataTestTools.getInstance(SocialAuth.this);

                userTools.save(userDataTest);
            }
        });

        Button loadButton = (Button)findViewById(R.id.load);

        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {

                UserDataTest userDataTest = new UserDataTest();
                userDataTest.setEmail("anmol.gupta91@gmail.com");

                UserDataTestTools userTools = UserDataTestTools.getInstance(SocialAuth.this);

                userTools.load(userDataTest);
            }
        });

        Button deleteButton = (Button)findViewById(R.id.delete);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {

                UserDataTest userDataTest = new UserDataTest();
                userDataTest.setEmail("anmol.gupta91@gmail.com");

                UserDataTestTools userTools = UserDataTestTools.getInstance(SocialAuth.this);

                userTools.delete(userDataTest);
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

        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onBack() {

        }
    }

    // To receive the profile response after authentication
    private final class ProfileDataListener implements SocialAuthListener<Profile> {

        @Override
        public void onExecute( String s, Profile t ) {

            checkIfPresentInDatabase(t);
        }

        @Override
        public void onError( SocialAuthError socialAuthError ) {

        }
    }

    public SocialAuth( SocialAuthAdapter adapter ) {

        this.adapter = adapter;
    }

    public void checkIfPresentInDatabase(Profile t) {

        String email = t.getEmail();
        String socialId = t.getValidatedId();
        String firstName = t.getFirstName();
        String lastName = t.getLastName();
        String gender = t.getGender();
        String country = t.getCountry();
        String location = t.getLocation();
        String language = t.getLanguage();
        String imageURL = t.getProfileImageURL();
        String dob = t.getDob().toString();

        if(email == null || email.equals("")) {
            //dosomething
            return;
        }

        UserTools userTools =
                UserTools.getInstance(SocialAuth.this);

        UserData userData =
                userTools.isDuplicateUser(new UserData(email));

        if(userData == null) {

            userData = new UserData(
                    email, country, dob, firstName,
                    lastName, language, imageURL,
                    gender, location, socialId, social);

        } else {

            userData.setSocialId(social, socialId);
        }

        userTools.saveUserData(userData);

        if(t.getContactInfo() != null) {

            UserContacts userContacts = userTools.isUserContactsPresent(email);

            if( userContacts == null ) {

                userContacts = new UserContacts();

                userContacts.setEmail(email);
                userContacts.setContacts(new EmailMappingToFullName(t.getContactInfo()));

                userTools.saveUserContact(userContacts);

            } else{
                //Add new information if any to user data
                userContacts.getContacts().add(t.getContactInfo());
                userTools.saveObject(userContacts);
            }



        }
    }
}