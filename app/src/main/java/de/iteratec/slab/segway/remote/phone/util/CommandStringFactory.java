package de.iteratec.slab.segway.remote.phone.util;

import com.segway.robot.mobile.sdk.connectivity.StringMessage;

/**
 * Created by mss on 23.02.18.
 */

public class CommandStringFactory {

    public static StringMessage getStringMessage(String[] command) {
        StringBuilder fullString = new StringBuilder();

        for (String elem : command) {
            fullString.append(elem);
            fullString.append(";");
        }
        // remove trailing ;
        fullString.deleteCharAt(fullString.length() -1);
        return new StringMessage(fullString.toString());
    }

}
