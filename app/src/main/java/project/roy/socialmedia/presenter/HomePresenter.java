package project.roy.socialmedia.presenter;

import java.util.ArrayList;

import project.roy.socialmedia.R;
import project.roy.socialmedia.data.model.Fragments;
import project.roy.socialmedia.ui.home.HomeView;
import project.roy.socialmedia.ui.profile.ProfileFragment;
import project.roy.socialmedia.ui.reminder.ReminderFragment;
import project.roy.socialmedia.ui.tips.TipsFragment;
import project.roy.socialmedia.ui.timeline.TimelineFragment;

/**
 * Created by roy on 4/10/2018.
 */

public class HomePresenter {

    private HomeView homeView;
    public HomePresenter(HomeView homeView){
        this.homeView = homeView;
    }

    public void showFragmentList(){
        ArrayList<Fragments> fragmentArrayList = new ArrayList<>();
        fragmentArrayList.add(new Fragments(new TimelineFragment(), "Timeline", R.drawable.ic_clock));
        fragmentArrayList.add(new Fragments(new ReminderFragment(), "Reminder", R.drawable.ic_alarm));
        fragmentArrayList.add(new Fragments(new TipsFragment(), "Tips", R.drawable.ic_receipt));
        fragmentArrayList.add(new Fragments(new ProfileFragment(), "Profile", R.drawable.ic_person_black_24dp));
        homeView.showData(fragmentArrayList);
    }
}
