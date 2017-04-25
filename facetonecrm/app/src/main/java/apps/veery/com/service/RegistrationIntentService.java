package apps.veery.com.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

import apps.veery.com.model.CommentResponse;
import apps.veery.com.restInterfaces.NotificationApiInterface;
import apps.veery.com.service.AppControler;
import apps.veery.com.service.NotificationService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Sijith on 10/21/2016.
 */
public class RegistrationIntentService extends IntentService {
    private static final String TAG = "RegIntentService"; // to pass to the constructor method parameter

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("Test", "Refreshed token: " + refreshedToken);
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        if (null != AppControler.getUser()) {
            NotificationApiInterface apiService = NotificationService.getClient().create(NotificationApiInterface.class);
            Call<CommentResponse> call = apiService.registerForNotifications("Bearer " + AppControler.getUser().getAccessToken(), token);

            call.enqueue(new Callback<CommentResponse>() {
                @Override
                public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().isSuccess()) {
                            Log.d("Test", "registration on app server successed");
                        } else {
                            Log.d("Test", "registration on app server NOT successed. ooopppss!!!!");
                        }
                    }

                }

                @Override
                public void onFailure(Call<CommentResponse> call, Throwable t) {
                    Log.d("Test", "failed to register. " + t.toString());
                }
            });
        }
    }
}
