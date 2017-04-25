package apps.veery.com.service;

import java.util.List;

import apps.veery.com.model.Engagement.Engagements;
import apps.veery.com.model.Engagement.SessionList;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Lakshan on 10/7/2016.
 */
public interface EngagementServiceGenarator {

    @GET("EngagementByProfile/{pid}")
    Call<Engagements> getEngagementByProfile(@Header("Authorization")String authorization, @Path("pid") String tid);

    @GET("Engagement/{engid}/EngagementSessions")
    Call<SessionList> getAllEngagementSessions(@Header("Authorization")String authorization,@Path("engid") String tid,@Query("session") List<String> filters);

    class EngagementServiceFactory{
        private static EngagementServiceGenarator service;
        public static EngagementServiceGenarator getInstance(){
//            HttpLoggingInterceptor.Level logLevel = HttpLoggingInterceptor.Level.BODY;
            String BASE_URL="http://interactions.app.veery.cloud/DVP/API/1.0.0.0/";
//            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//            interceptor.setLevel(logLevel);
            if(service==null){
                Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(BASE_URL)
                        .build();
                service=retrofit.create(EngagementServiceGenarator.class);
                return service;
            }else {
                return service;
            }
        }
    }
}
