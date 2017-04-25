package apps.veery.com.restInterfaces;

import apps.veery.com.model.CommentResponse;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Sijith on 10/14/2016.
 */
public interface NotificationApiInterface {
    @Headers({
            "Content-Type: application/json",
            "companyinfo: 1:103"
    })
    @POST("DVP/API/1.0.0.0/NotificationService/Notification/GCMRegistration")
    Call<CommentResponse> registerForNotifications(@Header("authorization")String authorization, @Header("appkey")String registrationToken);

}
