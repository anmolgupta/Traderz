package com.traderz.anmolgupta.utilities;

/**
 * Created by anmolgupta on 05/04/15.
 */
public class UserValidation {

    public static boolean isValidEmail(String email) {

        if (email == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.
                    matcher(email).matches();
        }
    }

    public static boolean isValidPassword(String password) {

        if (password == null) {

            return false;

        } else {

            if(password.length() > 5)
                return true;

            return false;
        }
    }
}
