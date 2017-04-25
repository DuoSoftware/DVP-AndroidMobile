package apps.veery.com.service;

import apps.veery.com.model.DashboardTicketResponse;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;


public interface DashboardApiInterface {

    @GET("DashboardEvent/CurrentCount/{type}/{user}/*")
    Call<DashboardTicketResponse> getOpenTicket(@Header("Authorization")String authorization, @Path("type") String type, @Path("user") String user);


}
