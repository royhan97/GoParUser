package project.roy.socialmedia.ui.home;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.PeriodicTask;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import project.roy.socialmedia.R;
import project.roy.socialmedia.adapter.TabFragmentAdapter;
import project.roy.socialmedia.data.local.SaveUserData;
import project.roy.socialmedia.data.local.SaveUserToken;
import project.roy.socialmedia.data.local.Session;
import project.roy.socialmedia.data.model.Fragments;
import project.roy.socialmedia.presenter.HomePresenter;
import project.roy.socialmedia.service.GcmService;
import project.roy.socialmedia.ui.about.AboutActivity;
import project.roy.socialmedia.ui.account.AccountActivity;
import project.roy.socialmedia.ui.childrendevelopment.ChildrenDevelopmentActivity;
import project.roy.socialmedia.ui.login.LoginActivity;
import pub.devrel.easypermissions.EasyPermissions;

public class HomeActivity extends AppCompatActivity implements HomeView {

    private ViewPager pager;
    private TabLayout tabs;
    private Toolbar toolbar;
    private TabFragmentAdapter adapter;
    private HomePresenter homePresenter;
    private static final int REQUEST_GALLERY_CODE = 200;
    private static final int READ_REQUEST_CODE = 300;

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
        tabs.setTabTextColors(getResources().getColor(R.color.grey_700),
                getResources().getColor(R.color.grey_800));
        tabs.setupWithViewPager(pager);
        tabs.setTabGravity(TabLayout.GRAVITY_FILL);
        homePresenter = new HomePresenter(this);
        homePresenter.showFragmentList();

        PeriodicTask periodic = new PeriodicTask.Builder()
                .setService(GcmService.class)
                .setPeriod(1)
//                    .setFlex(5)
                .setTag(GcmService.TAG_LIST_REQUEST)
                .setPersisted(false)
                .setRequiredNetwork(com.google.android.gms.gcm.Task.NETWORK_STATE_ANY)
                .setRequiresCharging(false)
                .setUpdateCurrent(true)
                .build();

        GcmNetworkManager.getInstance(this).schedule(periodic);
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( HomeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.READ_EXTERNAL_STORAGE }, 0);
        }
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( HomeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }
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
        if (item.getItemId() == R.id.perkembangan_anak){
            Intent intent = new Intent(HomeActivity.this, ChildrenDevelopmentActivity.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.akun){
            Intent intent = new Intent(HomeActivity.this, AccountActivity.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.about){
            Intent intent = new Intent(HomeActivity.this, AboutActivity.class);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, HomeActivity.this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_GALLERY_CODE && resultCode == Activity.RESULT_OK){
            if(EasyPermissions.hasPermissions(HomeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            }else{
                EasyPermissions.requestPermissions(this, getString(R.string.text_read_file), READ_REQUEST_CODE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }

            if(EasyPermissions.hasPermissions(HomeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

            }else{
                EasyPermissions.requestPermissions(this, getString(R.string.text_read_file), READ_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }

    }



}
