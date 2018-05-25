package project.roy.socialmedia.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import project.roy.socialmedia.data.model.Fragments;

/**
 * Created by roy on 4/10/2018.
 */

public class TabFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragments> fragments = new ArrayList<>();
    private Context context;

    public TabFragmentAdapter(Context context,FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    public void setData(List<Fragments> fragments){
        this.fragments = fragments;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position).getFragment();
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
