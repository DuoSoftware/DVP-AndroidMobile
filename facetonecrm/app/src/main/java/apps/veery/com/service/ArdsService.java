package apps.veery.com.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sajith on 3/21/17.
 */

public class ArdsService {
    public static final String BASE_URL = "http://ardsliteservice.app.veery.cloud/DVP/API/1.0.0.0/ARDS/";

    public static Retrofit getClient() {
            return new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
    }
}
