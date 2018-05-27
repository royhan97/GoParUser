package project.roy.socialmedia.presenter;

import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import project.roy.socialmedia.data.local.SaveUserData;
import project.roy.socialmedia.data.model.Commentar;
import project.roy.socialmedia.data.model.Reminder;
import project.roy.socialmedia.data.network.RetrofitClient;
import project.roy.socialmedia.ui.timeline.CommentarView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentarPresenter {

    private CommentarView commentarView;

    public CommentarPresenter (CommentarView commentarView){
        this.commentarView = commentarView;
    }

    public void postCommentar(String timelineId, String timelineCommentar){
        RetrofitClient.getInstance()
                .getApi()
                .postCommentar(String.valueOf(SaveUserData.getInstance().getUser().getId()), timelineId, timelineCommentar)
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if(response.isSuccessful()){
                            JsonObject body = response.body();
                            boolean status = body.get("status").getAsBoolean();
                            String message = body.get("messages").getAsString();
                            if(status){
                                commentarView.onSuccessPostCommentar(message);
                            }else {
                                commentarView.onFailedPostCommentar(message);
                            }
                        }else {
                            commentarView.onFailedPostCommentar("Gagal mengirim komentar");
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        commentarView.onFailedPostCommentar(t.getMessage());
                    }
                });
    }

    public void getCommentarByTimelineId(String timelineId){
        RetrofitClient.getInstance()
                .getApi()
                .getCommentarByTimelineId(timelineId)
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if(response.isSuccessful()){
                            JsonObject body = response.body();
                            boolean status = body.get("status").getAsBoolean();
                            if(status){
                                JsonArray reminderArray = body.get("data").getAsJsonArray();
                                Type type = new TypeToken<List<Commentar>>(){}.getType();
                                List<Commentar> commentarList =  new Gson().fromJson(reminderArray, type);
                                commentarView.onSuccessShowAllCommentarByTimelineId(commentarList);
                            }else{
                                commentarView.onFailedShowAllCommentarByTimelineId(body.get("messages").getAsString());
                            }
                        }else{
                            commentarView.onFailedShowAllCommentarByTimelineId("No Data Found");
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        commentarView.onFailedShowAllCommentarByTimelineId("Server Error");
                    }
                });
    }

    public void deleteCommentarByTimelineId(String timelineId){
        RetrofitClient.getInstance()
                .getApi()
                .deleteCommentar(timelineId)
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if(response.isSuccessful()){
                            JsonObject body = response.body();
                            boolean status = body.get("status").getAsBoolean();
                            String message = body.get("messages").getAsString();
                            if(status){
                                commentarView.onSuccessDeleteCommentar(message);
                            }else {
                                commentarView.onFailedDeleteCommentar(message);
                            }
                        }else {
                            commentarView.onFailedDeleteCommentar("Hapus komentar gagal");
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        commentarView.onFailedDeleteCommentar(t.getMessage());
                    }
                });

    }
}
