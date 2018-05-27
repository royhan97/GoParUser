package project.roy.socialmedia.util;

/**
 * Created by roy on 4/9/2018.
 */

public class Constant {

    public static final String USER_TOKEN = "USER_TOKEN";
    public static final String IS_LOGIN = "LOGIN";
    public static final String KEY_PATH_IMG = "REAL_PATH";
    public static final String CHILDREN_AGE = "children_age";
    public static String BASE_URL = "http://aiondevelop.xyz/";
    public static String KEY_USER = "UserData";
    public static class Profile {
        public static final int MAX_AVATAR_SIZE = 1280; //px, side of square
        public static final int MIN_AVATAR_SIZE = 100; //px, side of square
        public static final int MAX_NAME_LENGTH = 120;
    }
    public static class Post {
        public static final int MAX_TEXT_LENGTH_IN_LIST = 300; //characters
        public static final int MAX_POST_TITLE_LENGTH = 255; //characters
        public static final int POST_AMOUNT_ON_PAGE = 10;
    }


}
