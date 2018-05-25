package project.roy.socialmedia;

import android.app.Application;
import android.content.Context;

/**
 * Created by roy on 4/9/2018.
 */

public class Aion extends Application{

    private static Context sContext;

    public static Context getContext() {
        return sContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
    }
}
