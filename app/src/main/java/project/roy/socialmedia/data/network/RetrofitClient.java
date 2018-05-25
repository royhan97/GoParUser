package project.roy.socialmedia.data.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import project.roy.socialmedia.data.local.SaveUserToken;
import project.roy.socialmedia.util.Constant;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by roy on 4/9/2018.
 */

public class RetrofitClient {

    private static RetrofitClient retrofitClient;

    private RetrofitClient(){

    }

    public static RetrofitClient getInstance(){
        if(retrofitClient == null){
            retrofitClient = new RetrofitClient();
        }
        return  retrofitClient;
    }

    public Api getApi(){
        return getRetrofit().create(Api.class);
    }

    public Retrofit getRetrofit(){
        OkHttpClient okHttpClient = null;
        if(SaveUserToken.getInstance().getUserToken() == null){
            okHttpClient= new OkHttpClient.Builder()
                    .readTimeout(20, TimeUnit.SECONDS)
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .build();
        }else {
            okHttpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    Request newRequest;
                    newRequest = request.newBuilder()
                            .addHeader("Authorization", SaveUserToken.getInstance().getUserToken())
                            .build();

                    return chain.proceed(newRequest);
                }
            }).connectTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .build();


        }
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        return new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

}
