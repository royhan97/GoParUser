package project.roy.socialmedia.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import project.roy.socialmedia.data.local.SaveUserData;
import project.roy.socialmedia.data.local.SaveUserToken;
import project.roy.socialmedia.data.local.Session;
import project.roy.socialmedia.data.model.User;
import project.roy.socialmedia.data.network.RetrofitClient;
import project.roy.socialmedia.ui.login.LoginRegisterView;
import project.roy.socialmedia.util.ShowAlert;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by roy on 4/9/2018.
 */

public class LoginRegisterPresenter {

    private LoginRegisterView loginRegisterView;

    public LoginRegisterPresenter(LoginRegisterView loginRegisterView) {
        this.loginRegisterView = loginRegisterView;
    }

    public void loginCheck(){
        if (Session.getInstance().isLogin()) loginRegisterView.gotoHome();
    }

    public void userLogin (Context context, String username, String password){
        ShowAlert.showProgresDialog(context);
        RetrofitClient.getInstance()
                .getApi()
                .login(username,password)
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.isSuccessful()) {
                            JsonObject body = response.body();
                            boolean status = body.get("status").getAsBoolean();
                            if (status) {
                                JsonObject userObject = body.get("data").getAsJsonObject();
                                Type type = new TypeToken<User>() {
                                }.getType();
                                User user = new Gson().fromJson(userObject, type);
                                String id = userObject.get("id").getAsString();
                                String token = userObject.get("api_token").getAsString();
                                String childrenAge  = userObject.get("children_age").getAsString();
                                SaveUserToken.getInstance().saveUserToken(id,token);
                                SaveUserData.getInstance().saveUser(user);
                                SaveUserData.getInstance().saveUserChildrenAge(childrenAge);
                                loginRegisterView.gotoHome();
                                Session.getInstance().setLogin(true);
                                ShowAlert.closeProgresDialog();
                            } else {
                                String message = body.get("message").getAsString();
                                loginRegisterView.showMessageSnackbar(message);
                                ShowAlert.closeProgresDialog();
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        loginRegisterView.showMessageSnackbar("Login Gagal");
                        ShowAlert.closeProgresDialog();
                    }
                });
    }


    public void userRegister (final Context context, String name, String username, String password, String childrenAge, String childrenGender){
        ShowAlert.showProgresDialog(context);
        RetrofitClient.getInstance()
                .getApi()
                .register(name,username,password,childrenAge, childrenGender)
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.isSuccessful()) {
                            JsonObject body = response.body();
                            boolean status = body.get("status").getAsBoolean();
                            if (status) {
//                                String message = body.get("message").getAsString();
                                ShowAlert.showToast(context,"registrasi berhasil");
                                SaveUserData.getInstance().saveUserChildrenAge(childrenAge);
                                loginRegisterView.gotoLogin();
                                ShowAlert.closeProgresDialog();
                            } else {
                                String message = body.get("message").getAsString();
                                loginRegisterView.showMessageSnackbar(message);
                                ShowAlert.closeProgresDialog();
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        loginRegisterView.showMessageSnackbar("Registrasi Gagal");
                        ShowAlert.closeProgresDialog();
                    }
                });
    }
}
