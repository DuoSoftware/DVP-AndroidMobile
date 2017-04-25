package apps.veery.com.service;

import apps.veery.com.model.Auth;
import apps.veery.com.model.Revoke;
import apps.veery.com.requestmodel.rUser;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ServiceGenarator {
    @Headers({
            "Content-Type:application/json",
            "Authorization:Basic YWU4NDkyNDAtMmM2ZC0xMWU2LWIyNzQtYTllZWM3ZGFiMjZiOjYxNDU4MTMxMDIxNDQyNTgwNDg="
    })
    @POST("token")
    Call<Auth> getAuth(@Body rUser user);


    @DELETE("token/revoke/{jti}")
    Call<Revoke> getRevoke(@Header("Authorization") String authorization,@Path("jti") String jti );

    class CurrentFactory{

        private static ServiceGenarator service;
        public static ServiceGenarator getInstance(){
//            HttpLoggingInterceptor.Level logLevel = HttpLoggingInterceptor.Level.BODY;
            String BASE_URL="http://userservice.app.veery.cloud/oauth/";
//            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//            interceptor.setLevel(logLevel);
            if(service==null){
                Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(BASE_URL)
                        .build();
                service=retrofit.create(ServiceGenarator.class);
                return service;
            }else {
                return service;
            }
        }
    }
}
