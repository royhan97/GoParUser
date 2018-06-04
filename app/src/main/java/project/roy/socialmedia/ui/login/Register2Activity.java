package project.roy.socialmedia.ui.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import project.roy.socialmedia.R;
import project.roy.socialmedia.presenter.LoginRegisterPresenter;
import project.roy.socialmedia.ui.home.HomeActivity;
import project.roy.socialmedia.util.ShowAlert;

public class Register2Activity extends AppCompatActivity implements View.OnClickListener, LoginRegisterView {
    private RadioGroup rgChildrenGender;
    private RadioButton rbChildrenGenderMen, rbChildrenGenderWomen;
    private Button btnFinish;
    private String username, name, password, childrenAgeSelected;
    private LoginRegisterPresenter loginPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        btnFinish = findViewById(R.id.btn_finish);
        btnFinish.setOnClickListener(this);
        initPresenter();

        username = getIntent().getStringExtra("username");
        name = getIntent().getStringExtra("name");
        password = getIntent().getStringExtra("password");
        childrenAgeSelected = getIntent().getStringExtra("children_age");

        rgChildrenGender = findViewById(R.id.rg_children_gender);
        rbChildrenGenderMen = findViewById(R.id.rb_men);
        rbChildrenGenderWomen = findViewById(R.id.rb_women);


    }

    private void initPresenter() {
        loginPresenter = new LoginRegisterPresenter(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_finish){
            if(rgChildrenGender.getCheckedRadioButtonId() == -1){
                ShowAlert.showToast(getApplicationContext(), "Anda belum memilih jenis kelamin anak");
            }else {
                int selectedId = rgChildrenGender.getCheckedRadioButtonId();
                RadioButton rbChildrenGender = (RadioButton) findViewById(selectedId);
                loginPresenter.userRegister(this,name,username, password, childrenAgeSelected, rbChildrenGender.getText().toString());
            }

        }
    }

    @Override
    public void showMessageSnackbar(String message) {
        ShowAlert.showToast(Register2Activity.this, message);
    }

    @Override
    public void gotoHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void gotoLogin() {
        loginPresenter.userLogin(Register2Activity.this, username, password);
    }
}
