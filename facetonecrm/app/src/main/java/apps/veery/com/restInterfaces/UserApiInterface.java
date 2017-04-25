package apps.veery.com.restInterfaces;

import apps.veery.com.model.CommentResponse;
import apps.veery.com.model.CurrentUserResponse;
import apps.veery.com.model.EngagementUserResponse;
import apps.veery.com.model.GroupDetails;
import apps.veery.com.model.NewExternalUser;
import apps.veery.com.model.NewExternalUserResponse;
import apps.veery.com.model.UserGroupResponse;
import apps.veery.com.model.UsersResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Sijith on 9/27/2016.
 */
public interface UserApiInterface {
    @Headers({
            "Content-Type: application/json",
            "companyinfo: 1:103"
    })
    @GET("DVP/API/1.0.0.0/Users")
    Call<UsersResponse> getAllUsers(@Header("authorization")String authorization); // for TicketDetailActivity

    @GET("DVP/API/1.0.0.0/UserGroups")
    Call<UserGroupResponse> getAllUserGroups(@Header("authorization")String authorization);

    @GET("DVP/API/1.0.0.0/ExternalUser/Search/{search}")
    Call<EngagementUserResponse> getFilterUser(@Header("authorization")String authorization, @Path("search") String search);

    @GET("DVP/API/1.0.0.0/ExternalUser/ByContact/phone/{search}")
    Call<EngagementUserResponse> getUsersByNumber(@Header("authorization")String authorization, @Path("search") String search);

    @GET("DVP/API/1.0.0.0/Myprofile")
    Call<CurrentUserResponse> getCurrentUser(@Header("authorization")String authorization);

    @GET("DVP/API/1.0.0.0/UserGroup/{groupId}")
    Call<GroupDetails> getGroupDetails(@Header("authorization")String authorization, @Path("groupId") String groupid);

    @POST("DVP/API/1.0.0.0/ExternalUser")
    Call<NewExternalUserResponse> createExternalUser(@Header("authorization")String authorization, @Body NewExternalUser newExternalUser);

}
