package project.roy.socialmedia.ui.reminder;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

import java.util.List;

import project.roy.socialmedia.R;
import project.roy.socialmedia.adapter.ReminderAdapter;
import project.roy.socialmedia.data.model.Reminder;
import project.roy.socialmedia.presenter.ReminderPresenter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReminderFragment extends Fragment implements ReminderView, ReminderAdapter.OnDetailListener, View.OnClickListener {
    private ReminderAdapter reminderAdapter;
    private ReminderPresenter reminderPresenter;
    private RecyclerView rvReminder;
    private ProgressBar pbLoading;
    private TextView tvError;
    private SwipeRefreshLayout srlReminder;
    private FloatingActionButton fbAddReminder;
    private AlertDialog alert;
    private AlertDialog.Builder builder;

    public ReminderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reminder, container, false);
        rvReminder = view.findViewById(R.id.rv_reminder);
        pbLoading = view.findViewById(R.id.pb_loading);
        tvError = view.findViewById(R.id.tv_error);
        srlReminder = view.findViewById(R.id.srl_reminder);
        srlReminder.setOnRefreshListener(() -> {
            tvError.setText("");
            pbLoading.setVisibility(View.VISIBLE);
            rvReminder.setVisibility(View.GONE);
            reminderPresenter.showAllReminder();
            srlReminder.setRefreshing(false);
        });
        initView();
        initPresenter();
        return  view;
    }

    private void initPresenter() {
        reminderPresenter = new ReminderPresenter(this);
        reminderPresenter.showAllReminder();
    }

    private void initView() {
        reminderAdapter = new ReminderAdapter(getActivity());
        reminderAdapter.setOnClickDetailListener(this);
        rvReminder.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvReminder.setAdapter(reminderAdapter);
    }

    @Override
    public void onSuccessShowAllReminder(List<Reminder> reminderList) {
        rvReminder.setVisibility(View.GONE);
        tvError.setText("Tidak ada reminder");
        tvError.setVisibility(View.VISIBLE);
        if(reminderList.size()==0 || reminderList.isEmpty()){
            rvReminder.setVisibility(View.GONE);
            pbLoading.setVisibility(View.GONE);
            tvError.setText("Tidak ada reminder");
            tvError.setVisibility(View.VISIBLE);
        }else{
            tvError.setVisibility(View.GONE);
            pbLoading.setVisibility(View.GONE);
            rvReminder.setVisibility(View.VISIBLE);
            reminderAdapter.setData(reminderList);
            rvReminder.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFailureShowAllReminder(String message) {
        rvReminder.setVisibility(View.GONE);
        pbLoading.setVisibility(View.GONE);
        tvError.setVisibility(View.VISIBLE);
        tvError.setText(message);
    }

    @Override
    public void onSuccessShowDetailReminder(Reminder reminder) {

    }

    @Override
    public void onFailureShowDetailReminder(String message) {

    }

    @Override
    public void onItemDetailClicked(int reminderId) {
        Intent intent = new Intent(getActivity(), DetailReminderActivity.class);
        intent.putExtra("reminderId", reminderId);
        startActivity(intent);
    }

    @Override
    public void onButtonReminderYesClicked(String reminderYes) {
        alertDialogYesNo(reminderYes, true);
    }

    @Override
    public void onButtonReminderNoClicked(String reminderNo) {
        alertDialogYesNo(reminderNo, false);
    }

    @Override
    public void onClick(View v) {

    }

    public void alertDialogYesNo(String message, boolean doOrNot){
        builder = new AlertDialog.Builder(getActivity());
        if(doOrNot){
            builder.setTitle("Yes");
        }else{
            builder.setTitle("No");
        }
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setNegativeButton("Tutup", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert = builder.create();
        alert.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        reminderPresenter.showAllReminder();
    }
}
