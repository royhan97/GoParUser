/*
 *  Copyright 2017 Rozdoum
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package project.roy.socialmedia.ui.timeline;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Transition;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import project.roy.socialmedia.R;
import project.roy.socialmedia.adapter.CommentarAdapter;
import project.roy.socialmedia.adapter.TimeLineAdapter;
import project.roy.socialmedia.data.local.SaveUserData;
import project.roy.socialmedia.data.model.Commentar;
import project.roy.socialmedia.data.model.Media;
import project.roy.socialmedia.data.model.Timeline;
import project.roy.socialmedia.data.model.User;
import project.roy.socialmedia.presenter.CommentarPresenter;
import project.roy.socialmedia.ui.detailprofile.DetailProfileActivity;
import project.roy.socialmedia.util.Constant;
import project.roy.socialmedia.util.FormatterUtil;
import project.roy.socialmedia.util.ShowAlert;

public class DetailsTimelineActivity extends AppCompatActivity implements CommentarView, View.OnClickListener, CommentarAdapter.OnDetailListener {

    public static final String POST_ID_EXTRA_KEY = "PostDetailsActivity.POST_ID_EXTRA_KEY";
    public static final String AUTHOR_ANIMATION_NEEDED_EXTRA_KEY = "PostDetailsActivity.AUTHOR_ANIMATION_NEEDED_EXTRA_KEY";
    private static final int TIME_OUT_LOADING_COMMENTS = 30000;
    public static final int UPDATE_POST_REQUEST = 1;
    public static final String POST_STATUS_EXTRA_KEY = "PostDetailsActivity.POST_STATUS_EXTRA_KEY";

    private EditText commentEditText;
    @Nullable
    private ScrollView scrollView;
    private ViewGroup likesContainer;
    private ImageView likesImageView;
    private TextView commentsLabel;
    private TextView likeCounterTextView;
    private TextView commentsCountTextView;
    private TextView watcherCounterTextView;
    private TextView authorTextView;
    private TextView dateTextView;
    private ImageView authorImageView;
    private ProgressBar progressBar;
    private ImageView postImageView;
    private TextView titleTextView;
    private TextView descriptionEditText;
    private ProgressBar commentsProgressBar;
    private RecyclerView commentsRecyclerView;
    private TextView warningCommentsTextView;
    private CommentarPresenter commentarPresenter;
    private Button btnSendCommentar;
    private Timeline timeline;
    private User user;
    private CommentarAdapter commentarAdapter;
    private AlertDialog alert;
    private AlertDialog.Builder builder;

    private boolean attemptToLoadComments = false;

    private MenuItem complainActionMenuItem;
    private MenuItem editActionMenuItem;
    private MenuItem deleteActionMenuItem;

    private String postId;

    private boolean postRemovingProcess = false;
    private boolean isPostExist;
    private boolean authorAnimationInProgress = false;

    private boolean isAuthorAnimationRequired;
    private ActionMode mActionMode;
    private boolean isEnterTransitionFinished = false;
    private TextView warningNoCommentsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        isAuthorAnimationRequired = getIntent().getBooleanExtra(AUTHOR_ANIMATION_NEEDED_EXTRA_KEY, false);
        postId = getIntent().getStringExtra(POST_ID_EXTRA_KEY);

        titleTextView =  findViewById(R.id.titleTextView);
        descriptionEditText =  findViewById(R.id.descriptionEditText);
        postImageView =  findViewById(R.id.postImageView);
        progressBar =  findViewById(R.id.progressBar);
        commentsRecyclerView =  findViewById(R.id.commentsRecyclerView);
        scrollView =  findViewById(R.id.scrollView);
        commentsLabel =  findViewById(R.id.commentsLabel);
        commentEditText = findViewById(R.id.commentEditText);
        authorImageView =  findViewById(R.id.authorImageView);
        authorTextView =  findViewById(R.id.authorTextView);
        dateTextView =  findViewById(R.id.dateTextView);
        commentsProgressBar =  findViewById(R.id.commentsProgressBar);
        warningCommentsTextView =  findViewById(R.id.warningCommentsTextView);
        warningNoCommentsTextView = findViewById(R.id.warningNoCommentsTextView);
        btnSendCommentar = findViewById(R.id.sendButton);
        btnSendCommentar.setOnClickListener(this);
        authorTextView.setOnClickListener(this);
        initView();
        initPresenter();
    }

    public void initView (){
        timeline = getIntent().getParcelableExtra("timeline");
        user = getIntent().getParcelableExtra("user");
        setTitle(user.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ArrayList<Media> media = getIntent().getParcelableArrayListExtra("media");

        System.out.println("user pic " + user.getPicture());

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date d = f.parse(timeline.getCreatedAt());
            long milliseconds = d.getTime()+25200000;
            CharSequence date = FormatterUtil.getRelativeTimeSpanStringShort(this,milliseconds);
            dateTextView.setText(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        descriptionEditText.setText(timeline.getTimelineMoment());
        if (!media.isEmpty()){
            Picasso.get().load(Constant.BASE_URL+ media.get(0).getFilename())
                    .into(postImageView);
            progressBar.setVisibility(View.GONE);
        }
        else {
            Picasso.get().load(R.drawable.ic_stub).into(postImageView);
            progressBar.setVisibility(View.GONE);
        }

        if (user.getPicture() != null){
            Picasso.get().load(Constant.BASE_URL+ user.getPicture().toString())
                    .into(authorImageView);
            progressBar.setVisibility(View.GONE);
        }
        else {
            Picasso.get().load(R.drawable.ic_stub).into(authorImageView);
            progressBar.setVisibility(View.GONE);
        }

        authorTextView.setText(user.getName());
        if (timeline.getCommentar() != null){
            commentsProgressBar.setVisibility(View.GONE);
        }
        else {
            warningNoCommentsTextView.setVisibility(View.VISIBLE);
            commentsProgressBar.setVisibility(View.GONE);
        }

        commentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().length() > 0){
                    btnSendCommentar.setEnabled(true);
                }else{
                    btnSendCommentar.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        commentarAdapter = new CommentarAdapter(DetailsTimelineActivity.this);
        commentarAdapter.setOnClickDetailListener(this);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(DetailsTimelineActivity.this));
        commentsRecyclerView.setAdapter(commentarAdapter);
    }

    private void initPresenter(){
        commentarPresenter = new CommentarPresenter(this);
        commentarPresenter.getCommentarByTimelineId(String.valueOf(timeline.getId()));
    }

    @Override
    public void onFailedPostCommentar(String message) {
        ShowAlert.showToast(DetailsTimelineActivity.this, message);
    }

    @Override
    public void onSuccessPostCommentar(String message) {
        commentarPresenter.getCommentarByTimelineId(String.valueOf(timeline.getId()));
        commentEditText.setText("");
        hideKeyboard();
        ShowAlert.showToast(DetailsTimelineActivity.this, message);

    }

    @Override
    public void onSuccessShowAllCommentarByTimelineId(List<Commentar> reminderList) {
        commentsProgressBar.setVisibility(View.GONE);
        if (reminderList.size() < 1){
            warningNoCommentsTextView.setVisibility(View.VISIBLE);
            commentsRecyclerView.setVisibility(View.GONE);
        }
        else {
            commentsRecyclerView.setVisibility(View.VISIBLE);
            warningNoCommentsTextView.setVisibility(View.GONE);
            commentarAdapter.setData(reminderList);
        }
    }

    @Override
    public void onFailedShowAllCommentarByTimelineId(String messages) {
        warningNoCommentsTextView.setVisibility(View.VISIBLE);
        warningNoCommentsTextView.setText(messages);
        commentsRecyclerView.setVisibility(View.GONE);
        commentsProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onFailedDeleteCommentar(String message) {
        ShowAlert.closeProgresDialog();
        ShowAlert.showToast(DetailsTimelineActivity.this, message);
    }

    @Override
    public void onSuccessDeleteCommentar(String message) {
        ShowAlert.closeProgresDialog();
        ShowAlert.showToast(DetailsTimelineActivity.this, message);
        commentarPresenter.getCommentarByTimelineId(String.valueOf(timeline.getId()));
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.sendButton){
            String timelineCommentar = commentEditText.getText().toString().trim();
            if(timelineCommentar.isEmpty()){
                ShowAlert.showToast(DetailsTimelineActivity.this, "Anda belum mengisi komentar");
            }else{
                commentarPresenter.postCommentar(String.valueOf(timeline.getId()), timelineCommentar);
            }

        }

        if(view.getId() == R.id.authorTextView){
            Intent intent = new Intent(this, DetailProfileActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        }
    }

    private void hideKeyboard(){
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onItemDetailClicked(int tipsId) {

    }

    @Override
    public void onImgDeleteCommentarSelected(int timelineId) {
        builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(getString(R.string.text_delete_commentar_confirmation));
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(ShowAlert.dialog != null && ShowAlert.dialog.isShowing()){
                    ShowAlert.closeProgresDialog();
                }
                ShowAlert.showProgresDialog(DetailsTimelineActivity.this);
                commentarPresenter.deleteCommentarByTimelineId(String.valueOf(timelineId));
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
