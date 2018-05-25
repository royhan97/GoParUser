package project.roy.socialmedia.presenter;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import project.roy.socialmedia.data.model.Tips;
import project.roy.socialmedia.data.network.RetrofitClient;
import project.roy.socialmedia.ui.tips.TipsView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TipsPresenter {

    private TipsView tipsView;

    public TipsPresenter(TipsView tipsView){
        this.tipsView = tipsView;
    }

    public void showAllTips(){
        RetrofitClient.getInstance()
                .getApi()
                .showAllTips()
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if(response.isSuccessful()){
                            JsonObject body = response.body();
                            boolean status = body.get("status").getAsBoolean();
                            if(status){
                                JsonArray transactionArray = body.get("data").getAsJsonArray();
                                Type type = new TypeToken<List<Tips>>(){}.getType();
                                List<Tips> tipsList =  new Gson().fromJson(transactionArray, type);
                                tipsView.onSuccessShowAllTips(tipsList);
                            }else{
                                tipsView.onFailureShowAllTips(body.get("message").getAsString());
                            }
                        }else{
                            tipsView.onFailureShowAllTips("No Data Found");
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        tipsView.onFailureShowAllTips("Server Error");
                    }
                });


    }

    public void showDetailTips(int tipsId){
        RetrofitClient.getInstance()
                .getApi()
                .showDetailTips(tipsId)
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if(response.isSuccessful()){
                            JsonObject body = response.body();
                            boolean status = body.get("status").getAsBoolean();
                            String message = body.get("messages").getAsString();
                            if(status){
                                JsonObject tipsObject = body.get("data").getAsJsonObject();
                                Type type = new TypeToken<Tips>(){}.getType();
                                Tips tips = new Gson().fromJson(tipsObject, type);
                                tipsView.onSuccessShowDetailTips(tips);
                            }else{
                                tipsView.onFailureShowDetailTips(message);
                            }
                        }else{
                            tipsView.onFailureShowDetailTips("Pengambilan data gagal");
                        }

                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        tipsView.onFailureShowDetailTips(t.getMessage());
                    }
                });
    }

}
