package apps.veery.com.service;

import apps.veery.com.model.Break;
import apps.veery.com.model.State;
import apps.veery.com.model.StateResource;
import apps.veery.com.requestmodel.resource.Resourcereq;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ArdsServiceInterface {
    @PUT("resource/{rid}/state/NotAvailable/reason/{reason}")
    Call<Break> getBreakRequest(@Header("Authorization")String authorization,@Path("rid") String jti,@Path("reason") String reason);

    @PUT("resource/{rid}/state/Available/reason/EndBreak")
    Call<Break> getEndBreakRequest(@Header("Authorization")String authorization,@Path("rid") String jti);

    @PUT("resource/share")
    Call<Break> getAddResource(@Header("Authorization")String authorization,@Body Resourcereq body);

    @DELETE("resource/{rid}/removesSharing/{type}")
    Call<Break> getRemoveResourceSharing(@Header("Authorization")String authorization,@Path("rid") String jti,@Path("type") String type);

    @GET("resource/{rid}/state")
    Call<State> getCurrentState(@Header("Authorization")String authorization, @Path("rid") String rid);

    @GET("resource/{rid}")
    Call<StateResource> getCurrentStateResource(@Header("Authorization")String authorization, @Path("rid") String rid);
}
