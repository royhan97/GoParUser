package project.roy.socialmedia.ui.tips;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import project.roy.socialmedia.R;
import project.roy.socialmedia.data.model.Tips;
import project.roy.socialmedia.presenter.TipsPresenter;
import project.roy.socialmedia.util.ShowAlert;

public class DetailTipsActivity extends AppCompatActivity implements TipsView {

    private TextView tvTipsTitle, tvTipsDescription;
    private TipsPresenter detailTipsPresenter;
    private int tipsId;
    private AlertDialog alert;
    private AlertDialog.Builder builder;
    private Toolbar toolbar;
    boolean editTipsState = false;
    private String tipsTitle;
    private String tipsDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tips);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Detail Tips");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        tvTipsTitle = findViewById(R.id.tv_tips_title);
        tvTipsDescription = findViewById(R.id.tv_tips_description);
        tipsId = getIntent().getExtras().getInt("tipsId");
        initPresenter();
    }


    private void initPresenter() {
        detailTipsPresenter = new TipsPresenter(this);
        if(ShowAlert.dialog != null && ShowAlert.dialog.isShowing()){
            ShowAlert.closeProgresDialog();
        }
        ShowAlert.showProgresDialog(DetailTipsActivity.this);
        detailTipsPresenter.showDetailTips(tipsId);
    }

    @Override
    public void onSuccessShowAllTips(List<Tips> tipsList) {

    }

    @Override
    public void onFailureShowAllTips(String message) {

    }

    @Override
    public void onSuccessShowDetailTips(Tips tips) {
        ShowAlert.closeProgresDialog();
        tvTipsTitle.setText(tips.getTipsTitle());
        tvTipsDescription.setText(tips.getTipsDescription());
    }

    @Override
    public void onFailureShowDetailTips(String message) {
        ShowAlert.closeProgresDialog();
        alerDialogFailure();
    }


    private void alerDialogFailure() {
        builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(getString(R.string.text_failed_get_data));
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(ShowAlert.dialog != null && ShowAlert.dialog.isShowing()){
                    ShowAlert.closeProgresDialog();
                }
                ShowAlert.showProgresDialog(DetailTipsActivity.this);
                detailTipsPresenter.showDetailTips(tipsId);
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                DetailTipsActivity.super.onBackPressed();
            }
        });

        alert = builder.create();
        alert.show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) super.onBackPressed();

        return super.onOptionsItemSelected(item);
    }
}
