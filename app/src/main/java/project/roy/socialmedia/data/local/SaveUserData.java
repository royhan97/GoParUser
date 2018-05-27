package project.roy.socialmedia.data.local;

import io.reactivex.CompletableOnSubscribe;
import project.roy.socialmedia.data.model.User;
import project.roy.socialmedia.util.Constant;
import project.roy.socialmedia.util.SharedPrefUtil;

/**
 * Created by roy on 4/9/2018.
 */

public class SaveUserData {
    private static SaveUserData ourInstance;

    private SaveUserData() {
    }

    public static SaveUserData getInstance() {
        if (ourInstance == null) ourInstance = new SaveUserData();
        return ourInstance;
    }

    public User getUser() {
        return (User) SharedPrefUtil.getObject(Constant.KEY_USER, User.class);
    }

    public void saveUser(User user) {
        SharedPrefUtil.saveObject(Constant.KEY_USER, user);
    }

    public void removeUser(){
        SharedPrefUtil.remove(Constant.KEY_USER);
    }

    public void saveImagePath(String path){
        SharedPrefUtil.saveString(Constant.KEY_PATH_IMG, path);
    }

    public void removeImagePath(){
        SharedPrefUtil.remove(Constant.KEY_PATH_IMG);
    }

    public String getImagePath(){
        return SharedPrefUtil.getString(Constant.KEY_PATH_IMG);
    }

    public void saveUserChildrenAge(String childrenAge){
        SharedPrefUtil.saveString(Constant.CHILDREN_AGE , childrenAge);
    }
    public  String getChildrenAge(){
        return SharedPrefUtil.getString(Constant.CHILDREN_AGE);
    }

}
