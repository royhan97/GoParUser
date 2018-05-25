package project.roy.socialmedia.presenter;

import android.content.Context;
import android.graphics.Bitmap;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import project.roy.socialmedia.data.local.SaveUserData;
import project.roy.socialmedia.data.local.SaveUserToken;
import project.roy.socialmedia.data.local.Session;
import project.roy.socialmedia.data.model.User;
import project.roy.socialmedia.data.network.RetrofitClient;
import project.roy.socialmedia.ui.account.AccountView;
import project.roy.socialmedia.util.SharedPrefUtil;
import project.roy.socialmedia.util.ShowAlert;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by roy on 4/10/2018.
 */

public class AccountPresenter {

    AccountView accountView;
    JsonObject params;

    public AccountPresenter(AccountView accountView) {
        this.accountView = accountView;
    }

    public void changePassword (Context context, String newPassword, String currentPassword){
        ShowAlert.showProgresDialog(context);
        params = new JsonObject();
        params.addProperty("new_password",newPassword);
        params.addProperty("current_password",currentPassword);
        RetrofitClient.getInstance()
                .getApi()
                .changePassword(SaveUserData.getInstance().getUser().getId(),"application/json",params)
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.isSuccessful()) {
                            JsonObject body = response.body();
                            boolean status = body.get("status").getAsBoolean();
                            if (status) {
                                accountView.showMessageSnackbar("Password Berhasil Diubah");
                                ShowAlert.closeProgresDialog();
                            } else {
                                String message = body.get("messages").getAsString();
                                accountView.showMessage(message);
                                ShowAlert.closeProgresDialog();
                            }
                        }
                        else {
                            accountView.showMessage("Password Gagal Diubah");
                            ShowAlert.closeProgresDialog();
                        }

                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        accountView.showMessage("Ubah Password Gagal");
                        ShowAlert.closeProgresDialog();
                    }
                });
    }

    public void updateDataUser(String username, String password){
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
                                SaveUserToken.getInstance().saveUserToken(id,token);
                                SaveUserData.getInstance().saveUser(user);
                            } else {
                                String message = body.get("message").getAsString();
                                accountView.showMessage(message);
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        accountView.showMessage("Update Data User Gagal");
                    }
                });
    }

    public void changeProfile (Context context, String name, String username, String password){
        ShowAlert.showProgresDialog(context);
        params = new JsonObject();
        params.addProperty("name",name);
        params.addProperty("username",username);
        params.addProperty("password",password);
        RetrofitClient.getInstance()
                .getApi()
                .changeProfile(SaveUserData.getInstance().getUser().getId(),"application/json",params)
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.isSuccessful()) {
                            JsonObject body = response.body();
                            boolean status = body.get("status").getAsBoolean();
                            if (status) {
                                updateDataUser(username,password);
                                accountView.showChange(username,name);
                                accountView.showMessageSnackbar("Profil Berhasil Diubah");
                                ShowAlert.closeProgresDialog();
                            } else {
                                String message = body.get("messages").getAsString();
                                accountView.showMessage(message);
                                ShowAlert.closeProgresDialog();
                            }
                        }
                        else {
                            accountView.showMessage("Profil Gagal Diubah");
                            ShowAlert.closeProgresDialog();
                        }

                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        accountView.showMessage("Ubah Profil Gagal");
                        ShowAlert.closeProgresDialog();
                    }
                });
    }

    public void updatePhotoProfile(Context context,int id, File photo, Bitmap liteImage){
        ShowAlert.showProgresDialog(context);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), photo);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("profile_picture", photo.getName(), requestFile);
//        RequestBody fullName = RequestBody.create(MediaType.parse("multipart/form-data"), id);

        RetrofitClient.getInstance()
                .getApi()
                .updateProfilPicture(id,body)
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.isSuccessful()) {
                            JsonObject body = response.body();
                            boolean status = body.get("status").getAsBoolean();
                            if (status) {
                                accountView.showAvatarChange(liteImage);
                                SaveUserData.getInstance().saveImagePath(photo.getAbsolutePath());
                                accountView.showMessageSnackbar("Foto Profil Berhasil Diubah");
                                ShowAlert.closeProgresDialog();
                            } else {
                                String message = body.get("messages").getAsString();
                                accountView.showMessage(message);
                                ShowAlert.closeProgresDialog();
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        accountView.showMessage("Update Foto Profil User Gagal");
                        t.printStackTrace();
                        ShowAlert.closeProgresDialog();
                    }
                });
    }
}
