package project.roy.socialmedia.data.model;


import android.support.v4.app.Fragment;

/**
 * Created by roy on 4/10/2018.
 */

public class Fragments {

    private Fragment fragment;
    private String title;
    private int image;

    public Fragments(Fragment fragment, String title, int image) {
        this.fragment = fragment;
        this.title = title;
        this.image = image;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public String getTitle() {
        return title;
    }

    public int getImage() {
        return image;
    }
}
