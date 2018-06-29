package project.roy.socialmedia.ui.childrendevelopment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import project.roy.socialmedia.R;
import project.roy.socialmedia.adapter.ChildrenDevelopmentAdapter;
import project.roy.socialmedia.adapter.ReminderAdapter;
import project.roy.socialmedia.data.local.SaveUserData;
import project.roy.socialmedia.data.model.DDTK;
import project.roy.socialmedia.presenter.ChildrenDevelopmentPresenter;
import project.roy.socialmedia.presenter.ReminderPresenter;
import project.roy.socialmedia.util.ShowAlert;

public class ChildrenDevelopmentActivity extends AppCompatActivity implements ChildrenDevelopmentView, ChildrenDevelopmentAdapter.OnDetailListener, View.OnClickListener {


    private ChildrenDevelopmentAdapter childrenDevelopmentAdapter;
    private ChildrenDevelopmentPresenter childrenDevelopmentPresenter;
    private RecyclerView rvChildrenDevelopment;
    private ProgressBar pbLoading;
    private TextView tvError;
    private SwipeRefreshLayout srlChildrenDevelopment;
    private Button btnChildrenDevelopmentResult;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children_development);

        rvChildrenDevelopment = findViewById(R.id.rv_children_development);
        pbLoading = findViewById(R.id.pb_loading);
        tvError = findViewById(R.id.tv_error);
        btnChildrenDevelopmentResult = findViewById(R.id.btn_goto_children_development_result);
        btnChildrenDevelopmentResult.setOnClickListener(this);
        srlChildrenDevelopment = findViewById(R.id.srl_children_development);
        srlChildrenDevelopment.setOnRefreshListener(() -> {
            tvError.setText("");
            pbLoading.setVisibility(View.VISIBLE);
            rvChildrenDevelopment.setVisibility(View.GONE);
            childrenDevelopmentPresenter.showAllDDTK();
            srlChildrenDevelopment.setRefreshing(false);
        });
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Perkembangan Anak");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
        initPresenter();
    }

    private void initPresenter() {
        childrenDevelopmentPresenter = new ChildrenDevelopmentPresenter(this);
        childrenDevelopmentPresenter.showAllDDTK();
    }

    private void initView() {
        childrenDevelopmentAdapter = new ChildrenDevelopmentAdapter(this);
        childrenDevelopmentAdapter.setOnClickDetailListener(this);
        rvChildrenDevelopment.setLayoutManager(new LinearLayoutManager(this));
        rvChildrenDevelopment.setAdapter(childrenDevelopmentAdapter);
    }

    @Override
    public void onSuccessShowAllDdtk(List<DDTK> ddtkList) {
        rvChildrenDevelopment.setVisibility(View.GONE);
        tvError.setText("Tidak ada reminder");
        tvError.setVisibility(View.VISIBLE);
        if(ddtkList.size()==0 || ddtkList.isEmpty()){
            rvChildrenDevelopment.setVisibility(View.GONE);
            pbLoading.setVisibility(View.GONE);
            tvError.setText("Tidak ada reminder");
            tvError.setVisibility(View.VISIBLE);
        }else{
            tvError.setVisibility(View.GONE);
            pbLoading.setVisibility(View.GONE);
            rvChildrenDevelopment.setVisibility(View.VISIBLE);
            childrenDevelopmentAdapter.setData(ddtkList);
            rvChildrenDevelopment.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFailedShowAllDdtk(String messages) {
        rvChildrenDevelopment.setVisibility(View.GONE);
        pbLoading.setVisibility(View.GONE);
        tvError.setVisibility(View.VISIBLE);
        tvError.setText(messages);
    }

    @Override
    public void onFailedPostDDTK() {
        if(ShowAlert.dialog != null){
            ShowAlert.closeProgresDialog();
        }
        ShowAlert.showToast(this, "Gagal");
    }

    @Override
    public void onSuccessPostDDTK() {
        if(ShowAlert.dialog != null){
            ShowAlert.closeProgresDialog();
        }
        tvError.setText("");
        pbLoading.setVisibility(View.VISIBLE);
        rvChildrenDevelopment.setVisibility(View.GONE);
        childrenDevelopmentPresenter.showAllDDTK();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemDetailClicked(int tipsId, boolean b) {
        if(ShowAlert.dialog != null){
            ShowAlert.closeProgresDialog();
        }
        ShowAlert.showProgresDialog(this);
        if(b){
            childrenDevelopmentPresenter.postDDTK(SaveUserData.getInstance().getUser().getId(), tipsId, 1);
        }else {

            childrenDevelopmentPresenter.postDDTK(SaveUserData.getInstance().getUser().getId(), tipsId, 0);
        }

    }

    @Override
    public void onImgDeleteCommentarSelected(int timelineId) {

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_goto_children_development_result){
            Intent intent = new Intent(this, ChildrenDevelopmetResultActivity.class);
            startActivity(intent);
        }
    }
}
