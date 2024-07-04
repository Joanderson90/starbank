package com.uefs.starbank.data.token;

public class DataToken {

    private static boolean token = false;


    public static void setToken(boolean hasToken) {
        DataToken.token = hasToken;
    }

    public static boolean hasToken() {
        return token;
    }


}
