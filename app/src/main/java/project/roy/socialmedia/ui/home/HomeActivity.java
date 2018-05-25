package project.roy.socialmedia.ui.home;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;

import project.roy.socialmedia.R;
import project.roy.socialmedia.adapter.TabFragmentAdapter;
import project.roy.socialmedia.data.local.SaveUserData;
import project.roy.socialmedia.data.local.SaveUserToken;
import project.roy.socialmedia.data.local.Session;
import project.roy.socialmedia.data.model.Fragments;
import project.roy.socialmedia.presenter.HomePresenter;
import project.roy.socialmedia.ui.account.AccountActivity;
import project.roy.socialmedia.ui.login.LoginActivity;

public class HomeActivity extends AppCompatActivity implements HomeView {

    private ViewPager pager;
    private TabLayout tabs;
    private Toolbar toolbar;
    private TabFragmentAdapter adapter;
    private HomePresenter homePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        pager = (ViewPager)findViewById(R.id.pager);
        tabs = (TabLayout)findViewById(R.id.tabs);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");
        initView();
    }

    public void initView(){
        adapter = new TabFragmentAdapter(this, getSupportFragmentManager());
        pager.setAdapter(adapter);
        tabs.setTabTextColors(getResources().getColor(R.color.purple_100),
                getResources().getColor(R.color.white));
        tabs.setupWithViewPager(pager);
        tabs.setTabGravity(TabLayout.GRAVITY_FILL);
        homePresenter = new HomePresenter(this);
        homePresenter.showFragmentList();
    }

    @Override
    public void showData(ArrayList<Fragments> fragmentArrayList) {
        adapter.setData(fragmentArrayList);
        for (int i=0;i<tabs.getTabCount();i++){
            tabs.getTabAt(i).setIcon(fragmentArrayList.get(i).getImage());
//            tabs.getTabAt(i).setText(fragmentArrayList.get(i).getTitle());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        // return true so that the menu pop up is opened
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.akun){
            Intent intent = new Intent(HomeActivity.this, AccountActivity.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.keluar){
            Session.getInstance().setLogin(false);
            SaveUserData.getInstance().removeUser();
            SaveUserToken.getInstance().removeUserToken();
            SaveUserData.getInstance().removeImagePath();
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
