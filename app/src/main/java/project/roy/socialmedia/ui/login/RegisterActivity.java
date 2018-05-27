package project.roy.socialmedia.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import project.roy.socialmedia.R;
import project.roy.socialmedia.presenter.LoginRegisterPresenter;
import project.roy.socialmedia.ui.home.HomeActivity;
import project.roy.socialmedia.util.ShowAlert;

/**
 * Created by roy on 4/9/2018.
 */

public class RegisterActivity extends AppCompatActivity implements LoginRegisterView, View.OnClickListener {

    private EditText etUsername,etPassword,etName;
    private Button btnRegister;
    private TextView txtLogin;
    private LoginRegisterPresenter loginPresenter;
    private CoordinatorLayout coordinatorLayout;
    private ImageView imgVisibility, imgInvisibility;
    private Spinner spnChildrenAge;
    private String childrenAgeSelected;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etName = findViewById(R.id.name);
        etUsername = findViewById(R.id.username);
        etPassword = findViewById(R.id.password);
        btnRegister = findViewById(R.id.btn_register);
        txtLogin = findViewById(R.id.txt_login);
        coordinatorLayout = findViewById(R.id.coordinator);
        imgVisibility = findViewById(R.id.img_visibility);
        imgInvisibility = findViewById(R.id.img_invisibility);
        spnChildrenAge = findViewById(R.id.spn_children_age);
        loginPresenter = new LoginRegisterPresenter(this);

        btnRegister.setOnClickListener(this);
        txtLogin.setOnClickListener(this);
        imgVisibility.setOnClickListener(this);
        imgInvisibility.setOnClickListener(this);
        initSpinner();
    }

    @Override
    public void showMessageSnackbar(String message) {
        ShowAlert.showSnackBar(coordinatorLayout, message);
    }

    @Override
    public void gotoHome() {

    }

    private void initSpinner(){
        final List<String> list = new ArrayList<String>();
        list.add("Usia Anak Anda");
        list.add("< 1 Tahun");
        list.add("1 Tahun");
        list.add("2 Tahun");
        list.add("3 Tahun");
        ArrayAdapter<String> adp1 = new ArrayAdapter<String>(this,
                R.layout.spinner_item, list);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnChildrenAge.setAdapter(adp1);
        spnChildrenAge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                // TODO Auto-generated method stub
                childrenAgeSelected = list.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

    @Override
    public void gotoLogin() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_register){
            String username = etUsername.getText().toString().trim();
            String name = etName.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            if(username.isEmpty()){
                etUsername.setError(getResources().getString(R.string.text_user_name_empty));
                etUsername.requestFocus();
            }else if(password.isEmpty()){
                etPassword.setError(getResources().getString(R.string.text_password_empty));
                etPassword.requestFocus();
            }else if (name.isEmpty()){
                etName.setError(getResources().getString(R.string.txt_name_empty));
                etName.requestFocus();
            }else if(childrenAgeSelected.isEmpty() || childrenAgeSelected.equals("Usia Anak Anda")){
                ShowAlert.showToast(getApplicationContext(), "Anda belum memilih usia anak");
            }
            else {
                loginPresenter.userRegister(this,name,username, password, childrenAgeSelected);
            }
        }
        if (v.getId() == R.id.txt_login){
            super.onBackPressed();
        }
        if (v.getId() == R.id.img_visibility){
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT);
            imgInvisibility.setVisibility(View.VISIBLE);
            imgVisibility.setVisibility(View.GONE);
        }
        if (v.getId() == R.id.img_invisibility){
            etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
            imgVisibility.setVisibility(View.VISIBLE);
            imgInvisibility.setVisibility(View.GONE);
        }
    }
}
