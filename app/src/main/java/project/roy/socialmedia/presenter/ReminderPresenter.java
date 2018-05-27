package project.roy.socialmedia.presenter;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import project.roy.socialmedia.data.local.SaveUserData;
import project.roy.socialmedia.data.model.Reminder;
import project.roy.socialmedia.data.network.RetrofitClient;
import project.roy.socialmedia.ui.reminder.ReminderView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReminderPresenter {

    private ReminderView reminderView;

    public ReminderPresenter(ReminderView reminderView){
        this.reminderView = reminderView;
    }

    public void showAllReminder(){
        RetrofitClient.getInstance()
                .getApi()
                .showAllReminderByAge(SaveUserData.getInstance().getChildrenAge())
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if(response.isSuccessful()){
                            JsonObject body = response.body();
                            boolean status = body.get("status").getAsBoolean();
                            if(status){
                                JsonArray reminderArray = body.get("data").getAsJsonArray();
                                Type type = new TypeToken<List<Reminder>>(){}.getType();
                                List<Reminder> reminderList =  new Gson().fromJson(reminderArray, type);
                                reminderView.onSuccessShowAllReminder(reminderList);
                            }else{
                                reminderView.onFailureShowAllReminder(body.get("messages").getAsString());
                            }
                        }else{
                            reminderView.onFailureShowAllReminder("No Data Found");
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        reminderView.onFailureShowAllReminder("Server Error");
                    }
                });


    }

    public void showDetailReminder(int reminderId){
        RetrofitClient.getInstance()
                .getApi()
                .showDetailReminder(reminderId)
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if(response.isSuccessful()){
                            JsonObject body = response.body();
                            boolean status = body.get("status").getAsBoolean();
                            String message = body.get("messages").getAsString();
                            if(status){
                                JsonObject reminderObject = body.get("data").getAsJsonObject();
                                Type type = new TypeToken<Reminder>(){}.getType();
                                Reminder reminder = new Gson().fromJson(reminderObject, type);
                                reminderView.onSuccessShowDetailReminder(reminder);
                            }else{
                                reminderView.onFailureShowDetailReminder(message);
                            }
                        }else{
                            reminderView.onFailureShowDetailReminder("Pengambilan data gagal");
                        }

                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        reminderView.onFailureShowDetailReminder(t.getMessage());
                    }
                });
    }

}
