package project.roy.socialmedia.ui.detailprofile;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import project.roy.socialmedia.R;
import project.roy.socialmedia.adapter.DetailProfileAdapter;
import project.roy.socialmedia.adapter.TimeLineAdapter;
import project.roy.socialmedia.data.model.Media;
import project.roy.socialmedia.data.model.Timeline;
import project.roy.socialmedia.data.model.User;
import project.roy.socialmedia.presenter.TimelinePresenter;
import project.roy.socialmedia.ui.account.AccountActivity;
import project.roy.socialmedia.ui.timeline.DetailsTimelineActivity;
import project.roy.socialmedia.ui.timeline.TimelineView;
import project.roy.socialmedia.util.ShowAlert;

public class DetailProfileActivity extends AppCompatActivity implements TimeLineAdapter.OnDetailListener, TimelineView, DetailProfileAdapter.OnDetailListener {

    private User user;

    private TimelinePresenter timelinePresenter;
    private DetailProfileAdapter detailProfileAdapter;
    private ProgressBar pb;
    private SwipeRefreshLayout swipeToRefresh;
    private TextView tvEmptyPost;
    private RecyclerView rvReminder;
    private AlertDialog alert;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_profile);
        rvReminder = findViewById(R.id.recycler_view);
        pb = findViewById(R.id.progressBar);
//        swipeToRefresh = findViewById(R.id.swipeContainer);
        tvEmptyPost = findViewById(R.id.empty_post);
//        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                rvReminder.setVisibility(View.GONE);
//                pb.setVisibility(View.VISIBLE);
//                timelinePresenter.getTimeline();
//                swipeToRefresh.setRefreshing(false);
//            }
//        });
        initView();
        initPresenter();
    }

    private void initView(){
        user = getIntent().getParcelableExtra("user");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(user.getName());
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        detailProfileAdapter = new DetailProfileAdapter(DetailProfileActivity.this);
        detailProfileAdapter.setOnClickDetailListener(this);
        rvReminder.setLayoutManager(new LinearLayoutManager(DetailProfileActivity.this));
        rvReminder.setAdapter(detailProfileAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        initPresenter();
        initView();
    }

    private void initPresenter() {
        timelinePresenter = new TimelinePresenter(this);
        timelinePresenter.showTimelineBySelfIdDetailProfile(String.valueOf(user.getId()));
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
            detailProfileAdapter.setData(timelineList);
        }
    }

    @Override
    public void onFailureShowTimeline(String messages) {
        tvEmptyPost.setVisibility(View.VISIBLE);
        rvReminder.setVisibility(View.GONE);
        pb.setVisibility(View.GONE);
        ShowAlert.showToast(DetailProfileActivity.this,messages);
    }

    @Override
    public void onSuccessPostTimeline(String messages) {

    }

    @Override
    public void onFailurePostTimeline(String messages) {

    }

    @Override
    public void onSuccessDeleteTimeline(String message) {
        ShowAlert.showToast(DetailProfileActivity.this, message);
        ShowAlert.closeProgresDialog();
        timelinePresenter.showTimelineBySelfIdDetailProfile(String.valueOf(user.getId()));
    }

    @Override
    public void onFailedDeleteTimeline(String message) {
        ShowAlert.showToast(DetailProfileActivity.this, message);
        ShowAlert.closeProgresDialog();
    }

    @Override
    public void onItemClick(Timeline timeline, List<Media> mediaList, User user)  {
        Intent intent = new Intent(DetailProfileActivity.this, DetailsTimelineActivity.class);
        intent.putExtra("timeline", timeline);
        intent.putParcelableArrayListExtra("media", (ArrayList<? extends Parcelable>) mediaList);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    @Override
    public void onAuthorClick(int idUser) {
        Intent intent = new Intent(DetailProfileActivity.this, AccountActivity.class);
        intent.putExtra("idUser", idUser);
        startActivity(intent);
    }

    @Override
    public void onDeleteTimeline(int timelineId) {
        builder = new AlertDialog.Builder(DetailProfileActivity.this);
        builder.setCancelable(false);
        builder.setMessage(getString(R.string.text_delete_timeline_confirmation));
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(ShowAlert.dialog != null && ShowAlert.dialog.isShowing()){
                    ShowAlert.closeProgresDialog();
                }
                ShowAlert.showProgresDialog(DetailProfileActivity.this);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


}
