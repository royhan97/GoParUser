package project.roy.socialmedia.ui.timeline;


import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import project.roy.socialmedia.R;
import project.roy.socialmedia.adapter.ReminderAdapter;
import project.roy.socialmedia.adapter.TimeLineAdapter;
import project.roy.socialmedia.data.model.Media;
import project.roy.socialmedia.data.model.Timeline;
import project.roy.socialmedia.data.model.User;
import project.roy.socialmedia.presenter.ReminderPresenter;
import project.roy.socialmedia.presenter.TimelinePresenter;
import project.roy.socialmedia.ui.account.AccountActivity;
import project.roy.socialmedia.ui.post.CreatePostActivity;
import project.roy.socialmedia.ui.reminder.ReminderView;
import project.roy.socialmedia.util.ShowAlert;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimelineFragment extends Fragment implements TimelineView, TimeLineAdapter.OnDetailListener, View.OnClickListener {


    private TimelinePresenter timelinePresenter;
    private TimeLineAdapter timelineAdapter;
    private ProgressBar pb;
    private SwipeRefreshLayout swipeToRefresh;
    private FloatingActionButton fab;
    private TextView tvEmptyPost;
    private RecyclerView rvReminder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);

        rvReminder = view.findViewById(R.id.recycler_view);
        pb = view.findViewById(R.id.progressBar);
        fab = view.findViewById(R.id.addNewPostFab);
        swipeToRefresh = view.findViewById(R.id.swipeContainer);
        tvEmptyPost = view.findViewById(R.id.empty_post);
        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                rvReminder.setVisibility(View.GONE);
                pb.setVisibility(View.VISIBLE);
                timelinePresenter.getTimeline();
                swipeToRefresh.setRefreshing(false);
            }
        });

        fab.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initPresenter();
        initView();
    }

    private void initPresenter() {
        timelinePresenter = new TimelinePresenter(this);
        timelinePresenter.getTimeline();
    }

    private void initView() {
        timelineAdapter = new TimeLineAdapter(getActivity());
        timelineAdapter.setOnClickDetailListener(this);
        rvReminder.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvReminder.setAdapter(timelineAdapter);
    }

    @Override
    public void onSuccessShowTimeline(List<Timeline> timelineList) {
        pb.setVisibility(View.GONE);
        if (timelineList.size() < 1){
            tvEmptyPost.setVisibility(View.VISIBLE);
            rvReminder.setVisibility(View.GONE);
        }
        else {
            rvReminder.setVisibility(View.VISIBLE);
            tvEmptyPost.setVisibility(View.GONE);
            timelineAdapter.setData(timelineList);
        }
    }

    @Override
    public void onFailureShowTimeline(String messages) {
        tvEmptyPost.setVisibility(View.VISIBLE);
        rvReminder.setVisibility(View.GONE);
        pb.setVisibility(View.GONE);
        ShowAlert.showToast(getContext(),messages);
    }

    @Override
    public void onSuccessPostTimeline(String messages) {

    }

    @Override
    public void onFailurePostTimeline(String messages) {

    }

    @Override
    public void onItemClick(Timeline timeline, List<Media> mediaList, User user)  {
        Intent intent = new Intent(getActivity(), DetailsTimelineActivity.class);
        intent.putExtra("timeline", timeline);
        intent.putParcelableArrayListExtra("media", (ArrayList<? extends Parcelable>) mediaList);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    @Override
    public void onAuthorClick(int idUser) {
        Intent intent = new Intent(getActivity(), AccountActivity.class);
        intent.putExtra("idUser", idUser);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.addNewPostFab){
            Intent intent = new Intent(getActivity(), CreatePostActivity.class);
            startActivity(intent);
        }
    }
}
