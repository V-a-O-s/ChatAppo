package ch.jchat.chatapp.misc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator{
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^\\+\\d{1,19}$"
    );
    private static final Pattern USER_PATTERN = Pattern.compile(
        "^[0-9A-Za-z]{3,40}$"
    );

    public static boolean isEmail(String email){
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    };

    public static boolean isPhone(String phone){
        Matcher matcher = PHONE_PATTERN.matcher(phone);
        return matcher.matches();
    }

    public static boolean isName(String name){
        Matcher matcher = USER_PATTERN.matcher(name);
        return matcher.matches();
    }

    public static boolean isValidLimit(int limit){
        return limit<255;
    }
    
}