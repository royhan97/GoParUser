package project.roy.socialmedia.ui.childrendevelopment;

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
import project.roy.socialmedia.adapter.ChildrenDevelopmentResultAdapter;
import project.roy.socialmedia.data.model.DDTK;
import project.roy.socialmedia.presenter.ChildrenDevelopmentPresenter;
import project.roy.socialmedia.presenter.ChildrenDevelopmentResultPresenter;

public class ChildrenDevelopmetResultActivity extends AppCompatActivity implements ChildrenDevelopmentResultView, ChildrenDevelopmentResultAdapter.OnDetailListener {

    private ChildrenDevelopmentResultAdapter childrenDevelopmentResultAdapter;
    private ChildrenDevelopmentResultPresenter childrenDevelopmentResultPresenter;
    private RecyclerView rvChildrenDevelopmentResult;
    private ProgressBar pbLoading;
    private TextView tvError;
    private SwipeRefreshLayout srlChildrenDevelopmentResult;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children_developmet_result);

        rvChildrenDevelopmentResult = findViewById(R.id.rv_children_development_result);
        pbLoading = findViewById(R.id.pb_loading);
        tvError = findViewById(R.id.tv_error);
        srlChildrenDevelopmentResult = findViewById(R.id.srl_children_development_result);
        srlChildrenDevelopmentResult.setOnRefreshListener(() -> {
            tvError.setText("");
            pbLoading.setVisibility(View.VISIBLE);
            rvChildrenDevelopmentResult.setVisibility(View.GONE);
            childrenDevelopmentResultPresenter.showAllDDTK();
            srlChildrenDevelopmentResult.setRefreshing(false);
        });
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Hasil Perkembangan Anak");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
        initPresenter();
    }
    private void initPresenter() {
        childrenDevelopmentResultPresenter = new ChildrenDevelopmentResultPresenter(this);
        childrenDevelopmentResultPresenter.showAllDDTK();
    }

    private void initView() {
        childrenDevelopmentResultAdapter = new ChildrenDevelopmentResultAdapter(this);
        childrenDevelopmentResultAdapter.setOnClickDetailListener(this);
        rvChildrenDevelopmentResult.setLayoutManager(new LinearLayoutManager(this));
        rvChildrenDevelopmentResult.setAdapter(childrenDevelopmentResultAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFailedShowAllDdtk(String messages) {
        rvChildrenDevelopmentResult.setVisibility(View.GONE);
        pbLoading.setVisibility(View.GONE);
        tvError.setVisibility(View.VISIBLE);
        tvError.setText(messages);
    }

    @Override
    public void onSuccessShowAllDdtk(List<DDTK> ddtkList) {
        rvChildrenDevelopmentResult.setVisibility(View.GONE);
        tvError.setText("Tidak ada reminder");
        tvError.setVisibility(View.VISIBLE);
        if(ddtkList.size()==0 || ddtkList.isEmpty()){
            rvChildrenDevelopmentResult.setVisibility(View.GONE);
            pbLoading.setVisibility(View.GONE);
            tvError.setText("Tidak ada reminder");
            tvError.setVisibility(View.VISIBLE);
        }else{
            tvError.setVisibility(View.GONE);
            pbLoading.setVisibility(View.GONE);
            rvChildrenDevelopmentResult.setVisibility(View.VISIBLE);
            childrenDevelopmentResultAdapter.setData(ddtkList);
            rvChildrenDevelopmentResult.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemDetailClicked(int tipsId, boolean b) {

    }

    @Override
    public void onImgDeleteCommentarSelected(int timelineId) {

    }
}
