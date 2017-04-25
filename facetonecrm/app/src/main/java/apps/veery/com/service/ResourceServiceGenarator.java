package apps.veery.com.service;

import apps.veery.com.model.DashTimes;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

/**
 * Created by Lakshan on 10/6/2016.
 */
public interface ResourceServiceGenarator {

    @GET("ResourceManager/{rid}/Productivity")
    Call<DashTimes> getDashTimes(@Header("Authorization")String authorization, @Path("rid") String rid);

    class ResourceServiceFactory{
        private static ResourceServiceGenarator service;
        public static ResourceServiceGenarator getInstance(){
//            HttpLoggingInterceptor.Level logLevel = HttpLoggingInterceptor.Level.BODY;
            String BASE_URL="http://resourceservice.app.veery.cloud/DVP/API/1.0.0.0/";
//            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//            interceptor.setLevel(logLevel);
            if(service==null){
                Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(BASE_URL)
                        .build();
                service=retrofit.create(ResourceServiceGenarator.class);
                return service;
            }else {
                return service;
            }
        }
    }
}
