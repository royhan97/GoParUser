package project.roy.socialmedia.presenter;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import project.roy.socialmedia.data.local.SaveUserData;
import project.roy.socialmedia.data.model.Reminder;
import project.roy.socialmedia.data.model.Timeline;
import project.roy.socialmedia.data.network.RetrofitClient;
import project.roy.socialmedia.ui.timeline.TimelineView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimelinePresenter {

    private TimelineView timelineView;

    public TimelinePresenter(TimelineView timelineView) {
        this.timelineView = timelineView;
    }

    public void getTimeline(){
        RetrofitClient.getInstance()
                .getApi()
                .showTimeline()
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if(response.isSuccessful()){
                            JsonObject body = response.body();
                            boolean status = body.get("status").getAsBoolean();
                            if(status){
                                JsonArray timelineArray = body.get("data").getAsJsonArray();
                                Type type = new TypeToken<List<Timeline>>(){}.getType();
                                List<Timeline> timelineList =  new Gson().fromJson(timelineArray, type);
                                timelineView.onSuccessShowTimeline(timelineList);
                            }else{
                                timelineView.onFailureShowTimeline(body.get("messages").getAsString());
                            }
                        }else{
                            timelineView.onFailureShowTimeline("No Data Found");
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        timelineView.onFailureShowTimeline("Server Error");
                    }
                });
    }

    public void getTimelineById(int idUser){
        RetrofitClient.getInstance()
                .getApi()
                .showSelfTimeline(idUser)
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if(response.isSuccessful()){
                            JsonObject body = response.body();
                            boolean status = body.get("status").getAsBoolean();
                            if(status){
                                JsonArray timelineArray = body.get("data").getAsJsonArray();
                                Type type = new TypeToken<List<Timeline>>(){}.getType();
                                List<Timeline> timelineList =  new Gson().fromJson(timelineArray, type);
                                timelineView.onSuccessShowTimeline(timelineList);
                            }else{
                                timelineView.onFailureShowTimeline(body.get("messages").getAsString());
                            }
                        }else{
                            timelineView.onFailureShowTimeline("No Data Found");
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        timelineView.onFailureShowTimeline("Server Error");
                    }
                });
    }

    public void postTimeline(int userId, String moment, RequestBody name, MultipartBody.Part file ){
        RetrofitClient.getInstance()
                .getApi()
                .postTimeline(userId,moment, name,file)
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if(response.isSuccessful()){
                            JsonObject body = response.body();
                            boolean status = body.get("status").getAsBoolean();
                            if(status){
                                timelineView.onSuccessPostTimeline(body.get("messages").getAsString());
                            }else{
                                timelineView.onFailurePostTimeline(body.get("messages").getAsString());
                            }
                        }else{
                            timelineView.onFailurePostTimeline("No Data Post");
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        timelineView.onFailurePostTimeline("Server Error");
                    }
                });
    }

    public void getDetailTimeline(int idTimeline){
        RetrofitClient.getInstance()
                .getApi()
                .showDetailTimeline(idTimeline)
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if(response.isSuccessful()){
                            JsonObject body = response.body();
                            boolean status = body.get("status").getAsBoolean();
                            if(status){
                                JsonArray timelineArray = body.get("data").getAsJsonArray();
                                Type type = new TypeToken<List<Timeline>>(){}.getType();
                                List<Timeline> timelineList =  new Gson().fromJson(timelineArray, type);
                                timelineView.onSuccessShowTimeline(timelineList);
                            }else{
                                timelineView.onFailureShowTimeline(body.get("messages").getAsString());
                            }
                        }else{
                            timelineView.onFailureShowTimeline("No Data Found");
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        timelineView.onFailureShowTimeline("Server Error");
                    }
                });
    }

    public void deleteTimeline(int timelineId){
        RetrofitClient.getInstance()
                .getApi()
                .deleteTimeline(timelineId)
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if(response.isSuccessful()){
                            JsonObject body = response.body();
                            boolean status = body.get("status").getAsBoolean();
                            String message = body.get("messages").getAsString();
                            if(status){
                                timelineView.onSuccessDeleteTimeline(message);
                            }else {
                                timelineView.onFailedDeleteTimeline(message);
                            }
                        }else {
                            timelineView.onFailedDeleteTimeline("Hapus komentar gagal");
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        timelineView.onFailedDeleteTimeline(t.getMessage());
                    }
                });

    }

    public void showTimelineBySelfId(){
        RetrofitClient.getInstance()
                .getApi()
                .showSelfTimeline(SaveUserData.getInstance().getUser().getId())
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if(response.isSuccessful()){
                            JsonObject body = response.body();
                            boolean status = body.get("status").getAsBoolean();
                            if(status){
                                JsonArray timelineArray = body.get("data").getAsJsonArray();
                                Type type = new TypeToken<List<Timeline>>(){}.getType();
                                List<Timeline> timelineList =  new Gson().fromJson(timelineArray, type);
                                timelineView.onSuccessShowTimeline(timelineList);
                            }else{
                                timelineView.onFailureShowTimeline(body.get("messages").getAsString());
                            }
                        }else{
                            timelineView.onFailureShowTimeline("No Data Found");
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        timelineView.onFailureShowTimeline("Server Error");
                    }
                });
    }

    public void showTimelineBySelfIdDetailProfile(String userId){
        RetrofitClient.getInstance()
                .getApi()
                .showSelfTimeline(Integer.parseInt(userId))
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if(response.isSuccessful()){
                            JsonObject body = response.body();
                            boolean status = body.get("status").getAsBoolean();
                            if(status){
                                JsonArray timelineArray = body.get("data").getAsJsonArray();
                                Type type = new TypeToken<List<Timeline>>(){}.getType();
                                List<Timeline> timelineList =  new Gson().fromJson(timelineArray, type);
                                timelineView.onSuccessShowTimeline(timelineList);
                            }else{
                                timelineView.onFailureShowTimeline(body.get("messages").getAsString());
                            }
                        }else{
                            timelineView.onFailureShowTimeline("No Data Found");
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        timelineView.onFailureShowTimeline("Server Error");
                    }
                });
    }

}
