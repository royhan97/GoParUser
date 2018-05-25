package project.roy.socialmedia.ui.tips;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import project.roy.socialmedia.R;
import project.roy.socialmedia.adapter.TipsAdapter;
import project.roy.socialmedia.data.model.Tips;
import project.roy.socialmedia.presenter.TipsPresenter;
import project.roy.socialmedia.util.ShowAlert;

/**
 * A simple {@link Fragment} subclass.
 */
public class TipsFragment extends Fragment implements TipsAdapter.OnDetailListener, View.OnClickListener, TipsView {


    private TipsAdapter tipsAdapter;
    private TipsPresenter tipsPresenter;
    private RecyclerView rvTips;
    private ProgressBar pbLoading;
    private TextView tvError;
    private SwipeRefreshLayout srlTips;
    private FloatingActionButton fbAddTips;

    public TipsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tips, container, false);
        rvTips = view.findViewById(R.id.rv_tips);
        pbLoading = view.findViewById(R.id.pb_loading);
        tvError = view.findViewById(R.id.tv_error);
        srlTips = view.findViewById(R.id.srl_tips);
        srlTips.setOnRefreshListener(() -> {
            tvError.setText("");
            pbLoading.setVisibility(View.VISIBLE);
            rvTips.setVisibility(View.GONE);
            tipsPresenter.showAllTips();
            srlTips.setRefreshing(false);
        });
        initView();
        initPresenter();
        return view;
    }

    public void initView() {
        tipsAdapter = new TipsAdapter(getActivity());
        tipsAdapter.setOnClickDetailListener(this);
        rvTips.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvTips.setAdapter(tipsAdapter);

    }

    private void initPresenter(){
        tipsPresenter = new TipsPresenter(this);
        tipsPresenter.showAllTips();
    }

    @Override
    public void onFailureShowAllTips(String message) {
        rvTips.setVisibility(View.GONE);
        pbLoading.setVisibility(View.GONE);
        tvError.setVisibility(View.VISIBLE);
        tvError.setText(message);
    }

    @Override
    public void onSuccessShowDetailTips(Tips tips) {

    }

    @Override
    public void onFailureShowDetailTips(String message) {

    }

    @Override
    public void onSuccessShowAllTips(List<Tips> tipsList) {
        rvTips.setVisibility(View.GONE);
        tvError.setText("Tidak ada tips");
        tvError.setVisibility(View.VISIBLE);
        if(tipsList.size()==0 || tipsList.isEmpty()){
            rvTips.setVisibility(View.GONE);
            pbLoading.setVisibility(View.GONE);
            tvError.setText("Tidak ada tips");
            tvError.setVisibility(View.VISIBLE);
        }else{
            tvError.setVisibility(View.GONE);
            pbLoading.setVisibility(View.GONE);
            rvTips.setVisibility(View.VISIBLE);
            tipsAdapter.setData(tipsList);
            rvTips.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemDetailClicked(int tipsId) {
        Intent intent = new Intent(getActivity(), DetailTipsActivity.class);
        intent.putExtra("tipsId", tipsId);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        tipsPresenter.showAllTips();
    }

    @Override
    public void showMessageFailure() {
        ShowAlert.showToast(getActivity(), "Tidak ada  tips");
    }

    @Override
    public void onClick(View v) {

    }

}
