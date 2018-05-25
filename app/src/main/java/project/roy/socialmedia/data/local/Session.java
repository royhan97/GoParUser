package project.roy.socialmedia.data.local;

import project.roy.socialmedia.util.Constant;
import project.roy.socialmedia.util.SharedPrefUtil;

/**
 * Created by roy on 4/11/2018.
 */

public class Session {

    private static Session ourInstance;

    private Session() {
    }

    public static Session getInstance() {
        if (ourInstance == null) ourInstance = new Session();
        return ourInstance;
    }

    public boolean isLogin() {
        return SharedPrefUtil.getBoolean(Constant.IS_LOGIN);
    }

    public void setLogin(boolean isLogin) {
        SharedPrefUtil.saveBoolean(Constant.IS_LOGIN, isLogin);
    }
}
