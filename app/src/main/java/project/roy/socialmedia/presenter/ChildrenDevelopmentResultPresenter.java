package project.roy.socialmedia.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import project.roy.socialmedia.data.local.SaveUserData;
import project.roy.socialmedia.data.model.DDTK;
import project.roy.socialmedia.data.model.DDTKField;
import project.roy.socialmedia.data.network.RetrofitClient;
import project.roy.socialmedia.ui.childrendevelopment.ChildrenDevelopmentResultView;
import project.roy.socialmedia.ui.childrendevelopment.ChildrenDevelopmentView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChildrenDevelopmentResultPresenter {
    private ChildrenDevelopmentResultView childrenDevelopmentResultView;

    public ChildrenDevelopmentResultPresenter (ChildrenDevelopmentResultView childrenDevelopmentResultView){
        this.childrenDevelopmentResultView = childrenDevelopmentResultView;
    }

    public void showAllDDTK(){
        RetrofitClient.getInstance()
                .getApi()
                .showAllDDTK()
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if(response.isSuccessful()){
                            JsonObject body = response.body();
                            boolean status = body.get("status").getAsBoolean();
                            if(status){
                                JsonArray ddtkArray = body.get("data").getAsJsonArray();
                                Type type = new TypeToken<List<DDTK>>(){}.getType();
                                List<DDTK> ddtkList =  new Gson().fromJson(ddtkArray, type);
                                showAllDDTKDetail(ddtkList);
                                //childrenDevelopmentView.onSuccessShowAllDdtk(ddtkList);
                            }else{
                                childrenDevelopmentResultView.onFailedShowAllDdtk(body.get("messages").getAsString());
                            }
                        }else{
                            childrenDevelopmentResultView.onFailedShowAllDdtk("Server Error");
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        childrenDevelopmentResultView.onFailedShowAllDdtk(t.getMessage());
                    }
                });
    }

    public void showAllDDTKDetail(List<DDTK> ddtkList){
        RetrofitClient.getInstance()
                .getApi()
                .showAllDDTKField(SaveUserData.getInstance().getUser().getId())
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if(response.isSuccessful()){
                            JsonObject body = response.body();
                            boolean status = body.get("status").getAsBoolean();
                            if(status){
                                JsonArray ddtkArray = body.get("data").getAsJsonArray();
                                Type type = new TypeToken<List<DDTKField>>(){}.getType();
                                List<DDTKField> ddtkFieldList =  new Gson().fromJson(ddtkArray, type);
                                Log.d("c", "berubahp "+ ddtkFieldList.size());
                                for(int i =0; i< ddtkFieldList.size(); i++ ){

                                    if(ddtkFieldList.get(i).getDdtk().getId() == ddtkList.get(i).getId()){
                                        ddtkList.set(i, new DDTK(ddtkList.get(i).getId(),
                                                ddtkList.get(i).getDdtkDescription(),
                                                ddtkList.get(i).getCreatedAt(),
                                                ddtkList.get(i).getUpdatedAt(),
                                                ddtkList.get(i).getUsia(),
                                                ddtkList.get(i).getAspekPerkembangan(),
                                                1));
                                        Log.d("c", "berubah "+ i);
                                    }
                                }


                                childrenDevelopmentResultView.onSuccessShowAllDdtk(ddtkList);
                            }else{
                                childrenDevelopmentResultView.onFailedShowAllDdtk(body.get("messages").getAsString());
                            }
                        }else {
                            childrenDevelopmentResultView.onFailedShowAllDdtk("Server Error");
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        childrenDevelopmentResultView.onFailedShowAllDdtk(t.getMessage());
                    }
                });
    }
}
