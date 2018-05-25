package project.roy.socialmedia.data.network;

import com.google.gson.JsonObject;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by roy on 4/9/2018.
 */

public interface Api {

    @POST("api/user/register")
    @FormUrlEncoded
    Call<JsonObject> register (@Field("name") String name,
                               @Field("username") String username,
                               @Field("password") String password);

    @POST("api/user/login")
    @FormUrlEncoded
    Call<JsonObject> login (@Field("username") String username,
                            @Field("password") String password);

    @PUT("api/user/changepassword/{id}")
    Call<JsonObject> changePassword(@Path("id") int id,
                                    @Header("Content-Type") String contentType,
                                    @Body JsonObject params);

    @PUT("api/user/updateprofile/{id}")
    Call<JsonObject> changeProfile(@Path("id") int id,
                                   @Header("Content-Type") String contentType,
                                   @Body JsonObject params);

    @POST("api/user/updateprofilepicture")
    @Multipart
    Call<JsonObject> updateProfilPicture(@Part("id") int id,
                                         @Part MultipartBody.Part profile_picture);

    @GET("api/reminder/showallreminder")
    Call<JsonObject> showAllReminder();

    @GET("api/reminder/showdetailreminder/{reminderId}")
    Call<JsonObject> showDetailReminder(@Path("reminderId") int reminderId) ;

    @GET("api/tips/showalltips")
    Call<JsonObject> showAllTips();

    @GET("api/tips/showdetailtips/{tipsId}")
    Call<JsonObject> showDetailTips(@Path("tipsId") int tipsId) ;

    @GET("api/timeline/show")
    Call<JsonObject> showTimeline();

    @POST("api/timeline/post")
    @FormUrlEncoded
    Call<JsonObject> postTimeline(@Field("user_id") int userId,
                                  @Field("timeline_moment") String moment,
                                  @Field("media[]") File media);

    @GET("api/timeline/showDetail/{idTimeline}")
    Call<JsonObject> showDetailTimeline(@Path("idTimeline") int idTimeline);

    @GET("api/timeline/showSelf/{userId}")
    Call<JsonObject> showSelfTimeline(@Path("userId") int userId);

    @DELETE("api/timeline/delete/{timelineId}")
    Call<JsonObject> deleteTimeline(@Path("timelineId") int timelineId);

}
