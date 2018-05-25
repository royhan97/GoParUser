package project.roy.socialmedia.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
        loginPresenter = new LoginRegisterPresenter(this);

        btnRegister.setOnClickListener(this);
        txtLogin.setOnClickListener(this);
        imgVisibility.setOnClickListener(this);
        imgInvisibility.setOnClickListener(this);
    }

    @Override
    public void showMessageSnackbar(String message) {
        ShowAlert.showSnackBar(coordinatorLayout, message);
    }

    @Override
    public void gotoHome() {

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
            }
            else {
                loginPresenter.userRegister(this,name,username, password);
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
