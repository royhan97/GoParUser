package project.roy.socialmedia.ui.login;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import project.roy.socialmedia.R;
import project.roy.socialmedia.presenter.LoginRegisterPresenter;
import project.roy.socialmedia.ui.home.HomeActivity;
import project.roy.socialmedia.util.ShowAlert;

/**
 * Created by roy on 4/9/2018.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, LoginRegisterView {

    private EditText etUsername, etPassword;
    private Button btnLogin;
    private TextView txtRegister;
    private LoginRegisterPresenter loginPresenter;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etUsername = findViewById(R.id.username);
        etPassword = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btn_login);
        txtRegister = findViewById(R.id.txt_register);
        coordinatorLayout = findViewById(R.id.coordinator);
        loginPresenter = new LoginRegisterPresenter(this);

        btnLogin.setOnClickListener(this);
        txtRegister.setOnClickListener(this);
        loginPresenter.loginCheck();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_login){
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            if(username.isEmpty()){
                etUsername.setError(getResources().getString(R.string.text_user_name_empty));
                etUsername.requestFocus();
            }else if(password.isEmpty()){
                etPassword.setError(getResources().getString(R.string.text_password_empty));
                etPassword.requestFocus();
            } else {
                loginPresenter.userLogin(this, username, password);
            }
        }
        if (v.getId() == R.id.txt_register){
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void showMessageSnackbar(String message) {
        ShowAlert.showSnackBar(coordinatorLayout, message);
    }

    @Override
    public void gotoHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void gotoLogin() {

    }
}
