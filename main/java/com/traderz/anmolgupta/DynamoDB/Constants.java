package com.traderz.anmolgupta.DynamoDB;

/**
 * Created by anmolgupta on 25/04/15.
 */
import java.util.Random;

public class Constants {
    public static final String ACCOUNT_ID = "CHANGE_ME";
    public static final String IDENTITY_POOL_ID = "us-east-1:a7c3050c-cdd2-4cce-bbeb-d5a490076a45";
    public static final String UNAUTH_ROLE_ARN = "CHANGE_ME";
    // Note that spaces are not allowed in the table name
    public static final String TEST_TABLE_NAME = "CHANGE_ME";

    public static final Random random = new Random();
    public static final String[] NAMES = new String[] {
            "Norm", "Jim", "Jason", "Zach", "Matt", "Glenn", "Will", "Wade", "Trevor", "Jeremy",
            "Ryan", "Matty", "Steve", "Pavel"
    };

    public static String getRandomName() {
        int name = random.nextInt(NAMES.length);

        return NAMES[name];
    }

    public static int getRandomScore() {
        return random.nextInt(1000) + 1;
    }
}
