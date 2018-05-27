package project.roy.socialmedia.ui.profile;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import project.roy.socialmedia.R;
import project.roy.socialmedia.adapter.TimeLineAdapter;
import project.roy.socialmedia.data.model.Media;
import project.roy.socialmedia.data.model.Timeline;
import project.roy.socialmedia.data.model.User;
import project.roy.socialmedia.presenter.TimelinePresenter;
import project.roy.socialmedia.ui.account.AccountActivity;
import project.roy.socialmedia.ui.post.CreatePostActivity;
import project.roy.socialmedia.ui.timeline.DetailsTimelineActivity;
import project.roy.socialmedia.ui.timeline.TimelineView;
import project.roy.socialmedia.util.ShowAlert;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements TimelineView, TimeLineAdapter.OnDetailListener {


    private TimelinePresenter timelinePresenter;
    private TimeLineAdapter timelineAdapter;
    private ProgressBar pb;
    private SwipeRefreshLayout swipeToRefresh;
    private TextView tvEmptyPost;
    private RecyclerView rvReminder;
    private AlertDialog alert;
    private AlertDialog.Builder builder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        rvReminder = view.findViewById(R.id.recycler_view);
        pb = view.findViewById(R.id.progressBar);
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
        timelinePresenter.showTimelineBySelfId();
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
    public void onSuccessDeleteTimeline(String message) {
        ShowAlert.showToast(getActivity(), message);
        ShowAlert.closeProgresDialog();
        timelinePresenter.showTimelineBySelfId();
    }

    @Override
    public void onFailedDeleteTimeline(String message) {
        ShowAlert.showToast(getActivity(), message);
        ShowAlert.closeProgresDialog();
    }

    @Override
    public void onItemClick(Timeline timeline, List<Media> mediaList, User user, String url)  {
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
    public void onDeleteTimeline(int timelineId) {
        builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setMessage(getString(R.string.text_delete_timeline_confirmation));
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(ShowAlert.dialog != null && ShowAlert.dialog.isShowing()){
                    ShowAlert.closeProgresDialog();
                }
                ShowAlert.showProgresDialog(getActivity());
                timelinePresenter.deleteTimeline(Integer.parseInt(String.valueOf(timelineId)));
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert = builder.create();
        alert.show();
    }

    @Override
    public void onNameSelected(User user) {

    }


}
