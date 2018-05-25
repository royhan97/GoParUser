package project.roy.socialmedia.ui.account;

import android.graphics.Bitmap;

import java.io.File;

/**
 * Created by roy on 4/10/2018.
 */

public interface AccountView {
    void showMessage(String s);

    void showMessageSnackbar(String message);

    void showChange(String username, String name);

    void showAvatarChange(Bitmap photo);
}
