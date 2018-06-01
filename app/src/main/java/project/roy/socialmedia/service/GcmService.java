package project.roy.socialmedia.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import project.roy.socialmedia.R;
import project.roy.socialmedia.data.local.SaveUserData;
import project.roy.socialmedia.data.model.Reminder;
import project.roy.socialmedia.data.model.Tips;
import project.roy.socialmedia.data.network.RetrofitClient;
import project.roy.socialmedia.ui.home.HomeActivity;
import project.roy.socialmedia.util.SharedPrefUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GcmService extends GcmTaskService {

    public static String TAG_LIST_REQUEST = "check_list_request_for_kabim";
    public static String TAG_DETAIL_TRX = "check_detail_trx";


    @Override
    public int onRunTask(TaskParams taskParams) {
        checkReminder();
        checkTips();
        return GcmNetworkManager.RESULT_SUCCESS;
    }

    private void checkTips() {

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
                                int tipsTotal = SharedPrefUtil.getInt("tips_total");
                                if(transactionArray.size()> tipsTotal){
                                    SharedPrefUtil.saveInt("tips_total", transactionArray.size());

                                    showNotification(getApplicationContext(), "Go-Par", "Anda memiliki 1 tips baru", 1);
                                }

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                    }
                });

    }

    private void checkReminder() {
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
                                int reminderTotal = SharedPrefUtil.getInt("reminder_total");
                                if(reminderArray.size()> reminderTotal){
                                    SharedPrefUtil.saveInt("reminder_total", reminderArray.size());
                                    showNotification(getApplicationContext(), "Go-Par", "Anda memiliki 1 reminder baru", 0);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                    }
                });
    }

    private void showNotification(Context context, String title, String message, int notifId){
        Intent resultIntent = new Intent(this, HomeActivity.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setSmallIcon(R.drawable.logo)
                .setContentText(message)
                .setAutoCancel(true)
                .setColor(ContextCompat.getColor(context, android.R.color.black))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound);

        notificationManagerCompat.notify(notifId, builder.build());
    }


}

